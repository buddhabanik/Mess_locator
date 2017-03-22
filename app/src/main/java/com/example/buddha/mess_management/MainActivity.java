package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public  String name="sagor";
    public static Dialog dialog;
    public  static String username,password, fullnameText,emailText,retPasswordText ;
    public BackgroundWorker backgroundWorker;
    EditText usernameText,passwordText;
    Button messButton,loginButton,registerButton, submitButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button)findViewById(R.id.button_login);
        registerButton = (Button)findViewById(R.id.button_register);

    }
    public void Login(View v)
    {
        String validate_message = "";

        usernameText = (EditText) findViewById(R.id.editText_username);
        passwordText = (EditText) findViewById(R.id.editText_password);

        username = usernameText.getText().toString();
        password = passwordText.getText().toString();

        if (username.isEmpty())
            validate_message += "*username required\n";
        else if (password.isEmpty())
            validate_message += "*password required\n";

        if (validate_message.isEmpty()) {

            backgroundWorker = new BackgroundWorker(this);
            String type = "login";
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>     ");
            backgroundWorker.execute(type, username, password);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>                   >>>>>>>>>>>>>>>>>>>>>>>>");


        } else {
            alerDialog( "Login Unsuccessfull" ,validate_message);
        }
    }

    public void Registation(View v)
    {

        Intent intent=new Intent(MainActivity.this, Register.class);
        startActivity(intent);

    }

    public void alerDialog(String title, String message)
    {
//        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setCancelable(true);
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(message);
//        alertDialog.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
