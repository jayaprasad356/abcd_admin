package com.app.abcdadmin.voiceplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.app.abcdadmin.OnSelectedListener;
import com.app.abcdadmin.R;
import com.app.abcdadmin.adapters.FavAdapter;
import com.app.abcdadmin.models.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteMessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    Activity activity;
    Messages messages;
    OnSelectedListener onSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_message);
        activity=FavoriteMessageActivity.this;
        onSelectedListener= message -> {
            Intent intent = new Intent();
            intent.putExtra("message", message);
            setResult(101, intent);
            finish();
        };
        List<String> data = Arrays.asList("TestMessage", "LinearLayoutManager", "LinearLayoutManager(LinearLayoutManager(");
        recyclerView=findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);

        FavAdapter adapter = new FavAdapter(data,onSelectedListener,activity);
        recyclerView.setAdapter(adapter);
    }

}