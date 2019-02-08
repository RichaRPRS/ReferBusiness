package rprs.business.refer.referbusiness;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EntryAddActivity extends AppCompatActivity implements View.OnClickListener{

    Button submit;
    EditText names,contact,email,noentry,edetail;
    Spinner category,subcateg,state,country;

    Context context;

    String url="http://reichprinz.com/teaAndroid/referbusiness/fetchcategories.php";
    String url2="http://reichprinz.com/teaAndroid/referbusiness/fetchcountry.php";
    String HttpUrl="http://reichprinz.com/teaAndroid/referbusiness/insertevents.php";
    ProgressDialog dialog;

    ArrayList<HashMap> mylist, mylist2;

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    List<String> list,list2,listid;
    public static List<String> countrylists,statelist,countryidlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_add);

        context=this;
        category=(Spinner)findViewById(R.id.spinnercat);
        subcateg=(Spinner)findViewById(R.id.spinnersubcat);
        country=(Spinner)findViewById(R.id.spinnercontry);
        state=(Spinner)findViewById(R.id.spinnerstate);
        subcateg.setVisibility(View.GONE);
        state.setVisibility(View.GONE);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        submit=(Button)findViewById(R.id.button);

        names=(EditText)findViewById(R.id.editnames);
        contact=(EditText)findViewById(R.id.editcont);
        email=(EditText)findViewById(R.id.editemail);
        noentry=(EditText)findViewById(R.id.editpass);
        edetail=(EditText)findViewById(R.id.editrepass);

        contact.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        datafromServer();
        countrygetserver();

        submit.setOnClickListener(this);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                list2 = new ArrayList<String>();
                listid = new ArrayList<String>();
                if (category.getSelectedItem().equals("No Category")){
                    subcateg.setVisibility(View.VISIBLE);
                    list2.add("Sub Category");
                }else {
                    subcateg.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mylist.size(); i++) {
                        HashMap<String, String> hashmap = mylist.get(i);
                        String cat_id = hashmap.get("Category_Id");
                        String categ = hashmap.get("CategoryName");
                        String subcat = hashmap.get("subcategory");
                        if (category.getSelectedItem().equals(categ)) {
                            list2.add(subcat);
                            listid.add(cat_id);
                        }
                    }
                }
                if (list2.size() > 0) {
                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(context, R.layout.spinner_item, list2);
                    subcateg.setAdapter(adp1);
                } else {
                    /*ArrayAdapter<String> adp1 = new ArrayAdapter<String>(context, R.layout.spinner_item, list2);
                    subcateg.setAdapter(adp1);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statelist = new ArrayList<String>();
                countryidlist = new ArrayList<String>();
                if (category.getSelectedItem().equals("Country")){
                    state.setVisibility(View.VISIBLE);
                    statelist.add("No state");
                }else {
                    state.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mylist2.size(); i++) {
                        HashMap<String, String> hashmap = mylist2.get(i);
                        String cat_id = hashmap.get("Country_Id");
                        String countrynn = hashmap.get("countryName");
                        String statename = hashmap.get("state");
                        if (country.getSelectedItem().equals(countrynn)) {
                            statelist.add(statename);
                            countryidlist.add(cat_id);
                        }
                    }
                }
                if (statelist.size()>0) {
                    ArrayAdapter<String> adpstate = new ArrayAdapter<String>(context, R.layout.spinner_item, statelist);
                    state.setAdapter(adpstate);
                }else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.referedb:
                Intent intent=new Intent(context,BusinessDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.profile:
                Intent intents=new Intent(context,ProfileActivity.class);
                startActivity(intents);
                break;
            case R.id.aboutus:
                Intent inte=new Intent(context,AboutusActivity.class);
                startActivity(inte);
                break;
            case R.id.logout:
                editor.clear();
                editor.commit();
                Intent intent3=new Intent(context,LoginActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                EntryAddActivity.this.finish();
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void datafromServer(){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait....");
        dialog.show();
        dialog.setCancelable(false);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                //Toast.makeText(context, "data "+string, Toast.LENGTH_SHORT).show();
                if (string.equals("No Result"))
                {
                    Toast.makeText(context, "Something went wrong try after some time", Toast.LENGTH_SHORT).show();
                }else {
                    mylist = new ArrayList<HashMap>();
                    list = new ArrayList<String>();
                    try {
                        JSONArray fruitsArray = new JSONArray(string);
                        //list.add("Category");
                        for (int i = 0; i < fruitsArray.length(); ++i) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            JSONObject jsonObject = fruitsArray.getJSONObject(i);

                            String id = jsonObject.optString("id");
                            String categoryn = jsonObject.getString("categoryName");
                            String subcate = jsonObject.optString("subcategory");
                            if (!list.contains(categoryn)) {
                                list.add(categoryn);
                            }
                            hashMap.put("Category_Id", id);
                            hashMap.put("CategoryName", categoryn);
                            hashMap.put("subcategory", subcate);
                            mylist.add(hashMap);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                vendorspinner();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datafromServer();
                    }
                });
                alertDialogBuilder.show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    public void vendorspinner(){
        if (list.size()>0) {
            ArrayAdapter<String> adp = new ArrayAdapter<String>(this,R.layout.spinner_item, list);
            category.setAdapter(adp);
        }else {
            list.add("No Category");
            ArrayAdapter<String> adp = new ArrayAdapter<String>(this,R.layout.spinner_item, list);
            category.setAdapter(adp);
        }
    }

    public void countryspinner(){
        if (countrylists.size()>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, countrylists);
            country.setAdapter(adapter);
        }else {
            countrylists.add("Country");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, countrylists);
            country.setAdapter(adapter);
        }
    }

    public void countrygetserver(){
        dialog.show();
        dialog.setCancelable(false);
        StringRequest request = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                //Toast.makeText(context, "data "+string, Toast.LENGTH_SHORT).show();
                if (string.equals("No Result"))
                {
                    Toast.makeText(context, "Something went wrong try after some time", Toast.LENGTH_SHORT).show();
                }else {
                    mylist2 = new ArrayList<HashMap>();
                    countrylists = new ArrayList<String>();
                    try {
                        JSONArray fruitsArray = new JSONArray(string);
                        //list.add("Category");
                        for (int i = 0; i < fruitsArray.length(); ++i) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            JSONObject jsonObject = fruitsArray.getJSONObject(i);

                            String id = jsonObject.optString("id");
                            String countryName = jsonObject.getString("countryName");
                            String state = jsonObject.optString("state");
                            if (!countrylists.contains(countryName)) {
                                countrylists.add(countryName);
                            }
                            hashMap.put("Country_Id", id);
                            hashMap.put("countryName", countryName);
                            hashMap.put("state", state);
                            mylist2.add(hashMap);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                countryspinner();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        countrygetserver();
                    }
                });
                alertDialogBuilder.show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        if (names.getText().toString().trim().length()<=0 ){
            names.setError("Please Enter Contact Person Name !");
            names.requestFocus();
        }else if (noentry.getText().toString().trim().length()<=0){
            noentry.setError("Please Enter Business Name");
            noentry.requestFocus();
        }else if (contact.getText().toString().trim().length()<=0){
            contact.setError("Please Enter Mobile Number !");
            contact.requestFocus();
        }else if (contact.getText().toString().trim().length()!=10){
            contact.setError("Please Enter Correct 10 digit Number !");
            contact.requestFocus();
        }else if (email.getText().toString().trim().length() <=0){
            email.setError("Please Enter Email address");
            email.requestFocus();
        } else if (!isValidEmailId(email.getText().toString().trim())){
            email.setError("Please Enter valid Email address");
            email.requestFocus();
        }else if (category.getSelectedItem().toString().equals("No Category")){
            Toast.makeText(context, "No Category available", Toast.LENGTH_SHORT).show();
        }else if (subcateg.getSelectedItem().toString().equals("Sub Category")){
            Toast.makeText(context, "No Sub Category available", Toast.LENGTH_SHORT).show();
        }else if (country.getSelectedItem().toString().equals("Country")){
            Toast.makeText(context, "No Country available", Toast.LENGTH_SHORT).show();
        }else {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {

            }
            insertData();
        }
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void insertData(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String nowDate = df3.format(c.getTime());
        int ppps=subcateg.getSelectedItemPosition();
        final String categoryids=listid.get(ppps);
        final String subcategorystr=subcateg.getSelectedItem().toString();
        final String countryid=countryidlist.get(state.getSelectedItemPosition());
        String username=pref.getString("user_name", "");
        String emailbody="<br /><br /><br />Business name <b>: "+ names.getText().toString()
                +"</b><br /> Added by <b>: "+username
                +"</b><br /> Contact number of business <b>: "+contact.getText().toString()
                +"</b><br /> Email id of business : "+email.getText().toString();

        dialog.show();
        final String strbusname=noentry.getText().toString();
        final String stredetail;
        if (edetail.getText().toString().trim().length() <=0)
            stredetail=edetail.getText().toString();
        else
            stredetail="";

        new SendMailTask(EntryAddActivity.this).execute( "richa.sharma@reichprinz.com","Reference added by "+username,emailbody );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        dialog.dismiss();
                        if (ServerResponse.equals("1")) {
                            Toast.makeText(context, "Reference added successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(context,BusinessDetailsActivity.class);
                            intent.putExtra("bd","PAR");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            EntryAddActivity.this.finish();
                        }else {
                            Toast.makeText(context, "Some error Occured ! Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dialog.dismiss();
                        // Showing error message if something goes wrong.volleyError.toString()
                        Toast.makeText(context, "Server error! please try again ", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", DataField.user_id);
                params.put("name", names.getText().toString());
                params.put("mobile", contact.getText().toString());
                params.put("email", email.getText().toString());
                params.put("category_id", categoryids);
                params.put("subcategory", subcategorystr);
                params.put("country", countryid);
                params.put("state", state.getSelectedItem().toString());
                params.put("businessn", strbusname);
                params.put("entryDetail", stredetail);
                params.put("date", nowDate);
                return params;
            }
        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
