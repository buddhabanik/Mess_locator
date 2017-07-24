package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;

public class MonthSelectionActivity extends AppCompatActivity {

    public static String USERNAME;

    Spinner spinner1,spinner2;
    Button next;
    public static String selectedMonth="";
    public static String selectedYear,selectedYearpre;
    public static int selectedMonthNo,selectedMonthNopre;

//    public BackgroundWorker backgroundWorker = new BackgroundWorker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_selection);
        spinner1=(Spinner)findViewById(R.id.spinner2);
        spinner2 = (Spinner)findViewById(R.id.spinner_year);
        next = (Button)findViewById(R.id.button_next);

        final SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
        USERNAME = prefs.getString("username","");
        selectedMonthNopre = Integer.parseInt(prefs.getString("selectedMonthNo",""));
        selectedYearpre = prefs.getString("selectedYear","");

        selectedMonth="";
        selectedYear="";

        String[] months= {"Select Month","January","February","March","April","March","May","June","July","August","September","Octuber","November","December"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedMonth =adapterView.getItemAtPosition(i).toString();
                selectedMonthNo = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        String[] years= {"Select Year","2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027"};
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,years);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedYear =adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((selectedMonth.equals("")||selectedMonth.equals("Select Month")) && (selectedYear.equals("")|| selectedYear.equals("Select Year")))
                {
                    showMessage("Error!","Plz select a month & year");
                }
                else if(selectedMonth.equals("") ||selectedMonth.equals("Select Month") )
                {
                    showMessage("Error!","Plz select a month");
                }
                else if(selectedYear.equals("") || selectedYear.equals("Select Year"))
                {
                    showMessage("Error!","Plz select a year");
                }
                else
                {
                    try {
                        BackgroundWorker backgroundWorker2 = new BackgroundWorker(MonthSelectionActivity.this);
                        String res = backgroundWorker2.execute("updateMonth",USERNAME,Integer.toString(selectedMonthNopre),selectedYearpre,"1").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("selectedMonthNo", Integer.toString(selectedMonthNo));
                    editor.putString("selectedMonth", selectedMonth);
                    editor.putString("selectedYear", selectedYear);
                    editor.commit();

                    String res = "";
                    try {

                        BackgroundWorker backgroundWorker = new BackgroundWorker(MonthSelectionActivity.this);
                        res = backgroundWorker.execute("initMonth", USERNAME, Integer.toString(selectedMonthNo), selectedYear, "0").get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (res.equals("Month and year already selected")) {
                        showMessage("Error!!", "Month and year already selected");
                    } else {
                        Intent intent = new Intent(MonthSelectionActivity.this, Mess_activity.class);
                        startActivity(intent);
                        finish();
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
