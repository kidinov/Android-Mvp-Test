package uk.co.ribot.androidboilerplate.test.common;

import android.content.Context;

import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.remote.RibotsService;
import uk.co.ribot.androidboilerplate.test.common.injection.component.DaggerDataManagerTestComponent;
import uk.co.ribot.androidboilerplate.test.common.injection.component.TestComponent;
import uk.co.ribot.androidboilerplate.test.common.injection.module.DataManagerTestModule;

/**
 * Extension of DataManager to be used on a testing environment.
 * It uses DataManagerTestComponent to inject dependencies that are different to the
 * normal runtime ones. e.g. mock objects etc.
 * It also exposes some helpers like the DatabaseHelper or the Retrofit service that are helpful
 * during testing.
 */
public class TestDataManager extends DataManager {

    public TestDataManager(Context context) {
        super(context);
    }

    @Override
    protected void injectDependencies(Context context) {
        TestComponent testComponent = (TestComponent)
                BoilerplateApplication.get(context).getComponent();
        DaggerDataManagerTestComponent.builder()
                .testComponent(testComponent)
                .dataManagerTestModule(new DataManagerTestModule(context))
                .build()
                .inject(this);
    }

    public RibotsService getRibotsService() {
        return mRibotsService;
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }
}
