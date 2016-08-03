package org.kidinov.mvp_test.data;

import android.support.annotation.UiThread;

import org.kidinov.mvp_test.data.local.DatabaseHelper;
import org.kidinov.mvp_test.data.local.PreferencesHelper;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.data.remote.InstaService;
import org.kidinov.mvp_test.util.ListUtil;
import org.kidinov.mvp_test.util.RxUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import timber.log.Timber;

@Singleton
public class DataManager {
    private final InstaService instaService;
    private final DatabaseHelper databaseHelper;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(InstaService instaService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        this.instaService = instaService;
        this.preferencesHelper = preferencesHelper;
        this.databaseHelper = databaseHelper;
    }

    public Observable<List<InstaItem>> getOldFeedItemsFromServer() {
        List<InstaItem> saved = databaseHelper.getSavedInstaFeedItemsSortedByTime();
        String minId = saved.isEmpty() ? null : saved.get(saved.size() - 1).getId();
        return instaService.getInstaFeed(minId)
                .compose(RxUtil.applySchedulers())
                .flatMap(feed -> databaseHelper.saveInstaFeed(feed.getInstaItems()));
    }

    public Observable<List<InstaItem>> getFeedItemsFromServer() {
        return instaService.getInstaFeed(null)
                .compose(RxUtil.applySchedulers())
                .flatMap(feed -> {
                    if (!ListUtil.containsAll(databaseHelper.getSavedInstaFeedItemsSortedByTime(), feed.getInstaItems())) {
                        Timber.d("clearInstaFeed from db");
                        databaseHelper.clearInstaFeed();
                    }
                    return databaseHelper.saveInstaFeed(feed.getInstaItems());
                });
    }

    @UiThread
    public Observable<List<InstaItem>> subscribeOnFeedItemsChanges() {
        return databaseHelper.getSavedInstaFeedItemsObservable().distinct();
    }

    @UiThread
    public int getSavedFeedItemsCount() {
        return databaseHelper.getSavedInstaFeedItemsSortedByTime().size();
    }

}
