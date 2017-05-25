package com.swetabh.contentproviderandsyncadapterdemo.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.swetabh.contentproviderandsyncadapterdemo.R;

import static android.R.attr.tag;

/**
 * Created by swets on 15-05-2017.
 */

public class ActivityUtils {


    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, String tag) {

        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }
}
