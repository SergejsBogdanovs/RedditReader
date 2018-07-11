package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.http.oauth.Credentials;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.ui.login.LoginActivity;
import lv.st.sbogdano.redditreader.viewmodels.PostsViewModel;
import lv.st.sbogdano.redditreader.viewmodels.ViewModelFactory;

public class PostsFragment extends Fragment {

    private static final String TAG = PostsFragment.class.getSimpleName();

    private static final String ARG_PARAM = "subreddit";

    @BindView(R.id.posts_recycler_view)
    RecyclerView mPostsRecyclerView;
    @BindView(R.id.postsLL)
    LinearLayout mPostsLL;
    @BindView(R.id.noPostsText)
    TextView mNoPostsText;
    @BindView(R.id.noPosts)
    LinearLayout mNoPosts;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mPbLoadingIndicator;
    Unbinder unbinder;

    private SubredditEntry mSubredditEntry;

    private Parcelable mState;
    private static Bundle mBundleRecyclerViewState;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout_state";

    private PostsViewModel mPostsViewModel;
    private PostsAdapter mPostsAdapter = new PostsAdapter();


    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance(SubredditEntry entry) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, entry);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubredditEntry = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        unbinder = ButterKnife.bind(this, view);

        mPostsViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(getContext()))
                .get(PostsViewModel.class);

        mPostsRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        subscribeDataStreams();
        if (mBundleRecyclerViewState != null) {
            mState = mBundleRecyclerViewState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mPostsRecyclerView.getLayoutManager().onRestoreInstanceState(mState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        mState = mPostsRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mState);
    }

    private void subscribeDataStreams() {
        mPostsRecyclerView.setAdapter(mPostsAdapter);
        mPostsViewModel.getPosts(mSubredditEntry).observe(this, pagedLists -> {
            Log.v(TAG, "subscribeDataStreams: " + pagedLists.size());
            if (pagedLists != null && pagedLists.size() != 0) {
                showPostView();
            } else {
                showEmptyList();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
