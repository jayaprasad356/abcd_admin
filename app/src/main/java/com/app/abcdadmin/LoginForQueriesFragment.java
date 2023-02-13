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

import com.app.abcdadmin.helper.Session;


public class LoginForQueriesFragment extends Fragment {
    Button btnSignIn;
    EditText edEmail, edPassword;
    Session session;
    boolean login = false;
    String role = "";

    public LoginForQueriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_employee_login, container, false);
        btnSignIn = root.findViewById(R.id.btnLogin);
        edEmail = root.findViewById(R.id.edEmail);
        edPassword = root.findViewById(R.id.edPassword);

        session = new Session(getActivity());
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edEmail.getText().toString().trim().equals("admin@gmail.com") && edPassword.getText().toString().trim().equals("123456")) {
                    login = true;
                    role = "Admin";
                    signIn();
                }
            }
        });
        return root;
    }

    private void signIn() {
        session.setBoolean("is_logged_in", true);
        session.setData(ROLE, role);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(ROLE, role);
        startActivity(intent);
    }
}