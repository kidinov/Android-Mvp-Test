package org.kidinov.mvp_test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kidinov.mvp_test.data.DataManager;
import org.kidinov.mvp_test.data.local.DatabaseHelper;
import org.kidinov.mvp_test.data.local.PreferencesHelper;
import org.kidinov.mvp_test.data.model.InstaFeed;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.data.remote.InstaService;
import org.kidinov.mvp_test.test.common.TestDataFactory;
import org.kidinov.mvp_test.util.ListUtil;
import org.kidinov.mvp_test.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock
    DatabaseHelper mockDatabaseHelper;
    @Mock
    PreferencesHelper mockPreferencesHelper;
    @Mock
    InstaService mockInstaService;
    private DataManager dataManager;

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        dataManager = new DataManager(mockInstaService, mockPreferencesHelper, mockDatabaseHelper);
    }

    @Test
    public void getOldFeedItemsFromServerSaveData() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 20, 1);
        stubInstaServiceAndDbHelper(feed);

        TestSubscriber<List<InstaItem>> result = new TestSubscriber<>();
        dataManager.getOldFeedItemsFromServer().subscribe(result);
        result.assertNoErrors();
        List<InstaItem> nextEvent = result.getOnNextEvents().get(0);
        assertTrue(ListUtil.compareInstaItemLists(feed.getInstaItems(), nextEvent));
    }

    @Test
    public void getOldFeedItemsFromServerCallsApiAndDb() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 20, 1);
        stubInstaServiceAndDbHelper(feed);

        dataManager.getOldFeedItemsFromServer().subscribe();
        verify(mockInstaService).getInstaFeed(null);
        verify(mockDatabaseHelper).getSavedInstaFeedItemsSortedByTime();
        verify(mockDatabaseHelper).saveInstaFeed(feed.getInstaItems());
    }

    @Test
    public void getOldFeedItemsFromServerNotCallsDbIfApiFailed() {
        when(mockInstaService.getInstaFeed(null))
                .thenReturn(Observable.error(new RuntimeException()));

        dataManager.getOldFeedItemsFromServer().subscribe(new TestSubscriber<>());
        verify(mockInstaService).getInstaFeed(null);
        verify(mockDatabaseHelper, never()).saveInstaFeed(anyListOf(InstaItem.class));
    }

    @Test
    public void getFeedItemsFromServerSaveProperDateInDb() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 20, 1);
        stubInstaServiceAndDbHelper(feed);

        TestSubscriber<List<InstaItem>> result = new TestSubscriber<>();
        dataManager.getFeedItemsFromServer().subscribe(result);
        result.assertNoErrors();
        List<InstaItem> nextEvent = result.getOnNextEvents().get(0);
        assertTrue(ListUtil.compareInstaItemLists(feed.getInstaItems(), nextEvent));
    }

    @Test
    public void getFeedItemsFromServerSaveCallsApiAndDb() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 20, 1);
        stubInstaServiceAndDbHelper(feed);

        dataManager.getFeedItemsFromServer().subscribe();
        verify(mockInstaService).getInstaFeed(null);
        verify(mockDatabaseHelper).clearInstaFeed();
        verify(mockDatabaseHelper).saveInstaFeed(feed.getInstaItems());
    }

    @Test
    public void getFeedItemsFromServerNotCallDbIfApiFailed() {
        when(mockInstaService.getInstaFeed(null))
                .thenReturn(Observable.error(new RuntimeException()));

        dataManager.getFeedItemsFromServer().subscribe(new TestSubscriber<>());
        verify(mockInstaService).getInstaFeed(null);
        verify(mockDatabaseHelper, never()).saveInstaFeed(anyListOf(InstaItem.class));
        verify(mockDatabaseHelper, never()).clearInstaFeed();
    }

    @Test
    public void getFeedItemsFromServerNotClearDbIfNoNewItems() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 20, 1);
        stubInstaServiceAndDbHelper(feed);

        when(mockDatabaseHelper.getSavedInstaFeedItemsSortedByTime())
                .thenReturn(feed.getInstaItems());

        dataManager.getFeedItemsFromServer().subscribe();
        verify(mockInstaService).getInstaFeed(null);
        verify(mockDatabaseHelper).saveInstaFeed(anyListOf(InstaItem.class));
        verify(mockDatabaseHelper, never()).clearInstaFeed();
    }

    private void stubInstaServiceAndDbHelper(InstaFeed feed) {
        when(mockInstaService.getInstaFeed(null))
                .thenReturn(Observable.just(feed));
        when(mockDatabaseHelper.saveInstaFeed(feed.getInstaItems()))
                .thenReturn(Observable.just(feed.getInstaItems()));
    }

}
