package jdl.android.test;

import java.io.InputStream;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.os.Build;


public class MainActivity extends Activity {
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final int MESSAGE_STATE_CHANGE2 = 21;
    public static final int MESSAGE_READ2 = 22;
    public static final int MESSAGE_WRITE2 = 23;
    public static final int MESSAGE_DEVICE_NAME2 = 24;
    public static final int MESSAGE_TOAST2 = 25;
    //handler传过来判断

    public static final int ONE = 0xa21;
    public static final int TWO = 0xa22;
    public static final int THREE = 0xa23;
    public static final int FOUR = 0xa24;
    public static final int FIVE = 0xa25;
    
    public static final int MESSAGE_STATE_CHANGE3 = 31;
    public static final int MESSAGE_READ3 = 32;
    public static final int MESSAGE_WRITE3 = 33;
    public static final int MESSAGE_DEVICE_NAME3 = 34;
    public static final int MESSAGE_TOAST3 = 35;
     
    public static final int MESSAGE_STATE_CHANGE4 = 41;
    public static final int MESSAGE_READ4 = 42;
    public static final int MESSAGE_WRITE4 = 43;
    public static final int MESSAGE_DEVICE_NAME4 = 44;
    public static final int MESSAGE_TOAST4 = 45;
    
    public static final int MESSAGE_STATE_CHANGE5 = 51;
    public static final int MESSAGE_READ5 = 52;
    public static final int MESSAGE_WRITE5 = 53;
    public static final int MESSAGE_DEVICE_NAME5 = 54;
    public static final int MESSAGE_TOAST5 = 55;
    
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private Button lianjie1,lianjie2,lianjie3,lianjie4,lianjie5;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_NEW_CARD = 3;
    private TextView suotext;
    String data = "0000";
    String s1="a",s2="b",s3="c",s4="d",s5="e",s6="f",s7="g",s8="h",s9="i";
    String slianjie1="off",slianjie2="off",slianjie3="off",slianjie4="off",slianjie5="off",slianjie6="off",slianjie7="off",slianjie8="off",slianjie9="off";
    String S1="A",S2="B",S3="C",S4="D",S5="E",S6="F",S7="G",S8="H",S9="I";
    
    String Button_Buffer="00000000";
    String seekBar_BufferString="82";
    String messege_String=""; 
    // Layout Views
    private TextView mTitle,mb1,mb2,mb3,mb4,mb5,lj1,lj2,lj3,lj4,lj5;                //标题
    private char read_do;
    private char next_do='i';
    // Name of the connected device
    private LinearLayout suo;
    private String mConnectedDeviceName = null;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothService mCardService1 = null;
    private BluetoothService2 mCardService2 = null;
    private BluetoothService3 mCardService3 = null;
   
    
    Context mContext = null;
    private int temp = 0;
    private String dataread = "";//存储发过来的三个字节数据
    private Handler returnMessage=new Handler(){

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            
            int device = msg.what;
            String address =String.valueOf((String) msg.obj);
            super.handleMessage(msg);
            switch(device){
                case ONE:
                    Log.i("devicelist","收到设备1返回值");
                    Log.i("地址",address);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device1 = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mCardService1.connect(device1);
                    break;
                case TWO:
                    // Get the BLuetoothDevice object
                    BluetoothDevice device2 = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mCardService2.connect(device2);
                    break;
                case THREE:
                    // Get the BLuetoothDevice object
                    BluetoothDevice device3 = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mCardService3.connect(device3);
                    break;
               
                default:

                    break;
            }
        }
        
    };
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        // Set up the window layout
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //setContentView(R.layout.main);
        //mAnimView = new AnimView(this);
        //setContentView(mAnimView);
        setContentView(R.layout.main);
        DeviceListActivity.DoMsg(returnMessage);
        DeviceListActivity2.DoMsg(returnMessage);
        DeviceListActivity3.DoMsg(returnMessage);
    
       
        
       lianjie1 =(Button)findViewById(R.id.mainButton1);
       lianjie1.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
        });
        lianjie2=(Button)findViewById(R.id.mainButton2);
        lianjie2.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity2.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
        });
        lianjie3=(Button)findViewById(R.id.mainButton3);
        lianjie3.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity3.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }

            
        });
       
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        // Set up the custom title
        Log.e("MainActivity", "5");

        Log.e("MainActivity", "7");
        mTitle = (TextView) findViewById(R.id.title_left_text);     //左边应用标题
        Log.e("MainActivity", "622");
        mTitle.setText(R.string.app_name);
        Log.e("MainActivity", "6");
        mTitle = (TextView) findViewById(R.id.title_right_text);    //右边连接状态
        // Get local Bluetooth adapter
        Log.e("MainActivity", "8");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        Log.e("MainActivity", "2");
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不可用！", Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "3");
            finish();
            return;
        }
        
  mb1=(TextView)findViewById(R.id.maibo1);
  mb2=(TextView)findViewById(R.id.maibo2);
  mb3=(TextView)findViewById(R.id.maibo3);
 
  lj1=(TextView)findViewById(R.id.mainTextView1);
  lj2=(TextView)findViewById(R.id.mainTextView2);
  lj3=(TextView)findViewById(R.id.mainTextView3);
  
    }

    @Override
    public void onStart() 
    {
        super.onStart();
        //确保蓝牙打开，然后读取名片信息显示在节目上（未打开则在返回信息处理中setupCards()）
        if (!mBluetoothAdapter.isEnabled()) 
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } 
        else 
        {
            if (mCardService1 == null) setupCards1(); //初始化ListView对象
            if (mCardService2 == null) setupCards2(); //初始化ListView对象
            if (mCardService3 == null) setupCards3(); //初始化ListView对象
//            if (mCardService4 == null) setupCards4(); //初始化ListView对象
//            if (mCardService5 == null) setupCards5(); //初始化ListView对象
        }
    }

    @Override
    public synchronized void onResume() 
    {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mCardService1 != null) 
        {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mCardService1.getState() == BluetoothService.STATE_NONE) 
            {
                // Start the Bluetooth chat services
                mCardService1.start();
            }
        }
