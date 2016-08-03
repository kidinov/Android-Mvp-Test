package org.kidinov.mvp_test.ui.insta;

import org.kidinov.mvp_test.data.DataManager;
import org.kidinov.mvp_test.injection.annotation.ConfigPersistent;
import org.kidinov.mvp_test.ui.base.BasePresenter;
import org.kidinov.mvp_test.util.RxUtil;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;


@ConfigPersistent
public class InstaPresenter extends BasePresenter<InstaView> {
    private DataManager dataManager;
    private CompositeSubscription compositeSubscription;

    private boolean loadingMore;

    @Inject
    public InstaPresenter(DataManager dataManager) {
        compositeSubscription = new CompositeSubscription();
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(InstaView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(compositeSubscription);
    }

    public void subscribeToFeed() {
        getMvpView().showProgress();
        compositeSubscription.add(dataManager.subscribeOnFeedItemsChanges()
                .subscribe(items -> {
                    Timber.d("items from storage - %d", items.size());
                    if (!items.isEmpty()) {
                        getMvpView().showFeed(items);
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading insta feed.");
                    getMvpView().showError();
                }));
    }

    public void loadMore() {
        if (loadingMore) {
            return;
        }
        loadingMore = true;
        getMvpView().showProgressOfPagination();
        compositeSubscription.add(dataManager.getOldFeedItemsFromServer()
                .doAfterTerminate(() -> {
                    loadingMore = false;
                    getMvpView().stopPaginationLoading();
                })
                .subscribe(x -> {
                    Timber.d("load more - items fetched");
                }, e -> {
                    getMvpView().notifyError();
                    Timber.e(e, "");
                }));
    }

    public void reloadItems() {
        if (dataManager.getSavedFeedItemsCount() == 0) {
            getMvpView().showProgress();
        }
        compositeSubscription.add(dataManager.getFeedItemsFromServer()
                .subscribe(x -> {
                    Timber.d("reloadItems - items fetched");
                    getMvpView().hideRefresh();
                }, e -> {
                    Timber.e(e, "");
                    if (dataManager.getSavedFeedItemsCount() == 0) {
                        getMvpView().showError();
                    } else {
                        getMvpView().notifyError();
                    }
                }));
    }
}
