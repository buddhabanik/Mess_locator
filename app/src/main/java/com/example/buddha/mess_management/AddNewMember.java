package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewMember extends AppCompatActivity {

    EditText name, amount;
    Button submit;
    public String Name_pattern = "^[A-Za-z][A-Za-z0-9]*$";
    public static Pattern pattern;
    public static Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);
        name= (EditText)findViewById(R.id.name_of_member);
        amount=(EditText)findViewById(R.id.amount_of_money);
        submit=(Button) findViewById(R.id.Submit);



        SharedPreferences prefs=getSharedPreferences("MYPREFS",0);
        final String userName=prefs.getString("username","");
        final String selectedMonth=prefs.getString("selectedMonthNo","");
        final String selectedYear=prefs.getString("selectedYear","");

      //  System.out.println(">>>>>>>>>>>>>>>>>>out        "+memberName+"<<<<<<<<<<<<<<<<<<<<<<     "+Amount);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String memberName=name.getText().toString();
                String Amount=amount.getText().toString();
                String validate_message="";

                if (memberName.isEmpty())
                    validate_message += "*Membername required\n";
                if (Amount.isEmpty())
                    validate_message += "*Amount of money required\n";

                pattern=Pattern.compile( Name_pattern);
                matcher=pattern.matcher(memberName);
                if( !matcher.matches())
                {
                    validate_message +="*Invalid name";
                }

                System.out.println(">>>>>>>>>>>>>>>>>>        "+memberName+"<<<<<<<<<<<<<<<<<<<<<<     "+Amount);
                //to get member's name from database and check with editname
                try {
                    BackgroundWorker backgroundWorker = new BackgroundWorker( AddNewMember.this);
                    String res = backgroundWorker.execute("getMessMemberData",userName,selectedMonth,selectedYear).get();

                    JSONObject jsonObject=new JSONObject(res);
                    JSONArray jsonArray=jsonObject.getJSONArray("mess_member");
                    int countObject=0;
                    String member_name,total_meal,total_amount,total_cost;
                    while(countObject<jsonArray.length() )
                    {
                        JSONObject individual=jsonArray.getJSONObject(countObject);
                        member_name = individual.getString("member_name");
                        String Checkname= member_name;
                        if(Checkname.equals(memberName))
                        {
                            validate_message ="Member name is already exits.\n";
                            break;
                        }

                        countObject++;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }


                if (validate_message.isEmpty())
                {
                    String res2="";
                    try {

                        BackgroundWorker backgroundWorker2 = new BackgroundWorker( AddNewMember.this);
                        res2 = backgroundWorker2.execute("initMember", userName, memberName,selectedMonth,selectedYear, Amount ).get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if ( res2.equals("Successfully inserted")) {
                        Toast.makeText( AddNewMember.this, "Data is Inserted", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText( AddNewMember.this, "Data is not Inserted", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                else
                {
                     showMessage("Error!",validate_message);
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
