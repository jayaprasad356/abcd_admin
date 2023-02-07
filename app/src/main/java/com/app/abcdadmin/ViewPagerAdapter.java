package com.app.abcdadmin;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter
        extends FragmentPagerAdapter {

    public ViewPagerAdapter(
            @NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if (position == 0)
            fragment = new LoginForJoiningsFragment();
        else if (position == 1)
            fragment = new LoginForQueriesFragment();
        else if (position == 2)
            fragment = new SuperAdminFragment();
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "Login For Joinings";
        else if (position == 1)
            title = "Login For Queries";
        else if (position == 2)
            title = "Login For Super Admin";
        return title;
    }
}
