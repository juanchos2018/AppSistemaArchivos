package com.document.appfiles.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.document.appfiles.Clases.ClsColegas;
import com.document.appfiles.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Enviar_archivosActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    String id_usuario,nombre_usuario,foto_usuario;
    private DatabaseReference reference,reference2;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_archivos);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        id_usuario= mAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("MisColegas").child(id_usuario);

        reference2= FirebaseDatabase.getInstance().getReference("Usuarios").child(id_usuario);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    nombre_usuario=dataSnapshot.child("nombre_usuario").getValue().toString();
                    foto_usuario=dataSnapshot.child("image_usuario").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView=(RecyclerView)findViewById(R.id.recuclercolegasenviar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ClsColegas> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsColegas>()
                .setQuery(reference, ClsColegas.class).build();
        FirebaseRecyclerAdapter<ClsColegas,Items> adapter =new FirebaseRecyclerAdapter<ClsColegas, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsColegas claseesprofe) {
                final String key = getRef(i).getKey();
                reference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String correo=dataSnapshot.child("correo_usuario").getValue().toString();
                            final String nombre=dataSnapshot.child("nombre_usuario").getValue().toString();
                            final String image_usuario=dataSnapshot.child("image_usuario").getValue().toString();
                            items.tvcorreo.setText(correo);
                            items.tvtnombre.setText(nombre);

                            Glide.with(getApplicationContext())
                                    .load(image_usuario)
                                    .placeholder(R.drawable.default_profile_image)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(items.imgcam);
                            items.btnenviar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    items.btnenviar.setText("enviado");
                                    Toast.makeText(Enviar_archivosActivity.this, "listo para enviar we", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colegas_compartir, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void  enviar(String id_colega,String receptor,String ruta_archivo){

    }

    public   class Items extends RecyclerView.ViewHolder{
        TextView tvtnombre,tvcorreo,tvcargo;
        ImageView imgcam;
        String id_usario,nombreclase;
        Button  btnenviar;

        public Items(final View itemView) {
            super(itemView);
            tvcorreo=(TextView)itemView.findViewById(R.id.id_tvcorreo2);
            tvtnombre=(TextView)itemView.findViewById(R.id.id_nombre2);
            imgcam=(ImageView)itemView.findViewById(R.id.id_imgperfil2);
            btnenviar=(Button)itemView.findViewById(R.id.id_btnenviar);


        }
    }
}
