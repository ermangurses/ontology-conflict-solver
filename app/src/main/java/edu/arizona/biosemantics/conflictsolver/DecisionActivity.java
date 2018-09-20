package edu.arizona.biosemantics.conflictsolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class DecisionActivity extends AppCompatActivity {


    private static Vector<Integer> mOptionArr = new Vector<Integer>();


    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if(!SharedPreferencesManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        getIntent().getStringExtra("ConflictId");

        // Call the navigation method
        setNavigation();
        getOptions();
        // Call the layout method
        setLayout();
    }

    private void setLayout(){

        RadioGroup radioGroup = new RadioGroup(getApplicationContext());
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


        // Creating linearLayoutPHorizontal layout
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.MATCH_PARENT);

        layoutParams.setMargins(10, 10, 10, 10);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(layoutParams);

        int i;
        for(i = 0; i < mOptionArr.size(); ++i) {

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Option "+i );
            radioButton.setId(i);


            TextView textview = new TextView(this);
            textview.setText(R.string.Example_Sentence1);
            textview.setId(i);



            radioButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.branch,0);
            radioButton.setCompoundDrawablePadding(320);
            radioGroup.addView(radioButton);
            radioGroup.addView(textview);

        }
        // After the loop add the Non-of-Above option
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText("Non of above");
        radioButton.setId(++i);


        EditText editText = new EditText(this);
        editText.setHint(R.string.Example_Sentence3);
        editText.setId(++i);
        radioButton.setCompoundDrawablePadding(320);
        radioGroup.addView(radioButton);
        radioGroup.addView(editText);

        linearLayoutProgVertical.addView(radioGroup);
        relativeLayoutXML.addView(scrollView);

    }

    private void setNavigation(){

        ////////////////////////////////////////////////////////////////////
        ////Set active the selected navigation icon in the new activity/////
        ////////////////////////////////////////////////////////////////////
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void getOptions(){

        Integer id = Integer.valueOf(getIntent().getStringExtra("TermId"));
        String uri = String.format(Constants.URL_GETOPTIONS+"?ID=%1$s",id);
        //System.out.print(uri);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        String mOptionId;

                        try{
                            JSONObject root = new JSONObject(response);
                            JSONArray options_data = root.getJSONArray("options_data");

                            for (int i = 0; i < options_data.length(); i++) {
                                System.out.print(i);
                                JSONObject jsonObject = options_data.getJSONObject(i);

                                mOptionId = jsonObject.getString("optionId");

                                mOptionArr.addElement(Integer.parseInt(mOptionId));

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
                case R.id.navigation_home:
                    startActivity(new Intent(DecisionActivity.this, HomeActivity.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(DecisionActivity.this, TasksActivity.class));
                    finish();
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
}


