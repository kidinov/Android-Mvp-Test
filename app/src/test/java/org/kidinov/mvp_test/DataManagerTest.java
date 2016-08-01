package org.kidinov.mvp_test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;
import org.kidinov.mvp_test.data.DataManager;
import org.kidinov.mvp_test.data.local.DatabaseHelper;
import org.kidinov.mvp_test.data.local.PreferencesHelper;
import org.kidinov.mvp_test.data.model.Ribot;
import org.kidinov.mvp_test.data.remote.RibotsService;
import org.kidinov.mvp_test.test.common.TestDataFactory;
import org.kidinov.mvp_test.util.RxSchedulersOverrideRule;

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
    RibotsService mockRibotsService;
    private DataManager dataManager;

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        dataManager = new DataManager(mockRibotsService, mockPreferencesHelper, mockDatabaseHelper);
    }

    @Test
    public void syncRibotsEmitsValues() {
        List<Ribot> ribots = Arrays.asList(TestDataFactory.makeRibot("r1"),
                TestDataFactory.makeRibot("r2"));
        stubSyncRibotsHelperCalls(ribots);

        TestSubscriber<Ribot> result = new TestSubscriber<>();
        dataManager.syncRibots().subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(ribots);
    }

    @Test
    public void syncRibotsCallsApiAndDatabase() {
        List<Ribot> ribots = Arrays.asList(TestDataFactory.makeRibot("r1"),
                TestDataFactory.makeRibot("r2"));
        stubSyncRibotsHelperCalls(ribots);

        dataManager.syncRibots().subscribe();
        // Verify right calls to helper methods
        verify(mockRibotsService).getRibots();
        verify(mockDatabaseHelper).saveRibots(ribots);
    }

    @Test
    public void syncRibotsDoesNotCallDatabaseWhenApiFails() {
        when(mockRibotsService.getRibots())
                .thenReturn(Observable.error(new RuntimeException()));

        dataManager.syncRibots().subscribe(new TestSubscriber<>());
        // Verify right calls to helper methods
        verify(mockRibotsService).getRibots();
        verify(mockDatabaseHelper, never()).saveRibots(anyListOf(Ribot.class));
    }

    private void stubSyncRibotsHelperCalls(List<Ribot> ribots) {
        // Stub calls to the ribot service and database helper.
        when(mockRibotsService.getRibots())
                .thenReturn(Observable.just(ribots));
        when(mockDatabaseHelper.saveRibots(ribots))
                .thenReturn(Observable.just(ribots));
    }

}
