package uk.co.ribot.androidboilerplate.data.local;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import uk.co.ribot.androidboilerplate.data.model.Ribot;

@Singleton
public class DatabaseHelper {
    private final Realm realm;

    public DatabaseHelper(Realm realm) {
        this.realm = realm;
    }

    public Observable<List<Ribot>> saveRibots(final Collection<Ribot> newRibots) {
        return Observable.create(subscriber -> {
            getSavedRibots().deleteAllFromRealm();
            realm.executeTransaction(realm -> {
                subscriber.onNext(realm.copyToRealm(newRibots));
                subscriber.onCompleted();
            });
        });
    }

    public Observable<List<Ribot>> getRibots() {
        return getSavedRibots().asObservable()
                .flatMap(ribots -> {
                    List<Ribot> list = new ArrayList<>();
                    for (Ribot ribot : ribots) {
                        list.add(ribot);
                    }
                    return Observable.just(list);
                });
    }

    @NonNull
    private RealmResults<Ribot> getSavedRibots() {
        return realm.where(Ribot.class).findAll();
    }

}
