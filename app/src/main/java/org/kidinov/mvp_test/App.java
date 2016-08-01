package org.kidinov.mvp_test;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;
import org.kidinov.mvp_test.injection.component.ApplicationComponent;

import org.kidinov.mvp_test.injection.component.DaggerApplicationComponent;
import org.kidinov.mvp_test.injection.module.ApplicationModule;

public class App extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
