package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.dean.jraw.models.Submission;

public class PostsAdapter extends PagedListAdapter<Submission, RecyclerView.ViewHolder> {

    private static final String TAG = "PostsAdapter";
    private PostViewHolder.PostsAdapterOnItemClickHandler mClickHandler;
    private final LinearLayout.LayoutParams params;


    private static final DiffUtil.ItemCallback<Submission> POST_COMPARATOR
            = new DiffUtil.ItemCallback<Submission>() {

        @Override
        public boolean areItemsTheSame(Submission oldItem, Submission newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(Submission oldItem, Submission newItem) {
            return true;
        }
    };

    PostsAdapter(PostViewHolder.PostsAdapterOnItemClickHandler clickHandler) {
        super(POST_COMPARATOR);
        mClickHandler = clickHandler;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PostViewHolder.getInstance(parent, mClickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Submission submission = getItem(position);
        if (submission != null && !submission.isNsfw()) {
            ((PostViewHolder) holder).bind(submission);
        } else {
            params.height = 0;
            holder.itemView.setLayoutParams(params);
        }

    }
}
