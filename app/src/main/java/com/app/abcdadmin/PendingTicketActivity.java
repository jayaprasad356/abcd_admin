package com.app.abcdadmin;

import static com.app.abcdadmin.constants.IConstants.ADMIN_FCM_URL;
import static com.app.abcdadmin.constants.IConstants.CLOSED_TICKET;
import static com.app.abcdadmin.constants.IConstants.FCM_ID;
import static com.app.abcdadmin.constants.IConstants.JOINING_TICKET;
import static com.app.abcdadmin.constants.IConstants.OPENED_TICKET;
import static com.app.abcdadmin.constants.IConstants.PENDING_TICKET;
import static com.app.abcdadmin.constants.IConstants.ROLE;
import static com.app.abcdadmin.constants.IConstants.SUCCESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.abcdadmin.adapters.TicketAdapters;
import com.app.abcdadmin.constants.IConstants;
import com.app.abcdadmin.helper.ApiConfig;
import com.app.abcdadmin.helper.Session;
import com.app.abcdadmin.managers.Utils;
import com.app.abcdadmin.models.Ticket;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingTicketActivity extends AppCompatActivity {
    public RecyclerView mRecyclerView;
    public ArrayList<Ticket> mTickets;
    public TicketAdapters ticketAdapters;
    Activity activity;
    String type = "";
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_ticket);
        activity = this;
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        session = new Session(activity);

        FirebaseDatabase.getInstance().getReference(JOINING_TICKET).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        readTickets();


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            try {
                updateFCM(token);
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        });
    }


    private void readTickets() {


        mTickets = new ArrayList<>();
        Query reference;

        reference = Utils.getQueryNewJoining();

        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTickets.clear();
                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ticket user = snapshot.getValue(Ticket.class);
                        assert user != null;

                        if (user.getSupport() != null && user.getSupport().equals(session.getData(ROLE))) {
                            mTickets.add(user);
                        }
                    }

                }
                setAdatper();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateFCM(String token) {
        Map<String, String> params = new HashMap<>();
        params.put(FCM_ID, token);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {

            }
            //pass url
        }, activity, ADMIN_FCM_URL, params, true);


    }

    private void setAdatper() {
        mRecyclerView.setVisibility(View.VISIBLE);
        ticketAdapters = new TicketAdapters(activity, mTickets, type);
        mRecyclerView.setAdapter(ticketAdapters);
    }
}