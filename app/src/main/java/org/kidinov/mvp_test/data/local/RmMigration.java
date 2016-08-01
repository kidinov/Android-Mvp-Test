package org.kidinov.mvp_test.data.local;

import javax.inject.Singleton;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import timber.log.Timber;

@Singleton
public class RmMigration implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        Timber.i("migration from %d to %d", oldVersion, newVersion);
    }
}