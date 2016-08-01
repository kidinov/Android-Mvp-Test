package org.kidinov.mvp_test.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import org.kidinov.mvp_test.injection.component.ApplicationComponent;
import org.kidinov.mvp_test.test.common.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {
}
