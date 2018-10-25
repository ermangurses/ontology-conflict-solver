package edu.arizona.biosemantics.conflictsolver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.arizona.biosemantics.conflictsolver.ModelClass.FileSenderInfo;
import edu.arizona.biosemantics.conflictsolver.NetworkRelatedClass.NetworkCall;


/**
 * Created by egurses on 3/13/18.
 */

public class DecisionActivity extends AppCompatActivity {

    private static final String TAG = "DecisionActivity";

    private String mChoice="";
    private String mConflictId;
    private String mExpertId;
    private String file_type = "audio";
    private int    mPosition;
    private EditText mEditTextWrittenComment;
    private TermOptions mTermOptions  = new TermOptions();
    private ProgressDialog mProgressDialog;

    // Voice recorder items
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder;


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

        mConflictId  = getIntent().getStringExtra("ConflictId");
        mExpertId    = String.valueOf(SharedPreferencesManager.getInstance(this).getExpertId());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Submiting the answer...");
        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                        "AudioRecording.3gp";
        // Call the getOptions method
        getOptions();

        // Call the setNavigation method
        setNavigation();
    }

    private void setLayout(){

        // Set listener for SUBMIT button
        final Button button = findViewById(R.id.submit);
        setButtonListener (button);

        RecordView recordView = (RecordView) findViewById(R.id.record_view);
        RecordButton recordButton = (RecordButton) findViewById(R.id.record_button);
        mEditTextWrittenComment = (EditText) findViewById(R.id.editText);

        recordButton.setRecordView(recordView);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {

                    MediaRecorderReady();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                mEditTextWrittenComment.setHint("");
                mEditTextWrittenComment.setCursorVisible(false);
                button.setVisibility(View.INVISIBLE);
                //Toast toast = Toast.makeText(getApplicationContext(),"RECORD BUTTON CLICKED", Toast.LENGTH_SHORT);
                //toast.setGravity(Gravity.CENTER, 0, 0);
                //toast.show();
            }

            @Override
            public void onCancel() {

                button.setVisibility(View.VISIBLE);

                // Give delay to editTextWrittenComment for the animation
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mEditTextWrittenComment.setHint("Type or Record Comment");
                                mEditTextWrittenComment.setCursorVisible(true);

                            }
                        }, 1350);
                    }
                });
            }

            @Override
            public void onFinish(long recordTime) {

                try {

                    mediaRecorder.stop();

                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                button.setVisibility(View.VISIBLE);
                mEditTextWrittenComment.setHint("Type or Record Comment");
                mEditTextWrittenComment.setCursorVisible(true);
            }

            @Override
            public void onLessThanSecond() {

                mediaRecorder.reset();
                button.setVisibility(View.VISIBLE);
                mEditTextWrittenComment.setHint("Type or Record Comment");
                mEditTextWrittenComment.setCursorVisible(true);
            }
        });

        // Set the header for the confusing term
        final TextView textviewTerm = (TextView) findViewById(R.id.term);
        textviewTerm.setText(mTermOptions.getTerm());
        textviewTerm.setTextColor(0xFFCC0000);

        // Set the sentence for the confusing term
        final TextView textviewSentence = (TextView) findViewById(R.id.sentence);
        textviewSentence.setText(mTermOptions.getSentence());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Send data to the adapter
        TermOptionsAdapter adapter = new TermOptionsAdapter(this,
                mTermOptions.getImageLinks(),
                mTermOptions.getOptions(),
                mTermOptions.getDefinitions());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setButtonListener (Button button) {

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                mPosition = SharedPreferencesManager.getInstance(getApplicationContext()).
                        getSelectedOption();

                if (mPosition != -1) {

                    mChoice = String.valueOf(mTermOptions.getOptions().get(mPosition));
                    submitDecision();

                    // Send the audio file to the server
                    NetworkCall.fileUpload(AudioSavePathInDevice, new FileSenderInfo(file_type));

                    Intent intent = new Intent(DecisionActivity.this, TasksActivity.class);
                    intent.putExtra("solvedFlag", true );
                    startActivity(intent);

                } else {

                    Toast.makeText(getApplicationContext(),"Please select one of the options ",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getOptions(){

        Integer id = Integer.valueOf(getIntent().getStringExtra("TermId"));
        String uri = String.format(Constants.URL_GETOPTIONS+"?ID=%1$s",id);

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

                                mTermOptions.addOption(jsonObject.getString("option_"));
                                mTermOptions.addDefinition(jsonObject.getString("definition"));
                                mTermOptions.addImageLink(jsonObject.getString("image_link"));
                            }

                            mTermOptions.setTerm(getIntent().getStringExtra("Term"));
                            mTermOptions.setSentence(getIntent().getStringExtra("Sentence"));

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

        final String writtenComment = mEditTextWrittenComment.getText().toString().trim();

        mProgressDialog.show();

        // Inner Class for string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PROCESSDECISION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();

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
                        mProgressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        ){
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

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
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
                case R.id.navigation_tasks:
                    startActivity(new Intent(DecisionActivity.this, TasksActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    private void setNavigation(){

        //Set active the selected navigation icon in the new activity
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

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