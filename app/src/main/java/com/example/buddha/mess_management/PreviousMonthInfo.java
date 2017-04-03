package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class PreviousMonthInfo extends AppCompatActivity {

    String selectedMonth,selectedYear;
    Button submit;
    int selectedMonthNo;
    Spinner spinnerMonth,spinnerYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_month_info);
        spinnerMonth=(Spinner)findViewById(R.id.spinner_month);
        spinnerYear=(Spinner)findViewById(R.id.spinner_year);
        submit=(Button)findViewById(R.id.button_submit);

        String[] months= {"Select Month","January","February","March","April","March","May","June","July","August","September","Octuber","November","December"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectedMonth=adapterView.getItemAtPosition(i).toString();
                selectedMonthNo= i ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        String[] years= {"Select Year","2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027"};
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,years);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter2);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedYear =adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
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
                    SharedPreferences prefs = getSharedPreferences("MYPREFS",0);
                    String userName = prefs.getString("username","");

                    System.out.println(">>>>>>>>    "+userName+"        >>>>>>>  "+selectedMonthNo+"           >>>>>>>>>>>>>>>  "+selectedYear);

                    float meal_rate= (float) 0.0;
                    try {
                        BackgroundWorker backgroundWorker = new BackgroundWorker(PreviousMonthInfo.this);
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

                            buffer.append("Month :" +selectedMonth  + "\n");
                            buffer.append("Year  :" + selectedYear + "\n");

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

                        BackgroundWorker backgroundWorker2 = new BackgroundWorker(PreviousMonthInfo.this);
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
                                //  buffer.append("Tolal cost        :" + individual2.getString("total_cost") + "\n\n");
                                buffer.append("Tolal cost        :" +Float.valueOf(individual2.getString("total_meal"))*(meal_rate) + "\n\n");

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
                    } catch (Exception e) {
                        e.printStackTrace();
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
