package com.document.appfiles.ui.bandeja;

import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.document.appfiles.Clases.ClsEnvios;
import com.document.appfiles.R;
import com.document.appfiles.fragment.DialogoFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class BandejaFragment extends Fragment {

    private BandejaViewModel mViewModel;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    String user_id;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    public static BandejaFragment newInstance() {
        return new BandejaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.bandeja_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

        reference= FirebaseDatabase.getInstance().getReference("Bandeja").child(user_id);

        recyclerView=vista.findViewById(R.id.recyclerbandeja);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return vista;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ClsEnvios> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClsEnvios>()
                .setQuery(reference, ClsEnvios.class).build();
        FirebaseRecyclerAdapter<ClsEnvios,Items> adapter =new FirebaseRecyclerAdapter<ClsEnvios, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull ClsEnvios tutores) {
                final String key = getRef(i).getKey();
                reference.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            final String nombre_archivo=dataSnapshot.child("nombre_archivo").getValue().toString();
                            final String ruta=dataSnapshot.child("ruta_archivo").getValue().toString();
                            final String fecha=dataSnapshot.child("fecha_archivo").getValue().toString();
                            final String tipo_documento=dataSnapshot.child("tipo_documento").getValue().toString();
                            final String tipo_archivo=dataSnapshot.child("tipo_archivo").getValue().toString();
                            items.txtnomnarchivo.setText(nombre_archivo);
                            items.txtfecha.setText(fecha);

                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogoFragment bottomSheetDialog = DialogoFragment.newInstance();
                                    bottomSheetDialog.nombredearchivo=nombre_archivo;
                                    bottomSheetDialog.ruta_archivo=ruta;
                                    bottomSheetDialog.tipo_documento=tipo_documento;
                                    bottomSheetDialog.tipo_archivo=tipo_archivo;
//
                                    bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bandeja, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public  static class Items extends RecyclerView.ViewHolder {
        TextView txtnomnarchivo, txtfecha, txtcantidad;
        ImageView imgfoto;

        public Items(@NonNull View itemView) {
            super(itemView);
            txtnomnarchivo = (TextView) itemView.findViewById(R.id.id_tv_nombrearchivo2);
            txtfecha = (TextView) itemView.findViewById(R.id.id_tvfecha2);



        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BandejaViewModel.class);
        // TODO: Use the ViewModel
    }

}
