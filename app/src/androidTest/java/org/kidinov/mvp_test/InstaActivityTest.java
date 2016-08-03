package org.kidinov.mvp_test;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.kidinov.mvp_test.data.DataManager;
import org.kidinov.mvp_test.data.model.InstaFeed;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.test.common.TestComponentRule;
import org.kidinov.mvp_test.test.common.TestDataFactory;
import org.kidinov.mvp_test.ui.insta.InstaActivity;
import org.kidinov.mvp_test.ui.insta.InstaAdapter;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.kidinov.mvp_test.util.EspressoHelpers.waitId;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class InstaActivityTest {

    private final TestComponentRule component = new TestComponentRule(InstrumentationRegistry.getTargetContext());
    private final ActivityTestRule<InstaActivity> instaActivityActivityTestRule =
            new ActivityTestRule<>(InstaActivity.class, false, false);

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(instaActivityActivityTestRule);

    @Test
    public void listOfInstaItemsShows() {
        InstaFeed feed = TestDataFactory.makeInstaFeed("new", 20, 21);
        Collections.sort(feed.getInstaItems());
        Collections.reverse(feed.getInstaItems());

        InstaFeed oldFeed = TestDataFactory.makeInstaFeed("old", 20, 1);
        Collections.sort(oldFeed.getInstaItems());
        Collections.reverse(oldFeed.getInstaItems());
        DataManager mockDataManager = component.getMockDataManager();
        when(mockDataManager.subscribeOnFeedItemsChanges()).thenReturn(Observable.just(feed.getInstaItems()));
        when(mockDataManager.getSavedFeedItemsCount()).thenReturn(1);
        when(mockDataManager.getFeedItemsFromServer()).thenReturn(Observable.just(feed.getInstaItems()));
        when(mockDataManager.getOldFeedItemsFromServer()).thenReturn(Observable.just(oldFeed.getInstaItems())
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(x -> {
                    Timber.d("On next after delay");
                    when(mockDataManager.subscribeOnFeedItemsChanges())
                            .thenReturn(Observable.just(oldFeed.getInstaItems()));
                }));

        instaActivityActivityTestRule.launchActivity(null);

        int position = 0;
        for (InstaItem item : feed.getInstaItems()) {
            onView(withId(R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            onView(withText(item.getLocation().getName()))
                    .check(matches(isDisplayed()));
            onView(withText(InstaAdapter.dateFormat.format(new Date(item.getCreatedTime() * 1000))))
                    .check(matches(isDisplayed()));
            position++;
        }

        //TODO understand how to check this
        onView(withId(R.id.recycler_view))
                .perform(waitId(R.id.progress, 3000));

        for (InstaItem item : oldFeed.getInstaItems()) {
            onView(withId(R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            onView(withText(item.getLocation().getName()))
                    .check(matches(isDisplayed()));
            onView(withText(InstaAdapter.dateFormat.format(new Date(item.getCreatedTime() * 1000))))
                    .check(matches(isDisplayed()));
            position++;
        }
    }

}