package lv.st.sbogdano.redditreader.ui.subreddits;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.viewmodels.SubredditViewModel;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.SubredditsViewHolder> {

    private SubredditViewModel mSubredditViewModel;
    private Context mContext;
    private List<SubredditEntry> mSubredditEntries;

    SubredditAdapter(Context context, SubredditViewModel subredditViewModel, List<SubredditEntry> subredditEntries) {
        mSubredditViewModel = subredditViewModel;
        mContext = context;
        mSubredditEntries = subredditEntries;
    }

    @NonNull
    @Override
    public SubredditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subreddit_item, parent, false);
        return new SubredditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubredditsViewHolder holder, int position) {
        SubredditEntry subredditEntry = mSubredditEntries.get(position);
        holder.mSubredditTitle.setText(subredditEntry.getSubredditName());

        holder.mBtnUnsubsribe.setOnClickListener(item -> {
            mSubredditViewModel.deleteSubscription(subredditEntry);
            ((SubredditActivity)mContext).subscribeDataStreams();
        });
    }

    @Override
    public int getItemCount() {
        if (mSubredditEntries != null) {
            return mSubredditEntries.size();
        }
        return 0;
    }

    public void swapSubredditList(List<SubredditEntry> list) {
        mSubredditEntries = list;
        notifyDataSetChanged();
    }


    class SubredditsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.subreddit_title)
        TextView mSubredditTitle;
        @BindView(R.id.btn_unsubscribe)
        Button mBtnUnsubsribe;

        private SubredditsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
