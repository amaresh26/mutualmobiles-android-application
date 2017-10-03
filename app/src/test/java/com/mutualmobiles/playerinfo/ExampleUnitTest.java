package com.mutualmobiles.playerinfo;

import com.mutualmobiles.playerinfo.activities.BaseActivity;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest  {

    @Test
    public void dateFormatCheck() throws ParseException{
        BaseActivity mBaseActivity = new BaseActivity();
        String formattedDate = mBaseActivity.formatDate("04/05/2016T16:45","dd/MM/yyyy'T'HH:mm");
        assertEquals("06-Dec-2015 07:20:30",formattedDate);
    }

    @Test
    public void currencyFormatCheck() {
        BaseActivity mBaseActivity = new BaseActivity();
        String money = mBaseActivity.formatCurrency(987654,"GBP");
        assertEquals("9,87,654.00", money);
    }



}