package im.dacer.kata.core.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

/**
 * Created by Dacer on 13/02/2018.
 */

@AutoValue
public abstract class History implements Parcelable, HistoryModel {

    public static final Factory<History> FACTORY =
            new Factory<>(new HistoryModel.Creator<History>() {
                @Override
                public History create(long id, @Nullable String text) {
                    return new AutoValue_History(id, text);
                }
            });

    public static final RowMapper<History> SELECT_ALL_MAPPER = FACTORY.select_allMapper();

}