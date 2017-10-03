package com.mutualmobiles.playerinfo.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.mutualmobiles.playerinfo.R;
import com.mutualmobiles.playerinfo.adapter.GameListAdapter;
import com.mutualmobiles.playerinfo.models.GamesInfoModel;
import com.mutualmobiles.playerinfo.receiver.MyReceiver;
import com.mutualmobiles.playerinfo.utils.Constants;
import com.mutualmobiles.playerinfo.utils.Networking;
import com.mutualmobiles.playerinfo.utils.VolleyTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListOfGamesActivity extends BaseActivity {
    private final String TAG = "LIST OF GAMES";
    private ArrayList<GamesInfoModel> mGameList;
    private ListView mGameNameLV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_games);
        mContext = ListOfGamesActivity.this;
        //finding the view in the list of Games activity
        findViews();
        //getting the user data and storing the data in session
        apiCallGetPlayerData();
        //getting the data for list of games and game player
        apiCallGetGameData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(mContext)) {
            showNoNetworkDialogue(mContext);
        }
    }

    /**
     * this is to find the view of the layout
     */
    private void findViews() {
        mGameNameLV = (ListView) findViewById(R.id.games_list_lv);
        mGameNameLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //sending the object through the intent from listofgames to gameinfo
                GamesInfoModel mGamesInfoModel = mGameList.get(position);
                Intent mIntent = new Intent(ListOfGamesActivity.this, GameInfoActivity.class);
                mIntent.putExtra("GAME_INFO", mGamesInfoModel);
                startActivity(mIntent);
                //sending the event log to firebase
                logFireBaseEvent(mGamesInfoModel.getName());
            }
        });
    }

    /**
     * this is to get the data of the game list
     */
    private void apiCallGetGameData() {
        if (mSessionManager.getIsCached()) {
            try {
                JSONObject response = new JSONObject(mSessionManager.getGameList());
                jsonParser(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //starting the alarm for 1hr for caching the data
            repeatAlarm();
            //making the API call
            new VolleyTask(mContext, Constants.GAMES_LIST, null, "Please wait..") {
                @Override
                protected void handleError(VolleyError error) {
                    error.printStackTrace();
                }

                @Override
                protected void handleResponse(JSONObject response) {
                    jsonParser(response);
                    mSessionManager.setGameList(response.toString());
                    mSessionManager.setIsCached(true);
                }
            };
        }
    }

    /**
     * this si to get the data of the player
     */
    private void apiCallGetPlayerData() {
        new VolleyTask(mContext, Constants.PLAYER_INFO, null, "") {
            @Override
            protected void handleError(VolleyError error) {
                error.printStackTrace();
            }

            @Override
            protected void handleResponse(JSONObject response) {
                try {
                    //getting the values form the the responce
                    String mPlayerName = response.getString("name");
                    int mPlayerBalance = response.getInt("balance");
                    String mPlayerImage = response.getString("avatarLink");
                    String mPlayerLastLogin = response.getString("lastLogindate");
                    //setting the data in session manager
                    mSessionManager.setName(mPlayerName);
                    mSessionManager.setBalance(mPlayerBalance);
                    mSessionManager.setPlayerImg(mPlayerImage);
                    mSessionManager.setLastLoginDate(mPlayerLastLogin);
                    //setting the data
                    setPlayDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    //Repeat the alarm for every 1 hour and attach the receiver to be called   after 1 hr interval
    private void repeatAlarm() {
        int alarmPeriod = 60 * 60 * 1000;
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ListOfGamesActivity.this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ListOfGamesActivity.this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmPeriod, pendingIntent);
    }

    /**
     * This is for parsing the JSONObject
     *
     * @param response is the JSONObject which is to be parsed
     */
    private void jsonParser(JSONObject response) {
        JSONObject mGameInfoObj;
        mGameList = new ArrayList<>();
        try {
            String mResult = response.getString("response");
            //checking the response is success or not
            if (mResult.equalsIgnoreCase("success")) {
                String mCurrency = response.getString("currency");
                //setting the currency
                mSessionManager.setCurrency(mCurrency);
                JSONArray mData = response.getJSONArray("data");
                for (int i = 0; i < mData.length(); ++i) {
                    mGameInfoObj = mData.getJSONObject(i);
                    String mGameName = mGameInfoObj.getString("name");
                    int mJackpot = mGameInfoObj.getInt("jackpot");
                    String mGameDate = mGameInfoObj.getString("date");

                    GamesInfoModel mGamesInfoModel = new GamesInfoModel(mGameName, mJackpot, mGameDate);
                    mGameList.add(mGamesInfoModel);
                }
                GameListAdapter mGameListAdapter = new GameListAdapter(mGameList, ListOfGamesActivity.this);
                mGameNameLV.setAdapter(mGameListAdapter);
                //setting the data
                setPlayDetails();
            } else {
                //if response is not success displaying the msg
                makeToast("Api unSuccessful", LONG);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
