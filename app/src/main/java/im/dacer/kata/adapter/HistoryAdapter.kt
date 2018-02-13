package im.dacer.kata.adapter

import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import im.dacer.kata.R
import im.dacer.kata.core.model.History

/**
 * Created by Dacer on 13/02/2018.
 */
class HistoryAdapter : BaseItemDraggableAdapter<History, BaseViewHolder>(R.layout.item_history, listOf()) {

    override fun convert(helper: BaseViewHolder, item: History) {
        helper.setText(R.id.historyTv, item.text())
    }


}