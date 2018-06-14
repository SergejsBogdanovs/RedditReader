package lv.st.sbogdano.redditreader.ui.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.ViewHolder> {

    @NonNull
    @Override
    public SubredditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SubredditAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
