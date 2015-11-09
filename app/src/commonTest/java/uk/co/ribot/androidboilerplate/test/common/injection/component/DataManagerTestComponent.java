package uk.co.ribot.androidboilerplate.test.common.injection.component;

import dagger.Component;
import uk.co.ribot.androidboilerplate.injection.component.DataManagerComponent;
import uk.co.ribot.androidboilerplate.injection.scope.PerDataManager;
import uk.co.ribot.androidboilerplate.test.common.injection.module.DataManagerTestModule;

@PerDataManager
@Component(dependencies = TestComponent.class, modules = DataManagerTestModule.class)
public interface DataManagerTestComponent extends DataManagerComponent {
}
