package jdl.android.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class BluetoothService3 {
    //debug
    private final String TAG = "BluetoothService3";

    // Name for the SDP record when creating server socket
    private static final String NAME = "BluetoothTest3";

    //唯一的UUID，用于蓝牙连接
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //连接状态常量
    public static final int STATE_NONE = 8;       // 任何线程未启动
    public static final int STATE_LISTEN = 9;     // AcceptThread3启动，监听并准备连接
    public static final int STATE_CONNECTING = 10; // ConnectThread启动，主动连接
    public static final int STATE_CONNECTED = 11;  // ConnectedThread启动，已经连接完成

    //成员变量
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private int mState;
    //三个线程
    private AcceptThread3 mAcceptThread33;
    private ConnectThread mConnectThread3;
    private ConnectedThread mConnectedThread3;


    public BluetoothService3(Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
    }


    public synchronized int getState() {
        return mState;
    }


    public synchronized void setState(int state) {
        mState = state;

        //返回给Activity状态改变信息
        mHandler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE3, state, -1).sendToTarget();
    }

    //以下3个方法对应3个线程启动操作，stop停止所有线程

    /**
     * Start the chat service. Specifically start AcceptThread3 to begin a
     * session in listening (server) mode. Called by the Activity onResume() */
    public synchronized void start() {
        // Cancel any thread attempting to make a connection
        if (mConnectThread3 != null) {mConnectThread3.cancel(); mConnectThread3 = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread3 != null) {mConnectedThread3.cancel(); mConnectedThread3 = null;}

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread33 == null) {
            mAcceptThread33 = new AcceptThread3();
            mAcceptThread33.start();
        }
        setState(STATE_LISTEN);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread3 != null) {mConnectThread3.cancel(); mConnectThread3 = null;}
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread3 != null) {mConnectedThread3.cancel(); mConnectedThread3 = null;}

        // Start the thread to connect with the given device
        mConnectThread3 = new ConnectThread(device);
        mConnectThread3.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Cancel the thread that completed the connection
        if (mConnectThread3 != null) {mConnectThread3.cancel(); mConnectThread3 = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread3 != null) {mConnectedThread3.cancel(); mConnectedThread3 = null;}

        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread33 != null) {mAcceptThread33.cancel(); mAcceptThread33 = null;}

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread3 = new ConnectedThread(socket);
        mConnectedThread3.start();
        Log.i("thethief","ConnectdeThread is started");
        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_DEVICE_NAME3);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        Log.i("thethief","DEVICE_NAME msg is send");

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (mConnectThread3 != null) {mConnectThread3.cancel(); mConnectThread3 = null;}
        if (mConnectedThread3 != null) {mConnectedThread3.cancel(); mConnectedThread3 = null;}
        if (mAcceptThread33 != null) {mAcceptThread33.cancel(); mAcceptThread33 = null;}
        setState(STATE_NONE);
    }

    //写入信息到ConnectedThread，准备输出
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread3;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    //连接失败
    private void connectionFailed() {
        setState(STATE_LISTEN);

        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST3);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "设备3连接失败");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    //连接丢失
    private void connectionLost() {
        setState(STATE_LISTEN);

        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST3);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }


    //三个线程类

    /**
     * 监听线程
     * 服务启动即开启该线程，监听其他设备发出的连接请求
     * 直到连接成功或被取消时停止
     */
    private class AcceptThread3 extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread3() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            setName("AcceptThread3");
            BluetoothSocket socket = null;

            //如果不是STATE_CONNECTED就一直监听
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "accept() failed", e);
                    break;
                }

                //当接受到请求时
                if (socket != null) {
                    synchronized (BluetoothService3.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of server failed", e);
            }
        }
    }


    /**
     * 主动连接线程
     * 尝试主动发出连接请求，并对成功与否做出处理（成功则启动ConnectedThread，失败则返回AcceptThread3）
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread3");
            setName("ConnectThread");

            //主动连接则取消可被发现
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                //发送异常则重新启动服务进入监听状态
                BluetoothService3.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothService3.this) {
                mConnectThread3 = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * 已连接线程
     * 该线程在与其他设备连接后一直运行，处理所有输入输出信息
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {

            Log.i(TAG, "BEGIN mConnectedThread3");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    //接收传入信息
                    bytes = mmInStream.read(buffer);

                    //发送给UI Activity（保存到文件，返回给主界面结果（请求允许））
                    mHandler.obtainMessage(MainActivity.MESSAGE_READ3, bytes, -1, buffer)
                        .sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(MainActivity.MESSAGE_WRITE3, -1, -1, buffer)
                    .sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}

