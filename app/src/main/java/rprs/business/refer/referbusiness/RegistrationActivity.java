package rprs.business.refer.referbusiness;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    Button submit;
    EditText names,contact,email,password,repasswd;
    Context context;

    String HttpUrl="http://reichprinz.com/teaAndroid/referbusiness/registration.php";
    ProgressDialog dialog;
    String emailbody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        context=this;
        submit=(Button)findViewById(R.id.button);

        names=(EditText)findViewById(R.id.editnames);
        contact=(EditText)findViewById(R.id.editcont);
        email=(EditText)findViewById(R.id.editemail);
        password=(EditText)findViewById(R.id.editpass);
        repasswd=(EditText)findViewById(R.id.editrepass);

        contact.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        repasswd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
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
        } else if (!isValidEmailId(email.getText().toString().trim())){
            email.setError("Please Enter valid Email address");
            email.requestFocus();
        }else if (password.getText().toString().trim().length()<=0){
            password.setError("Please Enter password");
            password.requestFocus();
        }else if (password.getText().toString().trim().length()<8){
            password.setError("Password should be in between 8-10 character");
            password.requestFocus();
        }else if (repasswd.getText().toString().trim().length()<=0){
            repasswd.setError("Please Enter password Again");
            repasswd.requestFocus();
        }else if (repasswd.getText().toString().trim().length()<8){
            repasswd.setError("Password should be in between 8-10 character");
            repasswd.requestFocus();
        }else if (!password.getText().toString().equals(repasswd.getText().toString())){
            repasswd.setError("Password not match");
            repasswd.requestFocus();
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
        final String emailid=email.getText().toString();
        emailbody="<br /><br /><h1>Thank you for the registration!</h1>" +
                "<p>We are pleased to inform you that your registration with Refer Business App is successful. You will get notified about every update that is made in the app. </p>"
                +"<br /><p>We run business with flexibility and digitalization. It's a constant concern of our company to make the business more efficient and rapid result imparting. Hence the Refer Business App is functioned with the uncomplicated procedure to make your job more effortless.</p>" +
                "<br /><p>Follow very simple procedure by filling in your reference, then instantly submitting the reference details. Join the hands with us and expand the norms of business for both the company and yourself. </p>" +
                "<br /><p>According to the policy of the company, the business development executive will be given 5 to 15% commision from the business he/she provides. </p>"+
                "<br /><h1>Please verify email id and update bank account details</h1>"+
                "<br /> http://officeporto.com/accountdetails.php?uid=";
        final String emailsubject="Thank you for the registration!";

        dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait....");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        if (ServerResponse.equals("0")) {
                            dialog.dismiss();
                            Toast.makeText(context, "Some error Occured! Please try again", Toast.LENGTH_SHORT).show();
                        }else if (ServerResponse.equals("2")) {
                            dialog.dismiss();
                            Toast.makeText(context, "Email ID  is already Registered", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            emailbody=emailbody+ServerResponse;
                            new SendMailTask(RegistrationActivity.this).execute(emailid,emailsubject,emailbody );
                            dialog.dismiss();
                            Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(context,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            RegistrationActivity.this.finish();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dialog.dismiss();
                        Toast.makeText(context, "Server error please try again ", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", names.getText().toString());
                params.put("mobile", contact.getText().toString());
                params.put("email", emailid);
                params.put("password", password.getText().toString());
                params.put("date", nowDate);
                return params;
            }
        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
