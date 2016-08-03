package org.kidinov.mvp_test.ui.insta;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.kidinov.mvp_test.R;
import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.ui.base.BaseActivity;
import org.kidinov.mvp_test.ui.generic.EndlessRecyclerOnScrollListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstaActivity extends BaseActivity implements InstaView {
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;

    @Inject
    InstaAdapter instaAdapter;
    @Inject
    InstaPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_insta);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupList();
        setupSwipeRefreshLayout();

        presenter.attachView(this);
        presenter.subscribeToFeed();
        presenter.reloadItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detachView();
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            onRetryClick();
        });
    }

    private void setupList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(instaAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {

            @Override
            public void onLoadMore() {
                presenter.loadMore();
            }
        });
    }

    @OnClick(R.id.retry_button)
    void onRetryClick() {
        presenter.reloadItems();
    }

    @Override
    public void showProgress() {
        changeScreenState(R.id.progress_view);
    }

    @Override
    public void showProgressOfPagination() {
        instaAdapter.showLoading(true);
    }

    @Override
    public void showFeed(List<InstaItem> items) {
        instaAdapter.addInstaItems(items);
        changeScreenState(swipeRefreshLayout);
    }

    @Override
    public void showError() {
        changeScreenState(R.id.error_view);
    }

    @Override
    public void hideRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void stopPaginationLoading() {
        instaAdapter.showLoading(false);
    }

    @Override
    public void notifyError() {
        Toast.makeText(this, R.string.cant_get_feed, Toast.LENGTH_LONG).show();
    }

    private void changeScreenState(int state) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(viewFlipper.findViewById(state)));
    }

    private void changeScreenState(View state) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(state));
    }
}
