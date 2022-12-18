package com.app.abcdadmin.managers;

import static android.os.Build.VERSION.SDK_INT;


import static com.app.abcdadmin.constants.IConstants.EXTRA_SEARCH;
import static com.app.abcdadmin.constants.IConstants.IMG_DEFAULTS;
import static com.app.abcdadmin.constants.IConstants.REF_TOKENS;
import static com.app.abcdadmin.constants.IConstants.REF_USERS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.core.content.res.ResourcesCompat;

import com.app.abcdadmin.R;
import com.app.abcdadmin.constants.IDialogListener;
import com.app.abcdadmin.fcmmodels.Token;
import com.app.abcdadmin.models.Chat;
import com.app.abcdadmin.views.SingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author : Prashant Adesara
 * @url https://www.bytesbee.com
 * Util class set Default parameter and access in application
 */
public class Utils {

    public static final boolean IS_TRIAL = false;
    private static final int DEFAULT_VIBRATE = 500;
    public static boolean online = true, offline = true;
    public static boolean male = true, female = true, notset = true;
    public static boolean withPicture = true, withoutPicture = true;


    static final int ONE_MB = 1024;
    public static int MAX_SIZE_AUDIO = 10; // 10 MB Maximum
    public static int MAX_SIZE_VIDEO = 15; // 15 MB Maximum
    public static int MAX_SIZE_DOCUMENT = 5; // 5 MB Maximum

    final static String DEF_TEXT = "Please update your app to get attachment options and many new features.";
    public static String UPDATE_TEXT = "";

    public static String getDefaultMessage() {
        if (Utils.isEmpty(UPDATE_TEXT)) {
            return DEF_TEXT;
        } else {
            return UPDATE_TEXT;
        }
    }

    public static int getAudioSizeLimit() {
        return MAX_SIZE_AUDIO * ONE_MB;
    }

    public static int getVideoSizeLimit() {
        return MAX_SIZE_VIDEO * ONE_MB;
    }

    public static int getDocumentSizeLimit() {
        return MAX_SIZE_DOCUMENT * ONE_MB;
    }

