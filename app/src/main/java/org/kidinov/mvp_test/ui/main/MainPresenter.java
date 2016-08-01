package org.kidinov.mvp_test.ui.main;

import javax.inject.Inject;

import rx.Subscription;
import timber.log.Timber;
import org.kidinov.mvp_test.data.DataManager;
import org.kidinov.mvp_test.injection.annotation.ConfigPersistent;
import org.kidinov.mvp_test.ui.base.BasePresenter;
import org.kidinov.mvp_test.util.RxUtil;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {
    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadRibots() {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.getRibots()
                .subscribe(ribots -> {
                    if (ribots.isEmpty()) {
                        getMvpView().showRibotsEmpty();
                    } else {
                        getMvpView().showRibots(ribots);
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
                    getMvpView().showError();
                });
    }

}
