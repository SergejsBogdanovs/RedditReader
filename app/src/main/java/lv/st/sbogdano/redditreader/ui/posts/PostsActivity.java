package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    private SubredditViewModel mSubredditViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        ButterKnife.bind(this);

        setupToolBar();

        mSubredditViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(SubredditViewModel.class);
    }

    private void subscribeDataStreams() {
        mSubredditViewModel.getSubreddits().observe(this, this::setupViewPager);
    }

    private void setupViewPager(List<SubredditEntry> list) {
        Log.v(TAG, "setupViewPager: " + list.size() );
        mViewPager.setAdapter(new PostFragmentPagerAdapter(getSupportFragmentManager(), list));
        mViewPager.setOffscreenPageLimit(1);
        mTabs.setupWithViewPager(mViewPager);
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState() {
        AuthenticationState state = AuthenticationManager.get().checkAuthState();
        Log.v(TAG, "checkAuthenticationState: " + state.toString());
        switch (state) {
            case READY:
                subscribeDataStreams();
                break;
            case NONE:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case NEED_REFRESH:
                refreshAccessTokenAsync();
                break;
        }
    }

    public void refreshAccessTokenAsync() {
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
