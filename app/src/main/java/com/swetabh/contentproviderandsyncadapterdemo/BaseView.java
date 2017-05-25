package com.swetabh.contentproviderandsyncadapterdemo;

/**
 * Created by swets on 15-05-2017.
 */

public interface BaseView<T, V> {
    void setPresenter(T presenter);

    void setCommunicator(V communicator);

   void updateActionBar();
}
