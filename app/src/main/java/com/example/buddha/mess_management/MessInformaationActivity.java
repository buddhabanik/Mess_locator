package com.example.buddha.mess_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MessInformaationActivity extends AppCompatActivity {

    public int select;
    public  String username;

    Button addDatabutton,myPostbutton,newpostButton;

    public static String jsonData;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_informaation);
        addDatabutton=(Button)findViewById(R.id.button_add);
        myPostbutton=(Button)findViewById(R.id.button_mypost);
        username = getIntent().getExtras().getString("username");
        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        SharedPreferences.Editor editor = prefs.edit();
        //username of the login person
        editor.putString("usernamePost",username);
     //   editor.putString("username","");
        editor.commit();
        System.out.println(username+"<<<<<<<<<<<");


        final ArrayList<Item> list = new ArrayList<>();

        listView=(ListView)findViewById(R.id.listview);
        itemAdapter=new ItemAdapter(this, R.layout.raw_layout);
        listView.setAdapter(itemAdapter);
        String res="";
        try {
            BackgroundWorker backgroundWorker=new BackgroundWorker(this);
            res = backgroundWorker.execute("shownewpost").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        jsonData=res;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(DisplayDataActivity.this,Integer.toString(i),Toast.LENGTH_LONG).show();
                StringBuffer buffer = new StringBuffer();
                Item selectedItem = list.get(i);
                buffer.append("address = " + selectedItem.getAddress() + "\n");
                buffer.append("rent = " + selectedItem.getRent() + "\n");
                buffer.append("number of seat = " + selectedItem.getNumberofseat() + "\n");
                buffer.append("contact number = " + selectedItem.getContractnumber() + "\n");
                buffer.append("description = " + selectedItem.getDescription() + "\n\n");
                Intent intent=new Intent( MessInformaationActivity.this, PostDetailsActivity.class);
                intent.putExtra("id",selectedItem.getId());
                intent.putExtra("address",selectedItem.getAddress());
                intent.putExtra("rent",selectedItem.getRent());
                intent.putExtra("numberOfSeat",selectedItem.getNumberofseat());
                intent.putExtra("contactNumber",selectedItem.getContractnumber());
                intent.putExtra("description",selectedItem.getDescription());
                intent.putExtra("previous","newpost");
                startActivity(intent);
                //showMessage("Post",buffer.toString());

            }
        });



        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("mess_info");
            int count=0;
            String id,address,rent,numberofseat,contractnumber,description;
            while(count<jsonArray.length() )
            {
                JSONObject individual=jsonArray.getJSONObject(count);
                id = individual.getString("id");
                address= individual.getString("address");
                rent=individual.getString("rent");
                numberofseat=individual.getString("number_seat");
                contractnumber=individual.getString("contract_number");
                description=individual.getString("description");
                Item item=new Item(id,address, rent,numberofseat,contractnumber,description);
                list.add(item);
                itemAdapter.add(item);
                count++;
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
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
                else if(select == 1)
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
        System.out.println(">>>>>>>>>>>>MyPost              .........."+user);
        backgroundWorker.execute("showMypost", user);
    }

}
