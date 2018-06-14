package lv.st.sbogdano.redditreader.ui.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.ViewModelFactory;

public class SubredditActivity extends AppCompatActivity {

    public static final String TAG = SubredditActivity.class.getSimpleName();

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

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSubredditAdapter = new SubredditAdapter();
        mRvSubreddits.setLayoutManager(new LinearLayoutManager(this));
        mRvSubreddits.setHasFixedSize(true);
        mRvSubreddits.setAdapter(mSubredditAdapter);

        ViewModelFactory viewModelFactory
                = ViewModelFactory.getInstance(getApplication());
        mSubredditViewModel = ViewModelProviders.of(this, viewModelFactory).get(SubredditViewModel.class);

        mSubredditViewModel.getSubreddits().observe(this, subredditEntries -> {
            Log.v(TAG, "my subreddits " + subredditEntries.size());
            //mSubredditAdapter.swapSubreddits();
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

}
