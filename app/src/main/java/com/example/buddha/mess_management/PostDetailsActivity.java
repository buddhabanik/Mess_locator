package com.example.buddha.mess_management;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class PostDetailsActivity extends AppCompatActivity {

    //current user
    public static String USERNAME;
    public String id;

    EditText address_edit,rent_edit,numberOfSeat_edit,contactNumber_edit,description_edit;
    Button edit_btn,save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        USERNAME = prefs.getString("username","");

        address_edit = (EditText) findViewById(R.id.editText_address);
        rent_edit = (EditText) findViewById(R.id.editText_rent);
        numberOfSeat_edit = (EditText) findViewById(R.id.editText_numberofseat);
        contactNumber_edit = (EditText) findViewById(R.id.editText_contractnumber);
        description_edit = (EditText) findViewById(R.id.editText_description);
        edit_btn = (Button) findViewById(R.id.button_edit);
        save_btn = (Button) findViewById(R.id.button_save);

        save_btn.setEnabled(false);
        address_edit.setEnabled(false);
        rent_edit.setEnabled(false);
        numberOfSeat_edit.setEnabled(false);
        contactNumber_edit.setEnabled(false);
        description_edit.setEnabled(false);

        address_edit.setText(getIntent().getExtras().getString("address"));
        rent_edit.setText(getIntent().getExtras().getString("rent"));
        numberOfSeat_edit.setText(getIntent().getExtras().getString("numberOfSeat"));
        contactNumber_edit.setText(getIntent().getExtras().getString("contactNumber"));
        description_edit.setText(getIntent().getExtras().getString("description"));

        id = getIntent().getExtras().getString("id");

        if(getIntent().getExtras().getString("previous").equals("newpost")) {
            edit_btn.setEnabled(false);
            save_btn.setEnabled(false);
            edit_btn.setVisibility(View.INVISIBLE);
            save_btn.setVisibility(View.INVISIBLE);
        }

        editButton();
        saveButton();

    }

    public void editButton(){
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDetailsActivity.this, "Edit Mode On", Toast.LENGTH_LONG).show();
                address_edit.setEnabled(true);
                rent_edit.setEnabled(true);
                numberOfSeat_edit.setEnabled(true);
                contactNumber_edit.setEnabled(true);
                description_edit.setEnabled(true);
                save_btn.setEnabled(true);
                edit_btn.setEnabled(false);
            }
        });

    }

    public void saveButton(){
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address_text,rent_text,numberOfSeat_text,contactNumber_text,description_text;
                address_text = address_edit.getText().toString();
                rent_text = rent_edit.getText().toString();
                numberOfSeat_text = numberOfSeat_edit.getText().toString();
                contactNumber_text = contactNumber_edit.getText().toString();
                description_text = description_edit.getText().toString();

                String res = "";
                try {
                    BackgroundWorker backgroundWorker = new BackgroundWorker(PostDetailsActivity.this);
                    res = backgroundWorker.execute("updateMessInfo",id,address_text,rent_text,numberOfSeat_text,contactNumber_text,description_text).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println(">>>>>>>>>>> "+res);

                if(res.equals("data updated")){
                    Toast.makeText(PostDetailsActivity.this, "Data is saved", Toast.LENGTH_LONG).show();
                    address_edit.setEnabled(false);
                    rent_edit.setEnabled(false);
                    numberOfSeat_edit.setEnabled(false);
                    contactNumber_edit.setEnabled(false);
                    description_edit.setEnabled(false);
                    save_btn.setEnabled(false);
                    edit_btn.setEnabled(true);
                }
                else
                    Toast.makeText(PostDetailsActivity.this, "something is wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
}
