package com.document.appfiles.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.document.appfiles.Clases.ClsArchivos;
import com.document.appfiles.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListaArchivos extends AppCompatActivity {


    FirebaseStorage storage;
    private DatabaseReference referencearchivos;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ImageButton imgbutton;

    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;
    String user_id,keycarpeta;
    Uri uri;
    Uri pdfurl;
    String tipoarchivo,tipodocumento,correousuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_archivos);


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        correousuario=mAuth.getCurrentUser().getEmail();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        keycarpeta=getIntent().getStringExtra("key");
        referencearchivos= FirebaseDatabase.getInstance().getReference("Archivos2").child(keycarpeta);
        recyclerView=findViewById(R.id.recylcercarchivos);
        imgbutton=(ImageButton)findViewById(R.id.id_imgbutton);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirgaleria();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void abrirgaleria() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==86 && resultCode==RESULT_OK && data!=null){
            String nombrearchivo;
            uri=data.getData(); //return the uir of selected file

            String mimeType = getContentResolver().getType(uri);
            Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            nombrearchivo=returnCursor.getString(nameIndex);
            String tipo= getExtension(nombrearchivo);

            switch (tipo){
                case "mp4":
                    Toast.makeText(this, "Tipo de archivo no admitido", Toast.LENGTH_SHORT).show();
                    uri=null;

                    break;
                case "rar":
                    Toast.makeText(this, "Tipo de archivo no admitido", Toast.LENGTH_SHORT).show();
                    uri=null;

                    break;
                case "mp3":
                    Toast.makeText(this, "Tipo de archivo no admitido", Toast.LENGTH_SHORT).show();
                    uri=null;

                    break;
                case "doc":
                    tipoarchivo="file";
                    tipodocumento="doc";
                    break;
                case "docx":
                    tipoarchivo="file";
                    tipodocumento="doc";
                    break;
                case "pdf":
                    tipoarchivo="file";
                    tipodocumento="pdf";
                    break;
                case "ppt":
                    tipoarchivo="file";
                    tipodocumento="ppt";
                    break;
                case "pptx":
                    tipoarchivo="file";
                    tipodocumento="ppt";
                    break;
                case "img":
                    tipoarchivo="img";
                    tipodocumento="img";
                    break;
                case "jpg":
                    tipoarchivo="img";
                    tipodocumento="img";
                    break;
                case "jpeg":
                    tipoarchivo="img";
                    tipodocumento="img";
                    break;
                case "png":
                    tipoarchivo="img";
                    tipodocumento="img";
                    break;
                default:
                    tipodocumento="desconocido";
                        tipoarchivo="desconociddo";
                        break;


            }

            previus(tipodocumento,uri,nombrearchivo,tipoarchivo);
           // if (tipoarchivo.equals("img")){
           //    // guardarimagen(tipodocumento);
           // }
           // else if (tipoarchivo.equals("file")){
           //    // guardararchivo(tipodocumento,uri,nombrearchivo);
           // }


        }
        else{
            Toast.makeText(this, "No seleciono un archivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void previus(final String tipodocumento, final Uri uri, final String nombrearchivo, final String tipoarchivo){
        builder1 = new AlertDialog.Builder(this);
        Button btcerrrar,btnsaves;
        final ImageView imgprevia;

        builder1.setTitle("Archivo");
        final TextView etnombre;
        View v = LayoutInflater.from(this).inflate(R.layout.dialogo_archivo, null);

        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.id_btncancel);
        btnsaves=(Button)v.findViewById(R.id.id_btnsave) ;
        imgprevia=(ImageView)v.findViewById(R.id.id_imgprevia);
        etnombre=(TextView)v.findViewById(R.id.id_tv_nombrearchivo1);
        switch (tipodocumento){
            case "doc":
                imgprevia.setImageResource(R.drawable.logow1);
                break;
            case "docx":
                imgprevia.setImageResource(R.drawable.logow1);
                break;
            case "ppt":
                imgprevia.setImageResource(R.drawable.logoppt);
                break;
            case "pptx":
                imgprevia.setImageResource(R.drawable.logoppt);
                break;
            case "pdf":
                imgprevia.setImageResource(R.drawable.logopdf);
                break;
            case "img":
                imgprevia.setImageURI(uri);
                break;

        }
        etnombre.setText(nombrearchivo);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });
        btnsaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipoarchivo.equals("file")){
                    guardararchivo(tipodocumento,uri,nombrearchivo);
                }
                else if (tipoarchivo.equals("img")){
                    imgprevia.setDrawingCacheEnabled(true);
                    imgprevia.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imgprevia.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] path = baos.toByteArray();
                    guardarimagen(tipodocumento,nombrearchivo,path);
                }
                else {
                    Toast.makeText(ListaArchivos.this, "Nohay  nigun archivo we", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert  = builder1.create();
       // alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }

    private void guardararchivo(final String tipodocumento, Uri uri, final String nombrearchivo) {
        if (uri==null){

            Toast.makeText(this, "no hay archivo we", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nombrearchivo)){
            Toast.makeText(this, "falta nombre archivowe", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tipodocumento)){
            Toast.makeText(this, "no hay archivo we", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Subiendo..");
            progressDialog.setProgress(0);
            progressDialog.show();
            progressDialog.setCancelable(false);

            final StorageReference mountainsRef=mStorageRef.child(correousuario).child(nombrearchivo);
            final UploadTask uploadTask = mountainsRef.putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainsRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                Date date = new Date();
                                String fecha = dateFormat.format(date);
                                Uri dowloand=task.getResult();
                                //    progressDialog.dismiss();
                                String key  = referencearchivos.push().getKey();
                                ClsArchivos obj= new ClsArchivos(key,nombrearchivo,tipodocumento,"12",fecha,dowloand.toString());
                                referencearchivos.child(key).setValue(obj);



                            } else {
                                  Toast.makeText(ListaArchivos.this, "Error al subir", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ListaArchivos.this, "Error " +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    //track the progress of = our upload
                    int currentprogress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentprogress);
                    if (currentprogress==100){
                        progressDialog.dismiss();
                        alert.dismiss();
                        Toast.makeText(ListaArchivos.this, "Agregado We", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
//  guardarimagen(tipodocumento,nombrearchivo,path);
    private void guardarimagen(final String tipodocumento, final String nombrearhivo, byte[] path) {

        if (TextUtils.isEmpty(tipodocumento)){
            Toast.makeText(this, "falta docuemnto we", Toast.LENGTH_SHORT).show();
        }
        else  if (TextUtils.isEmpty(nombrearhivo)){
            Toast.makeText(this, "falta docuemnto ", Toast.LENGTH_SHORT).show();
        }
        else  if (path==null){
            Toast.makeText(this, "falta algo we", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Subiendo..");
            progressDialog.setProgress(0);
            progressDialog.show();
            progressDialog.setCancelable(false);
            final StorageReference mountainsRef=mStorageRef.child(correousuario).child(nombrearhivo);
            final UploadTask uploadTask = mountainsRef.putBytes(path);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainsRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                Date date = new Date();
                                String fecha = dateFormat.format(date);
                                Uri dowloand=task.getResult();

                                String key  = referencearchivos.push().getKey();

                                ClsArchivos obj= new ClsArchivos(key,nombrearhivo,tipodocumento,"12",fecha,dowloand.toString());
                                referencearchivos.child(key).setValue(obj);
                            } else {
                                Toast.makeText(ListaArchivos.this, "Error ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int currentprogress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentprogress);
                    if (currentprogress==100){
                        progressDialog.dismiss();
                        Toast.makeText(ListaArchivos.this, "Subido we", Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                    }

                }
            });

        }

    }

    public static String getExtension(String filename) {
        //TODO : ESTO ES PARA SACAR LA EXTENSION DEL TIPO DE ARCHIVO
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ClsArchivos> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsArchivos>()
                .setQuery(referencearchivos, ClsArchivos.class).build();
        FirebaseRecyclerAdapter<ClsArchivos,Items> adapter =new FirebaseRecyclerAdapter<ClsArchivos, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsArchivos tutores) {
                final String key = getRef(i).getKey();
                referencearchivos.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            final String nombre_carpeta=dataSnapshot.child("nombre_archivo").getValue().toString();
                            final String tipo=dataSnapshot.child("tipo_archivo").getValue().toString();
                            final String fecha=dataSnapshot.child("fecha_archivo").getValue().toString();

                            items.txtnombrefile.setText(nombre_carpeta);
                            items.txtfecha.setText(fecha);

                            if (tipo.equals("ppt")){
                                items.imgfoto.setImageResource(R.drawable.logoppt);
                            }
                            if (tipo.equals("doc")){
                                 items.imgfoto.setImageResource(R.drawable.logow1);
                            }
                            if (tipo.equals("pdf")){
                                items.imgfoto.setImageResource(R.drawable.logopdf);
                            }
                            if (tipo.equals("img")){
                                items.imgfoto.setImageResource(R.drawable.ic_foto);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ListaArchivos.this, "Error :"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_archivos, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public  static class Items extends RecyclerView.ViewHolder{
        TextView txtnombrefile,txtfecha,txtcantidad;
        ImageView imgfoto;

        public Items(@NonNull View itemView) {
            super(itemView);
            txtnombrefile=(TextView)itemView.findViewById(R.id.id_tv_nombrearchivo);
            txtfecha=(TextView)itemView.findViewById(R.id.id_tvfecha);
            imgfoto=(ImageView)itemView.findViewById(R.id.id_imgtipofoto);


        }
    }

}
