package com.document.appfiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registro extends AppCompatActivity {

    private EditText et_dni,et_correo,et_clave,et_celular;
    private TextView  tv_nombre,tv_apellidpaterno,tv_apellidomaterno;
    private Button btnconsultar,btnregistrar;
    Spinner spinnercargo;

    ArrayList<String> listaCargos;
    ArrayAdapter<String> adapterCargos;

    private ProgressDialog progressDialog;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        cargar();
        listaCargos=new ArrayList<>();
        listaCargos.add("Abogado");
        listaCargos.add("Juez");
        listaCargos.add("Empleado");
        listaCargos.add("Secretaria");

        adapterCargos= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listaCargos);
        spinnercargo.setAdapter(adapterCargos);



        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni=et_dni.getText().toString();
                consultardatos(dni);
            }
        });
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni=et_dni.getText().toString();
                String nombres =tv_nombre.getText().toString();
                String apellidos =tv_apellidpaterno.getText().toString();
                String correo=et_correo.getText().toString();
                String clave=et_clave.getText().toString();
                String cargo=spinnercargo.getSelectedItem().toString();
                registrar(dni,nombres,apellidos,correo,clave,cargo);
            }
        });
    }

    private void registrar(String dni, String nombres, String apellidos, String correo, String clave, String cargo) {

        progressDialog =new ProgressDialog(this);

        if (TextUtils.isEmpty(nombres)){
            tv_nombre.setError("campo requerido");
        }
        else if (TextUtils.isEmpty(apellidos)){
            tv_apellidpaterno.setError("campo requerido");
        }
        else if (TextUtils.isEmpty(correo)){
            et_correo.setError("campo requerido");
        }
        else if(TextUtils.isEmpty(clave)){
            et_clave.setError("campo requerido");
        }
        else if (TextUtils.isEmpty(cargo)){
            Toast.makeText(this, "falta cargo", Toast.LENGTH_SHORT).show();
        }
        else{

            progressDialog.setTitle("Creando Cuenta");
            progressDialog.setMessage("Espera We ....");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            mAuth.createUserWithEmailAndPassword(correo,clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String current_userID =  mAuth.getCurrentUser().getUid();
                        reference = FirebaseDatabase.getInstance().getReference().child("Profesores").child(current_userID);
                        reference.child("id_usuario").setValue(current_userID);
                        reference.child("dni_usuario").setValue(current_userID);
                        reference.child("nombre_usuario").setValue(current_userID);
                        reference.child("apellido_usuario").setValue(current_userID);
                        reference.child("correo_usuario").setValue(current_userID);
                        reference.child("cargo_usuario").setValue(current_userID);
                        reference.child("image_usuario").setValue(current_userID);
                        reference.child("created_at").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    user=mAuth.getCurrentUser();
                                    if (user!=null){
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(Registro.this, "Registrado", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    mAuth.signOut();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });



                    }else {
                        String message = task.getException().getMessage();

                        Toast.makeText(Registro.this, "Error occurred : " + message, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void consultardatos(String dni) {
        if (TextUtils.isEmpty(dni)){
            et_dni.setError("campo requerido");
            return;
        }
        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Consultando");
        progressDialog.setMessage("Espera We ....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
     //   String dni=et_dni.getText().toString();
        String api_represetnatees_legales="https://quertium.com/api/v1/sunat/legals/20134052989?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MTM3Mw.x-jUgUBcJukD5qZgqvBGbQVMxJFUAIDroZEm4Y9uTyg";
        String URL="https://api.reniec.cloud/dni/"+dni;
        String  api_ruc="https://quertium.com/api/v1/sunat/ruc/20159981216?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MTM3Mw.x-jUgUBcJukD5qZgqvBGbQVMxJFUAIDroZEm4Y9uTyg";
        //String URL="https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCeSJh3cP6hyfY4km8E2HVmw&maxResults=25&key=AIzaSyBgYb4U8DbCAzTAaXEZ3sLDZ414ZITcYLg";
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                  //  JSONObject nombre=jsonObject.getJSONObject("nombres");
                    String name=jsonObject.getString("nombres");
                    String apellido_paterno=jsonObject.getString("apellido_paterno");
                    String apellido_materno=jsonObject.getString("apellido_materno");
                    tv_nombre.setText(name);
                    tv_apellidpaterno.setText(apellido_paterno +" " +apellido_materno);

                progressDialog.dismiss();
                    //https://quertium.com/api/v1/sunat/ruc/{ruc}
                    // 20134052989  ruc eps
                    // ruc telednica 20100017491

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }

    private void cargar() {
        et_dni=(EditText)findViewById(R.id.id_etdni);
        tv_nombre=(TextView)findViewById(R.id.id_tvdnombre);
        btnconsultar=(Button)findViewById(R.id.id_btnconsultar);
        btnregistrar=(Button)findViewById(R.id.id_btnregistrar);
        tv_apellidpaterno=(TextView)findViewById(R.id.id_tvapellidopaterno);
        et_clave=(EditText)findViewById(R.id.id_etclave);
        spinnercargo=(Spinner)findViewById(R.id.id_spincargo);


    }
}
