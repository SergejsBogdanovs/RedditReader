package lv.st.sbogdano.redditreader.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import net.dean.jraw.models.Submission;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.ui.comments.CommentsActivity;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailActivity";

    public static final String SUBMISSION_EXTRA = "SUBMISSION_EXTRA";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.post_detail_title)
    TextView mPostDetailTitle;
    @BindView(R.id.post_detail_author)
    TextView mPostDetailUsername;
    @BindView(R.id.post_detail_date)
    TextView mPostDetailDate;
    @BindView(R.id.post_detail_image)
    ImageView mPostDetailImage;
    @BindView(R.id.post_detail_share)
    ImageView mPostDetailShare;
    @BindView(R.id.post_detail_vote_count)
    TextView mPostDetailVoteCount;
    @BindView(R.id.post_detail_vote_img)
    ImageView mPostDetailVoteImg;
    @BindView(R.id.post_detail_comment_count)
    TextView mPostDetailCommentCount;
    @BindView(R.id.post_detail_comment_img)
    ImageView mPostDetailCommentImg;

    private Submission mSubmission = null;
    private String mSubmissionJson;

    public static void start(AppCompatActivity activity, String dataNode) {
        Intent postDetailIntent = new Intent(activity, PostDetailActivity.class);
        postDetailIntent.putExtra(SUBMISSION_EXTRA, dataNode);
        activity.startActivity(postDetailIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        if (getIntent().getExtras().containsKey(SUBMISSION_EXTRA)) {
            mSubmissionJson = getIntent().getStringExtra(SUBMISSION_EXTRA);
            try {
                mSubmission = new Submission(new ObjectMapper().readTree(mSubmissionJson));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setupToolBar();
        bindUi();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(mSubmission.getSubredditName());
        }
    }

    private void bindUi() {

        /************************
         * Title
         ***********************/
        mPostDetailTitle.setText(mSubmission.getTitle());

        /************************
         * Post author
         ***********************/
        mPostDetailUsername.setText(mSubmission.getAuthor());

        /************************
         * Date
         ***********************/
        Date date = mSubmission.getCreated();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mPostDetailDate.setText(sdf.format(date));

        /************************
         * Image
         ***********************/
        String posterThumbnail = mSubmission.getThumbnail();
        if (posterThumbnail == null || posterThumbnail.isEmpty()) {
            mPostDetailImage.setVisibility(View.GONE);
        } else {
            Picasso.get()
                    .load(posterThumbnail)
                    .into(mPostDetailImage);
        }

        /************************
         * Votes
         ***********************/
        mPostDetailVoteCount.setText(String.valueOf(mSubmission.getScore()));

        /************************
         * Comments
         ***********************/
        mPostDetailCommentCount.setText(String.valueOf(mSubmission.getCommentCount()));

        mPostDetailCommentImg.setOnClickListener(view -> CommentsActivity.start(this, mSubmissionJson));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
