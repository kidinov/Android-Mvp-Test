package uk.co.ribot.androidboilerplate.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.ribot.androidboilerplate.injection.component.ApplicationComponent;
import uk.co.ribot.androidboilerplate.test.common.injection.module.ApplicationTestModule;
import uk.co.ribot.androidboilerplate.test.common.injection.module.DefaultSchedulersTestModule;

@Singleton
@Component(modules = {ApplicationTestModule.class, DefaultSchedulersTestModule.class})
public interface TestComponent extends ApplicationComponent {

}
