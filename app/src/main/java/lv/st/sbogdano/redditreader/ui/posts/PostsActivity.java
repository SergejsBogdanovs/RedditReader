package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.models.Submission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.ViewModelFactory;
import lv.st.sbogdano.redditreader.ui.login.LoginActivity;
import lv.st.sbogdano.redditreader.ui.settings.SubredditActivity;

public class PostsActivity extends AppCompatActivity
        implements PostsAdapter.PostAdapterOnItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = PostsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pager_strip)
    PagerTabStrip mPagerStrip;
    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.posts_recycler_view)
    RecyclerView mPostsRecyclerView;
    @BindView(R.id.postsLL)
    LinearLayout mPostsLL;
    @BindView(R.id.noPostsIcon)
    ImageView mNoPostsIcon;
    @BindView(R.id.noPostsText)
    TextView mNoPostsText;
    @BindView(R.id.noPosts)
    LinearLayout mNoPosts;

    private List<Submission> posts = new ArrayList<>();
    private PostsViewModel mPostsViewModel;
    private PostsAdapter mPostsAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        ButterKnife.bind(this);

        // Set up the toolbar.
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ViewModelFactory viewModelFactory
                = ViewModelFactory.getInstance(getApplication());
        mPostsViewModel = ViewModelProviders.of(this, viewModelFactory).get(PostsViewModel.class);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setupPostsAdapter();
        subscribeDataStreams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreferences.registerOnSharedPreferenceChangeListener(this);
        checkAuthenticationState();
    }


    private void setupPostsAdapter() {
        mPostsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPostsRecyclerView.setLayoutManager(layoutManager);
        mPostsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mPostsRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                subscribeDataStreams();
            }
        });
        mPostsAdapter = new PostsAdapter(this, this);
        mPostsRecyclerView.setAdapter(mPostsAdapter);
    }

    private void subscribeDataStreams() {

        // Getting Posts
        mPostsViewModel.getPosts().observe(this, postEntries -> {
            mPostsAdapter.swapPosts(postEntries);
            if (mPosition == RecyclerView.NO_POSITION) {
                mPosition = 0;
            }
            mPostsRecyclerView.smoothScrollToPosition(mPosition);

            if (postEntries != null && postEntries.size() != 0) {
                Log.v(TAG, "subscribeDataStreams: " + postEntries.size());
                showPostView();
            } else {
                showNoPostsView();
            }
        });

        // Getting Subreddits

    }

    private void checkAuthenticationState() {
        AuthenticationState state = AuthenticationManager.get().checkAuthState();
        switch (state) {
            case READY:
                if (posts.size() == 0) {
                    subscribeDataStreams();
                }
                break;
            case NONE:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case NEED_REFRESH:
                refreshAccessTokenAsync();
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

    private void showNoPostsView() {
        mPostsLL.setVisibility(View.INVISIBLE);
        mNoPosts.setVisibility(View.VISIBLE);
    }

    private void showPostView() {
        mNoPosts.setVisibility(View.INVISIBLE);
        mPostsLL.setVisibility(View.VISIBLE);
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

    @Override
    public void onItemClick() {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
