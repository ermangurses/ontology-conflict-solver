package edu.arizona.biosemantics.conflictsolver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by egurses on 3/13/18.
 */

public class DecisionActivity extends AppCompatActivity {

    private  Vector<String> optionArr     = new Vector<String>();
    private  Vector<String> definitionArr = new Vector<String>();

    private String mTerm;
    private String mSentence;
    private String mOption;
    private String mDefinition;
    private String mChoice;
    private String mTermId;
    private String mConflictId;
    private String mExpertId;
    private boolean mIsChecked;

    private EditText editTextWrittenComment;
    private ProgressDialog progressDialog;

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

        mIsChecked   = false;
        mTermId      = getIntent().getStringExtra("TermId");
        mConflictId  = getIntent().getStringExtra("ConflictId");
        mExpertId    = String.valueOf(SharedPreferencesManager.getInstance(this).getExpertId());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submiting the answer...");

        // Call the getOptions method
        getOptions();

        // Call the setNavigation method
        setNavigation();
    }

    private void setLayout(){

        // Set the header for the confusing term
        final TextView textviewTerm = (TextView) findViewById(R.id.term);
        textviewTerm.setText(mTerm);

        // Set the header for the confusing term
        final TextView textviewSentence = (TextView) findViewById(R.id.sentence);
        textviewSentence.setText(mSentence);

        editTextWrittenComment = (EditText) findViewById(R.id.editText);

        // Set listener for SUBMIT button
        Button button = findViewById(R.id.submit);
        setButtonListener (button);

        RadioGroup radioGroup = new RadioGroup(getApplicationContext());
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
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
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
        for(i = 0; i < optionArr.size(); ++i) {

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(optionArr.elementAt(i) );
            radioButton.setId(i);

            TextView textview = new TextView(this);
            //textview.setText(R.string.Example_Sentence1);
            textview.setText(definitionArr.elementAt(i));

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

        setRadioGroupListener(radioGroup);

    }
    private void setButtonListener (Button button) {

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (mIsChecked) {
                    submitDecision();
                    startActivity(new Intent(DecisionActivity.this, TasksActivity.class));

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please select one of the options",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setRadioGroupListener (RadioGroup radioGroup){

        // Set listener for radio group
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                mIsChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (mIsChecked) {
                    mChoice = (String) checkedRadioButton.getText();
                    // Changes the textview's text to "Checked: example radiobutton text"
                }
            }
        });

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
        System.out.print(uri);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject root = new JSONObject(response);
                            JSONArray options_data = root.getJSONArray("options_data");

                            for (int i = 0; i < options_data.length(); i++) {

                                JSONObject jsonObject = options_data.getJSONObject(i);

                                mOption = jsonObject.getString("option_");
                                optionArr.addElement(mOption);

                                mDefinition = jsonObject.getString("definition");
                                definitionArr.addElement(mDefinition);
                                //Toast.makeText( getApplicationContext(),mOption, Toast.LENGTH_SHORT).show();
                            }

                            mTerm = getIntent().getStringExtra("Term");
                            mSentence = getIntent().getStringExtra("Sentence");

                            // Call the layout method right after the data is fetched
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

    private void submitDecision() {


        final String writtenComment = editTextWrittenComment.getText().toString().trim();
        //final String voiceComment   = editTextVoiceComment.getText().toString().trim();


        progressDialog.show();

        // Inner Class for string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PROCESSDECISION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),
                                    jsonObject.getString("message"),
                                    Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("expertId", mExpertId);
                params.put("conflictId", mConflictId);
                params.put("choice", mChoice);
                params.put("writtenComment",writtenComment);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    /*private void getOptionImages(){

        Integer id = Integer.valueOf(getIntent().getStringExtra("TermId"));
        String uri = String.format(Constants.URL_GETOPTIONS+"?ID=%1$s",id);
        System.out.print(uri);

        ImageRequest imageRequest = new ImageRequest(
                uri,
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap response) {


                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(imageRequest);
    }*/

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