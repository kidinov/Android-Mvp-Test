package uk.co.ribot.androidboilerplate.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.ui.adapter.RibotsAdapter;
import uk.co.ribot.androidboilerplate.util.SchedulerAppliers;
import uk.co.ribot.androidboilerplate.data.SyncService;
import uk.co.ribot.androidboilerplate.data.model.Ribot;

public class MainActivity extends BaseActivity {

    private CompositeSubscription mSubscriptions;
    private RibotsAdapter mRibotsAdapter;

    @Inject
    DataManager mDataManager;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationComponent().inject(this);
        startService(SyncService.getStartIntent(this));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSubscriptions = new CompositeSubscription();
        mRibotsAdapter = new RibotsAdapter();
        mRecyclerView.setAdapter(mRibotsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadRibots();
    }

    @Override
    protected void onDestroy() {
        mSubscriptions.unsubscribe();
        super.onDestroy();
    }

    private void loadRibots() {
        mSubscriptions.add(mDataManager.getRibots()
                .compose(SchedulerAppliers.<List<Ribot>>defaultSchedulers(this))
                .subscribe(new Subscriber<List<Ribot>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                    }

                    @Override
                    public void onNext(List<Ribot> ribots) {
                        mRibotsAdapter.setRibots(ribots);
                        mRibotsAdapter.notifyDataSetChanged();
                    }
                }));
    }

}
