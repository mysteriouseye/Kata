package im.dacer.kata.ui.lyric

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import im.dacer.kata.R
import im.dacer.kata.adapter.LyricAdapter
import im.dacer.kata.core.extension.timberAndToast
import im.dacer.kata.core.util.LyricsHelper
import im.dacer.kata.core.util.SchemeHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_lyric.*

class LyricActivity : AppCompatActivity() {

    private var searchDisposable: Disposable? = null
    private val adapter: LyricAdapter = LyricAdapter()
    private val progressDialog: MaterialDialog by lazy { MaterialDialog.Builder(this).progress(true, 0).build() }
    private var pageIndex = 1
    private var searchKeyWord: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.bindToRecyclerView(recyclerView)
        val bottomView = layoutInflater.inflate(R.layout.item_history_bottom, recyclerView.parent as ViewGroup, false)
        adapter.setFooterView(bottomView)
        adapter.setOnLoadMoreListener({ loadMore() }, recyclerView)

        lyricEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    searchKeyWord = s.toString()
                    pageIndex = 1
                    searchDisposable = LyricsHelper.search(s.toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                adapter.setNewData(it.songs.toList())
                            }, { timberAndToast(it) })
                }
            }
        })

        adapter.setOnItemClickListener { _, _, pos ->
            adapter.getItem(pos)?.id?.run {
                progressDialog.show()
                searchDisposable = LyricsHelper.getLyric(this)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            progressDialog.dismiss()
                            SchemeHelper.startKata(this@LyricActivity, it, alias = adapter.getItem(pos)!!.name)
                        }, { timberAndToast(it) })
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        searchDisposable?.dispose()
    }

    private fun loadMore() {
        pageIndex++
        searchKeyWord?.run {
            searchDisposable = LyricsHelper.search(this, pageIndex)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.songs?.isNotEmpty() == true) {
                            adapter.addData(it.songs.toList())
                            adapter.loadMoreComplete()
                        } else {
                            adapter.loadMoreEnd()
                        }
                    }, {
                        timberAndToast(it)
                        adapter.loadMoreFail()
                    })
        }
    }
}
