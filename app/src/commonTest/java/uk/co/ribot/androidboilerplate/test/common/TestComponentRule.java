package uk.co.ribot.androidboilerplate.test.common;

import android.app.Application;
import android.support.test.InstrumentationRegistry;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.remote.RibotsService;
import uk.co.ribot.androidboilerplate.test.common.injection.component.DaggerTestComponent;
import uk.co.ribot.androidboilerplate.test.common.injection.component.TestComponent;
import uk.co.ribot.androidboilerplate.test.common.injection.module.ApplicationTestModule;

/**
 * Test rule that creates and sets a Dagger TestComponent into the application overriding the
 * existing application component.
 * Use this rule in your test case in order for the app to use mock dependencies.
 * It also exposes some of the dependencies so they can be easily accessed from the tests, e.g. to
 * stub mocks etc.
 */
public class TestComponentRule implements TestRule {

    private TestComponent mTestComponent;
    private boolean mMockableDataManager;

    /**
     * If mockableDataManager is true, it will crate a data manager using Mockito.spy()
     * Spy objects call real methods unless they are stubbed. So the DataManager will work as
     * usual unless an specific method is mocked.
     * A full mock DataManager is not an option because there are several methods that still
     * need to return the real value, i.e dataManager.getSubscribeScheduler()
     */
    public TestComponentRule(boolean mockableDataManager) {
        mMockableDataManager = mockableDataManager;
    }

    public TestComponentRule() {
        mMockableDataManager = false;
    }

    public TestComponent getTestComponent() {
        return mTestComponent;
    }

    public TestDataManager getDataManager() {
        return (TestDataManager) mTestComponent.dataManager();
    }

    public RibotsService getMockRibotsService() {
        return getDataManager().getRibotsService();
    }

    public DatabaseHelper getDatabaseHelper() {
        return getDataManager().getDatabaseHelper();
    }

    public PreferencesHelper getPreferencesHelper() {
        return getDataManager().getPreferencesHelper();
    }

    private void setupDaggerTestComponentInApplication() {
        BoilerplateApplication application = BoilerplateApplication
                .get(InstrumentationRegistry.getTargetContext());
        ApplicationTestModule module = new ApplicationTestModule(application,
                mMockableDataManager);
        mTestComponent = DaggerTestComponent.builder()
                .applicationTestModule(module)
                .build();
        application.setComponent(mTestComponent);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    setupDaggerTestComponentInApplication();
                    base.evaluate();
                } finally {
                    mTestComponent = null;
                }
            }
        };
    }
}
