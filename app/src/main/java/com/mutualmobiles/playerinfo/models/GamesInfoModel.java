package com.mutualmobiles.playerinfo.models;

import java.io.Serializable;

/**
 * Created by amareshjana on 24/09/17.
 */

public class GamesInfoModel implements Serializable{

    private String Name;
    private int Jackpot;
    private String Date;

    public GamesInfoModel(String name, int jackpot, String date) {
        Name = name;
        Jackpot = jackpot;
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getJackpot() {
        return Jackpot;
    }

    public void setJackpot(int jackpot) {
        Jackpot = jackpot;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "GamesInfoModel{" +
                "Name='" + Name + '\'' +
                ", Jackpot=" + Jackpot +
                ", Date='" + Date + '\'' +
                '}';
    }
}
