package lv.st.sbogdano.redditreader.ui.subreddits;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.viewmodels.SubredditViewModel;
import lv.st.sbogdano.redditreader.viewmodels.ViewModelFactory;

public class SubredditActivity extends AppCompatActivity {

    @BindView(R.id.rv_subreddits)
    RecyclerView mRvSubreddits;
    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private SubredditAdapter mSubredditAdapter;
    private SubredditViewModel mSubredditViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit);
        ButterKnife.bind(this);

        mSubredditViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(SubredditViewModel.class);

        mSubredditAdapter = new SubredditAdapter(this, mSubredditViewModel, new ArrayList<>());

        setupToolBar();

        subscribeDataStreams();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.my_subreddits_title);
        }
    }

    public void subscribeDataStreams() {
        mSubredditViewModel.getSubreddits().observe(this, list -> {
            //Log.v(TAG, "subscribeDataStreams: "  + list.size());
            if (list == null) {
                Toast.makeText(this, "Sorry! No data!", Toast.LENGTH_LONG).show();
            } else if (list.size() == 0) {
                showLoading();
            } else {
                showSubredditsView();
                mRvSubreddits.setAdapter(mSubredditAdapter);
                mSubredditAdapter.swapSubredditList(list);
            }
        });
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

    private void showSubredditsView() {
        mRvSubreddits.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        mRvSubreddits.setVisibility(View.GONE);
        mPbLoading.setVisibility(View.VISIBLE);
    }


}
