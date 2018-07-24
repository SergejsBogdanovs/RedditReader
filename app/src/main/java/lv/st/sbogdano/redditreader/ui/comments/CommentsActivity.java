package lv.st.sbogdano.redditreader.ui.comments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.dean.jraw.models.Submission;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.ui.details.PostDetailActivity;
import lv.st.sbogdano.redditreader.viewmodels.CommentsViewModel;
import lv.st.sbogdano.redditreader.viewmodels.ViewModelFactory;

public class CommentsActivity extends AppCompatActivity {

    private static final String SUBMISSION_COMMENT_EXTRA = "submission_comment_extra";
    private static final String TAG = "CommentsActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.rv_comments)
    RecyclerView mRvComments;
    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;

    private CommentsViewModel mCommentsViewModel;
    private CommentsAdapter mCommentsAdapter;
    private Submission mSubmission;

    public static void start(PostDetailActivity activity, String submissionJson) {
        Intent commentsIntent = new Intent(activity, CommentsActivity.class);
        commentsIntent.putExtra(SUBMISSION_COMMENT_EXTRA, submissionJson);
        activity.startActivity(commentsIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(SUBMISSION_COMMENT_EXTRA)) {
            String submissionJson = getIntent().getStringExtra(SUBMISSION_COMMENT_EXTRA);
            try {
                mSubmission = new Submission(new ObjectMapper().readTree(submissionJson));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mCommentsViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(CommentsViewModel.class);

        mCommentsAdapter = new CommentsAdapter(this);
        mRvComments.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        setupToolBar();

        subscribeDataStream();
    }

    private void subscribeDataStream() {
        mCommentsViewModel.getComments(mSubmission).observe(this, pagedLists -> {
            //Log.v(TAG, "subscribeDataStreams: " + pagedLists.size());

            if (pagedLists == null) {
                Toast.makeText(this, "Sorry! No data!", Toast.LENGTH_LONG).show();
                return;
            } else if (pagedLists.size() == 0) {
                showLoading();
            } else {
                mRvComments.setAdapter(mCommentsAdapter);
                showCommentsView();
            }
            mCommentsAdapter.submitList(pagedLists);
        });
    }

    private void showCommentsView() {
        mRvComments.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        mRvComments.setVisibility(View.GONE);
        mPbLoading.setVisibility(View.VISIBLE);
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.comments_title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
