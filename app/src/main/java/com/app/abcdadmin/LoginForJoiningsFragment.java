package com.app.abcdadmin;

import static com.app.abcdadmin.constants.IConstants.ROLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginForJoiningsFragment extends Fragment {
Button btn;
    public LoginForJoiningsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_admin_login, container, false);
        btn =root.findViewById(R.id.btnLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PendingTicketActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}