    public static void sout(String msg) {
        if (IS_TRIAL) {
            System.out.println("Pra :: " + msg);
        }
    }
    public static Map<String, Chat> sortByChatDateTime(Map<String, Chat> unsortMap, final boolean order) {

        List<Entry<String, Chat>> list = new LinkedList<>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Entry<String, Chat>>() {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            public int compare(Entry<String, Chat> o1, Entry<String, Chat> o2) {
                try {
                    if (order) {
                        return dateFormat.parse(o1.getValue().getDatetime()).compareTo(dateFormat.parse(o2.getValue().getDatetime()));
                    } else {
                        return dateFormat.parse(o2.getValue().getDatetime()).compareTo(dateFormat.parse(o1.getValue().getDatetime()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        Map<String, Chat> sortedMap = new LinkedHashMap<>();
        for (Entry<String, Chat> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static Query getQuerySortBySearch() {
        return FirebaseDatabase.getInstance().getReference(REF_USERS).orderByChild(EXTRA_SEARCH).startAt("").endAt("" + "\uf8ff");
    }
    public static void setProfileImage(Context context, String imgUrl, ImageView mImageView) {
        try {

            if (!imgUrl.equalsIgnoreCase(IMG_DEFAULTS)) {
//                Picasso.get().load(imgUrl).fit().placeholder(R.drawable.profile_avatar).into(mImageView);
                Glide.with(context).load(imgUrl).placeholder(R.drawable.profile_avatar)
                        .thumbnail(0.5f)
                        .into(mImageView);
            } else {
//                Picasso.get().load(R.drawable.profile_avatar).fit().into(mImageView);
                Glide.with(context).load(R.drawable.profile_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
            }
        } catch (Exception ignored) {
        }
    }
    public static void setVibrate(final Context mContext) {
        // Vibrate for 500 milliseconds
        setVibrate(mContext, DEFAULT_VIBRATE);
    }
    public static void setVibrate(final Context mContext, long vibrate) {
        try {
            Vibrator vib = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            if (SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(vibrate, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vib.vibrate(vibrate); //deprecated in API 26
            }
        } catch (Exception ignored) {
        }
    }



    public static boolean isEmpty(final Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String) s).trim().length() == 0)) {
            return true;
        }
        if (s instanceof Map) {
            return ((Map<?, ?>) s).isEmpty();
        }
        if (s instanceof List) {
            return ((List<?>) s).isEmpty();
        }
        if (s instanceof Object[]) {
            return (((Object[]) s).length == 0);
        }
        return false;
    }

    public static void getErrors(final Exception e) {
        if (IS_TRIAL) {
            final String stackTrace = "Pra ::" + Log.getStackTraceString(e);
            System.out.println(stackTrace);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String getDateTime() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        final Date date = new Date();

        return dateFormat.format(date);
    }

    public static String getDateTimeStampName() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        final Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCapsWord(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    public static String formatDateTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    public static String formatTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    public static String formatLocalTime(long timeInMillis) {
        SimpleDateFormat dateFormatUTC = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormatUTC.parse(formatTime(timeInMillis));
        } catch (Exception ignored) {
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        if (date == null) {
            return dateFormat.format(timeInMillis);
        }
        return dateFormat.format(date);
    }

    public static String formatLocalFullTime(long timeInMillis) {
        SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormatUTC.parse(formatDateTime(timeInMillis));
        } catch (Exception e) {
            Utils.getErrors(e);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        if (date == null) {
            return dateFormat.format(timeInMillis);
        }
        return dateFormat.format(date);
    }

    public static String formatDateTime(final Context context, final String timeInMillis) {
        long localTime = 0L;
        try {
            localTime = dateToMillis(formatLocalFullTime(dateToMillis(timeInMillis)));
        } catch (Exception e) {
            Utils.getErrors(e);
        }
        if (isToday(localTime)) {
            return formatTime(context, localTime);
        } else {
            return formatDateNew(localTime);
        }
    }

    public static long dateToMillis(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = sdf.parse(dateString);
        assert date != null;
        return date.getTime();
    }
    public static void uploadToken(String referenceToken) {
        try {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(REF_TOKENS);
                Token token = new Token(referenceToken);
                reference.child(firebaseUser.getUid()).setValue(token);
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public static String formatFullDate(String timeString) {
        long timeInMillis = 0;
        try {
            timeInMillis = dateToMillis(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(timeInMillis).toUpperCase();
    }

    /**
     * Formats timestamp to 'date month' format (e.g. 'February 3').
     */
    public static String formatDateNew(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yy HH:mm", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * Returns whether the given date is today, based on the user's current locale.
     */
    public static boolean isToday(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        String date = dateFormat.format(timeInMillis);
        return date.equals(dateFormat.format(Calendar.getInstance().getTimeInMillis()));
    }

    /**
     * Checks if two dates are of the same day.
     *
     * @param millisFirst  The time in milliseconds of the first date.
     * @param millisSecond The time in milliseconds of the second date.
     * @return Whether {@param millisFirst} and {@param millisSecond} are off the same day.
     */
    public static boolean hasSameDate(long millisFirst, long millisSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(millisFirst).equals(dateFormat.format(millisSecond));
    }

    public static String formatLocalTime(Context context, long when) {
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();

        int flags = DateUtils.FORMAT_NO_NOON | DateUtils.FORMAT_NO_MIDNIGHT | DateUtils.FORMAT_ABBREV_ALL;

        if (then.year != now.year) {
            flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (then.yearDay != now.yearDay) {
            flags |= DateUtils.FORMAT_SHOW_DATE;
        } else {
            flags |= DateUtils.FORMAT_SHOW_TIME;
        }

        return DateUtils.formatDateTime(context, when, flags);
    }

    public static String formatTime(Context context, long when) {
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();

        int flags = DateUtils.FORMAT_NO_NOON | DateUtils.FORMAT_NO_MIDNIGHT | DateUtils.FORMAT_ABBREV_ALL;

        if (then.year != now.year) {
            flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (then.yearDay != now.yearDay) {
            flags |= DateUtils.FORMAT_SHOW_DATE;
        } else {
            flags |= DateUtils.FORMAT_SHOW_TIME;
        }

        return DateUtils.formatDateTime(context, when, flags);
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        // Create a new LinkedHashSet

        // Add the elements to set
        Set<T> set = new LinkedHashSet<>(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }


    public static void RTLSupport(Window window) {
        try {
            window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }



    public static Map<String, String> sortByString(Map<String, String> unsortMap, final boolean order) {

        List<Entry<String, String>> list = new LinkedList<>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Entry<String, String>>() {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            public int compare(Entry<String, String> o1, Entry<String, String> o2) {
                try {
                    if (order) {
                        return dateFormat.parse(o1.getValue()).compareTo(dateFormat.parse(o2.getValue()));
                    } else {
                        return dateFormat.parse(o2.getValue()).compareTo(dateFormat.parse(o1.getValue()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        Map<String, String> sortedMap = new LinkedHashMap<>();
        for (Entry<String, String> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    public static void chatSendSound(Context context) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = context.getAssets().openFd("chat_tone.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }


}
