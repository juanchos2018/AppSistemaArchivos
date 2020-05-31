package com.document.appfiles.ui.clientes;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.document.appfiles.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ClientesFragment extends Fragment {

    private ClientesViewModel mViewModel;
    private final int MIS_PERMISOS = 100;
    public static final int PICK_CONTACT_REQUEST = 1 ;
    private Uri contactUri;

    TextView tvnombre;
    public static ClientesFragment newInstance() {
        return new ClientesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.clientes_fragment, container, false);

        tvnombre=(TextView)vista.findViewById(R.id.idnombre);
        FloatingActionButton fab = vista.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              mostrarcontactos();
            }
        });

        if (solicitaPermisosVersionesSuperiores()){

        }
        return vista;
    }

    private void mostrarcontactos() {

        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                contactUri = data.getData();
                renderContact(contactUri);
            }
        }

    }

    private void renderContact(Uri uri) {
        tvnombre.setText(getName(uri));

    }
    private String getName(Uri uri) {

        String name = null;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor c = contentResolver.query(
                uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        if(c.moveToFirst()){
            name = c.getString(0);
        }
        c.close();
        return name;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ClientesViewModel.class);
        // TODO: Use the ViewModel
    }

    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((getActivity().checkSelfPermission(READ_CONTACTS)== PackageManager.PERMISSION_GRANTED)&&getActivity().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(READ_CONTACTS)||(shouldShowRequestPermissionRationale(READ_CONTACTS)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{READ_CONTACTS, READ_CONTACTS}, MIS_PERMISOS);
        }

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar SMS.");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para enviar SMS!");
        }
        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui AAsdsd
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe conceder los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{READ_CONTACTS,READ_CONTACTS},100);
            }
        });
        dialogo.show();
    }
}
