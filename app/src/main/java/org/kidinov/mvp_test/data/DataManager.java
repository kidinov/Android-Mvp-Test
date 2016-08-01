package org.kidinov.mvp_test.data;

import android.support.annotation.UiThread;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import org.kidinov.mvp_test.data.local.DatabaseHelper;
import org.kidinov.mvp_test.data.local.PreferencesHelper;
import org.kidinov.mvp_test.data.model.Ribot;
import org.kidinov.mvp_test.data.remote.RibotsService;
import org.kidinov.mvp_test.util.RxUtil;

@Singleton
public class DataManager {
    private final RibotsService ribotsService;
    private final DatabaseHelper databaseHelper;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        this.ribotsService = ribotsService;
        this.preferencesHelper = preferencesHelper;
        this.databaseHelper = databaseHelper;
    }

    public Observable<Ribot> syncRibots() {
        return ribotsService.getRibots()
                .compose(RxUtil.applySchedulers())
                .flatMap(ribots -> databaseHelper.saveRibots(ribots)
                        .flatMapIterable(x -> x));
    }

    @UiThread
    public Observable<List<Ribot>> getRibots() {
        return databaseHelper.getRibots().distinct();
    }

}
