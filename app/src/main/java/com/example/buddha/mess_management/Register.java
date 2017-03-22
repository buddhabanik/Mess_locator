package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {


    EditText usernameText,passwordText, fullnameText,emailText,retPasswordText;
    public  static String username,password, fullname,email,ret_password ;
    TextView textView;
    Button submit_button;
    public Dialog  instruction_dialog;
    public String username_pattern = "^[a-zA-Z][a-zA-Z0-9]{6,12}$";
    public String mobile_pattern   = "^[0-9]{11,11}";
    public String fullname_pattern = "^[a-zA-Z\\s]{6,30}";
    public String password_pattern = "^[a-zA-Z0-9_]{6,20}";
    public Pattern pattern;
    public Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText    = (EditText) findViewById(R.id.username);
        fullnameText    = (EditText) findViewById(R.id.fullname);
        emailText       = (EditText) findViewById(R.id.email);
        passwordText    = (EditText) findViewById(R.id.password);
        retPasswordText = (EditText) findViewById(R.id.ret_password);

        textView=(TextView)findViewById(R.id.instrustions);
        submit_button=(Button)findViewById(R.id.submit_button) ;

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instruction_dialog = new Dialog(Register.this);
                instruction_dialog.setContentView(R.layout.instruction);
                instruction_dialog.setTitle("Registration Instructions");
                instruction_dialog.show();

                Button ok_button = (Button) instruction_dialog.findViewById(R.id.ok_button);
                ok_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        instruction_dialog.dismiss();
                    }
                });

            }
        });


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;
                String validate_message = "";

                username     = usernameText.getText().toString();
                fullname     = fullnameText.getText().toString();
                email        = emailText.getText().toString();
                password     = passwordText.getText().toString();
                ret_password = retPasswordText.getText().toString();

                pattern = Pattern.compile(username_pattern);
                matcher = pattern.matcher(username);

                if (username.isEmpty()) {
                    ok = false;
                    validate_message += "*Username required.\n";
                } else if (!matcher.matches()) {
                    ok = false;
                    validate_message += "*Invalid username syntax.\n";
                }


                pattern = Pattern.compile(fullname_pattern);
                matcher = pattern.matcher(fullname);

                if (fullname.isEmpty()) {
                    ok = false;
                    validate_message += "*Fullname required.\n";
                } else if (!matcher.matches()) {
                    ok = false;
                    validate_message += "*Invalid fullname.\n";
                }

                if (email.isEmpty()) {
                    ok = false;
                    validate_message += "*Email required.\n";
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    ok = false;
                    validate_message += "*Invalid email syntax.\n";
                }

                pattern = Pattern.compile(password_pattern);
                matcher = pattern.matcher(password);

                if (password.isEmpty()) {
                    ok = false;
                    validate_message += "*Password field required.\n";
                } else if (!matcher.matches()) {
                    ok = false;
                    validate_message += "*Invalid password.\n";
                }

                if (ret_password.isEmpty()) {
                    ok = false;
                    validate_message += "*Please retype password.\n";
                } else if (!password.equals(ret_password)) {
                    ok = false;
                    validate_message += "*Password did not match.\n";
                }

                if (ok) {

                    BackgroundWorker backgroundWorker = new BackgroundWorker(Register.this);
                    backgroundWorker.execute("registation",fullname, username,email,password);
                } else {
                    showMessage("Registation Error",validate_message);
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
