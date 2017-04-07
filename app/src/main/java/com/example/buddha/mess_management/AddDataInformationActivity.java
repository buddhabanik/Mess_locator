package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                String validate_message = "";

                Address=editaddress.getText().toString();
                Rent=editrent.getText().toString();
                numberOfseat=editnumberofseat.getText().toString();
                contractNumber=editcontractnumber.getText().toString();
                description=editdescription.getText().toString();

                if (Address.isEmpty())
                    validate_message += "*address required\n";
                else if (Rent.isEmpty())
                    validate_message += "*Rent field is required\n";
                else if (numberOfseat.isEmpty())
                    validate_message += "*The number of available seat is required\n";
                else if ( contractNumber.isEmpty())
                    validate_message += "*Contract number field is required\n";

                if(validate_message.isEmpty())
                {
                    backgroundWorker.execute("AddInfoData", userName, Address, Rent, numberOfseat, contractNumber, description);
                    Toast.makeText(AddDataInformationActivity.this, "Your Data is Posted", Toast.LENGTH_LONG).show();
                    MessInformaationActivity.MIA.finish();
                    Intent intent=new Intent( AddDataInformationActivity.this, MessInformaationActivity.class);
                    intent.putExtra("username",userName);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    showMessage("Error!",validate_message);
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
