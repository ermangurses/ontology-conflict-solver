package edu.arizona.biosemantics.conflictsolver;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by egurses on 3/13/18.
 */

public class DashboardActivity extends AppCompatActivity {

    private TextView mTextMessage;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if(!SharedPreferencesManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        ////////////////////////////////////////////////////////////////////
        ////Set active the selected navigation icon in the new activity/////
        ////////////////////////////////////////////////////////////////////
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        RelativeLayout relativeLayoutXML =(RelativeLayout)findViewById(R.id.relativeLayoutXML);

        //LinearLayout linearVertical1 =(LinearLayout)findViewById(R.id.linearVertical1);
        //LinearLayout linearHorizontal1 =(LinearLayout)findViewById(R.id.linearHorizontal1);
        //LinearLayout linearHorizontal2 =(LinearLayout)findViewById(R.id.linearHorizontal2);


        TextView textView1 = (TextView)findViewById(R.id.textView1);
        Button button1 =(Button)findViewById(R.id.button1);


        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        ////////////////////////////////////////////////////////////////////
        ////////Adding linearLayoutProgVertical layout to scrollView////////
        ////////////////////////////////////////////////////////////////////
        LinearLayout linearLayoutProgVertical = new LinearLayout(this);
        linearLayoutProgVertical.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        linearLayoutProgVertical.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayoutProgVertical);


        // Creating linearLayoutPHorizontal layout
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(10, 10, 10, 10);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(layoutParams);

        for(int i = 0; i < 6; i++) {

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Option "+i );
            radioButton.setId(i);


            TextView textview = new TextView(this);
            textview.setText(R.string.Example_Sentence);
            textview.setId(i);



            radioButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.rsz_1rsz_1rsz_branch,0);
            //radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            radioButton.setCompoundDrawablePadding(320);
            radioGroup.addView(radioButton);
            radioGroup.addView(textview);

        }
        linearLayoutProgVertical.addView(radioGroup);
        relativeLayoutXML.addView(scrollView);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(DashboardActivity.this, HomeActivity.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(DashboardActivity.this, NotificationsActivity.class));
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


