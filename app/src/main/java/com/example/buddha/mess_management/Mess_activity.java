package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mess_activity extends AppCompatActivity {

    public static String USERNAME;

    Database myDatabase;
    EditText editName,editAmount,editnumber_of_member ;
    Button buttonSaveData, buttonShowData, updataInfo, deleteData;
    ArrayAdapter<CharSequence> arrayAdapter;
    public static int total_member;
    public String Name_pattern = "^[A-Za-z][A-Za-z0-9]*$";
    public static  Pattern pattern;
    public static  Matcher matcher;
    public int count;
 //   public static String[] months;
    public static String totalMember="";
    public static String selectedMonth="";
    public static String selectedYear;
    public static int selectedMonthNo;
 //   public static String message="";
    public static boolean monthflag= true;
    public boolean ok=true;
    public int total_money;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_activity);
        myDatabase = new Database(this);
        editnumber_of_member=(EditText)findViewById(R.id.editText_memberId);
        editName=(EditText)findViewById(R.id.editText_Name);
        editAmount=(EditText)findViewById(R.id.editText_Amount);
        buttonSaveData=(Button)findViewById(R.id.button_SaveData);

        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        USERNAME = prefs.getString("username","");
        selectedMonthNo = Integer.parseInt(prefs.getString("selectedMonthNo",""));
        selectedYear = prefs.getString("selectedYear","");
        selectedMonth = prefs.getString("selectedMonth","");
        System.out.println(">>>>>>>>" + USERNAME + ">>" + selectedMonthNo + ">>"+selectedYear);
        count=0;
        total_money = 0;

        AddData();

    }

    public void AddData()
    {
        buttonSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean totalMemberCheckFlag=true;
                ok =true;
                String message="";
                String monthMessage ="";


                totalMember=editnumber_of_member.getText().toString();

                if(totalMember.isEmpty() )
                {
                  //  Toast.makeText( Mess_activity.this,"Select the number of members",Toast.LENGTH_SHORT).show();
                    message +="Select the number of members.\n";
                    ok=false;
                    totalMemberCheckFlag=false;
                }
                else
                    total_member = Integer.parseInt(editnumber_of_member.getText().toString());


                String editname,amount ;

                boolean  nameflag=false;
                editname = editName.getText().toString();
                amount   = editAmount.getText().toString();
                if(editname.isEmpty())
                {
                    ok=false;
                    nameflag=true;
                    message += "Name is required.\n";
                }

                pattern=Pattern.compile(Name_pattern);
                matcher=pattern.matcher(editname);
                if(!matcher.matches() && !nameflag)
                {
                    ok=false;
                    message +="Invalid name.\n";
                }


                //to get member's name from database and check with editname
                try {
                    BackgroundWorker backgroundWorker = new BackgroundWorker(Mess_activity.this);
                    String res = backgroundWorker.execute("getMessMemberData",USERNAME,Integer.toString(selectedMonthNo),selectedYear).get();

                    JSONObject jsonObject=new JSONObject(res);
                    JSONArray jsonArray=jsonObject.getJSONArray("mess_member");
                    int countObject=0;
                    String member_name,total_meal,total_amount,total_cost;
                    while(countObject<jsonArray.length() )
                    {
                        JSONObject individual=jsonArray.getJSONObject(countObject);
                        member_name = individual.getString("member_name");
                        String Checkname= member_name;
                        if(Checkname.equals(editname))
                        {
                            ok = false;
                            message ="Member name is already exits.\n";
                            break;
                        }
                        total_meal = individual.getString("total_meal");
                        total_amount = individual.getString("total_amount");
                        total_cost = individual.getString("total_cost");
                        countObject++;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }


                if(amount.isEmpty())
                {
                    ok=false;
                    message += "Amount is required.\n";
                }


                if(!ok)
                {
                    showMessage("Error!",message);
                }
                else
                {
                  //  boolean isInserted = myDatabase.InsertData(editName.getText().toString(), editAmount.getText().toString() );
                    String res = "";

                    //to update member details in messmember table
                    try {
                        BackgroundWorker backgroundWorker1 = new BackgroundWorker(Mess_activity.this);
                        res = backgroundWorker1.execute("initMember",USERNAME,editName.getText().toString(),Integer.toString(selectedMonthNo),selectedYear,editAmount.getText().toString()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if ( res.equals("Successfully inserted")) {
                        Toast.makeText(Mess_activity.this, "Data is Inserted", Toast.LENGTH_LONG).show();
                       // myDatabase.listOfMember.add(editName.getText().toString());   // insert the member in list
                       // myDatabase.totalMoney += (Float.valueOf(editAmount.getText().toString()));

                        total_money += Integer.parseInt(editAmount.getText().toString());

                        count++;
                        if(count == total_member )
                        {
                            buttonSaveData.setEnabled(false);

                            try {

                                BackgroundWorker backgroundWorker = new BackgroundWorker(Mess_activity.this);
                                res = backgroundWorker.execute("initResult",USERNAME,Integer.toString(selectedMonthNo),selectedYear,Integer.toString(total_money)).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            Intent intent=new Intent( Mess_activity.this, Mess_ContentAcitivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Toast.makeText(Mess_activity.this, "Data is not Inserted", Toast.LENGTH_LONG).show();
                    }
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
