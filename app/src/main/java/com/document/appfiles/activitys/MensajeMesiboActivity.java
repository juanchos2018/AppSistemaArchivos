package com.document.appfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.document.appfiles.R;
import com.mesibo.api.Mesibo.ConnectionListener;
import com.mesibo.api.m;
import  com.mesibo.api.Mesibo;

public class MensajeMesiboActivity extends AppCompatActivity implements Mesibo.MessageListener, Mesibo.ConnectionListener{


    private static final String TAG = "MensajeMesiboActivity";

    Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_mesibo);
        Mesibo api = Mesibo.getInstance();
        api.init(this);

        api.addListener(this);
        api.setAccessToken("a061ed6ed751bd907aad91196027804faae2a39c470edd135d28");
        api.start();
        bt1=(Button)findViewById(R.id.btnenviar);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user="juancho";
                String mensjae="perrro";
                sendTextMessage(user,mensjae);
            }
        });

    }

    @Override
    public boolean Mesibo_onMessage(Mesibo.MessageParams messageParams, byte[] bytes) {

        Toast.makeText(this, new String(bytes), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void Mesibo_onMessageStatus(Mesibo.MessageParams messageParams) {
        Toast.makeText(this, messageParams.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Mesibo_onActivity(Mesibo.MessageParams messageParams, int i) {

    }

    @Override
    public void Mesibo_onLocation(Mesibo.MessageParams messageParams, Mesibo.Location location) {

    }

    @Override
    public void Mesibo_onFile(Mesibo.MessageParams messageParams, Mesibo.FileInfo fileInfo) {

    }


    @Override
    public void Mesibo_onConnectionStatus(int status) {
        Log.d(TAG, "on Mesibo Connection: " + status);
    }

    void sendTextMessage(String to, String message) {
        Toast.makeText(this, "emvaido", Toast.LENGTH_SHORT).show();
        Mesibo.MessageParams p = new Mesibo.MessageParams();
        p.peer = to;
        p.flag = Mesibo.FLAG_READRECEIPT | Mesibo.FLAG_DELIVERYRECEIPT;

        Mesibo.sendMessage(p, Mesibo.random(), message);
    }
}