//        if (mCardService2 != null) 
//        {
//            // Only if the state is STATE_NONE, do we know that we haven't started already
//            if (mCardService2.getState() == BluetoothService2.STATE_NONE) 
//            {
//                // Start the Bluetooth chat services
//                mCardService2.start();
//            }
//        }
//        if (mCardService3 != null) 
//        {
//            // Only if the state is STATE_NONE, do we know that we haven't started already
//            if (mCardService3.getState() == BluetoothService3.STATE_NONE) 
//            {
//                // Start the Bluetooth chat services
//                mCardService3.start();
//            }
//        }
//        if (mCardService4 != null) 
//        {
//            // Only if the state is STATE_NONE, do we know that we haven't started already
//            if (mCardService4.getState() == BluetoothService4.STATE_NONE) 
//            {
//                // Start the Bluetooth chat services
//                mCardService4.start();
//            }
//        }
//        if (mCardService5 != null) 
//        {
//            // Only if the state is STATE_NONE, do we know that we haven't started already
//            if (mCardService5.getState() == BluetoothService5.STATE_NONE) 
//            {
//                // Start the Bluetooth chat services
//                mCardService5.start();
//            }
//        }
    }
    
    private void setupCards1()
    {
        //完成显示内容后，初始化蓝牙服务对象
        mCardService1 = new BluetoothService(mHandler1);
        // Initialize the buffer for outgoing messages
        setmOutStringBuffer(new StringBuffer(""));
    }

    private void setupCards2()
    {
        //完成显示内容后，初始化蓝牙服务对象
        mCardService2 = new BluetoothService2(mHandler2);
        // Initialize the buffer for outgoing messages
        setmOutStringBuffer(new StringBuffer(""));
    }

    private void setupCards3()
    {
        //完成显示内容后，初始化蓝牙服务对象
        mCardService3 = new BluetoothService3(mHandler3);
        // Initialize the buffer for outgoing messages
        setmOutStringBuffer(new StringBuffer(""));
    }

