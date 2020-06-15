package com.document.appfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.document.appfiles.R;

public class ConsultasActivity extends AppCompatActivity {


    EditText etdni;
    Button btnbuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        etdni=(EditText)findViewById(R.id.id_etdnibuscar);
        btnbuscar=(Button)findViewById(R.id.btnbuscardni);

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni=etdni.getText().toString();
                buscardni(dni);
            }
        });

    }

    private void buscardni(String dni) {
        Toast.makeText(this, "hola we", Toast.LENGTH_SHORT).show();
    }
}
