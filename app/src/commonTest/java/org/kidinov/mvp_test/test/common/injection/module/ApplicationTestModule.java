package org.kidinov.mvp_test.test.common.injection.module;

import android.app.Application;
import android.content.Context;

import org.kidinov.mvp_test.data.DataManager;
import org.kidinov.mvp_test.data.remote.InstaService;
import org.kidinov.mvp_test.injection.annotation.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {

    private final Application application;

    public ApplicationTestModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    /*************
     * MOCKS
     *************/

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return mock(DataManager.class);
    }

    @Provides
    @Singleton
    InstaService provideInstaService() {
        return mock(InstaService.class);
    }

}
