package com.app.abcdadmin;

import static com.app.abcdadmin.constants.IConstants.ADMIN_FCM_URL;
import static com.app.abcdadmin.constants.IConstants.CLOSED_JOINING;
import static com.app.abcdadmin.constants.IConstants.FCM_ID;
import static com.app.abcdadmin.constants.IConstants.FOLLOWUP_TICKET;
import static com.app.abcdadmin.constants.IConstants.JOINING_TICKET;
import static com.app.abcdadmin.constants.IConstants.ROLE;
import static com.app.abcdadmin.constants.IConstants.SUCCESS;
import static com.app.abcdadmin.constants.IConstants.TYPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoiningActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    public RecyclerView mRecyclerView;
    public ArrayList<Ticket> mTickets;
    public TicketAdapters ticketAdapters;
    Activity activity;
    String type = "";
    Session session;
    Chip pending, followUp, closed;
    TextView txtUsername;
    ImageView imgSearch,imgMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining);
        activity = this;
        session = new Session(activity);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        pending=findViewById(R.id.chipPending);
        followUp=findViewById(R.id.chipFollowup);
        closed=findViewById(R.id.chipClosed);
        txtUsername=findViewById(R.id.txtUsername);
        imgSearch=findViewById(R.id.imgSearch);
        imgMenu=findViewById(R.id.imgMenu);
        txtUsername.setText(session.getData(IConstants.NAME));
        if (session.getData(ROLE).equals("Super Admin")){
           pending.setVisibility(View.GONE);
        }

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,SearchActivity.class);
                startActivity(intent);

            }
        });
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup(view);
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readTickets(JOINING_TICKET);

            }
        });
        followUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readTickets(FOLLOWUP_TICKET);
            }
        });
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readTickets(CLOSED_JOINING);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);



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

        readTickets(JOINING_TICKET);


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            try {
                updateFCM(token);
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        });
    }
    private void showpopup(View v) {

        PopupMenu popup = new PopupMenu(activity,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.logoutitem){
            session.logoutUser(activity);
        }


        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    private void readTickets(String type) {


        mTickets = new ArrayList<>();
        Query reference;

        if (type.equals(JOINING_TICKET)){
            reference = Utils.getJoiningTicket();


        }
        else if (type.equals(FOLLOWUP_TICKET)){
            reference = Utils.getFollowUpTicket();


        }
        else {
            reference = Utils.geCloseJoining();

        }

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