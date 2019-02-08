package rprs.business.refer.referbusiness;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    EditText names,contact,email,pass;
    TextView percentagev,accnumberv;
    Button updatebtn,discardbtn;
    CheckBox showp;

    String url="http://reichprinz.com/teaAndroid/referbusiness/fetchuser.php";
    String HttpUrl2="http://reichprinz.com/teaAndroid/referbusiness/updateuser.php";

    ProgressDialog dialog;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context=this;

        updatebtn=(Button)findViewById(R.id.btnupdate);
        discardbtn=(Button)findViewById(R.id.btndiscard);

        names=(EditText)findViewById(R.id.editnames);
        contact=(EditText)findViewById(R.id.editcont);
        email=(EditText)findViewById(R.id.editemail);
        pass=(EditText)findViewById(R.id.editpass);
        percentagev=(TextView)findViewById(R.id.textPercentage);
        accnumberv=(TextView)findViewById(R.id.textacnumber);

        showp=(CheckBox)findViewById(R.id.checkBox);

        contact.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        pass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait....");
        dialog.show();

        updatebtn.setOnClickListener(this);
        discardbtn.setOnClickListener(this);

        getServerData();

        showp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show password
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass.requestFocus();
                    pass.setSelection(pass.getText().length());
                } else {
                    // hide password
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass.requestFocus();
                    pass.setSelection(pass.getText().length());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnupdate:
                String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+\\.+[a-z]+";
                if (names.getText().toString().trim().length()<=0 ){
                    names.setError("Please Enter Name !");
                    names.requestFocus();
                }else if (contact.getText().toString().trim().length()<=0){
                    contact.setError("Please Enter Mobile Number !");
                    contact.requestFocus();
                }else if (contact.getText().toString().trim().length()!=10){
                    contact.setError("Please Enter Correct 10 digit Number !");
                    contact.requestFocus();
                }else if (email.getText().toString().trim().length() <=0){
                    email.setError("Please Enter Email address");
                    email.requestFocus();
                } else if (!email.getText().toString().matches(emailPattern)){
                    email.setError("Please Enter valid Email address");
                    email.requestFocus();
                }else if (pass.getText().toString().trim().length() <=0){
                    pass.setError("Password not blank");
                    pass.requestFocus();
                }else if (pass.getText().toString().trim().length() <8){
                    pass.setError("Password should be in between 8-10 character");
                    pass.requestFocus();
                }else {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                    }
                    updateData();
                }
                break;

            case R.id.btndiscard:
                AlertDialog.Builder alertbuilder=new AlertDialog.Builder(context);
                alertbuilder.setTitle("Discard changes?");
                alertbuilder.setCancelable(true);
                alertbuilder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertbuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProfileActivity.this.finish();
                    }
                });
                alertbuilder.show();
                break;
        }
    }


    public void getServerData(){

        dialog.show();
        StringRequest request2 = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();

                //Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray fruitsArray =  new  JSONArray(string);
                    for(int i = 0; i < fruitsArray.length(); ++i) {
                        JSONObject jsonObject=fruitsArray.getJSONObject(i);
                        String Name=jsonObject.getString("fullName");
                        String PhoneNo=jsonObject.getString("contactNo");
                        String Email=jsonObject.getString("userEmail");
                        String password=jsonObject.optString("password");
                        names.setText(Name);
                        contact.setText(PhoneNo);
                        email.setText(Email);
                        pass.setText(password);
                        accnumberv.setText(jsonObject.getString("accountno"));
                        percentagev.setText(jsonObject.getString("per"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getServerData();
                    }
                });
                alertDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", DataField.user_id);
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request2);
    }

    public void updateData(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        dialog.dismiss();
                        if (ServerResponse.equals("1")) {
                            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                            ProfileActivity.this.finish();
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
                        Toast.makeText(context, "Server error please try again ", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", DataField.user_id);
                params.put("name", names.getText().toString());
                params.put("mobile", contact.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", pass.getText().toString());
                return params;
            }
        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
