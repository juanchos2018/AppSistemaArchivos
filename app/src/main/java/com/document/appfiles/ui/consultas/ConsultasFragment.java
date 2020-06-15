package com.document.appfiles.ui.consultas;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.document.appfiles.R;
import com.document.appfiles.activitys.ConsultasActivity;

public class ConsultasFragment extends Fragment {

    private ConsultasViewModel mViewModel;

    public static ConsultasFragment newInstance() {
        return new ConsultasFragment();
    }

    Button btn1,btn2,btn3;
CardView card1,card2,card3;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.consultas_fragment, container, false);

        card1=(CardView)vista.findViewById(R.id.idcar1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta1();
            }
        });


        return vista;
    }

    private void consulta1() {

        startActivity(new Intent(getContext(), ConsultasActivity.class));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ConsultasViewModel.class);
        // TODO: Use the ViewModel
    }

}
