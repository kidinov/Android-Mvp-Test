package org.kidinov.mvp_test.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;
import org.kidinov.mvp_test.data.local.DatabaseHelper;
import org.kidinov.mvp_test.data.local.RmMigration;
import org.kidinov.mvp_test.injection.annotation.ApplicationContext;
import org.kidinov.mvp_test.util.RxEventBus;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    private static final int DATABASE_VERSION = 1;
    private final Application application;

    public ApplicationModule(Application application) {
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

    @Provides
    @Singleton
    Realm provideRealm(RealmConfiguration config) {
        Realm.setDefaultConfiguration(config);
        try {
            return Realm.getDefaultInstance();
        } catch (Exception e) {
            Timber.e(e, "");
            Realm.deleteRealm(config);
            Realm.setDefaultConfiguration(config);
            return Realm.getDefaultInstance();
        }
    }

    @Provides
    @Singleton
    RealmConfiguration provideRealmConfig(Application application) {
        return new RealmConfiguration.Builder(application)
                .schemaVersion(DATABASE_VERSION)
                .migration(new RmMigration())
                .build();
    }

    @Provides
    @Singleton
    RxEventBus provideRxEventBus() {
        return new RxEventBus();
    }

    @Provides
    @Singleton
    DatabaseHelper provideDatabaseHelper(Realm realm) {
        return new DatabaseHelper(realm);
    }

}
