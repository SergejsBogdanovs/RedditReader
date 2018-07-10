package lv.st.sbogdano.redditreader.ui.posts;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.post_image)
    ImageView mPostImage;
    @BindView(R.id.post_title)
    TextView mPostTitle;
    @BindView(R.id.comment_count)
    TextView mCommentCount;
    @BindView(R.id.score_count)
    TextView mScoreCount;
    @BindView(R.id.subreddit_name)
    TextView mSubredditName;

    private PostEntry mPostEntry = null;

    public static PostViewHolder getInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    private PostViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(PostEntry postEntry) {
        if (postEntry == null) {
            Resources resources = itemView.getResources();
            mSubredditName.setText(resources.getString(R.string.loading));
            mPostImage.setVisibility(View.GONE);
            mPostTitle.setVisibility(View.GONE);
            mCommentCount.setVisibility(View.GONE);
            mScoreCount.setVisibility(View.GONE);
        } else {
            showPostData(postEntry);
        }
    }

    private void showPostData(PostEntry postEntry) {
        this.mPostEntry = postEntry;
        mSubredditName.setText(postEntry.getSubredditName());

        Picasso.get()
                .load(postEntry.getPostThumbnail())
                .into(mPostImage);

        mPostTitle.setText(postEntry.getPostTitle());
        mCommentCount.setText(String.valueOf(postEntry.getPostCommentsCount()));
        mScoreCount.setText(String.valueOf(postEntry.getPostScore()));
    }

    @Override
    public void onClick(View view) {

    }

}


