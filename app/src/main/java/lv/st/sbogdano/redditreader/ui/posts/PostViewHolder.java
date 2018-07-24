package lv.st.sbogdano.redditreader.ui.posts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.dean.jraw.models.Submission;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;

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

    private Submission mSubmission;

    private final PostsAdapterOnItemClickHandler mClickHandler;

    public interface PostsAdapterOnItemClickHandler {
        void onItemClick(String submissionDataNode);
    }

    public static PostViewHolder getInstance(ViewGroup parent, PostsAdapterOnItemClickHandler clickHandler) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view, clickHandler);
    }

    private PostViewHolder(View itemView, PostsAdapterOnItemClickHandler clickHandler) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mClickHandler = clickHandler;
        itemView.setOnClickListener(this);
    }

    public void bind(Submission submission) {
        this.mSubmission = submission;
        mSubredditName.setText(submission.getSubredditName());

        String thumbnail = submission.getThumbnail();
        if (thumbnail == null || thumbnail.isEmpty()) {
            mPostImage.setVisibility(View.GONE);
        } else {
            Picasso.get()
                    .load(submission.getThumbnail())
                    .into(mPostImage);
        }

        mPostTitle.setText(submission.getTitle());
        mCommentCount.setText(String.valueOf(submission.getCommentCount()));
        mScoreCount.setText(String.valueOf(submission.getScore()));
    }

    @Override
    public void onClick(View view) {
        mClickHandler.onItemClick(mSubmission.getDataNode().toString());
    }

}


