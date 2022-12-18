package com.app.abcdadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.abcdadmin.adapters.UserAdapters;
import com.app.abcdadmin.models.User;

import java.util.ArrayList;


public class BaseFragment extends Fragment {

    public RecyclerView mRecyclerView;
    public ArrayList<User> mUsers;
    public UserAdapters userAdapters;
    public Activity mActivity;
    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = getContext();
    }


}