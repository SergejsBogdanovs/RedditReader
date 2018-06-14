package lv.st.sbogdano.redditreader.ui.posts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostAdapterViewHolder> {

    private final Context mContext;
    private final PostAdapterOnItemClickListener mClickHandler;
    private List<PostEntry> mPostEntries;

    public PostsAdapter(Context context, PostAdapterOnItemClickListener clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public interface PostAdapterOnItemClickListener {
        void onItemClick();
    }

    @NonNull
    @Override
    public PostsAdapter.PostAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.PostAdapterViewHolder holder, int position) {

        PostEntry postEntry = mPostEntries.get(position);

        // Post Image
        Picasso.get()
                .load(postEntry.getPostThumbnail())
                .into(holder.mPostImage);

        // Post Title
        holder.mPostTitle.setText(postEntry.getPostTitle());

        // Post comments count
        holder.mCommentCount.setText(String.valueOf(postEntry.getPostCommentsCount()));

        // Posts rating count
        holder.mScoreCount.setText(String.valueOf(postEntry.getPostScore()));
    }

    @Override
    public int getItemCount() {
        if (mPostEntries == null) {
            return 0;
        }
        return mPostEntries.size();
    }

    public void swapPosts(List<PostEntry> newPostEntries) {
        if (mPostEntries == null) {
            mPostEntries = newPostEntries;
            notifyDataSetChanged();
        }
    }

    public class PostAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.post_image)
        ImageView mPostImage;
        @BindView(R.id.post_title)
        TextView mPostTitle;
        @BindView(R.id.comment_count)
        TextView mCommentCount;
        @BindView(R.id.score_count)
        TextView mScoreCount;

        public PostAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
