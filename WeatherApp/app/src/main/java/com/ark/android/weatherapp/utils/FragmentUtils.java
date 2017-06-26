package com.ark.android.weatherapp.utils;

import android.app.Fragment;
import android.app.FragmentManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Ark on 6/26/2017.
 */

public class FragmentUtils {

    private final List<WeakReference<Fragment>> fragList = new ArrayList<>();

    public void onAttachFragment (Fragment fragment) {
        fragList.add(new WeakReference<>(fragment));
    }


    private List<Fragment> getActiveFragments() {
        ArrayList<Fragment> ret = new ArrayList<>();
        for(WeakReference<Fragment> ref : fragList) {
            Fragment f = ref.get();
            if(f != null) {
                if(!f.isHidden()) {
                    ret.add(f);
                }
            }
        }
        return ret;
    }

    public Fragment getCurrentTopFragment(FragmentManager fm) {
        int stackCount = fm.getBackStackEntryCount();

        if (stackCount > 0) {
            FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(stackCount-1);
            return  fm.findFragmentByTag(backEntry.getName());
        } else {
            List<Fragment> fragments = getActiveFragments();
            if (fragments != null && fragments.size()>0) {
                for (Fragment f: fragments) {
                    if (f != null && !f.isHidden()) {
                        return f;
                    }
                }
            }
        }
        return null;
    }
}
