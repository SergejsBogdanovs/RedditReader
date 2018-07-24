package lv.st.sbogdano.redditreader.viewmodels;

import android.arch.lifecycle.ViewModel;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.VoteDirection;

import io.reactivex.Maybe;
import lv.st.sbogdano.redditreader.data.RedditDataRepository;
import lv.st.sbogdano.redditreader.data.database.submission.SubmissionEntry;
import lv.st.sbogdano.redditreader.ui.login.LoginActivity;

public class PostDetailViewModel extends ViewModel {

    private final RedditDataRepository mRedditDataRepository;

    public PostDetailViewModel(RedditDataRepository redditDataRepository) {
        mRedditDataRepository = redditDataRepository;
    }

    public void votePost(Submission submission, VoteDirection voteDirection) {

        final AccountManager manager = new AccountManager(AuthenticationManager.get().getRedditClient());

        new Thread(() -> {
            try {
                AuthenticationState state = AuthenticationManager.get().checkAuthState();
                if (state == AuthenticationState.NEED_REFRESH) {
                    AuthenticationManager.get().refreshAccessToken(LoginActivity.CREDENTIALS);
                }
                manager.vote(submission, voteDirection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void saveSubmissionId(String submissionId, boolean isPostLiked) {
        mRedditDataRepository.saveSubmissionId(submissionId, isPostLiked);
    }

    public Maybe<SubmissionEntry> getSubmissionId(String id) {
        return mRedditDataRepository.getSubmission(id);
    }
}
