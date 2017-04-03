package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayDataActivity extends AppCompatActivity {

    public static String jsonData;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        final ArrayList<Item> list = new ArrayList<>();

        listView=(ListView)findViewById(R.id.listview);
        itemAdapter=new ItemAdapter(this, R.layout.raw_layout);
        listView.setAdapter(itemAdapter);
        jsonData=getIntent().getExtras().getString("json_data");

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
                Intent intent=new Intent( DisplayDataActivity.this, PostDetailsActivity.class);
                intent.putExtra("id",selectedItem.getId());
                intent.putExtra("address",selectedItem.getAddress());
                intent.putExtra("rent",selectedItem.getRent());
                intent.putExtra("numberOfSeat",selectedItem.getNumberofseat());
                intent.putExtra("contactNumber",selectedItem.getContractnumber());
                intent.putExtra("description",selectedItem.getDescription());
                intent.putExtra("previous",getIntent().getExtras().getString("previous"));
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
    public void showMessage(String title,String message)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

           }
       });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
