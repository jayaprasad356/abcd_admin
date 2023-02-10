package com.app.abcdadmin;

import static com.app.abcdadmin.constants.IConstants.ROLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.abcdadmin.helper.Session;


public class LoginForJoiningsFragment extends Fragment {
    Button btnSignIn;
    EditText edEmail,edPassword;
    Session session;

    public LoginForJoiningsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_login, container, false);
        btnSignIn = root.findViewById(R.id.btnLogin);
        edEmail=root.findViewById(R.id.edEmail);
        edPassword=root.findViewById(R.id.edPassword);

        session =new Session(getActivity());
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edEmail.getText().toString().trim().equals("admin@gmail.com") && edPassword.getText().toString().trim().equals("123456")){
                    session.setData(ROLE, "Admin");
                    Intent intent = new Intent(getActivity(), PendingTicketActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "Invalid login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}