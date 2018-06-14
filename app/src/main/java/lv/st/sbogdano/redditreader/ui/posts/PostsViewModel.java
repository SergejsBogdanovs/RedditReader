package lv.st.sbogdano.redditreader.ui.posts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import lv.st.sbogdano.redditreader.data.PostsRepository;
import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;

public class PostsViewModel extends ViewModel{

    private final PostsRepository mPostsRepository;
    private final LiveData<List<PostEntry>> mPosts;

    public PostsViewModel(PostsRepository postsRepository) {
        mPostsRepository = postsRepository;
        mPosts = mPostsRepository.getCurrentPosts();
    }

    public LiveData<List<PostEntry>> getPosts() {
        return mPosts;
    }

    public void refreshPosts() {
        mPostsRepository.refreshPosts();
    }
}
