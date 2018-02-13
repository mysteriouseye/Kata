package im.dacer.kata.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import im.dacer.kata.R
import im.dacer.kata.adapter.HistoryAdapter
import im.dacer.kata.core.extension.timberAndToast
import im.dacer.kata.core.extension.toast
import im.dacer.kata.core.model.History
import im.dacer.kata.service.ListenClipboardService
import im.dacer.kata.ui.AboutActivity
import im.dacer.kata.ui.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity(), MainMvp {

    private val mainPresenter by lazy { MainPresenter(baseContext, this) }
    private val historyAdapter = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popupView.listener = mainPresenter
        mainPresenter.importDictDb()
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(historyAdapter)
        val itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
        historyAdapter.openLoadAnimation(SlideInBottomAnimation())
        historyAdapter.bindToRecyclerView(historyRecyclerView)
        itemTouchHelper.attachToRecyclerView(historyRecyclerView)
        historyAdapter.setOnItemClickListener { _, _, pos -> mainPresenter.onHistoryClicked(pos)}
        historyAdapter.setEmptyView(R.layout.empty_history)
        historyAdapter.enableSwipeItem()
        historyAdapter.setOnItemSwipeListener(mainPresenter.swipeListener)

        clipTv.setOnLongClickListener {
            popupView.show(Point((clipTv.width / 2), clipTv.y.toInt()- bigbangTipTv.height))
            return@setOnLongClickListener true
        }
        permissionErrorLayout.setOnClickListener {
            val requestIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + packageName))
            ListenClipboardService.stop(this)
            try {
                startActivityForResult(requestIntent, REQUEST_CODE_OVERLAY_PERMISSION)
            } catch (e: Exception) {
                toast(getString(R.string.cannot_open_overlay_permission_settings))
            }
        }

    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        mainPresenter.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val canDraw = Settings.canDrawOverlays(this)
            permissionErrorLayout.visibility = if (canDraw) View.GONE else View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        mainPresenter.onStop()
        nothingHappenedView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            mainPresenter.restartListenService()
        }
    }

    override fun showNothingHappenedView() {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        nothingHappenedView.visibility = View.VISIBLE
        nothingHappenedView.startAnimation(slideUp)
    }

    override fun showHistory(historyList: List<History>) {
        tutorialLayout.visibility = View.GONE
        historyRecyclerView.visibility = View.VISIBLE
        historyAdapter.setNewData(historyList)
    }

    override fun getDecorView() = window.decorView!!
    override fun setBigbangTipTv(strId: Int) = bigbangTipTv.setText(strId)
    override fun catchError(throwable: Throwable) = timberAndToast(throwable)
    override fun getClipTvText() = clipTv.text.toString()


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val REQUEST_CODE_OVERLAY_PERMISSION = 123
    }
}
