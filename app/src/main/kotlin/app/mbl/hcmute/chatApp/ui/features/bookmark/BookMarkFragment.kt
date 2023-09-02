package app.mbl.hcmute.chatApp.ui.features.bookmark

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.base.ext.observe
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentBookmarkBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.ui.features.bookmark.bookmarksProvider.BookmarkBinder
import app.mbl.hcmute.chatApp.ui.features.conversation.ChatStartType
import app.mbl.hcmute.chatApp.ui.firstScreen.FirstScreenFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mva3.adapter.ListSection
import mva3.adapter.MultiViewAdapter
import javax.inject.Inject

@AndroidEntryPoint
class BookMarkFragment : BaseVmDbFragment<BookmarkViewModel, FragmentBookmarkBinding>() {
    private val bookmarkAdapter = MultiViewAdapter()
    private val bookmarkSection = ListSection<ChatBookmark>()
    override val viewModel: BookmarkViewModel by viewModels()
    override fun getLayoutId() = R.layout.fragment_bookmark

    @Inject
    lateinit var navigator: AppNavigator

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
        binding.rvBookmark.apply {
//            addItemDecoration(BottomItemDecoration())
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = bookmarkAdapter
            bookmarkAdapter.registerItemBinders(BookmarkBinder(onItemClick))
            bookmarkAdapter.removeAllSections()
            bookmarkAdapter.addSection(bookmarkSection)
        }
        bookmarkAdapter.itemTouchHelper.attachToRecyclerView(binding.rvBookmark)
        bookmarkSection.setSwipeToDismissListener { position, item ->
            showUndoSnackBar(position, item)
        }
    }

    private fun showUndoSnackBar(position: Int, deleteItem: ChatBookmark) {
        var deleteBookmark = true
        Snackbar.make(binding.root, "Will delete bookmark after 3 seconds!", Snackbar.LENGTH_SHORT).apply {
            setAction(getString(R.string.undo)) {
                deleteBookmark = false
                bookmarkSection.add(position, deleteItem)
            }.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (deleteBookmark) viewModel.deleteBookmark(deleteItem)
                }
            }).show()
        }
    }

    private val onItemClick: (convId: Long, messId: String) -> Unit = { convId: Long, messId: String ->
        val direction = FirstScreenFragmentDirections.actionFirstScreenFragmentToChatAssistantFragment(null, convId, messId, ChatStartType.BOOKMARK.name)
        navigator.navigateTo(direction)
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observe(viewModel.bookmarks) { bookmarkSection.set(it) }
    }
}