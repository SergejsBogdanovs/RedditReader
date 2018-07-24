package lv.st.sbogdano.redditreader.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import lv.st.sbogdano.redditreader.data.RedditDataRepository;
import lv.st.sbogdano.redditreader.util.InjectorUtils;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final RedditDataRepository mRedditDataRepository;

    public static ViewModelFactory getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(
                            InjectorUtils.providePostsRepository(context));
                }
            }
        }
        return INSTANCE;
    }

    private ViewModelFactory(RedditDataRepository redditDataRepository) {
        this.mRedditDataRepository = redditDataRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PostsViewModel.class)) {
            return (T) new PostsViewModel(mRedditDataRepository);
        } else if (modelClass.isAssignableFrom(SubredditViewModel.class)) {
            return (T) new SubredditViewModel(mRedditDataRepository);
        } else if (modelClass.isAssignableFrom(CommentsViewModel.class)) {
            return (T) new CommentsViewModel(mRedditDataRepository);
        } else if (modelClass.isAssignableFrom(PostDetailViewModel.class)) {
            return (T) new PostDetailViewModel(mRedditDataRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}

