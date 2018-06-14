package lv.st.sbogdano.redditreader.data.network;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import lv.st.sbogdano.redditreader.util.InjectorUtils;

public class PostsFirebaseJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        PostsNetworkDataSource postsNetworkDataSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        postsNetworkDataSource.fetchPosts();
        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
