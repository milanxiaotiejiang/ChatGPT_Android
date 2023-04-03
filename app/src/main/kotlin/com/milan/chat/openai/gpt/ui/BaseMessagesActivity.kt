package com.milan.chat.openai.gpt.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.elvishew.xlog.XLog
import com.milan.chat.openai.gpt.R
import com.milan.chat.openai.gpt.widget.pop.OperateAttachPopupView
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.sample.common.data.model.Message
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseMessagesActivity<out ViewModel : BaseViewModel, DataBinding : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : BaseVmActivity<ViewModel, DataBinding>(layoutId), MessagesListAdapter.SelectionListener,
    MessagesListAdapter.OnLoadMoreListener {

    private var firstTime: Long = 0

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (selectionCount == 0) {
                val secondTime = System.currentTimeMillis()
                if (secondTime - firstTime < 2000) {
                    finish()
                } else {
                    showToast("再点一次退出程序")
                    firstTime = System.currentTimeMillis()
                }
            } else {
                messagesAdapter.unselectAllItems()
            }
        }
    }

    open lateinit var messagesAdapter: MessagesListAdapter<Message>

    open lateinit var imageLoader: ImageLoader

    private lateinit var menu: Menu
    private var selectionCount = 0

    private lateinit var operateAttachPopupView: OperateAttachPopupView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPermission.permissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).request { allGranted, _, deniedList ->
            if (!allGranted) {
                XLog.d("These permissions are denied: $deniedList")
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        operateAttachPopupView = OperateAttachPopupView(this).setOperateListener {
            XLog.e("setOperateListener $it")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.chat_actions_menu, menu)
        onSelectionChanged(0)
        return true
    }

    private val messageStringFormatter: MessagesListAdapter.Formatter<Message>
        get() = MessagesListAdapter.Formatter { message: Message ->
            val imageUrl = message.imageUrl
            if (imageUrl != null) {
                return@Formatter imageUrl
            }

            var text = message.text
            if (text != null) {
                return@Formatter text
            }

            val createdAt = SimpleDateFormat(
                "MMM d, EEE 'at' h:mm a",
                Locale.getDefault()
            ).format(message.createdAt)
            text = "[attachment]"
            String.format(
                Locale.getDefault(), "%s: %s (%s)", message.user.name, text, createdAt
            )
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> messagesAdapter.deleteSelectedMessages()
            R.id.action_copy -> {
                messagesAdapter.copySelectedMessagesText(this, messageStringFormatter, true)
                showToast("Message copied!")
            }
        }
        return true
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        Log.i("TAG", "onLoadMore: $page $totalItemsCount")
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages()
        }
    }

    override fun onSelectionChanged(count: Int) {
        selectionCount = count
        menu.findItem(R.id.action_delete).isVisible = count > 0
        menu.findItem(R.id.action_copy).isVisible = count > 0
    }

    private fun loadMessages() {}


    companion object {
        private const val TOTAL_MESSAGES_COUNT = 100
        const val USER_ID = "0"
        const val ASSISTANT_ID = "1"
    }
}