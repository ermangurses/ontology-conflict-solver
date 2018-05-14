package edu.arizona.biosemantics.conflictsolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by egurses on 3/13/18.
 */

public class NotificationsActivity extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


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
        for(i = 0; i < 6; ++i) {

            Button button = new Button(this);
            button.setText(i+ "  You have a conflict!!!");
            button.setLeft(10);
            button.setId(i);

            linearLayoutProgVertical.addView(button);

        }



        Button button = new Button(this);
        button.setText(" 10 You have a conflict!!!");
        button.setLeft(10);
        button.setId(i);

        linearLayoutProgVertical.addView(button, 0);



        Button button1 = new Button(this);
        button1.setText(" 11 You have a conflict!!!");
        button1.setLeft(10);
        button1.setId(i);

        linearLayoutProgVertical.addView(button1, 0);

        relativeLayoutXML.addView(scrollView);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case  R.id.navigation_home:
                    startActivity(new Intent(NotificationsActivity.this, HomeActivity.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(NotificationsActivity.this, DashboardActivity.class));
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
}
