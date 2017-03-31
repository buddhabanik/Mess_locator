package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.Intent;
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



public class DailyInfoActivity extends AppCompatActivity {

    //current user
    public static String USERNAME;

    //current month and year
    public static String selectedYear;
    public static int selectedMonthNo;

    Spinner spinnerMemberName,spinnerbreakfastMeal,spinnerlaunchMeal, spinnerdinnerMeal;
    EditText edittextDay,editText_CostOfShopping;
    public Button saveDatabtn;
    public static Cursor res = null;
    public static Cursor res2=null;
    public final Database myDatabase =  new Database(this);
    public static String message ,selectMember="",selectBreakfastmeal,selectLunchmeal,selectDinnermeal;

    public int currentDay,shopCost,entryCount,memberNo,total_cost,lengthOfMonth;
    public float total_meal;
    public boolean dayFlag;
    public List<String> enteredMembers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_info);

        spinnerMemberName=(Spinner)findViewById(R.id.sipnner_memberName);
        spinnerbreakfastMeal=(Spinner)findViewById(R.id.spinner_breakfastmeal);
        spinnerlaunchMeal=(Spinner)findViewById(R.id.spinner_lunchmeal);
        spinnerdinnerMeal=(Spinner)findViewById(R.id.spinner_dinnermeal);
        saveDatabtn=(Button)findViewById(R.id.button_saveinfo);
        edittextDay=(EditText)findViewById(R.id.editText_day);
        editText_CostOfShopping=(EditText)findViewById(R.id.editText_CostOfShopping);

        //    res = myDatabase.GetData();
        //    res2 = myDatabase.GetData();
