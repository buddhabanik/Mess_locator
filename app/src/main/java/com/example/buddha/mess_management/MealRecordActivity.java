package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MealRecordActivity extends AppCompatActivity {

    //current user
    public static String USERNAME;

    //current month and year
    public static String selectedYear;
    public static int selectedMonthNo,currentDay;
    public String selectedMonth,selectedDay,selectedMember,mealRecord;
    public int memberNo;
    List<String> shopCost,labels;

    TextView currentMonthYear,memberText;
    Spinner spinnerDay,spinnerMember;
    EditText breakMeal,lunchMeal,dinnerMeal,shopCostEdit;
    Button edit,save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_record);

        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        USERNAME = prefs.getString("username","");
        selectedMonthNo = Integer.parseInt(prefs.getString("selectedMonthNo",""));
        selectedYear = prefs.getString("selectedYear","");

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

        currentMonthYear = (TextView) findViewById(R.id.textView_month);
        spinnerDay = (Spinner) findViewById(R.id.spinner_day);
        memberText = (TextView) findViewById(R.id.textView_mealInfo);
        spinnerMember = (Spinner) findViewById(R.id.spinner_member);
        breakMeal = (EditText) findViewById(R.id.editText_breakMeal);
        lunchMeal = (EditText) findViewById(R.id.editText_lunchMeal);
        dinnerMeal = (EditText) findViewById(R.id.editText_dinnerMeal);
        edit = (Button) findViewById(R.id.button_edit);
        save = (Button) findViewById(R.id.button_save);
        shopCostEdit = (EditText) findViewById(R.id.editText_shopCost);

        spinnerMember.setEnabled(false);
        breakMeal.setEnabled(false);
        lunchMeal.setEnabled(false);
        dinnerMeal.setEnabled(false);
        shopCostEdit.setEnabled(false);
        edit.setEnabled(false);
        save.setEnabled(false);


        currentMonthYear.setText("Current Month and Year : " + selectedMonth  + ", "+ selectedYear);

        labels=new ArrayList<>();
        shopCost = new ArrayList<>();
        labels.add("Select Member Name");
        memberNo = 0;

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
                count++;
            }
            memberNo = count;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            String res = backgroundWorker.execute("getMealRecord",USERNAME,Integer.toString(selectedMonthNo),selectedYear).get();
            mealRecord = res;
            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("meal_record");
            if(jsonArray.length()>0) {
                int count = 0,cost = 0;
                int cDay = 0,i=0;
                String member_name,day;
                while (count < jsonArray.length()) {
                    JSONObject individual = jsonArray.getJSONObject(count);
                    day = individual.getString("day");
                    if(cDay != Integer.parseInt(day)) {
                        cDay = Integer.parseInt(day);
                        i=0;
                        cost = Integer.parseInt(individual.getString("shopCost"));
                        shopCost.add(Integer.toString(cost));
                    }
                    member_name = individual.getString("member_name");

                    i++;
                    count++;
                }
                currentDay = cDay;


            }else{
                currentDay = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        String[] mealNumber_day= new String[currentDay + 1];
        mealNumber_day[0] = "Select a Day";
        for(int i=1;i<=currentDay;i++){
            mealNumber_day[i] = Integer.toString(i);
        }

        ArrayAdapter<String> adapter_day=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mealNumber_day);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapter_day);


        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedDay =adapterView.getItemAtPosition(i).toString();
                if(selectedDay!="Select a Day"){
                    shopCostEdit.setText(shopCost.get(i-1));
                    breakMeal.setText("");
                    lunchMeal.setText("");
                    dinnerMeal.setText("");
                    spinnerMember.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter_member=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, labels);
        adapter_member.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMember.setAdapter(adapter_member);


        spinnerMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedMember =adapterView.getItemAtPosition(i).toString();
                if(selectedMember!="Select Member Name"){
                    memberText.setText("Meals taken by " + selectedMember + " on day "+ selectedDay +" : ");
                    try {
                        JSONObject jsonObject = new JSONObject(mealRecord);
                        JSONArray jsonArray = jsonObject.getJSONArray("meal_record");
                        if (jsonArray.length() > 0) {
                            int count = 0;
                            String member_name, day;
                            while (count < jsonArray.length()) {
                                JSONObject individual = jsonArray.getJSONObject(count);
                                day = individual.getString("day");
                                member_name = individual.getString("member_name");
                                if(day.equals(selectedDay) && member_name.equals(selectedMember) ){
                                    breakMeal.setText(individual.getString("breakfastMeal"));
                                    lunchMeal.setText(individual.getString("lunchMeal"));
                                    dinnerMeal.setText(individual.getString("dinnerMeal"));
                                    edit.setEnabled(true);
                                }
                                count++;
                            }

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editButton();
        saveButton();

    }

    public void editButton(){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MealRecordActivity.this, "Edit Mode On", Toast.LENGTH_LONG).show();
                breakMeal.setEnabled(true);
                lunchMeal.setEnabled(true);
                dinnerMeal.setEnabled(true);
                shopCostEdit.setEnabled(true);
                save.setEnabled(true);
                edit.setEnabled(false);
            }
        });

    }

    public void saveButton(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String breakMealText,lunchMealText,dinnerMealText,shopCostText;
                breakMealText = breakMeal.getText().toString();
                lunchMealText = lunchMeal.getText().toString();
                dinnerMealText = dinnerMeal.getText().toString();
                shopCostText = shopCostEdit.getText().toString();

                String res = "";
                try {
                    BackgroundWorker backgroundWorker = new BackgroundWorker(MealRecordActivity.this);
                    res = backgroundWorker.execute("updateMealRecord",USERNAME,Integer.toString(selectedMonthNo),selectedYear,selectedMember,breakMealText,lunchMealText,dinnerMealText,shopCostText,selectedDay).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(res.equals("data updated" + "\n")){
                    Toast.makeText(MealRecordActivity.this, "Data is saved", Toast.LENGTH_LONG).show();
                    save.setEnabled(false);
                    edit.setEnabled(true);
                    breakMeal.setEnabled(false);
                    lunchMeal.setEnabled(false);
                    dinnerMeal.setEnabled(false);
                    shopCostEdit.setEnabled(false);
                }
                else
                    Toast.makeText(MealRecordActivity.this, "something is wrong", Toast.LENGTH_LONG).show();
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
