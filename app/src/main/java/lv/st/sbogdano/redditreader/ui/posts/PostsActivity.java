package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.http.oauth.Credentials;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.ui.login.LoginActivity;
import lv.st.sbogdano.redditreader.ui.subreddits.SubredditActivity;
import lv.st.sbogdano.redditreader.viewmodels.PostsViewModel;
import lv.st.sbogdano.redditreader.viewmodels.ViewModelFactory;

public class PostsActivity extends AppCompatActivity {

    private static final String TAG = PostsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.posts_recycler_view)
    RecyclerView mPostsRecyclerView;
    @BindView(R.id.postsLL)
    LinearLayout mPostsLL;
    @BindView(R.id.noPosts)
    LinearLayout mNoPosts;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mPbLoadingIndicator;

    private boolean mFirstLoad = true;

    private PostsViewModel mPostsViewModel;
    private PostsAdapter mPostsAdapter = new PostsAdapter();

    private Parcelable mState;
    private static Bundle mBundleRecyclerViewState;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout_state";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        ButterKnife.bind(this);

        mPostsViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(PostsViewModel.class);

        setupToolBar();
        setupPostsAdapter();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupPostsAdapter() {
        mPostsRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAuthenticationState();
        if (mBundleRecyclerViewState != null) {
            mState = mBundleRecyclerViewState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mPostsRecyclerView.getLayoutManager().onRestoreInstanceState(mState);
        }
        mPostsAdapter.submitList(null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        mState = mPostsRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mState);
    }

    private void checkAuthenticationState() {
        AuthenticationState state = AuthenticationManager.get().checkAuthState();
        Log.v(TAG, "checkAuthenticationState: " + state.toString());
        switch (state) {
            case READY:
                subscribeDataStreams(mFirstLoad);
                mFirstLoad = false;
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
                subscribeDataStreams(true);
            }
        }.execute();
    }

    private void subscribeDataStreams(boolean firstLoad) {
        mPostsRecyclerView.setAdapter(mPostsAdapter);
        mPostsViewModel.getPosts(firstLoad).observe(this, pagedLists -> {
            Log.v(TAG, "subscribeDataStreams: " + pagedLists.size());
            if (pagedLists != null && pagedLists.size() != 0) {
                showPostView();
            } else {
                showEmptyList();
                mPostsAdapter.submitList(null);
                return;
            }
            mPostsAdapter.submitList(pagedLists);
        });
    }

    private void showEmptyList() {
        mNoPosts.setVisibility(View.VISIBLE);
        mPostsRecyclerView.setVisibility(View.GONE);
    }

    private void showLoading() {
        mPostsLL.setVisibility(View.INVISIBLE);
        mNoPosts.setVisibility(View.INVISIBLE);
        mPbLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showPostView() {
        mNoPosts.setVisibility(View.GONE);
        mPostsRecyclerView.setVisibility(View.VISIBLE);
        mPostsRecyclerView.getLayoutManager().onRestoreInstanceState(mState);
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
