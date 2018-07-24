package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.http.oauth.Credentials;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.ui.login.LoginActivity;
import lv.st.sbogdano.redditreader.ui.subreddits.SubredditActivity;
import lv.st.sbogdano.redditreader.ui.widget.PostWidgetManager;
import lv.st.sbogdano.redditreader.viewmodels.SubredditViewModel;
import lv.st.sbogdano.redditreader.viewmodels.ViewModelFactory;

public class PostsActivity extends AppCompatActivity {

    private static final String TAG = PostsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.loading)
    ProgressBar mLoading;
    @BindView(R.id.adView)
    AdView mAdView;

    private SubredditViewModel mSubredditViewModel;
    private PostFragmentPagerAdapter mPagerAdapter;

    private boolean mIsInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mSubredditViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(SubredditViewModel.class);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void subscribeDataStreams() {
        mSubredditViewModel.getSubreddits().observe(this, list -> {

            if (list == null) {
                Toast.makeText(this, "Sorry! No data!", Toast.LENGTH_LONG).show();
            } else if (list.size() == 0) {
                showLoading();
            } else {
                showData();
                addToWidget(list);
                mPagerAdapter = new PostFragmentPagerAdapter(getSupportFragmentManager(), list);
                mViewPager.setAdapter(mPagerAdapter);
                mViewPager.setOffscreenPageLimit(list.size());
                mTabs.setupWithViewPager(mViewPager);
            }
        });
    }

    private void addToWidget(List<SubredditEntry> subredditEntries) {
        PostWidgetManager postWidgetManager = new PostWidgetManager();
        postWidgetManager.updatePostWidget(subredditEntries);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsInitialized) {
            return;
        }
        checkAuthenticationState();
    }

    private void checkAuthenticationState() {
        AuthenticationState state = AuthenticationManager.get().checkAuthState();
        switch (state) {
            case READY:
                subscribeDataStreams();
                mIsInitialized = true;
                break;
            case NONE:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case NEED_REFRESH:
                refreshAccessTokenAsync();
                mIsInitialized = true;
                break;
        }
    }

    private void refreshAccessTokenAsync() {
        new AsyncTask<Credentials, Void, Void>() {
            @Override
            protected Void doInBackground(Credentials... credentials) {
                try {
                    AuthenticationManager.get().refreshAccessToken(LoginActivity.CREDENTIALS);
                } catch (Exception e) {
                    Log.e(TAG, "Could not refresh token!", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                subscribeDataStreams();
            }
        }.execute();
    }

    private void showLoading() {
        mViewPager.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
    }

    private void showData() {
        mViewPager.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.posts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_subreddits:
                startActivity(new Intent(this, SubredditActivity.class));
                return true;
        }
        return true;
    }

}
