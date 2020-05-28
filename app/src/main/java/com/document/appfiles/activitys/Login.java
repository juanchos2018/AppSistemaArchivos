package com.document.appfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.document.appfiles.R;
import com.document.appfiles.Registro;

public class Login extends AppCompatActivity {


    EditText et_correo,et_clave;
    Button btningresar;
    TextView tv_registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cargarcontroles();

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo=et_correo.getText().toString();
                String clave=et_clave.getText().toString();
                ingresar(correo,clave);
            }
        });
        tv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarnuvo();
            }
        });
    }

    private void registrarnuvo() {
        startActivity(new Intent(Login.this, Registro.class));

    }

    private void ingresar(String correo, String clave) {


    }

    private void cargarcontroles() {

        et_correo=(EditText)findViewById(R.id.inputEmail);
        et_clave=(EditText)findViewById(R.id.inputPassword);
        btningresar=(Button)findViewById(R.id.loginButton);
        tv_registrar=(TextView)findViewById(R.id.registarse);

    }
}
