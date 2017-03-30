package com.example.buddha.mess_management;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by BUDDHA on 2/13/2017.
 */

public class BackgroundWorker extends AsyncTask<String ,String, String> {

    public static String type;
    public static String USERNAME;
    public static String RESULT;

    public boolean ok = true;

    public SharedPreferences prefs;
    Context context;
    TextView textView;
    AlertDialog alertDialog;
    String server_url="http://192.168.0.103";
    BackgroundWorker(Context ct)
    {
        context=ct;
    }

    @Override
    protected String doInBackground(String... params) {

        type=params[0];

        //for login
        if( type.equals("login"))
        {
            server_url +="/Mess_locator/login.php";
            try {
                USERNAME=params[1];
                String username=params[1];
                String password=params[2];
                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    result +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //for registration
        else if( type.equals("registation"))
        {
            server_url +="/Mess_locator/register.php";
            try {

                String fullname=params[1];
                String username=params[2];
                String email=params[3];
                String password=params[4];
                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("fullname","UTF-8")+"="+URLEncoder.encode(fullname,"UTF-8")+"&"+
                                  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                                  URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                                  URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    result +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //to add mess details in database
        else if( type.equals("AddInfoData"))
        {
            server_url +="/Mess_locator/addmessdata.php";
            try {
                String username=params[1];
                String Address= params[2];
                String Rent= params[3];
                String numberOfseat= params[4];
                String contractNumber= params[5];
                String description= params[6] ;

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("Address","UTF-8")+"="+URLEncoder.encode(Address,"UTF-8")+"&"+
                        URLEncoder.encode("Rent","UTF-8")+"="+URLEncoder.encode( Rent,"UTF-8")+"&"+
                        URLEncoder.encode("numberOfseat","UTF-8")+"="+URLEncoder.encode(numberOfseat,"UTF-8")+"&"+
                        URLEncoder.encode("contractNumber","UTF-8")+"="+URLEncoder.encode(contractNumber,"UTF-8")+"&"+
                        URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode( description,"UTF-8");


                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    result +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //to get data for mess details
        else if( type.equals("shownewpost"))
        {
            server_url +="/Mess_locator/get_jsondata.php";
            try {
                URL url=new URL(server_url);

                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // get only the userlogin mess information
        else if( type.equals("showMypost"))
        {
            server_url +="/Mess_locator/get_jsondata_mypost.php";
            try {
                String username=params[1];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //to update the month table in database
        else if(type.equals("initMonth"))
        {
            server_url +="/Mess_locator/month.php";
            try {
                String username=params[1];
                String selectedMonth= params[2];
                String year= params[3];
                String status= params[4];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("selectedMonth","UTF-8")+"="+URLEncoder.encode(selectedMonth,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode( year,"UTF-8")+"&"+
                        URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8");


                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    result +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //to update messmember table in database
        else if(type.equals("initMember"))
        {
            server_url +="/Mess_locator/mess_member.php";
            try {
                String username=params[1];
                String memberName= params[2];
                String month= params[3];
                String year= params[4];
                String amount = params[5];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("memberName","UTF-8")+"="+URLEncoder.encode(memberName,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8") + "&" +
                        URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(amount,"UTF-8");


                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    result +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        //to get data from messmember table
        else if( type.equals("getMessMemberData"))
        {
            server_url +="/Mess_locator/get_mess_member_data.php";
            try {
                System.out.println("started >>>>>");
                String username = params[1];
                String month = params[2];
                String year = params[3];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println("endede >>>>>");
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //set guest password
        else if( type.equals("setGuestPassword"))
        {
            server_url +="/Mess_locator/set_guest_password.php";
            try {

                String username=params[1];
                String password=params[2];
                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    result +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        //check managername and password
        else if( type.equals("selectManager"))
        {
            server_url +="/Mess_locator/check_manager_forguest.php";
            try {

                String username=params[1];
                String password=params[2];
                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    result +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get current month & year
        else if( type.equals("getCurrentMonthYear"))
        {
            server_url +="/Mess_locator/get_current_month_year.php";
            try {

                String username=params[1];
                System.out.println("in background  >>>>>>>>>>>   "+username);
                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    result +=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        else if( type.equals("updateAmount"))
        {
            server_url +="/Mess_locator/updateAmount.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];
                String membername = params[4];
                String amount = params[5];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8") + "&" +
                        URLEncoder.encode("membername","UTF-8")+"="+URLEncoder.encode(membername,"UTF-8") + "&" +
                        URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(amount,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // update total money in result
        else if( type.equals("updateMoneyInResult"))
        {
            server_url +="/Mess_locator/updateMoneyInResult.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];
                String updated_money = params[4];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8") + "&" +
                        URLEncoder.encode("updated_money","UTF-8")+"="+URLEncoder.encode(updated_money,"UTF-8") ;


                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        else if( type.equals("initMealRecord"))
        {
            server_url +="/Mess_locator/initMealRecord.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];
                String membername = params[4];
                String day = params[5];
                String breakfastMeal = params[6];
                String lunchMeal = params[7];
                String dinnerMeal = params[8];
                String shopCost = params[9];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8") + "&" +
                        URLEncoder.encode("memberName","UTF-8")+"="+URLEncoder.encode(membername,"UTF-8") + "&" +
                        URLEncoder.encode("day","UTF-8")+"="+URLEncoder.encode(day,"UTF-8") + "&" +
                        URLEncoder.encode("breakfastMeal","UTF-8")+"="+URLEncoder.encode(breakfastMeal,"UTF-8") + "&" +
                        URLEncoder.encode("lunchMeal","UTF-8")+"="+URLEncoder.encode(lunchMeal,"UTF-8") + "&" +
                        URLEncoder.encode("dinnerMeal","UTF-8")+"="+URLEncoder.encode(dinnerMeal,"UTF-8") + "&" +
                        URLEncoder.encode("shopCost","UTF-8")+"="+URLEncoder.encode(shopCost,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if( type.equals("getMealRecord"))
        {
            server_url +="/Mess_locator/getMealRecord.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if( type.equals("initResult"))
        {
            server_url +="/Mess_locator/initResult.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];
                String total_money = params[4];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8") + "&" +
                        URLEncoder.encode("total_money","UTF-8")+"="+URLEncoder.encode(total_money,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        else if( type.equals("getResult"))
        {
            server_url +="/Mess_locator/getResult.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        else if( type.equals("updateResult"))
        {
            server_url +="/Mess_locator/updateResult.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];
                String total_cost = params[4];
                String total_meal = params[5];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8") + "&" +
                        URLEncoder.encode("total_cost","UTF-8")+"="+URLEncoder.encode(total_cost,"UTF-8") + "&" +
                        URLEncoder.encode("total_meal","UTF-8")+"="+URLEncoder.encode(total_meal,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if( type.equals("updateTotalMealMessMember"))
        {
            server_url +="/Mess_locator/updateTotalMealMessMember.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];
                String total_meal = params[4];
                String selectedMember = params[5];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8") + "&" +
                        URLEncoder.encode("total_meal","UTF-8")+"="+URLEncoder.encode(total_meal,"UTF-8") + "&" +
                        URLEncoder.encode("selectedMember","UTF-8")+"="+URLEncoder.encode(selectedMember,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if( type.equals("updateMonth"))
        {
            server_url +="/Mess_locator/updateMonth.php";
            try {
                String username = params[1];
                String month = params[2];
                String year = params[3];
                String status = params[4];

                URL url=new URL(server_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data=  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("month","UTF-8")+"="+URLEncoder.encode( month,"UTF-8")+"&"+
                        URLEncoder.encode("year","UTF-8")+"="+URLEncoder.encode(year,"UTF-8") + "&" +
                        URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader( inputStream ));
                StringBuilder stringBuilder=new StringBuilder();
                String result="",line;
                while( (line=bufferedReader.readLine()) != null)
                {
                    stringBuilder.append( line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


                return null;
    }




    @Override
    protected void onPreExecute()
    {
       alertDialog = new AlertDialog.Builder(context).create();

    }


    @Override
    protected void onPostExecute(String result) {

        alertDialog.setMessage(result);
     //   alertDialog.setCancelable(true);
      //  alertDialog.show();


        if(type.equals("login"))
        {
            alertDialog.setTitle("Login status!");
            alertDialog.show();
            // Toast.makeText(BackgroundWorker.this, result ,Toast.LENGTH_SHORT).show();
            if( result.equals("login successfully"))
            {
                Intent intent=new Intent( context, MessInformaationActivity.class);
                intent.putExtra("username",USERNAME);
                context.startActivity(intent);
                //MainActivity.dialog.dismiss();
            }
        }

        else if(type.equals("registation"))
        {
            alertDialog.setTitle("Registration status!");
            alertDialog.show();
            // Toast.makeText(BackgroundWorker.this, result ,Toast.LENGTH_SHORT).show();
            if( result.equals("Successfully registered"))
            {
                Intent intent=new Intent( context, MainActivity.class);
                context.startActivity(intent);
               // MainActivity.dialog.dismiss();
            }
        }
        else if(type.equals("shownewpost"))
        {
            Intent intent=new Intent( context, DisplayDataActivity.class);
            intent.putExtra("json_data",result);
            context.startActivity(intent);
        }
        else if(type.equals("showMypost"))
        {
            Intent intent=new Intent( context, DisplayDataActivity.class);
            intent.putExtra("json_data",result);
            context.startActivity(intent);
        }
        else if(type.equals("updateMonth"))
        {
//            alertDialog.show();
        }
        else if(type.equals("updateMember"))
        {
//            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    public boolean getOk(){
        return ok;
    }
}