//        labels = new ArrayList<String>();
//        labels=myDatabase.listOfMember;
//        int size=labels.size();
//        if(size==1){
//            while(res2.moveToNext())
//            {
//                labels.add(res2.getString(1));
//            }
//        }

        SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        USERNAME = prefs.getString("username","");
        selectedMonthNo = Integer.parseInt(prefs.getString("selectedMonthNo",""));
        selectedYear = prefs.getString("selectedYear","");

        if(selectedMonthNo == 1)    lengthOfMonth=31;
        else if (selectedMonthNo == 2)  lengthOfMonth = 28;
        else if (selectedMonthNo == 3)  lengthOfMonth = 31;
        else if (selectedMonthNo == 4)  lengthOfMonth = 30;
        else if (selectedMonthNo == 5)  lengthOfMonth = 31;
        else if (selectedMonthNo == 6)  lengthOfMonth = 30;
        else if (selectedMonthNo == 7)  lengthOfMonth = 31;
        else if (selectedMonthNo == 8)  lengthOfMonth = 31;
        else if (selectedMonthNo == 9)  lengthOfMonth = 30;
        else if (selectedMonthNo == 10)  lengthOfMonth = 31;
        else if (selectedMonthNo == 11)  lengthOfMonth = 30;
        else if (selectedMonthNo == 12)  lengthOfMonth = 31;




        List<String> labels=new ArrayList<String>();
        labels.add("Select Member Name");

        memberNo = 0;
        shopCost= 0;
        entryCount = 0;

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

        enteredMembers = new ArrayList<>();

        try {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            String res = backgroundWorker.execute("getMealRecord",USERNAME,Integer.toString(selectedMonthNo),selectedYear).get();

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
                        enteredMembers.clear();
                    }
                    member_name = individual.getString("member_name");
                    enteredMembers.add(member_name);
                    cost = Integer.parseInt(individual.getString("shopCost"));
                    i++;
                    count++;
                }
                if(i==memberNo){
                    currentDay = cDay + 1;
                    dayFlag = true;
                    enteredMembers.clear();
                }else{
                    currentDay = cDay;
                    dayFlag = false;
                    shopCost = cost;
                    entryCount = i;
                }

            }else{
                currentDay = 1;
                dayFlag = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("shopcost>>" + shopCost + " >>currentday>>" + currentDay + ">>dayflag>>" + dayFlag + ">>enter>>" + enteredMembers.size());

        if(shopCost>0){
            editText_CostOfShopping.setText(Integer.toString(shopCost));
            editText_CostOfShopping.setEnabled(false);
        }

        try {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            String res = backgroundWorker.execute("getResult",USERNAME,Integer.toString(selectedMonthNo),selectedYear).get();

            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("result");
            if(jsonArray.length()>0) {
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject individual = jsonArray.getJSONObject(count);
                    total_meal = Float.valueOf(individual.getString("total_meal"));
                    total_cost = Integer.parseInt(individual.getString("total_cost"));
                    count++;
                }

            }else{
                total_cost = 0;
                total_meal = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        if( currentDay <= lengthOfMonth) {
            edittextDay.setText("" + currentDay);
            edittextDay.setEnabled(false);
            if (!dayFlag) {
                editText_CostOfShopping.setText("" + shopCost);
                editText_CostOfShopping.setEnabled(false);
            }
        }
        else
        {
            saveDatabtn.setEnabled(false);
            edittextDay.setText("Current Month has Ended.");
            edittextDay.setEnabled(false);
        }

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMemberName.setAdapter(adapter);

        //select member
        spinnerMemberName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectMember=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        String[] mealNumber_breakfast= {"Meal Of Breakfast","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};

        ArrayAdapter<String> adapter_breakfast=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mealNumber_breakfast);
        adapter_breakfast.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerbreakfastMeal.setAdapter(adapter_breakfast);


        //select breakfast meal of a member
        spinnerbreakfastMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectBreakfastmeal =adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] mealNumber_lunch= {"Meal Of Lunch","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};

        ArrayAdapter<String> adapter_lunch=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mealNumber_lunch);
        adapter_lunch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlaunchMeal.setAdapter(adapter_lunch);


        //select lunchmeal of a member
        spinnerlaunchMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectLunchmeal =adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String[] mealNumber_dinner= {"Meal Of Dinner","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};

        ArrayAdapter<String> adapter_dinner=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mealNumber_dinner);
        adapter_dinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdinnerMeal.setAdapter(adapter_dinner);


        //select dinnner ,eal of a member
        spinnerdinnerMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectDinnermeal =adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        InsertMealInfo();

        // System.out.println("       ....................... "+selectBreakfastmeal+"                  ..........."+selectLunchmeal+"      ......... "+selectDinnermeal);
    }

    public void  InsertMealInfo()
    {
        saveDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                message="";
                boolean flag=false;
                res = myDatabase.GetData();
                String costOfShopping=editText_CostOfShopping.getText().toString();
                shopCost = Integer.parseInt(costOfShopping);

                if( dayFlag) {

                    if (costOfShopping.isEmpty()) {
                        showMessage("Error!", "Put the Cost Of Shopping");
                        return;
                    }
                }


                if(selectMember.equals("Select Member Name"))
                {
                    //Toast.makeText(DailyInfoActivity.this,"Plz Select member",Toast.LENGTH_SHORT).show();
                    showMessage("Error!","Plz Select member");
                    return ;
                }

                /*
                if(currentDay <= lengthOfMonth){
                    message +="Current Month has ended"+"\n";
                    flag=true;
                }
                */
                if(selectBreakfastmeal.equals("Meal Of Breakfast"))
                {
                    message +="Enter the number meal in Breakfast"+"\n";
                    flag=true;
                }
                if(selectLunchmeal.equals("Meal Of Lunch"))
                {
                    message +="Enter the number meal in Lunch"+"\n";
                    flag=true;
                }
                if(selectDinnermeal.equals("Meal Of Dinner"))
                {
                    message +="Enter the number meal in Dinner"+"\n";
                    flag=true;
                }
                int j = 0;
                while(j<enteredMembers.size()){
                    if(selectMember.equals(enteredMembers.get(j++))){
                        message +="This Member's details already added"+"\n";
                        flag=true;
                        break;
                    }
                }
                if(flag)
                {
                    showMessage("Error!",message);
                    return;
                }


                else {

                    String res = "";

                    try {
                        BackgroundWorker backgroundWorker = new BackgroundWorker(DailyInfoActivity.this);
                       // float breakfastmeal=Float.valueOf(selectBreakfastmeal)/2 ;
                        res = backgroundWorker.execute("initMealRecord",USERNAME,Integer.toString(selectedMonthNo),selectedYear,selectMember,Integer.toString(currentDay),selectBreakfastmeal,selectLunchmeal,selectDinnermeal,costOfShopping).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (res.equals("Successfully inserted" + "\n")) {

                        enteredMembers.add(selectMember);

                        //cost & meal update
                        if(dayFlag)
                        {
                        total_cost  += shopCost;
                        }
                        total_meal  += ( (float)( (Float.valueOf(selectBreakfastmeal))/2 ) + ( Float.valueOf(selectLunchmeal)) + Float.valueOf(selectDinnermeal) );
                        float total_meal_current = 0;
                        try {
                            BackgroundWorker backgroundWorker1 = new BackgroundWorker(DailyInfoActivity.this);
                            String res1 = backgroundWorker1.execute("getMessMemberData",USERNAME,Integer.toString(selectedMonthNo),selectedYear).get();

                            JSONObject jsonObject=new JSONObject(res1);
                            JSONArray jsonArray=jsonObject.getJSONArray("mess_member");
                            int count=0;
                            String member_name,total_meal;
                            while(count<jsonArray.length() )
                            {
                                JSONObject individual=jsonArray.getJSONObject(count);
                                member_name = individual.getString("member_name");
                                total_meal = individual.getString("total_meal");
                                if(member_name.equals(selectMember))
                                    total_meal_current = Float.valueOf(total_meal);
                                count++;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        total_meal_current += ( (float)( (Float.valueOf(selectBreakfastmeal))/2 ) + ( Float.valueOf(selectLunchmeal)) + Float.valueOf(selectDinnermeal) );
                        System.out.println(">>>>>>>>  " + total_meal + " <<<<<<<");

                        try {
                            BackgroundWorker backgroundWorker2 = new BackgroundWorker(DailyInfoActivity.this);
                            res = backgroundWorker2.execute("updateTotalMealMessMember",USERNAME,Integer.toString(selectedMonthNo),selectedYear,Float.toString(total_meal_current),selectMember).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        try {
                            BackgroundWorker backgroundWorker3 = new BackgroundWorker(DailyInfoActivity.this);
                            res = backgroundWorker3.execute("updateResult",USERNAME,Integer.toString(selectedMonthNo),selectedYear,Integer.toString(total_cost),Float.toString(total_meal)).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }


                        entryCount++;
                        dayFlag= false  ;//edit
                        editText_CostOfShopping.setText(Integer.toString(shopCost));
                        editText_CostOfShopping.setEnabled(false);

                        if( entryCount == memberNo)
                        {
                            saveDatabtn.setEnabled(false);
                            dayFlag= true ;

                            if(currentDay == lengthOfMonth )
                            {
                                saveDatabtn.setEnabled(false);
                                edittextDay.setText("Current Month is Ended.");
                                try {
                                    BackgroundWorker backgroundWorker4 = new BackgroundWorker(DailyInfoActivity.this);
                                    res = backgroundWorker4.execute("updateMonth",USERNAME,Integer.toString(selectedMonthNo),selectedYear,"1").get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            }

                            Intent intent=new Intent( DailyInfoActivity.this, Mess_ContentAcitivity.class);
                            startActivity(intent);
                            finish();
                        }

                        Toast.makeText(DailyInfoActivity.this, "Data is Inserted", Toast.LENGTH_LONG).show();



                    } else {
                        Toast.makeText(DailyInfoActivity.this, "Data is not Inserted", Toast.LENGTH_LONG).show();
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
