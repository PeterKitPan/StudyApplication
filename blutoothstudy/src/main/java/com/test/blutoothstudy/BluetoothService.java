package com.test.blutoothstudy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private final UUID MY_UUID = UUID
            .fromString("abcd1234-ab12-ab12-ab12-abcdef123456");//随便定义一个
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private BluetoothService.AcceptThread mAcceptThread;
    private BluetoothService.TransferThread mTransferThread;
    private BluetoothService.ConnectThread mConnectThread;
    private static Handler uiHandler;
    private boolean connecting = true;

//    private int mState;
//    public static final int STATE_TRANSFER = 3;  // 现在连接到一个远程的设备，可以进行传输

//    懒汉式
//    private static BluetoothService bluetoothService;
//
//    public static BluetoothService getInstance() {
//        if (bluetoothService == null) {
//            synchronized (BluetoothService.class) {
//                if (bluetoothService == null) {
//                    bluetoothService = new BluetoothService();
//                }
//            }
//        }
//        return bluetoothService;
//    }

    //    饿汉式
    private static BluetoothService bluetoothService = new BluetoothService();

    public static BluetoothService getInstance(Handler handler) {
        uiHandler = handler;
        return bluetoothService;
    }

    private BluetoothService() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 服务端监听客户端的线程类
     */
    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;

        public AcceptThread() {
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            BluetoothSocket socket = null;
            while (connecting) {
                try {
                    if (serverSocket == null) {
                        serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID);
                    }
                    socket = serverSocket.accept();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Log.e("MSG","明显的日志，看看socket是不是为空"+(socket == null));
                if (socket != null) {
                    mTransferThread = new BluetoothService.TransferThread(socket);
                    mTransferThread.start();
                }
            }
        }

        public void cancel() {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接线程
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private BluetoothDevice mDevice;

        public ConnectThread(BluetoothDevice device) {
            mDevice = device;
//            BluetoothSocket mSocket = null;
            try {
                //建立通道
                socket = mDevice.createRfcommSocketToServiceRecord(
                        MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
//                sendMessageToUi(MainActivity.BLUE_TOOTH_TOAST , "连接失败，请重新连接");
            }
//            socket = mSocket;
        }

        @Override
        public void run() {
            super.run();
            //建立后取消扫描
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }

            try {
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.obj = "连接失败，请检查双方蓝牙是否都打开";
                msg.what = 1;
                uiHandler.sendMessage(msg);
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //TODO 连接失败显示
//                sendMessageToUi(MainActivity.BLUE_TOOTH_TOAST , "连接失败，请重新连接");
//                BluetoothService.this.start();
            }


            // 重置
            mTransferThread = new BluetoothService.TransferThread(socket);
            mTransferThread.start();
        }

        public void cancel() {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 传输数据的线程
     */
    class TransferThread extends Thread {
        private final BluetoothSocket socket;
        private final OutputStream out;
        private final InputStream in;

        public TransferThread(BluetoothSocket mBluetoothSocket) {
            connecting = false;
//            if (mAcceptThread != null) {
//                mAcceptThread.cancel();
//                mAcceptThread = null;
//            }
            socket = mBluetoothSocket;
            OutputStream mOutputStream = null;
            InputStream mInputStream = null;
            try {
                if (socket != null) {
                    //获取连接的输入输出流
                    mOutputStream = socket.getOutputStream();
                    mInputStream = socket.getInputStream();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            out = mOutputStream;
            in = mInputStream;
//            isTransferError = false;
        }

        @Override
        public void run() {
            super.run();
            //读取数据
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = in.read(buffer);
                    //TODO 分发到主线程显示
                    String msgContent = new String(buffer, 0, bytes, "utf-8");
                    Log.e("MSG", "接收到消息是：" + msgContent);
                    Message msg = new Message();
                    msg.obj = msgContent;
                    uiHandler.sendMessage(msg);
//                    uiHandler.obtainMessage(MainActivity.BLUE_TOOTH_READ, bytes, -1, buffer)
//                            .sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    BluetoothService.this.start();
                    //TODO 连接丢失显示并重新开始连接
//                    sendMessageToUi(MainActivity.BLUE_TOOTH_TOAST , "设备连接失败/传输关闭");
//                    isTransferError = true;
                    break;
                }
            }
        }

        /**
         * 写入数据传输
         *
         * @param buffer
         */
        public void write(byte[] buffer) {
            try {
                out.write(buffer);
                //TODO 到到UI显示
//                uiHandler.obtainMessage(MainActivity.BLUE_TOOTH_WRAITE , -1, -1, buffer)
//                        .sendToTarget();
            } catch (IOException e) {
//                Log.e(TAG, "Exception during write " + e);
            }
        }

        public void cancel() {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
//                Log.e(TAG, "close() of connect socket failed" + e);
            }
        }
    }


    /**
     * 开启服务监听
     */
    public synchronized void start() {
//        if(mTransferThread != null){
//            mTransferThread.cancel();
//            mTransferThread = null;
//        }

//        setState(STATE_LISTEN);

        if (mAcceptThread == null) {
            mAcceptThread = new BluetoothService.AcceptThread();
            mAcceptThread.start();
        }
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        if (mTransferThread != null) {
            mTransferThread.cancel();
            mTransferThread = null;
        }

//        setState(STATE_NONE);
    }

    /**
     * 连接访问
     *
     * @param device
     */
    public synchronized void connectDevice(BluetoothDevice device) {
        // 如果有正在传输的则先关闭
//        if (mTransferThread != null) {mTransferThread.cancel(); mTransferThread = null;}


        //如果有正在连接的则先关闭
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

//        sendMessageToUi(MainActivity.BLUE_TOOTH_DIALOG , "正在与" + device.getName() + "连接");
        mConnectThread = new BluetoothService.ConnectThread(device);
        mConnectThread.start();
        //标志为正在连接
//        setState(STATE_CONNECTING);
    }

    /**
     * 传输数据
     *
     * @param out
     */
    public void sendData(byte[] out) {
//        BluetoothChatService.TransferThread r;
//        synchronized (this) {
//            if (mState != STATE_TRANSFER) return;
//            r = mTransferThread;
//        }
        mTransferThread.write(out);
    }
}
