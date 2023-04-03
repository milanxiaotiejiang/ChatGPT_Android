package com.milan.chat.openai.gpt.widget.pop

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.lxj.xpopup.core.AttachPopupView
import com.milan.chat.openai.gpt.R
import com.milan.chat.openai.gpt.databinding.ItemPopupOperateAttachBinding

/**
 * User: milan
 * Time: 2023/4/3 13:51
 * Des:
 */
class OperateItem(
    val title: Int,
    val res: Int
)

class OperateAdapter :
    BaseQuickAdapter<OperateItem, BaseDataBindingHolder<ItemPopupOperateAttachBinding>>(R.layout.item_popup_operate_attach) {

    override fun convert(
        holder: BaseDataBindingHolder<ItemPopupOperateAttachBinding>,
        item: OperateItem
    ) {
        holder.dataBinding?.item = item
    }
}

class OperateAttachPopupView(context: Context) : AttachPopupView(context) {

    private lateinit var mOperateListener: (position: Int) -> Unit

    private val mAdapter: OperateAdapter by lazy {
        OperateAdapter().apply {
            setOnItemClickListener { _, _, position ->
                if (this@OperateAttachPopupView::mOperateListener.isInitialized)
                    mOperateListener.invoke(position)
            }
        }
    }

    override fun getImplLayoutId() = R.layout.pop_operate_attach

    override fun onCreate() {
        super.onCreate()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = mAdapter

        mAdapter.addData(
            OperateItem(R.string.action_delete, R.drawable.ic_action_delete)
        )
        mAdapter.addData(
            OperateItem(R.string.action_copy, R.drawable.ic_action_copy)
        )
    }

    fun setOperateListener(listener: (position: Int) -> Unit): OperateAttachPopupView {
        mOperateListener = listener
        return this
    }

}