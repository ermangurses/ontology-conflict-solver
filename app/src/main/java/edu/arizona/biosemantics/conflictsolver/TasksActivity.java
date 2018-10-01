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

    private ProgressDialog mProgressDialog;

    private static Vector<Integer>  termIdArr     = new Vector<Integer>();
    private static Vector<String>   termArr       = new Vector<String>();
    private static Vector<Integer>  conflictIdArr = new Vector<Integer>();
    private static Vector<String>   usernameArr   = new Vector<String>();
    private static Vector<String>   sentenceArr   = new Vector<String>();
    private static boolean          startedFlag   = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // Make sure getTasks() is run only once
        // and setLayout is called when the user return to the TaskActivity
        if (!startedFlag) {

            getTasks();

        }else{

            setLayout();
        }

        setNavigation();
    }

    private void setNavigation(){

        // Set active the selected navigation icon in the new activity
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        // Add the listener to the navigation
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void setLayout(){

        // Take relativeLayoutXML from interface using by id
        RelativeLayout relativeLayoutXML =(RelativeLayout)findViewById(R.id.relativeLayoutXML);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        ////////////////////////////////////////////////////////////////////
        ////////Adding linearLayoutProgVertical layout to scrollView////////
        ////////////////////////////////////////////////////////////////////
        LinearLayout linearLayoutProgVertical = new LinearLayout(this);
        linearLayoutProgVertical.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        linearLayoutProgVertical.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayoutProgVertical);


        for (int i = 0; i < usernameArr.size(); i++) {

            Button button = new Button(this);
            String srt ="A conflict for " + "<em>" + ( termArr.get(i)) + "</em>" + " from " + usernameArr.get(i) +" "+ conflictIdArr.get(i)+" "+termIdArr.get(i);

            button.setText(Html.fromHtml(srt));
            button.setTextColor(0xFFFF0000);
            button.setLeft(10);

            // Set the button Id as the same conflict Id
            button.setId(conflictIdArr.get(i));

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
                Request.Method.GET,                     // Get Method
                Constants.URL_GETTASKS,                 // The URL
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {

                        String mTermId;
                        String mTerm;
                        String mConflictId;
                        String mUsername;
                        String mSentence;

                        try{
                            // Get the response from server
                            JSONObject root = new JSONObject(response);

                            // Place the response to the JSONArray
                            JSONArray task_data = root.getJSONArray("task_data");

                            for (int i = 0; i < task_data.length(); ++i) {

                                // Get single JSON object from array
                                JSONObject jsonObject = task_data.getJSONObject(i);

                                // Get the items from the object
                                mTermId     = jsonObject.getString("termId");
                                mTerm       = jsonObject.getString("term");
                                mConflictId = jsonObject.getString("conflictId");
                                mUsername   = jsonObject.getString("username");
                                mSentence   = jsonObject.getString("sentence");


                                // Put the items to the arrays
                                termIdArr.addElement(Integer.parseInt(mTermId));
                                termArr.addElement(mTerm);

                                conflictIdArr.addElement(Integer.parseInt(mConflictId));
                                usernameArr.addElement(mUsername);
                                sentenceArr.addElement(mSentence);

                            }

                            // Call setLayout() after the data in the arrays
                            setLayout();

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

                        Toast.makeText(getApplicationContext(),
                                "Oops error!",
                                Toast.LENGTH_LONG).show();
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

    // Navigate the user to the DecisionActivity
    @Override
    public void onClick(View v) {

        int index = conflictIdArr.indexOf(v.getId());
        termIdArr.get(index);
        termArr.get(index);
        usernameArr.get(index);

        //Toast.makeText( getApplicationContext(),"Conflict ID is "+ v.getId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(TasksActivity.this, DecisionActivity.class);

        // Send the data to Decision Activity
        intent.putExtra("ConflictId", ( String.valueOf( v.getId() ) ) );
        intent.putExtra("TermId", ( String.valueOf( termIdArr.get(index) ) ) );
        intent.putExtra("Term", ( String.valueOf(  termArr.get(index) ) ) );
        intent.putExtra("Username", ( String.valueOf(  usernameArr.get(index) ) ) );
        intent.putExtra("Sentence", ( String.valueOf(  sentenceArr.get(index) ) ) );

        startActivity(intent);
        startedFlag = true;
        finish();
    }
}