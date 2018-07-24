package lv.st.sbogdano.redditreader.data.database.submission;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import io.reactivex.Maybe;

@Dao
public interface SubmissionDao {

    @Query("SELECT * FROM submissions WHERE submissionId = :id")
    Maybe<SubmissionEntry> getSubmission(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubmission(SubmissionEntry submissionEntry);
}
