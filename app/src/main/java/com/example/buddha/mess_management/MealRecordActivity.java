package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MealRecordActivity extends AppCompatActivity {

    public final Database myDatabase=new Database(this);
    public static String message = null;
    public static String title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_meal_record);
        setContentView(R.layout.activity_mess__content_acitivity);

        Cursor res=myDatabase.GetData2();
        if(res.getCount() == 0)
        {
           // showMessage("Error!","Nothing is found");
            title="Error!";
            message="Nothing to show";
        }

        else {
            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
                buffer.append("ID :" + res.getString(0) + "\n");
                buffer.append("Day :" + res.getString(1) + "\n");
                buffer.append("Name:" + res.getString(2) + "\n");
                buffer.append("Breakfastmeal :" + res.getString(3) +"\n");
                buffer.append("Lunchmeal:" + res.getString(4) + "\n");
                buffer.append("Dinnermeal :" + res.getString(5) + "\n\n");

            }

            title = "Data";
            message = buffer.toString();
        }
        showMessage( title, message);

    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}
