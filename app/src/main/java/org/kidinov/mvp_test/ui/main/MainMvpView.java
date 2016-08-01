package org.kidinov.mvp_test.ui.main;

import java.util.List;

import org.kidinov.mvp_test.data.model.Ribot;
import org.kidinov.mvp_test.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
