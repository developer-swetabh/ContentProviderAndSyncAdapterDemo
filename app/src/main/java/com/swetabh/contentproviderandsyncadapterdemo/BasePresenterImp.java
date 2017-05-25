package com.swetabh.contentproviderandsyncadapterdemo;

/**
 * Created by swets on 15-05-2017.
 */

public class BasePresenterImp<V extends BaseView> implements BasePresenter {

    public V mView;

    @Override
    public void start() {
        if (mView != null) {
            mView.updateActionBar();
        }

    }

    @Override
    public void attachView(BaseView view) {
        mView = (V) view;
        if (mView != null) {
            mView.setPresenter(this);
        }
    }

    @Override
    public void onInsertClicked() {

    }
}
