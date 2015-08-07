package uk.co.ribot.androidboilerplate.injection;

import android.support.test.InstrumentationRegistry;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.remote.RibotsService;
import uk.co.ribot.androidboilerplate.injection.component.DaggerTestComponent;
import uk.co.ribot.androidboilerplate.injection.component.TestComponent;
import uk.co.ribot.androidboilerplate.injection.module.ApplicationTestModule;
import uk.co.ribot.androidboilerplate.util.TestDataManager;

/**
 * Test rule that creates and sets a Dagger TestComponent into the application overriding the
 * existing application component.
 * Use this rule in your test case in order for the app to use mock dependencies.
 * It also exposes some of the dependencies so they can be easily accessed from the tests, e.g. to
 * stub mocks etc.
 */
public class TestComponentRule implements TestRule {

    private TestComponent mTestComponent;

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
        if (application.getComponent() instanceof TestComponent) {
            mTestComponent = (TestComponent) application.getComponent();
        } else {
            mTestComponent = DaggerTestComponent.builder()
                    .applicationTestModule(new ApplicationTestModule(application))
                    .build();
            application.setComponent(mTestComponent);
        }
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
