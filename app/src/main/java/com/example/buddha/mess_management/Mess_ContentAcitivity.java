package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

    public static String userName="";
    public static int  selectedMonthNo;
    public  static  String selectedYear="",selectedMonth;

    Button initializationbtn,updatebtn, currentinfobtn,dailyInforeservation,mealRecord,addNewMember,setGuestPassBtn,mealInfoBtn;
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
        addNewMember=(Button)findViewById(R.id.button_addNewMember);
        setGuestPassBtn = (Button) findViewById(R.id.button_setguestpassword);
        mealInfoBtn = (Button) findViewById(R.id.button_mealInfo);

        SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
        userName=prefs.getString("username","");
        selectedMonthNo=Integer.parseInt(prefs.getString("selectedMonthNo","0"));
        selectedYear=prefs.getString("selectedYear","");
        if(selectedMonthNo==1)  selectedMonth="January";
        else if(selectedMonthNo==2) selectedMonth="February";
        else if(selectedMonthNo==3) selectedMonth="March";
        else if(selectedMonthNo==4) selectedMonth="April";
        else if(selectedMonthNo==5) selectedMonth="May";
        else if(selectedMonthNo==6) selectedMonth="June";
        else if(selectedMonthNo==7) selectedMonth="July";
        else if(selectedMonthNo==8) selectedMonth="August";
        else if(selectedMonthNo==9) selectedMonth="September";
        else if(selectedMonthNo==10) selectedMonth="October";
        else if(selectedMonthNo==11) selectedMonth="November";
        else if(selectedMonthNo==12) selectedMonth="December";
        System.out.println("username sfdsfffdsfdsfs    >>>>>>>>>>>> ..."+userName);

        if(getIntent().getExtras().getString("type").equals("guest")){
            initializationbtn.setEnabled(false);
            updatebtn.setEnabled(false);
            dailyInforeservation.setEnabled(false);
            addNewMember.setEnabled(false);
            setGuestPassBtn.setEnabled(false);
            mealInfoBtn.setEnabled(false);

            initializationbtn.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_content_paste_grey_24dp),null,null);
            updatebtn.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_attach_money_grey_24dp),null,null);
            dailyInforeservation.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_assignment_turned_in_grey_24dp),null,null);
            addNewMember.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_person_add_grey_24dp),null,null);
            setGuestPassBtn.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_vpn_key_grey_24dp),null,null);
            mealInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_edit_grey_24dp),null,null);

            initializationbtn.setTextColor(getResources().getColor(R.color.grey));
            updatebtn.setTextColor(getResources().getColor(R.color.grey));
            dailyInforeservation.setTextColor(getResources().getColor(R.color.grey));
            addNewMember.setTextColor(getResources().getColor(R.color.grey));
            setGuestPassBtn.setTextColor(getResources().getColor(R.color.grey));
            mealInfoBtn.setTextColor(getResources().getColor(R.color.grey));
        }

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
            JSONObject individual = jsonArray.getJSONObject(0);
            System.out.println("month        >>.." + individual.getString("current_month") + "year        >>>>> " + individual.getString("current_year"));


            if(jsonArray.length() > 0)
            {
                individual = jsonArray.getJSONObject(0);
                System.out.println("month        >>.." + individual.getString("current_month") + "year        >>>>> " + individual.getString("current_year"));
                selectedMonthNo=Integer.parseInt( individual.getString("current_month"));
                selectedYear=individual.getString("current_year");
            }
            else
            {
                selectedMonthNo=0;
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

    public  void addNewMember(View v)
    {
        SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
        String userName=prefs.getString("username","");
        String selectedMonth=prefs.getString("selectedMonthNo","");
        String selectedYear=prefs.getString("selectedYear","");
        if(selectedMonth.equals("0") && selectedYear.equals(""))
        {
            showMessage("Error!","First select Month & year\nthen add member\n");
        }
        else
        {
            startActivity(new Intent(Mess_ContentAcitivity.this,AddNewMember.class));
        }

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
        getCurrentMonthYear();

        try{
            BackgroundWorker backgroundWorker = new BackgroundWorker(Mess_ContentAcitivity.this);
            System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);
            String res = backgroundWorker.execute("getResult",userName, Integer.toString(selectedMonthNo), selectedYear).get();


            BackgroundWorker backgroundWorker2 = new BackgroundWorker(Mess_ContentAcitivity.this);
            // System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);
            String res2 = backgroundWorker2.execute("getMessMemberData",userName, Integer.toString(selectedMonthNo), selectedYear).get();


            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("result");

            StringBuffer buffer = new StringBuffer();

            if(jsonArray.length()<=0){
                Toast.makeText(Mess_ContentAcitivity.this,"No Data Found!",Toast.LENGTH_LONG).show();

            } else{
                Intent intent=new Intent(Mess_ContentAcitivity.this, CurrentMealInfoActivity.class);
                intent.putExtra("res",res);
                intent.putExtra("res2",res2);
                startActivity(intent);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(Mess_ContentAcitivity.this,"No Data Found!",Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(Mess_ContentAcitivity.this,"No Data Found!",Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Mess_ContentAcitivity.this,"No Data Found!",Toast.LENGTH_LONG).show();
        }


//        float meal_rate= (float) 0.0;
//        try {
//            BackgroundWorker backgroundWorker = new BackgroundWorker(Mess_ContentAcitivity.this);
//            System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);
//            String res = backgroundWorker.execute("getResult",userName, Integer.toString(selectedMonthNo), selectedYear).get();
//
//            StringBuffer buffer = new StringBuffer();
//            System.out.println("  <<<<<<<<<<<"+res);
//            JSONObject jsonObject=new JSONObject(res);
//            JSONArray jsonArray=jsonObject.getJSONArray("result");
//            if(jsonArray.length() > 0) {
//                JSONObject individual = jsonArray.getJSONObject(0);
//                System.out.println("...............  " + individual.getString("total_money") + "            ........." + individual.getString("total_cost") + "                   .............." + individual.getString("total_meal"));
//
//
//                meal_rate = (float) (Integer.parseInt(individual.getString("total_cost")) / (Float.valueOf(individual.getString("total_meal"))));
//
//
//                buffer.append("Total money       :" + individual.getString("total_money") + "\n");
//                buffer.append("Total spent money :" + individual.getString("total_cost") + "\n");
//                buffer.append("Tolal meal        :" + individual.getString("total_meal") + "\n");
//                buffer.append("Per meal cost     :" + meal_rate + "\n\n");
//            }
//            else
//            {
//                buffer.append("No Data Found!");
//            }
//
//
//            //get all member info result
//
//            BackgroundWorker backgroundWorker2 = new BackgroundWorker(Mess_ContentAcitivity.this);
//            // System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);
//            String res2 = backgroundWorker2.execute("getMessMemberData",userName, Integer.toString(selectedMonthNo), selectedYear).get();
//
//            JSONObject jsonObject2=new JSONObject(res2);
//            JSONArray jsonArray2=jsonObject2.getJSONArray("mess_member");
//            if(jsonArray2.length() > 0) {
//                buffer.append("Mess member Information"+"\n\n\n");
//                int countObject = 0;
//
//                while (countObject < jsonArray2.length()) {
//                    JSONObject individual2 = jsonArray2.getJSONObject(countObject);
//                    buffer.append("member name        :" + individual2.getString("member_name") + "\n");
//                    buffer.append("Tolal money        :" + individual2.getString("total_amount") + "\n");
//                    buffer.append("Tolal meal        :" + individual2.getString("total_meal") + "\n");
//                  //  buffer.append("Tolal cost        :" + individual2.getString("total_cost") + "\n\n");
//                    buffer.append("Tolal cost        :" +Float.valueOf(individual2.getString("total_meal"))*(meal_rate) + "\n\n");
//
//                    countObject++;
//                }
//
//            }
//            else
//            {
//                buffer.append("");
//            }
//            showMessage("MEAL INFORMATION",buffer.toString());
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }


    public void DailyInfoReservation(View v)
    {
        Intent intent=new Intent(Mess_ContentAcitivity.this, DailyInfoActivity.class);
        startActivity(intent);
    }
    public void MealRecords(View v)
    {

        getCurrentMonthYear();

        try{
            BackgroundWorker backgroundWorker = new BackgroundWorker(Mess_ContentAcitivity.this);
            System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);
            String res = backgroundWorker.execute("getMealRecord",userName, Integer.toString(selectedMonthNo), selectedYear).get();


            System.out.println("  <<<<<<<<<<<"+res);
            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("meal_record");

            StringBuffer buffer = new StringBuffer();

            if(jsonArray.length()<=0){
                Toast.makeText(Mess_ContentAcitivity.this,"No Data Found!",Toast.LENGTH_LONG).show();

            } else{
                Intent intent=new Intent(Mess_ContentAcitivity.this, MealInfoPerDayActivity.class);
                intent.putExtra("res",res);
                startActivity(intent);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(Mess_ContentAcitivity.this,"No Data Found!",Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(Mess_ContentAcitivity.this,"No Data Found!",Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Mess_ContentAcitivity.this,"No Data Found!",Toast.LENGTH_LONG).show();
        }



//        try {
//            BackgroundWorker backgroundWorker = new BackgroundWorker(Mess_ContentAcitivity.this);
//            System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);
//            String res = backgroundWorker.execute("getMealRecord",userName, Integer.toString(selectedMonthNo), selectedYear).get();
//
//
//            System.out.println("  <<<<<<<<<<<"+res);
//            JSONObject jsonObject=new JSONObject(res);
//            JSONArray jsonArray=jsonObject.getJSONArray("meal_record");
//            if(jsonArray.length() > 0) {
//                int count = 0;
//                buffer.append("Current Month     : " + selectedMonth +", " +selectedYear + "\n");
//                int dayFlag = 0;
//                while (count < jsonArray.length()) {
//                    JSONObject individual = jsonArray.getJSONObject(count);
//                    int cday = Integer.parseInt(individual.getString("day"));
//                    if(dayFlag!= cday) {
//                        dayFlag = cday;
//                        buffer.append("Day           : " + individual.getString("day") + "\n");
//                        buffer.append("Shopping Cost : " + individual.getString("shopCost") + "\n\n");
//                    }
//                    buffer.append("Name          :" + individual.getString("member_name") + "\n");
//                    buffer.append("Breakfastmeal :" + individual.getString("breakfastMeal") +"\n");
//                    buffer.append("Lunchmeal     :" + individual.getString("lunchMeal") + "\n");
//                    buffer.append("Dinnermeal    :" + individual.getString("dinnerMeal") + "\n\n");
//                    count++;
//                }
//            }
//            else
//            {
//                buffer.append("No Data Found!");
//            }
//
//            showMessage("MEAL INFORMATION",buffer.toString());
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    public void mealInfo(View v){
        Intent intent=new Intent(Mess_ContentAcitivity.this, MealRecordActivity.class);
        startActivity(intent);
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
