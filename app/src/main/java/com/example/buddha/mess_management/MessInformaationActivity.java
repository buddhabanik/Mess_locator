package com.example.buddha.mess_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MessInformaationActivity extends AppCompatActivity {

    public int select;
    public  String username;

    Button addDatabutton,myPostbutton,newpostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_informaation);
        addDatabutton=(Button)findViewById(R.id.button_add);
        myPostbutton=(Button)findViewById(R.id.button_mypost);
        newpostButton=(Button)findViewById(R.id.button_newpost);
        username = getIntent().getExtras().getString("username");
        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        SharedPreferences.Editor editor = prefs.edit();
        //username of the login person
        editor.putString("usernamePost",username);
     //   editor.putString("username","");
        editor.commit();
        System.out.println(username+"<<<<<<<<<<<");
    }

    public void AddNewData(View v)
    {
        startActivity(new Intent(MessInformaationActivity.this, AddDataInformationActivity.class));
    }
    public void GoToMess(View v)
    {

        final String str[]={"Manager","Guest"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);


        final String selection ;

        builder.setTitle("Access Mode").setSingleChoiceItems(str, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                    select=i;

            }
        }).setPositiveButton("OK",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(select == 0)
                {
                    SharedPreferences prefs= getSharedPreferences("MYPREFS",0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username",username);
                    System.out.println("username in mess information    >>>>>>.."+username);
                    editor.commit();
                    Intent intent=new Intent(MessInformaationActivity.this, Mess_ContentAcitivity.class);
                    startActivity(intent);
                }
                else
                {
                    startActivity(new Intent(MessInformaationActivity.this, GuestLoginMealActivity.class));
                }

            }
        });
     //   builder.create();
        builder.show();

    }

    public void NewPost(View v)
    {
        BackgroundWorker backgroundWorker=new BackgroundWorker(this);
        backgroundWorker.execute("shownewpost");
    }
    public void MyPost(View v)
    {
        BackgroundWorker backgroundWorker=new BackgroundWorker(this);
        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        String user=prefs.getString("usernamePost","");
        backgroundWorker.execute("showMypost", user);
    }

}
