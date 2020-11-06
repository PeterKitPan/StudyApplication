package com.test.blutoothstudy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 蓝牙传输信息
 */
public class MainActivity extends AppCompatActivity {

    private Button bt_connect, bt_disconnect;
    private EditText et_send;
    private Button bt_send;
    private TextView tv_remote;
    private ListView lv_bonded_devices;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> bluetoothDevices = new ArrayList<String>();

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService bluetoothService;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(MainActivity.this,String.valueOf(msg.obj),Toast.LENGTH_LONG).show();
                    break;
                default:
                    tv_remote.setText(String.valueOf(msg.obj));
//            Toast.makeText(getApplicationContext(), String.valueOf(msg.obj),
//                    Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_connect = findViewById(R.id.bt_connect);
        bt_disconnect = findViewById(R.id.bt_disconnect);
        lv_bonded_devices = findViewById(R.id.lv_bonded_devices);
        et_send = findViewById(R.id.et_send);
        bt_send = findViewById(R.id.bt_send);
        tv_remote = findViewById(R.id.tv_remote);
        bluetoothService = BluetoothService.getInstance(handler);
        bluetoothService.start();

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送信息
                String sendContent = et_send.getText().toString();
                if (TextUtils.isEmpty(sendContent)){
                    Toast.makeText(MainActivity.this,"发送信息不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    bluetoothService.sendData(sendContent.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取蓝牙适配器
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null)
                    return;
                //2.打开蓝牙
                if (!bluetoothAdapter.isEnabled())
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1);

                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();//获取已配对的设备
                for (BluetoothDevice bondedDevice:bondedDevices){
                    Log.e("MSG","Name:"+bondedDevice.getName()+"===Address:"+bondedDevice.getAddress());
                    bluetoothDevices.add("设备名:"+bondedDevice.getName()+"===设备地址:"+bondedDevice.getAddress());
                }

                arrayAdapter = new ArrayAdapter<String>(MainActivity.this.getApplicationContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1,bluetoothDevices);
                lv_bonded_devices.setAdapter(arrayAdapter);


                lv_bonded_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = bluetoothDevices.get(position).split("===")[0].substring(4);
                        String address = bluetoothDevices.get(position).split("===")[1].substring(5);
                        Log.e("MSG","选中的是Name:"+name+"===Address:"+address);
                        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                        bluetoothService.connectDevice(device);
                    }
                });
            }
        });

        bt_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter.disable();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();//获取已配对的设备
            for (BluetoothDevice bondedDevice:bondedDevices){
                Log.e("MSG","Name:"+bondedDevice.getName()+"===Address:"+bondedDevice.getAddress());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.disable();
        bluetoothService.stop();
    }
}
