package uk.co.ribot.androidboilerplate.injection.component;

import dagger.Component;
import uk.co.ribot.androidboilerplate.injection.module.PresentersModule;
import uk.co.ribot.androidboilerplate.injection.scope.PerActivity;
import uk.co.ribot.androidboilerplate.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = PresentersModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
