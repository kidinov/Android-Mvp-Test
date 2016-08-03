package org.kidinov.mvp_test.data.local;

import android.support.annotation.NonNull;

import org.kidinov.mvp_test.data.model.InstaFeed;
import org.kidinov.mvp_test.data.model.InstaItem;

import java.util.List;

import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import timber.log.Timber;

@Singleton
public class DatabaseHelper {
    private final Realm realm;
    private final static int MAX_SAVED_FEED_SIZE = 100;

    public DatabaseHelper(Realm realm) {
        this.realm = realm;
    }

    public void clearInstaFeed() {
        realm.executeTransaction(realm -> realm.delete(InstaFeed.class));
    }

    public Observable<List<InstaItem>> saveInstaFeed(final List<InstaItem> instaItems) {
        Timber.d("saveInstaFeed items - %d", instaItems.size());
        return Observable.create(subscriber -> {
            realm.executeTransaction(realm -> {
                subscriber.onNext(realm.copyToRealmOrUpdate(instaItems));
                subscriber.onCompleted();
            });
        });
    }

    @NonNull
    public Observable<List<InstaItem>> getSavedInstaFeedItemsObservable() {
        RealmResults<InstaItem> list = (RealmResults<InstaItem>) getSavedInstaFeedItemsSortedByTime();
        if (list.size() > MAX_SAVED_FEED_SIZE) {
            Timber.d("getSavedInstaFeedItemsObservable removing extra items");
            RealmResults<InstaItem> results = realm.where(InstaItem.class).findAllSorted("createdTime");
            Long oldestItem = results.get(MAX_SAVED_FEED_SIZE).getCreatedTime();
            realm.executeTransaction(realm ->
                    realm.where(InstaItem.class).lessThan("createdTime", oldestItem)
                            .findAllSorted("created").deleteAllFromRealm());
        }
        return list.asObservable().flatMap(items -> {
            Timber.d("getSavedInstaFeedItemsObservable local items - %d", items.size());
            return Observable.just(realm.copyFromRealm(items));
        });
    }

    public List<InstaItem> getSavedInstaFeedItemsSortedByTime() {
        return realm.where(InstaItem.class).findAllSorted("createdTime", Sort.DESCENDING);
    }

}
