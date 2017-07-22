package com.example.buddha.mess_management;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MealInfoPerDayActivity extends AppCompatActivity {

    public static String userName="";
    public static int  selectedMonthNo;
    public  static  String selectedYear="",selectedMonth;
    public String res;

    TextView tv_currentMonth;
    TableLayout t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info_per_day);

        SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
        userName=prefs.getString("username","");
        selectedMonthNo=Integer.parseInt(prefs.getString("selectedMonthNo","0"));
        selectedYear=prefs.getString("selectedYear","");

        tv_currentMonth = (TextView) findViewById(R.id.textView_currentMonth);
        t1 = (TableLayout) findViewById(R.id.t1);

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

        tv_currentMonth.setText("Current Month : " + selectedMonth +", " +selectedYear);

        res = getIntent().getExtras().getString("res");

        try{
            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("meal_record");
            if(jsonArray.length() > 0) {
                int count = 0;
                int dayFlag = 0;
                while (count < jsonArray.length()) {
                    JSONObject individual = jsonArray.getJSONObject(count);
                    int cday = Integer.parseInt(individual.getString("day"));
                    if(dayFlag!= cday) {
                        dayFlag = cday;
                        TableRow tr = new TableRow(this);
                        TextView tv1,tv2;

                        tv1 = new TextView(this);
                        tv2 = new TextView(this);

                        tv1.setText("Day " + individual.getString("day"));
                        tv2.setText("Shopping Cost = " + individual.getString("shopCost"));

                        tv1.setTextColor(getResources().getColor(R.color.white));
                        tv2.setTextColor(getResources().getColor(R.color.white));
                        tv1.setGravity(Gravity.CENTER);
                        tv2.setGravity(Gravity.CENTER);
                        tv1.setWidth(0);
                        tv2.setWidth(0);
                        tv1.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));
                        tv2.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                        tv1.setBackgroundResource(R.drawable.shape_design_05);
                        tv2.setBackgroundResource(R.drawable.shape_design_05);
                        tr.addView(tv1);
                        tr.addView(tv2);
                        t1.addView(tr);

                        TableRow tr1 = new TableRow(this);
                        TextView tv11,tv22,tv3,tv4;

                        tv11 = new TextView(this);
                        tv22 = new TextView(this);
                        tv3 = new TextView(this);
                        tv4 = new TextView(this);

                        tv11.setText("NAME");
                        tv22.setText("BreakfastMeal");
                        tv3.setText("LunchMeal");
                        tv4.setText("DinnerMeal");

                        tv11.setTextColor(getResources().getColor(R.color.white));
                        tv22.setTextColor(getResources().getColor(R.color.white));
                        tv3.setTextColor(getResources().getColor(R.color.white));
                        tv4.setTextColor(getResources().getColor(R.color.white));

                        tv11.setGravity(Gravity.CENTER);
                        tv22.setGravity(Gravity.CENTER);
                        tv3.setGravity(Gravity.CENTER);
                        tv4.setGravity(Gravity.CENTER);

                        tv11.setWidth(0);
                        tv22.setWidth(0);
                        tv3.setWidth(0);
                        tv4.setWidth(0);

                        tv11.setBackgroundResource(R.drawable.shape_design_05);
                        tv22.setBackgroundResource(R.drawable.shape_design_05);
                        tv3.setBackgroundResource(R.drawable.shape_design_05);
                        tv4.setBackgroundResource(R.drawable.shape_design_05);

                        tv11.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));
                        tv22.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                        tv3.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));
                        tv4.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));

                        tr1.addView(tv11);
                        tr1.addView(tv22);
                        tr1.addView(tv3);
                        tr1.addView(tv4);
                        t1.addView(tr1);

                    }

                    TableRow tr2 = new TableRow(this);
                    TextView tv1,tv2,tv3,tv4;

                    tv1 = new TextView(this);
                    tv2 = new TextView(this);
                    tv3 = new TextView(this);
                    tv4 = new TextView(this);

                    tv1.setText(individual.getString("member_name"));
                    tv2.setText(individual.getString("breakfastMeal"));
                    tv3.setText(individual.getString("lunchMeal"));
                    tv4.setText(individual.getString("dinnerMeal"));

                    tv1.setTextColor(getResources().getColor(R.color.black));
                    tv2.setTextColor(getResources().getColor(R.color.black));
                    tv3.setTextColor(getResources().getColor(R.color.black));
                    tv4.setTextColor(getResources().getColor(R.color.black));

                    tv1.setGravity(Gravity.CENTER);
                    tv2.setGravity(Gravity.CENTER);
                    tv3.setGravity(Gravity.CENTER);
                    tv4.setGravity(Gravity.CENTER);

                    tv1.setWidth(0);
                    tv2.setWidth(0);
                    tv3.setWidth(0);
                    tv4.setWidth(0);

                    tv1.setBackgroundResource(R.drawable.shape_design_04);
                    tv2.setBackgroundResource(R.drawable.shape_design_04);
                    tv3.setBackgroundResource(R.drawable.shape_design_04);
                    tv4.setBackgroundResource(R.drawable.shape_design_04);

                    tv1.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));
                    tv4.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));

                    tr2.addView(tv1);
                    tr2.addView(tv2);
                    tr2.addView(tv3);
                    tr2.addView(tv4);
                    t1.addView(tr2);


//                    buffer.append("Name          :" + individual.getString("member_name") + "\n");
//                    buffer.append("Breakfastmeal :" + individual.getString("breakfastMeal") +"\n");
//                    buffer.append("Lunchmeal     :" + individual.getString("lunchMeal") + "\n");
//                    buffer.append("Dinnermeal    :" + individual.getString("dinnerMeal") + "\n\n");
                    count++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
