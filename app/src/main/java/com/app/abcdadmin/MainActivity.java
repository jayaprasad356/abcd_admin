package com.app.abcdadmin;

import static com.app.abcdadmin.constants.IConstants.CHAT_SUPPORT;
import static com.app.abcdadmin.constants.IConstants.FALSE;
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
import android.app.ProgressDialog;
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
import com.app.abcdadmin.models.ChatSupport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    Fragment chatFragment, pendingTicketFragment, ticketFragment;
    public static FragmentManager fm = null;
    ImageView imgMenu, imgSearch;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.imageView);
        mTxtUsername = findViewById(R.id.txtUsername);
        imgMenu = findViewById(R.id.imgMenu);
        imgSearch = findViewById(R.id.imgSearch);
        fm = getSupportFragmentManager();
        progressDialog = new ProgressDialog(this);
        final Toolbar mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        mTxtUsername.setText(session.getData(ROLE));


        chatFragment = new ChatsFragment();
        pendingTicketFragment = new PendingTicketFragment();
        ticketFragment = new TicketFragment();


        fm.beginTransaction().replace(R.id.container, ticketFragment).commit();


        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup(view);
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                startActivity(intent);
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

        PopupMenu popup = new PopupMenu(mActivity, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.logoutitem) {
            session.logoutUser(mActivity);
        } else if (item.getItemId() == R.id.newJoinings) {
            Intent intent = new Intent(mActivity, JoiningActivity.class);
            startActivity(intent);
        }


        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
