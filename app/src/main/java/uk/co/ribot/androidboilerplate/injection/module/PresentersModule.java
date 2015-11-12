package uk.co.ribot.androidboilerplate.injection.module;

import android.content.Context;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;
import uk.co.ribot.androidboilerplate.injection.scope.PerActivity;
import uk.co.ribot.androidboilerplate.ui.main.MainPresenter;

/**
 * This module provides instances of Presenters.
 */
@Module
public class PresentersModule {

    private WeakReference<Context> mContextWeakRef;

    public PresentersModule(Context context) {
        mContextWeakRef = new WeakReference<>(context);
    }

    @Provides
    @PerActivity
    MainPresenter providesMainPresenter() {
        return new MainPresenter(mContextWeakRef.get());
    }

}
