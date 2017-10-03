package com.mutualmobiles.playerinfo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mutualmobiles.playerinfo.R;
import com.mutualmobiles.playerinfo.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by amareshjana on 24/09/17.
 */

public class BaseActivity extends AppCompatActivity {

    public static SessionManager mSessionManager;
    public static final int SHORT = 0;
    public static final int LONG = 1;
    public Context mContext;
    private ImageView mPlayerImage;
    private TextView mBalance;
    private TextView mPlayerName;
    private TextView mLastLogin;
    public TableRow LastLoginDateRow;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getting the context
        mContext = getApplicationContext();
        //initializing the session manager by passing the context
        mSessionManager = new SessionManager(mContext);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    /**
     * this is to find the view of the layout
     */
    private void findView() {
        mPlayerImage = (ImageView) findViewById(R.id.user_img);
        mPlayerName = (TextView) findViewById(R.id.username_tv);
        mBalance = (TextView) findViewById(R.id.balance_tv);
        mLastLogin = (TextView) findViewById(R.id.last_login_tv);
        LastLoginDateRow = (TableRow) findViewById(R.id.date_visibility);
    }

    /*
     * this is to make the toast just sending the string and * int value as 0 =
	 * long & 1 = short length of the toast
	 */
    public void makeToast(String msg, int length) {
        if (length == LONG)
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /*
    * to show the dialogue if network is not available
    * @param mContext is to send the context of the method calling class
    * */
    public void showNoNetworkDialogue(Context mContext) {
        new AlertDialog.Builder(mContext)
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Turn On Internet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * setting the player data from the session manager
     */
    public void setPlayDetails() {
        //finding the view used in header
        findView();
        //setting the data for each component
        try {
            mPlayerName.setText(mSessionManager.getName());
            mBalance.setText(formatCurrency(mSessionManager.getBalance(), mSessionManager.getCurrency()));
            mLastLogin.setText(formatDate(mSessionManager.getLastLoginDate(), "dd/MM/yyyy'T'HH:mm"));
            //setting the image for the images
            Picasso.with(mContext).load(mSessionManager.getPlayerImg()).fit().centerCrop().into(mPlayerImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Currency Formatting
     *
     * @param rawCurrency in int
     * @return String formattedCurrency
     * <p>
     * * @used url - https://stackoverflow.com/questions/7131922/how-to-format-a-float-value-with-the-device-currency-format
     * <p>
     * for currency format
     */
    public String formatCurrency(int rawCurrency, String currency) {
        NumberFormat numFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        numFormat.setCurrency(Currency.getInstance(currency));
        String formattedCurrency = numFormat.format(rawCurrency);
        return formattedCurrency;
    }

    /**
     * Date Formatting
     *
     * @param rawDate in String
     * @return String formattedDate
     */
    public String formatDate(String rawDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(rawDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return dateFormat.format(date);
    }

    /**
     * Firebase Logging Event
     * <p>
     * when ever the user clicks on the list of games that game name should be logged int he firebase
     */
    public void logFireBaseEvent(String itemName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle);
    }
}
