package uk.co.ribot.androidboilerplate.injection.component;

import android.app.Application;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.SyncService;
import uk.co.ribot.androidboilerplate.injection.module.ApplicationModule;
import uk.co.ribot.androidboilerplate.injection.module.DefaultSchedulersModule;
import uk.co.ribot.androidboilerplate.ui.main.MainPresenter;
import uk.co.ribot.androidboilerplate.util.SchedulerApplier;

@Singleton
@Component(modules = {ApplicationModule.class, DefaultSchedulersModule.class})
public interface ApplicationComponent {

    void inject(SyncService syncService);
    void inject(SchedulerApplier.DefaultSchedulers defaultSchedulers);
    void inject(MainPresenter mainPresenter);

    Application application();
    DataManager dataManager();
    Bus eventBus();
}
