package com.app.abcdadmin;

import static com.app.abcdadmin.constants.IConstants.CHAT_SUPPORT;
import static com.app.abcdadmin.constants.IConstants.ROLE;
import static com.app.abcdadmin.constants.IConstants.SUPPORT;
import static com.app.abcdadmin.constants.IConstants.TRUE;
import static com.app.abcdadmin.constants.IConstants.ZERO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdadmin.managers.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
    private CircleImageView mImageView;
    private TextView mTxtUsername;
    private ViewPager2 mViewPager;
    private long exitTime = 0;
    Fragment chatFragment,pendingTicketFragment,ticketFragment;
    public static FragmentManager fm = null;
    ImageView imgMenu;
    SwitchMaterial supportSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.imageView);
        mTxtUsername = findViewById(R.id.txtUsername);
        imgMenu = findViewById(R.id.imgMenu);
        supportSwitch = findViewById(R.id.supportSwitch);
        fm = getSupportFragmentManager();
        final Toolbar mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        mTxtUsername.setText(session.getData(ROLE));




        chatFragment = new ChatsFragment();
        pendingTicketFragment = new PendingTicketFragment();
        ticketFragment = new TicketFragment();

        if (session.getData(ROLE).equals("Super Admin")){
            supportSwitch.setVisibility(View.VISIBLE);
        }

        Query freq = Utils.getQuerySupportStatus();
        freq.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    supportSwitch.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        supportSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(CHAT_SUPPORT);
                if (b){

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(SUPPORT, TRUE);
                    ref.updateChildren(hashMap);


                }else {
                    ref.removeValue();

                }
            }
        });
        fm.beginTransaction().replace(R.id.container, ticketFragment).commit();


        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup(view);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void exitApp() {
        try {
            if (mViewPager.getCurrentItem() == ZERO) {
                int DEFAULT_DELAY = 2000;
                if ((System.currentTimeMillis() - exitTime) > DEFAULT_DELAY) {
                    try {
                        screens.showToast("Press Again to Exist");

                    } catch (Exception e) {
                        screens.showToast("Press Again to Exist");
                    }
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            } else {
                mViewPager.setCurrentItem(ZERO);
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Utils.readStatus(STATUS_ONLINE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showpopup(View v) {

        PopupMenu popup = new PopupMenu(mActivity,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.logoutitem){
            session.logoutUser(mActivity);
        }


        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
