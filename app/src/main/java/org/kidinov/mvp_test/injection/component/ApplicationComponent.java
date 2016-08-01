package org.kidinov.mvp_test.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

import org.kidinov.mvp_test.data.DataManager;
import org.kidinov.mvp_test.data.SyncService;
import org.kidinov.mvp_test.injection.annotation.ApplicationContext;
import org.kidinov.mvp_test.injection.module.ApplicationModule;
import org.kidinov.mvp_test.injection.module.NetworkModule;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext
    Context context();

    Application application();

    DataManager dataManager();

}
