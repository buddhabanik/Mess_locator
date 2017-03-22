package com.example.buddha.mess_management;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Update_Activity extends AppCompatActivity {

    //current user
    public static String USERNAME;

    //current month and year
    public static String selectedYear;
    public static int selectedMonthNo;

    EditText editText;
    Button updateBtn;
    public static String selectMember ="";
    public static Cursor res = null;
    public static Cursor res2=null;
    public int amount;
    public final Database myDatabase =  new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_);
        editText=(EditText)findViewById(R.id.editText);
        updateBtn = (Button)findViewById(R.id.button_updatebtn);

        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        USERNAME = prefs.getString("username","");
        selectedMonthNo = Integer.parseInt(prefs.getString("selectedMonthNo",""));
        selectedYear = prefs.getString("selectedYear","");

    //    Intent i=getIntent();

        res = myDatabase.GetData();
//        res2 = myDatabase.GetData();
        List<String> labels=new ArrayList<String>();
        labels.add("Select Member Name");
        final ArrayList<String> amounts = new ArrayList<>();
        amounts.add("0");


        //to get the names of members on current month
        try {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            String res = backgroundWorker.execute("getMessMemberData",USERNAME,Integer.toString(selectedMonthNo),selectedYear).get();

            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("mess_member");
            int count=0;
            String member_name,amount;
            while(count<jsonArray.length() )
            {
                JSONObject individual=jsonArray.getJSONObject(count);
                member_name = individual.getString("member_name");
                amount = individual.getString("total_amount");
                labels.add(member_name);
                amounts.add(amount);
                count++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }


        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,labels);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectMember=adapterView.getItemAtPosition(i).toString();
                amount = Integer.parseInt(amounts.get(i));
            //    System.out.println("                         ===================================    =====================   "+selectMember);
                Update_Amount();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void Update_Amount()
    {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean flag=true;
                String edit_amount=editText.getText().toString();

                if(edit_amount.isEmpty() || selectMember.equals("Select Member Name") || selectMember.equals(""))
                {
                    Toast.makeText(getBaseContext(),"Plz select a member & add updated value", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
//                    int amount = Integer.parseInt(res.getString(2).toString());
                    int update = Integer.parseInt(edit_amount);
                    int total_amount = amount + update;

//                    boolean isInserted = myDatabase.UpdateData(res.getString(0),res.getString(1), ""+total_amount , res.getString(3));

                    try {
                        BackgroundWorker backgroundWorker = new BackgroundWorker(Update_Activity.this);
                        String res = backgroundWorker.execute("updateAmount",USERNAME,Integer.toString(selectedMonthNo),selectedYear,selectMember,Integer.toString(total_amount)).get();
                        if (res.equals("data updated"+"\n")) {
                            Toast.makeText(Update_Activity.this, "Data is updated", Toast.LENGTH_LONG).show();
                            amount = total_amount;
                        } else {
                            Toast.makeText(Update_Activity.this, "Data is not updated", Toast.LENGTH_LONG).show();
                        }

                    //update total money
                        BackgroundWorker backgroundWorker2 = new BackgroundWorker(Update_Activity.this);
                        String res2 = backgroundWorker2.execute("updateMoneyInResult",USERNAME,Integer.toString(selectedMonthNo),selectedYear,Integer.toString(update)).get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }

//                if(flag) {
//                    Toast.makeText(getBaseContext(),"Plz select a member & add updated value", Toast.LENGTH_LONG).show();
//                }
        });
    }

}
