package uk.co.ribot.androidboilerplate.data.local;

import android.content.Context;
import android.database.Cursor;

import uk.co.ribot.androidboilerplate.data.model.Ribot;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class DatabaseHelper {

    private BriteDatabase mDb;

    public DatabaseHelper(Context context) {
        mDb = SqlBrite.create().wrapDatabaseHelper(new DbOpenHelper(context));
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    /**
     * Remove all the data from all the tables in the database.
     */
    public Observable<Void> clearTables() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    Cursor cursor = mDb.query("SELECT name FROM sqlite_master WHERE type='table'");
                    while (cursor.moveToNext()) {
                        mDb.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                    }
                    cursor.close();
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Ribot> setRibots(final Collection<Ribot> newRibots) {
        return Observable.create(new Observable.OnSubscribe<Ribot>() {
            @Override
            public void call(Subscriber<? super Ribot> subscriber) {
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    deleteAllRibotsApartFrom(newRibots);
                    for (Ribot ribot : newRibots) {
                        long result = mDb.insert(Db.RibotsTable.TABLE_NAME,
                                Db.RibotsTable.toContentValues(ribot));
                        if (result >= 0) subscriber.onNext(ribot);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Ribot>> getRibots() {
        return mDb.createQuery(Db.RibotsTable.TABLE_NAME,
                "SELECT * FROM " + Db.RibotsTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, Ribot>() {
                    @Override
                    public Ribot call(Cursor cursor) {
                        return Db.RibotsTable.parseCursor(cursor);
                    }
                });
    }

    private void deleteAllRibotsApartFrom(Collection<Ribot> ribotsToKeep) {
        if (ribotsToKeep.isEmpty()) {
            mDb.delete(Db.RibotsTable.TABLE_NAME, null);
        } else {
            mDb.delete(Db.RibotsTable.TABLE_NAME,
                    Db.RibotsTable.COLUMN_ID +
                            " NOT IN (" + createPlaceholders(ribotsToKeep.size()) + ")",
                    Ribot.getIds(ribotsToKeep));
        }
    }

    private String createPlaceholders(int length) {
        if (length < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(length * 2 - 1);
            sb.append('?');
            for (int i = 1; i < length; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

}
