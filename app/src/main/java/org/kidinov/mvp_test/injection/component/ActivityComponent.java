package org.kidinov.mvp_test.injection.component;

import org.kidinov.mvp_test.injection.annotation.PerActivity;
import org.kidinov.mvp_test.injection.module.ActivityModule;
import org.kidinov.mvp_test.ui.insta.InstaActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(InstaActivity o);

}
