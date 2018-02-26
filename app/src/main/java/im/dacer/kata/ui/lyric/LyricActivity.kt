package im.dacer.kata.ui.lyric

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.bindToRecyclerView(recyclerView)

        lyricEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
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
                            SchemeHelper.startKata(this@LyricActivity, it)
                        }, { timberAndToast(it) })
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        searchDisposable?.dispose()
    }

}
