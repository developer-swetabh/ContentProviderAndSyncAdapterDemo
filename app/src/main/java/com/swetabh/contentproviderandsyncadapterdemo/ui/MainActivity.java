package com.swetabh.contentproviderandsyncadapterdemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.swetabh.contentproviderandsyncadapterdemo.utils.ActivityUtils;
import com.swetabh.contentproviderandsyncadapterdemo.BasePresenter;
import com.swetabh.contentproviderandsyncadapterdemo.BaseView;
import com.swetabh.contentproviderandsyncadapterdemo.R;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements MainContract.ActivityCommunicator {

    private Toolbar toolbar;
    private Menu mMenu;
    private ArrayList<Integer> mMenuItemsToEnable = new ArrayList<>();
    private BasePresenter mPresenter;
    private Stack<BasePresenter> mPresenterStack;
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        // Set up the toolbar.
        mPresenterStack = new Stack<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCurrentFragment = (MainFragment) mFragmentManager.findFragmentById(R.id.contentFrame);

        if (mCurrentFragment == null) {
            mCurrentFragment = MainFragment.newInstance();
            mPresenter = new MainFragmentMainPresenter();
            attachCommunicator();
            attachView();
            mPresenterStack.push(mPresenter);
            ActivityUtils.addFragmentToActivity(
                    mFragmentManager, mCurrentFragment, MainContract.MAIN_FRAGMENT);
        }

    }

    @Override
    public void updateTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listmenu, menu);
        this.mMenu = menu;

        menuItemsToEnable(mMenuItemsToEnable.toArray(new Integer[mMenuItemsToEnable.size()]));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                mPresenter.onInsertClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void menuItemsToEnable(Integer... menuItems) {
        mMenuItemsToEnable.clear();
        disableAllMenuItems();
        for (Integer menuItem : menuItems) {
            mMenuItemsToEnable.add(menuItem);
            if (mMenu != null && mMenu.findItem(menuItem) != null)
                mMenu.findItem(menuItem).setVisible(true);
        }
    }

    private void disableAllMenuItems() {
        if (mMenu != null) {
            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setVisible(false);
            }
        }
    }

    @Override
    public void openDetailFragment() {
        disableAllMenuItems();
        mCurrentFragment = TodoDetailFragment.newInstance();
        mPresenter = new TodoDetailPresenter();
        attachCommunicator();
        attachView();
        mPresenterStack.push(mPresenter);
        ActivityUtils.addFragmentToActivity(
                mFragmentManager, mCurrentFragment, MainContract.DETAIL_FRAGMENT);

    }


    private void attachCommunicator() {
        if (mCurrentFragment != null && mCurrentFragment instanceof BaseView)
            ((BaseView) mCurrentFragment).setCommunicator(this);
    }

    private void attachView() {
        if (mPresenter != null)
            mPresenter.attachView((BaseView) mCurrentFragment);
    }

    @Override
    public void onBackPressed() {
        onBackPressed(false);

    }


    public void onBackPressed(boolean isToPopClear) {
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0 || backStackEntryCount == 1) {
            finish();
        } else {
            String fragmentTag = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 2).getName();
            mCurrentFragment = mFragmentManager.findFragmentByTag(fragmentTag);
            if (!mPresenterStack.isEmpty()) {
                mPresenterStack.pop();
                mPresenter = mPresenterStack.peek();
                if (!isToPopClear) {
                    attachCommunicator();
                    attachView();
                }
            }
            if (isToPopClear)
                mFragmentManager.popBackStackImmediate(mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            else
                super.onBackPressed();
        }

    }
}
