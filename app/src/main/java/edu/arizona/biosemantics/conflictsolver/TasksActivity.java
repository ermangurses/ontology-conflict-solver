package edu.arizona.biosemantics.conflictsolver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by egurses on 3/13/18.
 */

public class TasksActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextMessage;
    private ProgressDialog mProgressDialog;
    //private static HashMap<String, String> hashMap = new HashMap<String, String>();
    private static Vector<String>  mUsernameArr = new Vector<String>();
    private static Vector<String>  mTermArr = new Vector<String>();
    private static boolean startedFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


        if(!startedFlag){
            getTasks();
        }

        if(!SharedPreferencesManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        // Set active the selected navigation icon in the new activity
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        // Add the listener to the navigation
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Take relativeLayoutXML from interface using by id
        RelativeLayout relativeLayoutXML =(RelativeLayout)findViewById(R.id.relativeLayoutXML);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ////////////////////////////////////////////////////////////////////
        ////////Adding linearLayoutProgVertical layout to scrollView////////
        ////////////////////////////////////////////////////////////////////
        LinearLayout linearLayoutProgVertical = new LinearLayout(this);
        linearLayoutProgVertical.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        linearLayoutProgVertical.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayoutProgVertical);


        int i = 0;
        for (int ii = 0; ii < mUsernameArr.size(); ii++) {
            System.out.println("Key = " + mUsernameArr.get(ii) + ", Value = " + mTermArr.get(ii));

                Button button = new Button(this);
                String srt ="A conflict " + "<em>" + ( mUsernameArr.get(ii)) + "</em>" + " from " + mTermArr.get(ii);
                button.setText(Html.fromHtml(srt));
                button.setTextColor(0xFFFF0000);
                button.setLeft(10);
                button.setId(i);
                i++;
                button.setOnClickListener(this);
                linearLayoutProgVertical.addView(button);
        }
        relativeLayoutXML.addView(scrollView);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            startedFlag = true;
        }
    }

    private void getTasks(){

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constants.URL_GETTASKS,
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        String mTerm;
                        String mUsername;

                        try{
                            JSONObject root = new JSONObject(response);
                            JSONArray task_data = root.getJSONArray("task_data");

                            for (int i = 0; i < task_data.length(); i++) {

                                JSONObject jsonObject = task_data.getJSONObject(i);

                                mTerm = jsonObject.getString("term");
                                mUsername = jsonObject.getString("username");
                                mUsernameArr.addElement(mUsername);
                                mTermArr.addElement(mTerm);
                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case  R.id.navigation_home:
                    startActivity(new Intent(TasksActivity.this, HomeActivity.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(TasksActivity.this, DecisionActivity.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuLogout:
                SharedPreferencesManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        Toast.makeText( getApplicationContext(),"Hello there "+v.getId(), Toast.LENGTH_SHORT).show();
    }
}
