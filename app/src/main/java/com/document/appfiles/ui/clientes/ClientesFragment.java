package com.document.appfiles.ui.clientes;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.document.appfiles.R;

public class ClientesFragment extends Fragment {

    private ClientesViewModel mViewModel;

    public static ClientesFragment newInstance() {
        return new ClientesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.clientes_fragment, container, false);
        return vista;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ClientesViewModel.class);
        // TODO: Use the ViewModel
    }

}
