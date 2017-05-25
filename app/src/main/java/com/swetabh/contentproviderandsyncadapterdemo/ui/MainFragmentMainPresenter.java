package com.swetabh.contentproviderandsyncadapterdemo.ui;

import com.swetabh.contentproviderandsyncadapterdemo.BasePresenterImp;

/**
 * Created by swets on 15-05-2017.
 */

public class MainFragmentMainPresenter extends BasePresenterImp<MainContract.FragmentMainViewContract>
        implements MainContract.FragmentMainPresenterContract {

    @Override
    public void onInsertClicked() {
        super.onInsertClicked();
        if(mView!=null){
            mView.createTodoItem();
        }
    }
}
