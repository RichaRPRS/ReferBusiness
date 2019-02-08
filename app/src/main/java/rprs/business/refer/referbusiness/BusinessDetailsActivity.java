package rprs.business.refer.referbusiness;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusinessDetailsActivity extends AppCompatActivity {

    String HttpUrl="http://reichprinz.com/teaAndroid/referbusiness/fetchbusiness.php";
    ProgressDialog dialog;
    ListView listView;
    ImageView imageView;

    ArrayList<BusinessModel> dataModels;
    private static BusinessAdapter adapter;

    Context context;
    Activity activity;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    Button addref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        context=this;
        activity=this;
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        listView=(ListView)findViewById(R.id.list);
        addref=(Button)findViewById(R.id.button2);
        imageView=(ImageView)findViewById(R.id.imageView6);
        imageView.setVisibility(View.GONE);

        /*FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,EntryAddActivity.class);
                //BusinessDetailsActivity.this.finish();
                startActivity(intent);
            }
        });*/

        addref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EntryAddActivity.class);
                //BusinessDetailsActivity.this.finish();
                startActivity(intent);
            }
        });

        serverData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.addentry:
                Intent intent=new Intent(context,EntryAddActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                BusinessDetailsActivity.this.finish();
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void serverData(){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();
        StringRequest request2 = new StringRequest(Request.Method.POST,HttpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                dataModels= new ArrayList<>();

                //Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray fruitsArray =  new  JSONArray(string);
                    for(int i = 0; i < fruitsArray.length(); ++i) {
                        JSONObject jsonObject=fruitsArray.getJSONObject(i);
                        String Event_Id=jsonObject.getString("entryNumber");
                        String name=jsonObject.optString("name");
                        String mobile=jsonObject.optString("contactno");
                        String status;
                        if (jsonObject.isNull("status")){
                            status="in process";
                        }else {
                            status=jsonObject.optString("status");//closed
                        }
                        if (jsonObject.isNull("name")) {

                        }else {
                            BusinessModel entriesModel = new BusinessModel(Event_Id, name, mobile, status);
                            dataModels.add(entriesModel);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataModels.size()>0) {
                    adapter= new BusinessAdapter(dataModels,getApplicationContext(),activity);
                    listView.setAdapter(adapter);
                    imageView.setVisibility(View.GONE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
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
                        serverData();
                    }
                });
                alertDialogBuilder.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", DataField.user_id);
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request2);
    }


}
