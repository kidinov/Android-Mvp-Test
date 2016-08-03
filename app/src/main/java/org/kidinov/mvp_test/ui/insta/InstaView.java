package org.kidinov.mvp_test.ui.insta;


import org.kidinov.mvp_test.data.model.InstaItem;
import org.kidinov.mvp_test.ui.base.MvpView;

import java.util.List;

public interface InstaView extends MvpView {
    void showProgress();

    void showProgressOfPagination();

    void showFeed(List<InstaItem> items);

    void showError();

    void hideRefresh();

    void stopPaginationLoading();

    void notifyError();
}
