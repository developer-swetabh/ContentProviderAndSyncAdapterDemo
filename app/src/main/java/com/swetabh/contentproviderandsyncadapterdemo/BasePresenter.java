package com.swetabh.contentproviderandsyncadapterdemo;

/**
 * Created by swets on 15-05-2017.
 */

public interface BasePresenter {

    void start();

    void attachView(BaseView view);

    void onInsertClicked();
}
