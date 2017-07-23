package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

public class GuestLoginMealActivity extends AppCompatActivity {

    EditText managerName, password;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login_meal);
        managerName=(EditText)findViewById(R.id.editText_managerName);
        password=(EditText)findViewById(R.id.editText_password);
        submitButton=(Button)findViewById(R.id.button);
        System.out.println(">>>>>      "+managerName.getText().toString()+"               "+password.getText().toString());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(">>>>>     inside button ");
                String res="";
                BackgroundWorker backgroundWorker=new BackgroundWorker(GuestLoginMealActivity.this);
                try {
                      res=backgroundWorker.execute("selectManager",managerName.getText().toString(),password.getText().toString()).get();
                    System.out.println(">>>>>     inside button 2 ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(res.equals("matched"))
                {

                    SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putString("username",managerName.getText().toString());
                    System.out.println(">>>>>     inside button 3 "+managerName.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(GuestLoginMealActivity.this,Mess_ContentAcitivity.class);
                    intent.putExtra("type","guest");
                    startActivity(intent);
                }
                else
                {
                   showMessage("Error",res);
                }

            }
        });
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
