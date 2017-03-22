package com.example.buddha.mess_management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BUDDHA on 1/21/2017.
 */

public class Database extends SQLiteOpenHelper implements Serializable{

    public static final long serialVersionUID = 1L;

    public static List<String> listOfMember = new ArrayList<String>();

    public static int dayCounter=1;
    public static int lengthOfMonth=0;
    public static boolean dayFlag = true ;
    public static boolean finalDayFlag= true;
    public static float totalMeal  = 0;
    public static float mealRate   = 0;
    public static float totalMoney = 0 ;
    public static float totalSpentMoney = 0 ;


    //Member information table
    public static final String DATABASE_NAME = "Meal_manager";
    public static final String TABLE_NAME = "member_info";
    public static final String COL_1="ID";
    public static final String COL_2="NAME";
    public static final String COL_3="AMOUNT";
    public static final String COL_4="MEAL_NUMBER";
    public static final String COL_5="SPENT_MONEY";
    public static final String COL_6="DUE_MONEY";

    //Month information table
    public static final String TABLE_NAME2 = "Month_info";
    public static final String COL_21="ID";
    public static final String COL_22="DAY";
    public static final String COL_23="NAME";
    public static final String COL_24="BREAKFAST_MEAL";
    public static final String COL_25="LUNCH_MEAL";
    public static final String COL_26="DINNER_MEAL";



    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
      //  SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public  void onCreate(SQLiteDatabase db) //" VARCHAR, " +COL_3+ " INTEGER);");
    {
        db.execSQL("CREATE TABLE " +TABLE_NAME
                +"(" +COL_1+
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +COL_2+
                " VARCHAR, " +COL_3+ " VARCHAR, "+COL_4+" VARCHAR, "+COL_5+" VARCHAR, "+COL_6+" VARCHAR);");


        db.execSQL("CREATE TABLE " +TABLE_NAME2
            +"(" +COL_21+
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +COL_22+
            " VARCHAR, " +COL_23+ " VARCHAR, "+COL_24+" VARCHAR, "+COL_25+" VARCHAR, "+COL_26+" VARCHAR);");


       // db.execSQL("create table  "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTO INCREMENT, NAME TEXT,AMOUNT INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
         db.execSQL("DROP TABLE IF EXITS "+TABLE_NAME);
         db.execSQL("DROP TABLE IF EXITS "+TABLE_NAME2);
         onCreate(db);
    }


    public boolean initialize_member_info(){

        DeleteAllDataFromTable_MemberInfo();
        SQLiteDatabase db = this.getWritableDatabase();


        return true;
    }

    public boolean InsertData(String name, String  amount)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,amount);
        contentValues.put(COL_4,"0");
        contentValues.put(COL_5,"0");
        contentValues.put(COL_6,"0");

        long result = db.insert(TABLE_NAME,null,contentValues);
        db.close();
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean InsertData2(String day, String name, String breakfast,String lunch, String dinner)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_22,day);
        contentValues.put(COL_23,name);
        contentValues.put(COL_24,breakfast);
        contentValues.put(COL_25, lunch);
        contentValues.put(COL_26, dinner);

        long result2 = db.insert(TABLE_NAME2,null,contentValues);
        db.close();
        if(result2 == -1)
            return false;
        else
            return true;
    }


    public boolean InsertMealOfMember(String name, float totalDailymeal)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query= "select "+COL_4 +" from "+TABLE_NAME + " where "+ COL_2 +" = '" + name + "'";
        Cursor res = db.rawQuery( query ,null );
        res.moveToNext();
        String meal = res.getString(0).toString();
        System.out.println("           >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>     "+meal);
        float totalMeal = Float.valueOf(meal);
        totalMeal += totalDailymeal;

        String query_2 = "select * from "+TABLE_NAME + " where "+ COL_2 +" = '" + name + "'";
        Cursor res_2 =db.rawQuery(query_2, null);
        res_2.moveToNext();
        boolean result = UpdateData(res_2.getString(0),res_2.getString(1),res_2.getString(2), (""+totalMeal) );
        return result;
    }


    public Cursor GetData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor GetData2()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2,null);
        return res;
    }

    public boolean UpdateData(String id, String name, String amount, String mealNumber )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,amount);
        contentValues.put(COL_4, mealNumber );
        db.update(TABLE_NAME,contentValues,"ID = ?",new String[] { id });
        return true;
    }

    public int DeleteData(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID =?",new String[] {id});
    }

    public void  DeleteAllDataFromTable_MemberInfo()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,null, null);
    }
    public void DeleteAllDataFromTable_MonthInfo()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME2,null, null);
    }


}
