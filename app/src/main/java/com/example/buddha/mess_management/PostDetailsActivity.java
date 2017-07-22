package com.example.buddha.mess_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class PostDetailsActivity extends AppCompatActivity {

    //current user
    public static String USERNAME;
    public String id;
    public String jsonData;
    public String previous;

    EditText address_edit,rent_edit,numberOfSeat_edit,contactNumber_edit,description_edit;
    Button edit_btn,save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        USERNAME = prefs.getString("username","");
        jsonData = getIntent().getExtras().getString("json_data");

        address_edit = (EditText) findViewById(R.id.editText_address);
        rent_edit = (EditText) findViewById(R.id.editText_rent);
        numberOfSeat_edit = (EditText) findViewById(R.id.editText_numberofseat);
        contactNumber_edit = (EditText) findViewById(R.id.editText_contractnumber);
        description_edit = (EditText) findViewById(R.id.editText_description);
        edit_btn = (Button) findViewById(R.id.button_edit);
        save_btn = (Button) findViewById(R.id.button_save);

        save_btn.setEnabled(false);

        address_edit.setTag(address_edit.getKeyListener());
        address_edit.setKeyListener(null);
        rent_edit.setTag(rent_edit.getKeyListener());
        rent_edit.setKeyListener(null);
        numberOfSeat_edit.setTag(numberOfSeat_edit.getKeyListener());
        numberOfSeat_edit.setKeyListener(null);
        contactNumber_edit.setTag(contactNumber_edit.getKeyListener());
        contactNumber_edit.setKeyListener(null);
        description_edit.setTag(description_edit.getKeyListener());
        description_edit.setKeyListener(null);

        address_edit.setText(getIntent().getExtras().getString("address"));
        rent_edit.setText(getIntent().getExtras().getString("rent"));
        numberOfSeat_edit.setText(getIntent().getExtras().getString("numberOfSeat"));
        contactNumber_edit.setText(getIntent().getExtras().getString("contactNumber"));
        description_edit.setText(getIntent().getExtras().getString("description"));

        id = getIntent().getExtras().getString("id");
        previous = getIntent().getExtras().getString("previous");

        if(previous.equals("newpost")) {
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
                address_edit.setKeyListener((KeyListener) address_edit.getTag());
                rent_edit.setKeyListener((KeyListener) rent_edit.getTag());
                numberOfSeat_edit.setKeyListener((KeyListener) numberOfSeat_edit.getTag());
                contactNumber_edit.setKeyListener((KeyListener) contactNumber_edit.getTag());
                description_edit.setKeyListener((KeyListener) description_edit.getTag());
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
                    address_edit.setTag(address_edit.getKeyListener());
                    address_edit.setKeyListener(null);
                    rent_edit.setTag(rent_edit.getKeyListener());
                    rent_edit.setKeyListener(null);
                    numberOfSeat_edit.setTag(numberOfSeat_edit.getKeyListener());
                    numberOfSeat_edit.setKeyListener(null);
                    contactNumber_edit.setTag(contactNumber_edit.getKeyListener());
                    contactNumber_edit.setKeyListener(null);
                    description_edit.setTag(description_edit.getKeyListener());
                    description_edit.setKeyListener(null);

                    save_btn.setEnabled(false);
                    edit_btn.setEnabled(true);
                }
                else
                    Toast.makeText(PostDetailsActivity.this, "something is wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(previous.equals("mypost")){
            DisplayDataActivity.DDA.finish();
            BackgroundWorker backgroundWorker=new BackgroundWorker(this);
            SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
            String user=prefs.getString("usernamePost","");
            System.out.println(">>>>>>>>>>>>MyPost              .........."+user);
            backgroundWorker.execute("showMypost", user);
            finish();
        }
        else{
            finish();
        }
    }
}
