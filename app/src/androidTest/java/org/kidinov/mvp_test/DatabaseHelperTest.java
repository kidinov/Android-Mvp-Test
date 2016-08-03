package org.kidinov.mvp_test;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.kidinov.mvp_test.data.local.DatabaseHelper;
import org.kidinov.mvp_test.data.model.InstaFeed;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.test.common.TestDataFactory;
import org.kidinov.mvp_test.util.ListUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.functions.Action0;
import rx.observers.TestSubscriber;
import rx.subscriptions.CompositeSubscription;

import static junit.framework.Assert.assertTrue;

/**
 * Unit tests integration with
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {
    private DatabaseHelper databaseHelper;
    private Realm defaultInstance;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private CompositeSubscription compositeSubscription;

    @Before
    public void setup() throws IOException {
        File tempFolder = testFolder.newFolder("realmdata");
        runOnUi(() -> {
            RealmConfiguration config =
                    new RealmConfiguration.Builder(InstrumentationRegistry.getTargetContext(), tempFolder)
                            .build();
            defaultInstance = Realm.getInstance(config);
            databaseHelper = new DatabaseHelper(defaultInstance);

            compositeSubscription = new CompositeSubscription();
        });
    }

    @After
    public void release() {
        runOnUi(() -> {
            defaultInstance.close();
            compositeSubscription.unsubscribe();
        });
    }

    @Test
    public void saveInstaFeed() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 10, 0);

        runOnUi(() -> {
            TestSubscriber<List<InstaItem>> testObserver = new TestSubscriber<>();
            databaseHelper.saveInstaFeed(feed.getInstaItems())
                    .subscribe(testObserver);

            List<InstaItem> savedInstaItems = getSavedInstaItems();

            testObserver.assertNoErrors();
            List<List<InstaItem>> nextEvent = testObserver.getOnNextEvents();

            assertTrue(ListUtil.compareInstaItemLists(feed.getInstaItems(), nextEvent.get(0)));
            assertTrue(ListUtil.compareInstaItemLists(savedInstaItems, nextEvent.get(0)));
        });
    }

    @NonNull
    private RealmResults<InstaItem> getSavedInstaItems() {
        return defaultInstance.where(InstaItem.class).findAll();
    }

    @Test
    public void getSavedInstaFeedItemsObservable() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("", 10, 0);
        runOnUi(() -> {
            defaultInstance.executeTransaction(realm -> realm.copyToRealm(feed));

            TestSubscriber<List<InstaItem>> testObserver = new TestSubscriber<>();
            databaseHelper.getSavedInstaFeedItemsObservable()
                    .subscribe(testObserver);

            List<InstaItem> savedInstaItems = getSavedInstaItems();

            testObserver.assertNoErrors();
            List<List<InstaItem>> nextEvent = testObserver.getOnNextEvents();

            assertTrue(ListUtil.compareInstaItemLists(feed.getInstaItems(), nextEvent.get(0)));
            assertTrue(ListUtil.compareInstaItemLists(savedInstaItems, nextEvent.get(0)));
        });
    }

    private void runOnUi(Action0 action) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(action::call);
    }

}