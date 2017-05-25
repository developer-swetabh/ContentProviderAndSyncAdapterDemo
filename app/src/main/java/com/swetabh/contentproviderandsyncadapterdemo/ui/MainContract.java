package com.swetabh.contentproviderandsyncadapterdemo.ui;

import com.swetabh.contentproviderandsyncadapterdemo.BaseActivityCommunicator;
import com.swetabh.contentproviderandsyncadapterdemo.BasePresenter;
import com.swetabh.contentproviderandsyncadapterdemo.BaseView;

/**
 * Created by swets on 15-05-2017.
 */

public interface MainContract {


    String DETAIL_FRAGMENT = "detail_fragment";
    String MAIN_FRAGMENT = "main_fragment";

    interface ActivityCommunicator extends BaseActivityCommunicator {

        void openDetailFragment();

        void onBackPressed(boolean b);
    }

    interface FragmentMainPresenterContract extends BasePresenter {
    }

    interface FragmentMainViewContract extends BaseView<FragmentMainPresenterContract, ActivityCommunicator> {

        void createTodoItem();
    }

    // For detail fragment
    interface FragmentDetailPresenterContract extends BasePresenter {
    }

    interface FragmentDetailViewContract extends BaseView<FragmentDetailPresenterContract, ActivityCommunicator> {

    }

    // For detail fragment
    interface SyncAdapterPresenterContract extends BasePresenter {
    }

    interface SyncAdapterViewContract extends BaseView<SyncAdapterPresenterContract, ActivityCommunicator> {

    }
}
