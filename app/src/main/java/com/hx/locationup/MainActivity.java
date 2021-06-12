package com.hx.locationup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, LocationService.class));

        //00000000000000000000000000000000000000000000000000000000000000000000000
        initLayout();

        LocationReceiver locationReceiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("location.reportsucc");
        registerReceiver(locationReceiver, filter);
        //00000000000000000000000000000000000000000000000000000000000000000000000

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private TextView textView;
    private Button clearLogBtn;
    private void init() {

        clearLogBtn = (Button) findViewById(R.id.clear_log);
        //核心
        clearLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText(textView);
            }
        });
    }

    //添加日志
    public void initLayout(){
        textView = (TextView)findViewById(R.id.text_view);
    }

    private void clearText(TextView mTextView) {
        mTextView.setText("");
    }


    //000000000000000000000000000000000000000000000000000000000000000000000
    public class LocationReceiver extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            //0000
            System.out.println("OnReceiver");
            Bundle bundle = intent.getExtras();

            String a = bundle.getString("result");

            textView.setText(String.valueOf(a));
            //0000
        }
    }
    //000000000000000000000000000000000000000000000000000000000000000000000

}

