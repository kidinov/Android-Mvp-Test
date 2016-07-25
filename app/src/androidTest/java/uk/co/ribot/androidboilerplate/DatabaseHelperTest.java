package uk.co.ribot.androidboilerplate;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.functions.Action0;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.model.Ribot;
import uk.co.ribot.androidboilerplate.test.common.TestDataFactory;

import static junit.framework.Assert.assertEquals;
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
    public void saveRibots() {
        Ribot ribot1 = TestDataFactory.makeRibot("r1");
        Ribot ribot2 = TestDataFactory.makeRibot("r2");
        List<Ribot> ribots = Arrays.asList(ribot1, ribot2);

        runOnUi(() -> {
            databaseHelper.saveRibots(ribots)
                    .subscribe(r -> {
                        assertTrue(r.retainAll(ribots));
                    }, e -> {
                        Timber.e("", e);
                        Assert.fail();
                    });

            List<Ribot> savedRibots = defaultInstance.where(Ribot.class).findAll();
            assertEquals(savedRibots.size(), 2);

            for (int i = 0; i < ribots.size(); i++) {
                assertEquals(ribots.get(i).compareTo(savedRibots.get(i)), 0);
            }
        });
    }

    @Test
    public void getRibots() {
        Ribot ribot1 = TestDataFactory.makeRibot("r1");
        Ribot ribot2 = TestDataFactory.makeRibot("r2");
        List<Ribot> ribots = Arrays.asList(ribot1, ribot2);

        runOnUi(() -> {
            compositeSubscription.add(databaseHelper.saveRibots(ribots).subscribe());

            compositeSubscription.add(databaseHelper.getRibots()
                    .subscribe(r -> {
                        assertTrue(r.retainAll(ribots));
                    }, e -> {
                        Timber.e("", e);
                        Assert.fail();
                    }));
        });
    }

    private void runOnUi(Action0 action) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(action::call);
    }
}