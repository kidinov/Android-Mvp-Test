package org.kidinov.mvp_test.injection.component;

import dagger.Subcomponent;
import org.kidinov.mvp_test.injection.annotation.PerActivity;
import org.kidinov.mvp_test.ui.main.MainActivity;
import org.kidinov.mvp_test.injection.module.ActivityModule;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
