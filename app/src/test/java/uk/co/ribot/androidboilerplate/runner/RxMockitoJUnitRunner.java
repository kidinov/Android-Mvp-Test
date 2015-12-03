package uk.co.ribot.androidboilerplate.runner;

import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Ensures that RxJava Observables always subscribe and observe on immediate() during testing.
 * This test runner extends the MockitoJUnitRunner tests runner and registers RxJava and RxAndroid
 * scheduler hooks so that it overrides the default schedulers returned.
 */
public class RxMockitoJUnitRunner extends MockitoJUnitRunner {

    public RxMockitoJUnitRunner(Class<?> paramClass) throws InvocationTargetException {
        super(paramClass);
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }

            @Override
            public Scheduler getNewThreadScheduler() {
                return Schedulers.immediate();
            }
        });

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }
}
