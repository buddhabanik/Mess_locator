package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDataInformationActivity extends AppCompatActivity {

    public BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    public static String Address,Rent,numberOfseat,contractNumber,description;
    EditText editaddress,editrent,editnumberofseat,editcontractnumber,editdescription;
    Button saveDatabutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_information);
        editaddress=(EditText)findViewById(R.id.editText_address);
        editrent=(EditText)findViewById(R.id.editText_rent);
        editnumberofseat=(EditText)findViewById(R.id.editText_numberofseat);
        editcontractnumber= (EditText)findViewById(R.id.editText_contractnumber);
        editdescription= (EditText)findViewById(R.id.editText_description);
        saveDatabutton=(Button)findViewById(R.id.button_saveData);

        SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
        final String userName=prefs.getString("usernamePost","");

        saveDatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Address=editaddress.getText().toString();
                Rent=editrent.getText().toString();
                numberOfseat=editnumberofseat.getText().toString();
                contractNumber=editcontractnumber.getText().toString();
                description=editdescription.getText().toString();

                backgroundWorker.execute("AddInfoData", userName ,Address, Rent, numberOfseat, contractNumber,description);

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
