package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.ui.details.PostDetailActivity;
import lv.st.sbogdano.redditreader.viewmodels.PostsViewModel;
import lv.st.sbogdano.redditreader.viewmodels.ViewModelFactory;

public class PostsFragment extends Fragment implements PostViewHolder.PostsAdapterOnItemClickHandler {

    private static final String TAG = PostsFragment.class.getSimpleName();

    private static final String ARG_PARAM = "subreddit";

    private static final String POST_SCROLL_POSITION = "post_scroll_position";

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

    private PostsViewModel mPostsViewModel;
    private PostsAdapter mPostsAdapter = new PostsAdapter(this);


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            mSubredditEntry = getArguments().getParcelable(ARG_PARAM);
        }

        subscribeDataStreams();

        // Restore scroll position
        if (savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray(POST_SCROLL_POSITION);
            if (position != null)
                mPostsRecyclerView.post(() -> mPostsRecyclerView.scrollTo(position[0], position[1]));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Saving scroll position
        outState.putIntArray(POST_SCROLL_POSITION,
                new int[]{mPostsRecyclerView.getScrollX(), mPostsRecyclerView.getScrollY()});
    }

    private void subscribeDataStreams() {
        mPostsViewModel.getPosts(mSubredditEntry).observe(this, pagedLists -> {
            if (pagedLists != null && pagedLists.size() != 0) {
                mPostsRecyclerView.setAdapter(mPostsAdapter);
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

    private void showPostView() {
        mNoPosts.setVisibility(View.GONE);
        mPostsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(String submissionDataNode) {
        PostDetailActivity.start((AppCompatActivity) getActivity(), submissionDataNode);
    }

}
