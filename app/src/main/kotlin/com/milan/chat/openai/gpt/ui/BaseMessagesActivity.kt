package com.milan.chat.openai.gpt.ui

import android.Manifest
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.elvishew.xlog.XLog
import com.milan.chat.openai.gpt.R
import com.milan.chat.openai.gpt.ui.fragment.ProgressDialogFragment
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.sample.common.data.model.Message
import com.stfalcon.chatkit.sample.utils.AppUtils
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseMessagesActivity<out ViewModel : BaseViewModel, DataBinding : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int
) : BaseVmActivity<ViewModel, DataBinding>(layoutId), MessagesListAdapter.SelectionListener,
    MessagesListAdapter.OnLoadMoreListener {

    open lateinit var messagesAdapter: MessagesListAdapter<Message>

    open lateinit var imageLoader: ImageLoader

    private lateinit var menu: Menu
    private var selectionCount = 0

    private lateinit var progressDialogFragment: ProgressDialogFragment

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.chat_actions_menu, menu)
        onSelectionChanged(0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> messagesAdapter.deleteSelectedMessages()
            R.id.action_copy -> {
                messagesAdapter.copySelectedMessagesText(this, messageStringFormatter, true)
                AppUtils.showToast(this, R.string.copied_message, true)
            }
        }
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed()
        } else {
            messagesAdapter.unselectAllItems()
        }
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

    private val messageStringFormatter: MessagesListAdapter.Formatter<Message>
        get() = MessagesListAdapter.Formatter { message: Message ->
            val createdAt = SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                .format(message.createdAt)
            var text = message.text
            if (text == null) text = "[attachment]"
            String.format(
                Locale.getDefault(), "%s: %s (%s)",
                message.user.name, text, createdAt
            )
        }



    companion object {
        private const val TOTAL_MESSAGES_COUNT = 100
        const val USER_ID = "0"
        const val ASSISTANT_ID = "1"
    }
}