package rprs.business.refer.referbusiness;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button login,signup;
    EditText emailadd,pass;
    CheckBox showp;

    ProgressDialog dialog;
    String url="http://reichprinz.com/teaAndroid/referbusiness/checkuser.php";
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        context=this;

        login=(Button)findViewById(R.id.button3);
        signup=(Button)findViewById(R.id.buttonregister);
        emailadd=(EditText) findViewById(R.id.textuname);
        pass=(EditText) findViewById(R.id.textpass);

        showp=(CheckBox)findViewById(R.id.checkBox);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailadd.getText().toString().trim().length()<=0){
                    emailadd.setError("Please Enter Email ID !");
                    emailadd.requestFocus();
                }else if (!isValidEmailId(emailadd.getText().toString().trim())){
                    emailadd.setError("Please Enter valid Email address");
                    emailadd.requestFocus();
                }else if (pass.getText().toString().trim().length()<=0){
                    pass.setError("Please Enter password");
                    pass.requestFocus();
                }else {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    serverdata(emailadd.getText().toString(), pass.getText().toString());
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        showp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show password
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass.setSelection(pass.getText().length());
                } else {
                    // hide password
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass.setSelection(pass.getText().length());
                }
            }
        });

    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void serverdata(final String emailid, final String passwd){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                if (string.equals("not")){
                    Toast.makeText(context, "Username and password Not match", Toast.LENGTH_SHORT).show();
                    emailadd.requestFocus();
                }else {
                    //Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                    try {
                        JSONArray fruitsArray = new JSONArray(string);
                        for (int i = 0; i < fruitsArray.length(); ++i) {
                            JSONObject jsonObject = fruitsArray.getJSONObject(i);
                            String name = jsonObject.getString("fullName");
                            String mobile = jsonObject.optString("contactNo");
                            String iduser = jsonObject.getString("id");
                            editor.putString("user_name", name);
                            editor.putString("user_id", iduser);
                            editor.putString("mobile", mobile);
                            editor.commit();
                            DataField.user_id = iduser;
                            DataField.user_name = name;
                            DataField.user_mobile = mobile;
                            Intent it = new Intent(getApplicationContext(), BusinessDetailsActivity.class);
                            LoginActivity.this.finish();
                            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(it);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        serverdata(emailid,passwd);
                    }
                });
                alertDialogBuilder.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                // Adding All values to Params.
                params.put("email", emailid);
                params.put("password", passwd);

                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }


}
