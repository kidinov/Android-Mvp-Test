package org.kidinov.mvp_test.ui.base;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
