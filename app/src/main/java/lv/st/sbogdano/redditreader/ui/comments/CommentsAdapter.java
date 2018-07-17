package lv.st.sbogdano.redditreader.ui.comments;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import lv.st.sbogdano.redditreader.data.model.PostComment;

public class CommentsAdapter extends PagedListAdapter<PostComment, RecyclerView.ViewHolder> {

    private Context mContext;


    private static final DiffUtil.ItemCallback<PostComment> COMMENT_COMPARATOR = new DiffUtil.ItemCallback<PostComment>() {
        @Override
        public boolean areItemsTheSame(PostComment oldItem, PostComment newItem) {
            return oldItem.getCommentBody().equals(newItem.getCommentBody());
        }

        @Override
        public boolean areContentsTheSame(PostComment oldItem, PostComment newItem) {
            return true;
        }
    };

    public CommentsAdapter(Context context) {
        super(COMMENT_COMPARATOR);
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CommentsViewHolder.getInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostComment postComment = getItem(position);
        if (postComment != null) {
            ((CommentsViewHolder) holder).bind(postComment);
            holder.itemView.setPadding(makeIndent(postComment.getDepth()), 0, 0, 0);
        }
    }

    private int makeIndent(int depth) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (10 * depth * scale + 0.5f);
    }
}
