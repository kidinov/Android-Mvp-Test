package uk.co.ribot.androidboilerplate.util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rx.observers.TestSubscriber;

public class RxEventBusTest {
    private RxEventBus eventBus;

    @Rule
    // Must be added to every test class that targets app code that uses RxJava
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        eventBus = new RxEventBus();
    }

    @Test
    public void postedObjectsAreReceived() {
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        eventBus.observable().subscribe(testSubscriber);

        Object event1 = new Object();
        Object event2 = new Object();
        eventBus.post(event1);
        eventBus.post(event2);

        testSubscriber.assertValues(event1, event2);
    }

    @Test
    public void filteredObservableOnlyReceivesSomeObjects() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        eventBus.filteredObservable(String.class).subscribe(testSubscriber);

        String stringEvent = "Event";
        Integer intEvent = 20;
        eventBus.post(stringEvent);
        eventBus.post(intEvent);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(stringEvent);
    }
}