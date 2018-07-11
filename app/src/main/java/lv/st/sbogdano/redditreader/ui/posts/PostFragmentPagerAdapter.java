package lv.st.sbogdano.redditreader.ui.posts;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;

public class PostFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<SubredditEntry> mSubredditEntries;

    public PostFragmentPagerAdapter(FragmentManager fm, List<SubredditEntry> list) {
        super(fm);
        mSubredditEntries = list;
    }

    @Override
    public Fragment getItem(int position) {
        return PostsFragment.newInstance(mSubredditEntries.get(position));
    }

    @Override
    public int getCount() {
        return mSubredditEntries.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mSubredditEntries.get(position).getSubredditName();

    }
}
