package uk.co.ribot.androidboilerplate.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import uk.co.ribot.androidboilerplate.injection.annotation.ApplicationContext;

@Singleton
public class PreferencesHelper {
    private static final String PREF_FILE_NAME = "android_boilerplate_pref_file";

    private final SharedPreferences pref;

    @Inject
    PreferencesHelper(@ApplicationContext Context context) {
        pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        pref.edit().clear().apply();
    }

}
