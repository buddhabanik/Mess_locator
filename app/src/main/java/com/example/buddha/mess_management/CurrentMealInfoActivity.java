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
import org.w3c.dom.Text;

public class CurrentMealInfoActivity extends AppCompatActivity {

    public static String userName="";
    public static int  selectedMonthNo;
    public  static  String selectedYear="",selectedMonth;
    public String res,res2;

    TableLayout t2;
    TextView tv_currentMonth;
    TextView tv_totalMoney,tv_totalSpentMoney,tv_totalMeal,tv_perMealCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_meal_info);

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

        t2 = (TableLayout) findViewById(R.id.t2);

        tv_totalMeal = (TextView) findViewById(R.id.textView_totalMeal);
        tv_totalMoney = (TextView) findViewById(R.id.textView_totalMoney);
        tv_totalSpentMoney = (TextView) findViewById(R.id.textView_totalSpentMoney);
        tv_perMealCost = (TextView) findViewById(R.id.textView_PerMealCost);
        tv_currentMonth = (TextView) findViewById(R.id.textView_currentMonth);

        tv_currentMonth.setText("Current Month : " + selectedMonth +", " +selectedYear);


        res = getIntent().getExtras().getString("res");
        res2 = getIntent().getExtras().getString("res2");

        float meal_rate= (float) 0.0;
        try {
            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("result");
            if(jsonArray.length() > 0) {
                JSONObject individual = jsonArray.getJSONObject(0);
                System.out.println("...............  " + individual.getString("total_money") + "            ........." + individual.getString("total_cost") + "                   .............." + individual.getString("total_meal"));


                meal_rate = (float) (Integer.parseInt(individual.getString("total_cost")) / (Float.valueOf(individual.getString("total_meal"))));


                tv_totalMoney.setText("Total money       : "+individual.getString("total_money"));
                tv_totalMeal.setText("Tolal meal        : "+individual.getString("total_meal"));
                tv_totalSpentMoney.setText("Total spent money : "+individual.getString("total_cost"));
                tv_perMealCost.setText("Per meal cost     : "+meal_rate);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try{
            JSONObject jsonObject2=new JSONObject(res2);
            JSONArray jsonArray2=jsonObject2.getJSONArray("mess_member");
            if(jsonArray2.length() > 0) {
                int countObject = 0;

                while (countObject < jsonArray2.length()) {
                    JSONObject individual2 = jsonArray2.getJSONObject(countObject);

                    TableRow tr = new TableRow(this);

                    TextView tv1,tv2,tv3,tv4;

                    tv1 = new TextView(this);
                    tv2 = new TextView(this);
                    tv3 = new TextView(this);
                    tv4 = new TextView(this);

                    tv1.setText(individual2.getString("member_name"));
                    tv2.setText(individual2.getString("total_amount"));
                    tv3.setText(individual2.getString("total_meal"));
                    tv4.setText(Float.toString(Float.valueOf(individual2.getString("total_meal"))*meal_rate));

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

                    tv1.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                    tv2.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));
                    tv3.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));
                    tv4.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f));

                    tr.addView(tv1);
                    tr.addView(tv2);
                    tr.addView(tv3);
                    tr.addView(tv4);
                    t2.addView(tr);

                    countObject++;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
