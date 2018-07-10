package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;

public class PostsAdapter extends PagedListAdapter<PostEntry, RecyclerView.ViewHolder> {

    private static final String TAG = "PostsAdapter";


    public static final DiffUtil.ItemCallback<PostEntry> POST_COMPARATOR
            = new DiffUtil.ItemCallback<PostEntry>() {

        @Override
        public boolean areItemsTheSame(PostEntry oldItem, PostEntry newItem) {
            return oldItem.getPostTitle().equals(newItem.getPostTitle());
        }

        @Override
        public boolean areContentsTheSame(PostEntry oldItem, PostEntry newItem) {
            return true;
        }
    };

    PostsAdapter() {
        super(POST_COMPARATOR);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PostViewHolder.getInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostEntry postEntry = getItem(position);
        if (postEntry != null) {
            ((PostViewHolder) holder).bind(postEntry);
        }
    }
}
