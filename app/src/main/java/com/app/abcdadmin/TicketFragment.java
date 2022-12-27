package com.app.abcdadmin;

import static com.app.abcdadmin.constants.IConstants.CLOSED_TICKET;
import static com.app.abcdadmin.constants.IConstants.OPENED_TICKET;
import static com.app.abcdadmin.constants.IConstants.PENDING_TICKET;
import static com.app.abcdadmin.constants.IConstants.ROLE;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.abcdadmin.adapters.TicketAdapters;
import com.app.abcdadmin.helper.Session;
import com.app.abcdadmin.managers.Utils;
import com.app.abcdadmin.models.Ticket;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TicketFragment extends Fragment {
    public RecyclerView mRecyclerView;
    public ArrayList<Ticket> mTickets;
    public TicketAdapters ticketAdapters;
    Activity activity;
    Chip chipPending,chipOpened,chipClosed;
    String type = "";
    Session session;
    Spinner spinCategory;


    public TicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_ticket, container, false);
        activity = getActivity();
        mRecyclerView = root.findViewById(R.id.recyclerView);
        chipPending = root.findViewById(R.id.chipPending);
        chipOpened = root.findViewById(R.id.chipOpened);
        chipClosed = root.findViewById(R.id.chipClosed);
        spinCategory = root.findViewById(R.id.spinCategory);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        session = new Session(activity);

        FirebaseDatabase.getInstance().getReference(PENDING_TICKET).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int pendcount =  (int)snapshot.getChildrenCount();
                    chipPending.setText("Pending"+"("+pendcount+")");
                }
                else {
                    chipPending.setText("Pending(0)");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        FirebaseDatabase.getInstance().getReference(OPENED_TICKET).orderByChild(ROLE).equalTo(session.getData(ROLE)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int count =  (int)snapshot.getChildrenCount();
                    chipOpened.setText("Opened"+"("+count+")");
                }
                else {
                    chipOpened.setText("Opened(0)");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        FirebaseDatabase.getInstance().getReference(CLOSED_TICKET).orderByChild(ROLE).equalTo(session.getData(ROLE)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int count =  (int)snapshot.getChildrenCount();
                    chipClosed.setText("Closed"+"("+count+")");
                }
                else {
                    chipClosed.setText("Closed(0)");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        readTickets();

        chipPending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    readTickets();
                }
            }
        });
        chipOpened.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    readTickets();
                }
            }
        });
        chipClosed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    readTickets();
                }
            }
        });
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                readTickets();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    private void readTickets() {

        chipPending.setText("Pending");
        chipOpened.setText("Opened");
        chipClosed.setText("Closed");

        mTickets = new ArrayList<>();
        Query reference;
        if (chipOpened.isChecked()){
            type = chipOpened.getText().toString();
            reference = Utils.getQueryOpenedTicket();
        }else if (chipClosed.isChecked()){
            type = chipClosed.getText().toString();
            reference = Utils.getQueryClosedTicket();

        }
        else {
            type = chipPending.getText().toString();
            reference = Utils.getQueryPendingTicket();

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

                        if (user.getSupport() != null && user.getSupport().equals(session.getData(ROLE))){
                            if (user.getCategory() != null && user.getCategory().equals(spinCategory.getSelectedItem().toString())){
                                mTickets.add(user);
                            }
                        }

                    }

                }
                if (chipPending.isChecked()){
                    chipPending.setText("Pending"+"("+mTickets.size()+")");
                }else if (chipOpened.isChecked()){
                    chipOpened.setText("Opened"+"("+mTickets.size()+")");
                }else {
                    chipClosed.setText("Closed"+"("+mTickets.size()+")");

                }
                setAdatper();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setAdatper() {
        ticketAdapters = new TicketAdapters(activity, mTickets,type);
        mRecyclerView.setAdapter(ticketAdapters);
    }
}