package lv.st.sbogdano.redditreader.data.database.subreddits;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "subreddits",
        indices = {@Index(value = {"subredditName"}, unique = true)})
public class SubredditEntry implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String subredditName;

    public SubredditEntry(int id, String subredditName) {
        this.id = id;
        this.subredditName = subredditName;
    }

    @Ignore
    public SubredditEntry(String subredditName) {
        this.subredditName = subredditName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public void setSubredditName(String subredditName) {
        this.subredditName = subredditName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.subredditName);
    }

    protected SubredditEntry(Parcel in) {
        this.id = in.readInt();
        this.subredditName = in.readString();
    }

    public static final Creator<SubredditEntry> CREATOR = new Creator<SubredditEntry>() {
        @Override
        public SubredditEntry createFromParcel(Parcel source) {
            return new SubredditEntry(source);
        }

        @Override
        public SubredditEntry[] newArray(int size) {
            return new SubredditEntry[size];
        }
    };
}
