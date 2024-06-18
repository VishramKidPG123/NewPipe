package org.schabi.newpipe.fragments.list.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.schabi.newpipe.extractor.comments.CommentsInfoItem
import org.schabi.newpipe.ktx.serializable
import org.schabi.newpipe.ui.theme.AppTheme

class CommentRepliesFragment : Fragment() {
    private val disposables = CompositeDisposable()
    lateinit var comment: CommentsInfoItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        comment = requireArguments().serializable<CommentsInfoItem>(COMMENT_KEY)!!
        return ComposeView(requireContext()).apply {
            setContent {
                val flow = remember(comment) {
                    Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
                        CommentRepliesSource(comment)
                    }.flow
                }

                AppTheme {
                    CommentReplies(comment = comment, flow = flow, disposables = disposables)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    companion object {
        @JvmField
        val TAG = CommentRepliesFragment::class.simpleName!!

        const val COMMENT_KEY = "comment"
    }
}
