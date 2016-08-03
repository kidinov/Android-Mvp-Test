package org.kidinov.mvp_test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kidinov.mvp_test.data.DataManager;
import org.kidinov.mvp_test.data.model.InstaFeed;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.test.common.TestDataFactory;
import org.kidinov.mvp_test.ui.insta.InstaPresenter;
import org.kidinov.mvp_test.ui.insta.InstaView;
import org.kidinov.mvp_test.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import rx.Observable;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstaPresenterTest {

    @Mock
    InstaView mockInstaView;
    @Mock
    DataManager mockDataManager;

    private InstaPresenter mainPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mainPresenter = new InstaPresenter(mockDataManager);
        mainPresenter.attachView(mockInstaView);
    }

    @After
    public void tearDown() {
        mainPresenter.detachView();
    }

    @Test
    public void reloadItemsAndShowProgress() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 10, 1);
        when(mockDataManager.getFeedItemsFromServer())
                .thenReturn(Observable.just(feed.getInstaItems()));

        mainPresenter.reloadItems();
        verify(mockInstaView).showProgress();
        verify(mockInstaView, never()).showError();
    }

    @Test
    public void reloadItemsAndNotShowProgressOfThereIsItemsInDb() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 10, 1);
        when(mockDataManager.getFeedItemsFromServer())
                .thenReturn(Observable.just(feed.getInstaItems()));
        when(mockDataManager.getSavedFeedItemsCount())
                .thenReturn(1);

        mainPresenter.reloadItems();
        verify(mockInstaView, never()).showProgress();
        verify(mockInstaView, never()).showError();
    }

    @Test
    public void reloadItemsAndShowErrorIfErrorHappened() {
        when(mockDataManager.getFeedItemsFromServer())
                .thenReturn(Observable.error(new RuntimeException()));

        mainPresenter.reloadItems();
        verify(mockInstaView, never()).showFeed(anyListOf(InstaItem.class));
        verify(mockInstaView).showProgress();
        verify(mockInstaView).showError();
    }

    @Test
    public void loadMoreShowProgressItem() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 10, 1);
        when(mockDataManager.getOldFeedItemsFromServer())
                .thenReturn(Observable.just(feed.getInstaItems()));

        mainPresenter.loadMore();
        verify(mockInstaView, never()).showFeed(feed.getInstaItems());
        verify(mockInstaView, never()).showError();
        verify(mockInstaView).showProgressOfPagination();
        verify(mockInstaView).stopPaginationLoading();
    }

    @Test
    public void loadMoreDoesntStartLoadingIfFirstNotFinished() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 10, 1);
        when(mockDataManager.getOldFeedItemsFromServer())
                .thenReturn(Observable.just(feed.getInstaItems())
                        .delay(1, TimeUnit.SECONDS));

        mainPresenter.loadMore();
        mainPresenter.loadMore();
        verify(mockInstaView, only()).showProgressOfPagination();
        verify(mockDataManager, only()).getOldFeedItemsFromServer();
    }

    @Test
    public void loadMoreCallNotifyErrorIfErrorHappened() {
        when(mockDataManager.getOldFeedItemsFromServer())
                .thenReturn(Observable.error(new RuntimeException()));

        mainPresenter.loadMore();
        verify(mockInstaView).notifyError();
        verify(mockInstaView, never()).showError();
    }

    @Test
    public void subscribeToFeedReturnsFeed() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 10, 1);
        when(mockDataManager.subscribeOnFeedItemsChanges())
                .thenReturn(Observable.just(feed.getInstaItems()));

        mainPresenter.subscribeToFeed();
        verify(mockInstaView).showProgress();
        verify(mockInstaView).showFeed(feed.getInstaItems());
        verify(mockInstaView, never()).showError();
    }

    @Test
    public void subscribeToFeedReturnsNothingIfThereIsNoItems() {
        when(mockDataManager.subscribeOnFeedItemsChanges())
                .thenReturn(Observable.empty());

        mainPresenter.subscribeToFeed();
        verify(mockInstaView).showProgress();
        verify(mockInstaView, never()).showFeed(anyListOf(InstaItem.class));
        verify(mockInstaView, never()).showError();
    }

    @Test
    public void subscribeToShowErrorIfErrorHappened() {
        when(mockDataManager.subscribeOnFeedItemsChanges())
                .thenReturn(Observable.error(new RuntimeException()));

        mainPresenter.subscribeToFeed();
        verify(mockInstaView).showProgress();
        verify(mockInstaView, never()).showFeed(anyListOf(InstaItem.class));
        verify(mockInstaView).showError();
    }

}