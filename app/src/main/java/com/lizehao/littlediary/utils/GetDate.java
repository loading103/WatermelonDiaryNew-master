package com.lizehao.littlediary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 李 on 2017/1/26.
 */
public class GetDate {
    public static String getDate(){
//        StringBuilder stringBuilder = new StringBuilder();
//        Calendar now = Calendar.getInstance();
//        stringBuilder.append(now.get(Calendar.YEAR) + "年");
//        stringBuilder.append((int)(now.get(Calendar.MONTH) + 1)  + "月");
//        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "日");
//        return stringBuilder;


//        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return  str;

    }
}
