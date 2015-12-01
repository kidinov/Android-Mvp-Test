package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Component;
import uk.co.ribot.androidboilerplate.injection.module.DefaultSchedulersModule;
import uk.co.ribot.androidboilerplate.util.SchedulerApplier;

@Component(modules = DefaultSchedulersModule.class)
public interface DefaultSchedulersComponent {

    void inject(SchedulerApplier.DefaultSchedulers defaultSchedulers);

}
