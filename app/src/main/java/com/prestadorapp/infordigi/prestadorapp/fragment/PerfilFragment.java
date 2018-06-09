package com.prestadorapp.infordigi.prestadorapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prestadorapp.infordigi.prestadorapp.R;
import com.prestadorapp.infordigi.prestadorapp.activity.EditarPerfilActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private ProgressBar progressBarPerfil;
    private CircleImageView imagePerfil;
    private GridView gridViewPerfil;
    private TextView textAvaliacao, textCotratacao, textSeguindo;
    private Button buttonEditarPerfil;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //configurações dos componentes
        progressBarPerfil = view.findViewById(R.id.progressBarPerfil);
        imagePerfil = view.findViewById(R.id.imagePerfil);
        gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        textAvaliacao = view.findViewById(R.id.textAvaliacao);
        textCotratacao = view.findViewById(R.id.textContracacao);
        textSeguindo = view.findViewById(R.id.textSeguindo);
        buttonEditarPerfil = view.findViewById(R.id.buttonEditarPerfil);

        //abrir edição do perfil
        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
