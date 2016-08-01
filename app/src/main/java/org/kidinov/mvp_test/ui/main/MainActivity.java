package org.kidinov.mvp_test.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.kidinov.mvp_test.R;
import org.kidinov.mvp_test.data.SyncService;
import org.kidinov.mvp_test.data.model.Ribot;
import org.kidinov.mvp_test.ui.base.BaseActivity;
import org.kidinov.mvp_test.util.DialogFactory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements MainMvpView {
    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "uk.co.androidboilerplate.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    MainPresenter mainPresenter;
    @Inject
    RibotsAdapter ribotsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    /*
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setAdapter(ribotsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainPresenter.attachView(this);
        mainPresenter.loadRibots();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mainPresenter.detachView();
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showRibots(List<Ribot> ribots) {
        ribotsAdapter.setRibots(ribots);
        ribotsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(1)).show();
    }

    @Override
    public void showRibotsEmpty() {
        ribotsAdapter.setRibots(Collections.emptyList());
        ribotsAdapter.notifyDataSetChanged();
        Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    }

}
