package org.techtown.my_jubgging.trashmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


//초기화 작업:  공공쓰레기통 데이터를 응용프로그램내 저장할 용도

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "trash.db";

    // static Context context; // dbsize용

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //데이터 베이스가 생성이 될 때 호출
        db.execSQL("CREATE TABLE IF NOT EXISTS AddressList (id INTEGER PRIMARY KEY AUTOINCREMENT, address TEXT NOT NULL, kind TEXT NOT NULL, latitude TEXT NOT NULL, longitude TEXT NOT NULL, spec TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // SELECT 문 (주소 목록들을 조회)
    public ArrayList<PublicTrashAddress> getAddressList() {
        ArrayList<PublicTrashAddress> addressItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM AddressList ORDER BY id ASC ", null);
        if (cursor.getCount() != 0) {
            //조회 데이터가 있을때 내부 수행
            while (cursor.moveToNext()) {
                @SuppressLint("Range") Long id = cursor.getLong(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("number"));
                @SuppressLint("Range") String kind = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                @SuppressLint("Range") String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                @SuppressLint("Range") String spec = cursor.getString(cursor.getColumnIndex("spec"));

                PublicTrashAddress addressItem = new PublicTrashAddress();
                addressItem.setId(id);
                addressItem.setAddress(address);
                addressItem.setKind(kind);
                addressItem.setLatitude(latitude);
                addressItem.setLongitude(longitude);
                addressItem.setSpec(spec);

                addressItems.add(addressItem);
            }
        }

        cursor.close();

        return addressItems;
    }


    //INSERT 문 (주소 목록을 DB에 넣는다.)
    public void InsertAddress(String _address, String _kind, String _latitude, String _longitude, String _spec) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO AddressList (address, kind, latitude, longitude, spec) VALUES('" + _address + "','" + _kind + "', '" + _latitude + "', '" + _longitude + "', '" + _spec + "');");

    }

    // DELETE 문 (주소 목록을 제거 한다.)
    public void deleteAddress(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM AddressList WHERE id = '" + _id + "'");

    }


    public void dbInitialize(){ // DB를 초기화하는 메소드

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM AddressList");

    }
}