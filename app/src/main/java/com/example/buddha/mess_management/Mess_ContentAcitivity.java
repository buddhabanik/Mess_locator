package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Mess_ContentAcitivity extends AppCompatActivity {

    final Context context = this;

  //  public static String userName="";
    public static int  selectedMonthNo;
    public  static  String selectedYear="";
    Button initializationbtn,updatebtn, currentinfobtn,dailyInforeservation,mealRecord ;
    public final Database myDatabase =  new Database(this);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess__content_acitivity);
        initializationbtn=(Button)findViewById(R.id.button_initialization);
        updatebtn=(Button)findViewById(R.id.button_updatebalance);
        currentinfobtn=(Button)findViewById(R.id.button_currentInfo);
        dailyInforeservation=(Button)findViewById(R.id.button_dailyinfo);
        mealRecord=(Button)findViewById(R.id.button_mealrecords);

        SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
        String userName=prefs.getString("username","");
        System.out.println("username sfdsfffdsfdsfs    >>>>>>>>>>>> ..."+userName);

        getCurrentMonthYear();
    }

    // get manager current month & year

    public void getCurrentMonthYear()
    {
        SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
        String userName=prefs.getString("username","");
        System.out.println("username    >>>>>>>>>>>> ..."+userName);
        BackgroundWorker backgroundWorker=new BackgroundWorker(this);

        try {
            String res=backgroundWorker.execute("getCurrentMonthYear",userName).get();

            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            System.out.println("length         >>>>>>>>>>> "+jsonArray.length());
       //     JSONObject individual = jsonArray.getJSONObject(0);
        //    System.out.println("month        >>.." + individual.getString("current_month") + "year        >>>>> " + individual.getString("current_year"));


            if(jsonArray.length() > 0)
            {
                JSONObject individual = jsonArray.getJSONObject(0);
                System.out.println("month        >>.." + individual.getString("current_month") + "year        >>>>> " + individual.getString("current_year"));
                selectedMonthNo=Integer.parseInt(individual.getString("current_month"));
                selectedYear= ( individual.getString("current_year"));
            }
            else
            {
                selectedMonthNo= 0 ;
                selectedYear="";
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor= prefs.edit();
        editor.putString("selectedMonthNo", Integer.toString(selectedMonthNo));
        editor.putString("selectedYear", selectedYear);
        editor.commit();

    }


    public void Initialization(View v)
    {
//        myDatabase.DeleteAllDataFromTable_MemberInfo();
        Intent intent=new Intent(Mess_ContentAcitivity.this, MonthSelectionActivity.class);
        startActivity(intent);
    }
    public void UpdateBalance(View v)
    {
        Intent intent=new Intent(Mess_ContentAcitivity.this, Update_Activity.class);
        startActivity(intent);
    }

    public void SetGuestPassword(View view)
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.accessmodifier, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String res="";
                                try{
                                    SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
                                    String userName=prefs.getString("username","");
                                BackgroundWorker backgroundWorker=new BackgroundWorker(Mess_ContentAcitivity.this);
                                res = backgroundWorker.execute("setGuestPassword",userName,userInput.getText().toString()).get();
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>       "+userInput.getText().toString() );
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                                Toast.makeText(Mess_ContentAcitivity.this, res , Toast.LENGTH_LONG).show();

                            //    result.setText(userInput.getText()); //save the result
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void CurrentInfo(View v)
    {

        SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
        String userName=prefs.getString("username","");
        selectedMonthNo=Integer.parseInt(prefs.getString("selectedMonthNo","0"));
        selectedYear=prefs.getString("selectedYear","");

        float meal_rate=0;

        try {
            BackgroundWorker backgroundWorker = new BackgroundWorker(Mess_ContentAcitivity.this);
            System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);
            String res = backgroundWorker.execute("getResult",userName, Integer.toString(selectedMonthNo), selectedYear).get();

            StringBuffer buffer = new StringBuffer();
            System.out.println("  <<<<<<<<<<<"+res);
            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("result");
            if(jsonArray.length() > 0) {
                JSONObject individual = jsonArray.getJSONObject(0);
                System.out.println("...............  " + individual.getString("total_money") + "            ........." + individual.getString("total_cost") + "                   .............." + individual.getString("total_meal"));


                meal_rate = (float) (Integer.parseInt(individual.getString("total_cost")) / (Float.valueOf(individual.getString("total_meal"))));


                buffer.append("Total money       :" + individual.getString("total_money") + "\n");
                buffer.append("Total spent money :" + individual.getString("total_cost") + "\n");
                buffer.append("Tolal meal        :" + individual.getString("total_meal") + "\n");
                buffer.append("Per meal cost     :" + meal_rate + "\n\n");
            }
            else
            {
                buffer.append("No Data Found!");
            }


            //get all member info result

            BackgroundWorker backgroundWorker2 = new BackgroundWorker(Mess_ContentAcitivity.this);
            // System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);
            String res2 = backgroundWorker2.execute("getMessMemberData",userName, Integer.toString(selectedMonthNo), selectedYear).get();

            JSONObject jsonObject2=new JSONObject(res2);
            JSONArray jsonArray2=jsonObject2.getJSONArray("mess_member");
            if(jsonArray2.length() > 0) {
                buffer.append("Mess member Information"+"\n\n\n");
                int countObject = 0;

                while (countObject < jsonArray2.length()) {
                    JSONObject individual2 = jsonArray2.getJSONObject(countObject);
                    buffer.append("member name        :" + individual2.getString("member_name") + "\n");
                    buffer.append("Tolal money        :" + individual2.getString("total_amount") + "\n");
                    buffer.append("Tolal meal        :" + individual2.getString("total_meal") + "\n");
                 //   buffer.append("Tolal cost        :" + individual2.getString("total_cost") + "\n\n");
                    float totalMeal=Float.parseFloat(individual2.getString("total_meal"));
                    buffer.append("Tolal cost        :" +(float) (meal_rate*totalMeal) + "\n\n");

                    countObject++;
                }

            }
            else
            {
                buffer.append("");
            }
            showMessage("MEAL INFORMATION",buffer.toString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void DailyInfoReservation(View v)
    {
        Intent intent=new Intent(Mess_ContentAcitivity.this, DailyInfoActivity.class);
        startActivity(intent);
    }
    public void MealRecords(View v)
    {
      //  Intent intent=new Intent(Mess_ContentAcitivity.this,  MealRecordActivity.class);
       // startActivity(intent);

        String title,message;
        Cursor res=myDatabase.GetData2();
        if(res.getCount() == 0)
        {
            // showMessage("Error!","Nothing is found");
            title="Error!";
            message="Nothing to show";
        }

        else {
            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
              //  buffer.append("ID :" + res.getString(0) + "\n");
                buffer.append("Day           :" + res.getString(1) + "\n");
                buffer.append("Name          :" + res.getString(2) + "\n");
                buffer.append("Breakfastmeal :" + res.getString(3) +"\n");
                buffer.append("Lunchmeal     :" + res.getString(4) + "\n");
                buffer.append("Dinnermeal    :" + res.getString(5) + "\n\n");

            }

            title = "Daily Meal Information";
            message = buffer.toString();
        }
        showMessage( title, message);
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
