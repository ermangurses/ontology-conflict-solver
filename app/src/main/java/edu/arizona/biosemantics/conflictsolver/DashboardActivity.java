package edu.arizona.biosemantics.conflictsolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by egurses on 3/13/18.
 */

public class DashboardActivity extends AppCompatActivity {

    private TextView mTextMessage;

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

        // Set active the selected navigation icon in the new activity
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        LinearLayout linearLayoutXML =(LinearLayout)findViewById(R.id.linearLayoutXML);

        LinearLayout linearVertical1 =(LinearLayout)findViewById(R.id.linearVertical1);
        LinearLayout linearHorizontal1 =(LinearLayout)findViewById(R.id.linearHorizontal1);
        LinearLayout linearHorizontal2 =(LinearLayout)findViewById(R.id.linearHorizontal2);


        TextView textView1 = (TextView)findViewById(R.id.textView1);
        Button button1 =(Button)findViewById(R.id.button1);


        for(int i = 0; i < 3; i++) {


            LinearLayout linearLayoutP1 = new LinearLayout(this);
            linearLayoutP1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            linearLayoutP1.setOrientation(LinearLayout.HORIZONTAL);

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Option "+i);
            linearLayoutP1.addView(radioButton);

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.ic_launcher_round);
            linearLayoutP1.addView(imageView);

            linearLayoutXML.addView(linearLayoutP1);

            LinearLayout linearLayoutP2 = new LinearLayout(this);
            linearLayoutP2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
            linearLayoutP2.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView = new TextView(this);
            textView.setText("This is the example sentence that is used in this application to solve conflicts");
            linearLayoutP2.addView(textView);

            linearLayoutXML.addView(linearLayoutP2);

        }
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


