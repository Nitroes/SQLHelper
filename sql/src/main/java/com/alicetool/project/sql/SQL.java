package com.alicetool.project.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SQL {
    //https://jitpack.io/#Nitroes/SQLHelper

    public static void insert(SQLiteDatabase database,String TABLE_NAME,JSONObject json) throws Exception {
        /*
            {"Name":"abc","Password":"123"}
        */
        ContentValues values = new ContentValues();
        String name[]=getJsonKey(json);
        for (int i = 0; i < json.length(); i++) {
            values.put(name[i],json.getString(name[i]));
        }
        database.insert(TABLE_NAME,null,values);
    }

    public static void updata(SQLiteDatabase database,String TABLE_NAME,JSONObject json) throws Exception {
        /*
            {"before":{"Name","abc"},"after":{"Name","cba"}}
        */
        ContentValues values = new ContentValues();
        JSONObject afterJson=json.getJSONObject("after");
        String afterName[]=getJsonKey(afterJson);
        for (int i = 0; i < afterJson.length(); i++) {
            values.put(afterName[i],afterJson.getString(afterName[i]));

        }
        JSONObject beforeJson=json.getJSONObject("before");
        String beforeName[]=getJsonKey(beforeJson);
        for (int i = 0; i < beforeJson.length(); i++) {
            database.update(TABLE_NAME, values, beforeName[i]+" = ?", new String[]{beforeJson.getString(beforeName[i])});
        }
    }

    public static void delete(SQLiteDatabase database,String TABLE_NAME,JSONObject json) throws Exception {
        /*
            {"Name":"abc"}
        */
        String name[]=getJsonKey(json);
        for (int i = 0; i < json.length(); i++) {
            database.delete(TABLE_NAME,name[i]+"=?",new String[]{json.getString(name[i])});
        }
    }

    public static void createTable(SQLiteDatabase database,String TABLE_NAME, JSONObject json) throws Exception {
        String str = "";
        /*
            {"Name","text";"Password":"text"}
        */
        String name[]=getJsonKey(json);
        for (int i = 0; i < json.length(); i++) {
            str += name[i] + " " + json.getString(name[i]) + " NOT NULL,";
        }
        str = str.substring(0, str.length() - 1);
        database.execSQL("CREATE TABLE " + TABLE_NAME + " ("+ str +");");
    }

    public static JSONArray select(SQLiteDatabase database,String TABLE_NAME, String[] columns) throws Exception {
        JSONArray jsonArray=new JSONArray();
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        String textview_data = "";
        while(cursor.moveToNext()){
            JSONObject json=new JSONObject();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                json.put(cursor.getColumnName(i),
                        cursor.getString(i));
            }
            jsonArray.put(json);
        }
        return jsonArray;
    }

    public static String[] getJsonKey(JSONObject json){
        List<String> array=new ArrayList<>();
        Iterator<String> it=json.keys();
        while(it.hasNext()){// 获得key
            array.add(it.next()) ;
        }
        String req[] = new String[array.size()];
        for (int i = 0; i <array.size() ; i++) {
            req[i]=array.get(i);
        }
        return req;
    }


}
