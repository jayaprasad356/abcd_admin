package com.app.abcdadmin;

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
import android.widget.TextView;

import com.app.abcdadmin.managers.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity{
    private CircleImageView mImageView;
    private TextView mTxtUsername;
    private ViewPager2 mViewPager;
    private long exitTime = 0;
    Fragment chatFragment;
    public static FragmentManager fm = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.imageView);
        mTxtUsername = findViewById(R.id.txtUsername);
        fm = getSupportFragmentManager();
        final Toolbar mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        chatFragment = new ChatsFragment();
        fm.beginTransaction().replace(R.id.container, chatFragment).commit();
        //reference = FirebaseDatabase.getInstance().getReference(REF_USERS).child(firebaseUser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                try {
//                    if (dataSnapshot.hasChildren()) {
//                        final User user = dataSnapshot.getValue(User.class);
//                        assert user != null;
//                        mTxtUsername.setText(user.getUsername());
//                        if (user.getGenders() == GEN_UNSPECIFIED) {
//                            Utils.selectGenderPopup(mActivity, firebaseUser.getUid(), GEN_UNSPECIFIED);
//                        }
//
//                        Utils.setProfileImage(getApplicationContext(), user.getMyImg(), mImageView);
//                    }
//                } catch (Exception ignored) {
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        mImageView.setOnClickListener(new SingleClickListener() {
//            @Override
//            public void onClickView(View v) {
//                mViewPager.setCurrentItem(2);
//            }
//        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    public static void applyFontToMenu(Menu m, Context mContext) {
//        for (int i = 0; i < m.size(); i++) {
//            applyFontToMenuItem(m.getItem(i), mContext);
//        }
//    }
//
//    public static void applyFontToMenuItem(MenuItem mi, Context mContext) {
//        final SpannableString mNewTitle = new SpannableString(mi.getTitle());
//        mNewTitle.setSpan(new CustomTypefaceSpan("", Utils.getRegularFont(mContext)), ZERO, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mi.setTitle(mNewTitle);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        applyFontToMenu(menu, this);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        final int itemId = item.getItemId();
//        if (itemId == R.id.itemSettings) {
//            screens.openSettingsActivity();
//            return true;
//        } else if (itemId == R.id.itemLogout) {
//            Utils.logout(mActivity);
//            return true;
//        }
//        return true;
//    }

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

}
