package com.app.abcdadmin;

import static com.app.abcdadmin.constants.IConstants.MOBILE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.abcdadmin.adapters.TicketAdapters;
import com.app.abcdadmin.helper.Session;
import com.app.abcdadmin.managers.Utils;
import com.app.abcdadmin.models.Ticket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public RecyclerView mRecyclerView;
    public ArrayList<Ticket> mTickets,AllTickets;
    public TicketAdapters ticketAdapters;
    Activity activity;
    Session session;
    String type = "";
    EditText edMobile;
    Button btnSearch;
    String Mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activity = SearchActivity.this;
        session = new Session(activity);
        mRecyclerView = findViewById(R.id.recyclerView);
        edMobile = findViewById(R.id.edMobile);
        btnSearch = findViewById(R.id.btnSearch);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edMobile.getText().length() != 10){
                    Toast.makeText(activity, "Invalid Mobile number", Toast.LENGTH_SHORT).show();
                }else {
                    Mobile = edMobile.getText().toString().trim();
                    readTickets();

                }

            }
        });



    }

    private void readTickets() {

        mTickets = new ArrayList<>();
        Query reference;
        reference = Utils.getQueryPendingTicketByMyId(Mobile);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ticket user = snapshot.getValue(Ticket.class);
                        assert user != null;
                        mTickets.add(user);
                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference = Utils.getQueryOpenedTicketByMyId(Mobile);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ticket user = snapshot.getValue(Ticket.class);
                        assert user != null;
                        mTickets.add(user);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference = Utils.getQueryClosedTicketByMyId(Mobile);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ticket user = snapshot.getValue(Ticket.class);
                        assert user != null;
                        mTickets.add(user);
                    }

                }
                setAdatper();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    private void setAdatper() {
        ticketAdapters = new TicketAdapters(activity, mTickets,"");
        mRecyclerView.setAdapter(ticketAdapters);
    }
}