//    private void setupCards4()
//    {
//        //完成显示内容后，初始化蓝牙服务对象
//        mCardService4 = new BluetoothService4(mHandler4);
//        // Initialize the buffer for outgoing messages
//        setmOutStringBuffer(new StringBuffer(""));
//    }
//
//    private void setupCards5()
//    {
//        //完成显示内容后，初始化蓝牙服务对象
//        mCardService5 = new BluetoothService5(mHandler5);
//        // Initialize the buffer for outgoing messages
//        setmOutStringBuffer(new StringBuffer(""));
//    }
    
    public void onDestroy() 
    {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mCardService1 != null) mCardService1.stop();
        if (mCardService2 != null) mCardService2.stop();
        if (mCardService3 != null) mCardService3.stop();
     

    }

    //开启蓝牙可见
    private void ensureDiscoverable() 
    {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) 
        {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    //处理线程返回信息

    private final Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            lj1.setText(R.string.title_connected_to);
                            lj1.append(mConnectedDeviceName);
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            lj1.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            lj1.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                    //发送了信息，显示在本地屏幕上（重写，显示结果即可）
                case MESSAGE_WRITE:
                    Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_LONG);
                    break;
                    //收到了信息，显示在本地屏幕上（重写，加入到通信录中）
                case MESSAGE_READ:
                    // construct a string from the valid bytes in the buffer
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                    byte[] readBuf = (byte[]) msg.obj;
                    temp++;
                    onReceiveMess1(readBuf,msg.arg1);   

                    //if(temp==4)
                    //{
                    //  str = "";//重新接受下一次的温度值
                    //  temp = 0;//表示又一轮的温度值传送。开始准备接收温度值的十位
                    //}
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name

                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "已连接到蓝牙设备1 "
                                   + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                                   Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //处理线程返回信息
    private final Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE2:
                    switch (msg.arg1) {
                        case BluetoothService2.STATE_CONNECTED:
                            lj2.setText(R.string.title_connected_to);
                            lj2.append(mConnectedDeviceName);
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService2.STATE_CONNECTING:
                            lj2.setText(R.string.title_connecting);
                            break;
                        case BluetoothService2.STATE_LISTEN:
                        case BluetoothService2.STATE_NONE:
                            lj2.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                    //发送了信息，显示在本地屏幕上（重写，显示结果即可）
                case MESSAGE_WRITE2:
                    Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_LONG);
                    break;
                    //收到了信息，显示在本地屏幕上（重写，加入到通信录中）
                case MESSAGE_READ2:
                    // construct a string from the valid bytes in the buffer
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                    byte[] readBuf = (byte[]) msg.obj;
                    temp++;
                    onReceiveMess2(readBuf,msg.arg1);   

                    //if(temp==4)
                    //{
                    //  str = "";//重新接受下一次的温度值
                    //  temp = 0;//表示又一轮的温度值传送。开始准备接收温度值的十位
                    //}
                    break;
                case MESSAGE_DEVICE_NAME2:
                    // save the connected device's name

                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "已连接到蓝牙设备2 "
                                   + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST2:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                                   Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    //处理线程返回信息
    private final Handler mHandler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE3:
                    switch (msg.arg1) {
                        case BluetoothService3.STATE_CONNECTED:
                            lj3.setText(R.string.title_connected_to);
                            lj3.append(mConnectedDeviceName);
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService3.STATE_CONNECTING:
                            lj3.setText(R.string.title_connecting);
                            break;
                        case BluetoothService3.STATE_LISTEN:
                        case BluetoothService3.STATE_NONE:
                            lj3.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                    //发送了信息，显示在本地屏幕上（重写，显示结果即可）
                case MESSAGE_WRITE3:
                    Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_LONG);
                    break;
                    //收到了信息，显示在本地屏幕上（重写，加入到通信录中）
                case MESSAGE_READ3:
                    // construct a string from the valid bytes in the buffer
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                    byte[] readBuf = (byte[]) msg.obj;
                    temp++;
                    onReceiveMess3(readBuf,msg.arg1);   

                    //if(temp==4)
                    //{
                    //  str = "";//重新接受下一次的温度值
                    //  temp = 0;//表示又一轮的温度值传送。开始准备接收温度值的十位
                    //}
                    break;
                case MESSAGE_DEVICE_NAME3:
                    // save the connected device's name

                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "已连接到蓝牙设备3 "
                                   + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST3:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                                   Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


   

    
    //通过蓝牙发送字节数据 （发送六个字节为一组）  发送时间给单片机显示   预设输入格式为：
    //    如果在文本框中输入211030表示时间为21：10：30

    private void sendByte(byte[] data)
    {
        //检查是否已经连接
        if (mCardService1.getState() != BluetoothService.STATE_CONNECTED) 
        {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return; 
        }
        //这里还可以加入对数据的处理（加入信息类别等）
        byte[] send = data;
        mCardService1.write(send);
        // Reset out string buffer to zero and clear the edit text field
        mOutStringBuffer.setLength(0);
    }

    //处理收到的信息
    //温度数据预设为只是两位数以内，一位小数位。一次温度值分三次发送
    //分别发送十位、个位、小数位
    private void onReceiveMess1(byte[] data,int len)
    {
//        TextView tv = (TextView)findViewById(R.id.wd_data);
        String data_str = new String(data,0,len);
        char[] strChar = data_str.toCharArray();
        int q=strChar[0];
        mb1.setText(q+"");  
       
    }
    private void onReceiveMess2(byte[] data,int len)
    {
        
        String data_str = new String(data,0,len);
        char[] strChar = data_str.toCharArray();
        int q=strChar[0];
        mb2.setText(q+"");
       
    }
    private void onReceiveMess3(byte[] data,int len)
    {
//        TextView tv = (TextView)findViewById(R.id.wd_data);
        String data_str = new String(data,0,len);
        char[] strChar = data_str.toCharArray();
        int q=strChar[0];
        mb3.setText(q+"");
    }
    private void onReceiveMess4(byte[] data,int len)
    {

        String data_str = new String(data,0,len);
        char[] strChar = data_str.toCharArray();
        int q=strChar[0];
        mb2.setText(q+"");

    }
    private void onReceiveMess5(byte[] data,int len)
    {
//        TextView tv = (TextView)findViewById(R.id.wd_data);
        String data_str = new String(data,0,len);
        char[] strChar = data_str.toCharArray();
        int q=strChar[0];
        mb3.setText(q+"");
    }

    public void setmOutStringBuffer(StringBuffer mOutStringBuffer) {
        this.mOutStringBuffer = mOutStringBuffer;
    }


    public StringBuffer getmOutStringBuffer() {
        return mOutStringBuffer;
    }
    
    private void sleep(int x){
        try
        {
            Thread.sleep(x);
        }
        catch (InterruptedException e)
        {}
    }

    @Override
    protected void onRestart()
    {
        // TODO: Implement this method
        super.onRestart();
//        getDeviceReturn();
        System.out.println("获取列表返回");
    }
    
}
