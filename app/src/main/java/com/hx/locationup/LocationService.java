package com.hx.locationup;

import android.app.Service;
import android.content.Intent;
//import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class LocationService extends Service {

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    private final static String baseUrl = "https://test.jiawei.xin";
    private String result;
    private Object Callback;
    //private final static String baseUrl = "http://192.168.3.138:8080/jsb";

    public LocationService() {
    }

    //000000000000000000000000000000000000000000
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
    //000000000000000000000000000000000000000000
    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);

        initLocation();

        mLocationClient.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
        //return START_NOT_STICKY;
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 3000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    class MyLocationListener implements BDLocationListener {



        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            //testLocation(location);
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();
            Log.i("latitude:", latitude + "");
            Log.i("longitude", longitude + "");

            int errorCode = location.getLocType();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpGet httpGet = new HttpGet(baseUrl + "/Map?" +
                            "latitude=" + latitude + "&longitude=" + longitude);
                    HttpClient httpClient = new DefaultHttpClient();

                    // 发送请求


                    try {

                        HttpResponse response = httpClient.execute(httpGet);
                        String result = EntityUtils.toString(response.getEntity());

                        // 显示响应
                        // 一个私有方法，将响应结果显示出来
                        showString(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }).start();
        }
    }


    public void showString(String result) {
        //System.out.println(result);
        //PrintStream out = new PrintStream("test.log");
        //System.setOut(out);
        //System.out.println(result);

        /*

        Handler handler;
        Message message = Message.obtain(handler);
        message.obj = result;
        handler.sendMessage(message);

         */

        Intent intent = new Intent();
        intent.putExtra("result",result);
        intent.setAction("location.reportsucc");
        sendBroadcast(intent);


    }
}

