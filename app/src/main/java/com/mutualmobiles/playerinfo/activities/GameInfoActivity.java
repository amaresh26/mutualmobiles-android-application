package com.mutualmobiles.playerinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.mutualmobiles.playerinfo.R;
import com.mutualmobiles.playerinfo.models.GamesInfoModel;
import com.mutualmobiles.playerinfo.utils.Networking;
import com.mutualmobiles.playerinfo.utils.SessionManager;

/**
 * Created by amareshjana on 24/09/17.
 */

public class GameInfoActivity extends BaseActivity {
    private final String TAG = "GameInfoActivity";
    private GamesInfoModel mGamesInfoModel;
    private TextView mGameName;
    private TextView mJackpot;
    private TextView mGameDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        mSessionManager = new SessionManager(GameInfoActivity.this);
        //setting the player data in BaseActivity
        setPlayDetails();
        //finding the views
        findView();
        //getting the bundle data which is sent from the ListOfGames
        getBundleData(savedInstanceState);
        //setting the data from the object we got from the intent
        setGameInfoData();
    }

    private void findView(){
        mGameName = (TextView) findViewById(R.id.name_tv);
        mJackpot = (TextView) findViewById(R.id.jackpot_tv);
        mGameDate = (TextView) findViewById(R.id.date_tv);
        //removing the visibility of the lastlogindate
        LastLoginDateRow.setVisibility(View.GONE);
    }

    private void setGameInfoData() {
        mGameName.setText(mGamesInfoModel.getName());
        mJackpot.setText(formatCurrency(mGamesInfoModel.getJackpot(),mSessionManager.getCurrency()));
        mGameDate.setText(formatDate(mGamesInfoModel.getDate(),"yyyy-MM-dd'T'HH:mm:ss"));
    }

    /*
    * this is to get the data from the ListOfGameActivity
    * through Intent we will directly get the GameInfoModel Object
    * */
    private void getBundleData(Bundle savedInstanceState) {
        Intent extra = getIntent();
        if (savedInstanceState == null) {
            if (extra == null) {
                mGamesInfoModel = null;
            } else {
                mGamesInfoModel = (GamesInfoModel) extra.getSerializableExtra("GAME_INFO");
            }
        } else {
            mGamesInfoModel = (GamesInfoModel) extra.getSerializableExtra("GAME_INFO");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(GameInfoActivity.this)) {
            showNoNetworkDialogue(GameInfoActivity.this);
        }
    }
}
