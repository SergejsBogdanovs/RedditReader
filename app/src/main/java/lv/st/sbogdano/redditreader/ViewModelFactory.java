package lv.st.sbogdano.redditreader;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import lv.st.sbogdano.redditreader.data.PostsRepository;
import lv.st.sbogdano.redditreader.ui.posts.PostsViewModel;
import lv.st.sbogdano.redditreader.ui.settings.SubredditViewModel;
import lv.st.sbogdano.redditreader.util.InjectorUtils;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final PostsRepository mPostsRepository;

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(
                            InjectorUtils.providePostsRepository(application.getApplicationContext()));
                }
            }
        }
        return INSTANCE;
    }

    private ViewModelFactory(PostsRepository postsRepository) {
        this.mPostsRepository = postsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PostsViewModel.class)) {
            return (T) new PostsViewModel(mPostsRepository);
        } else if (modelClass.isAssignableFrom(SubredditViewModel.class)) {
            return (T) new SubredditViewModel(mPostsRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}

