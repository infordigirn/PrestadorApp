package com.prestadorapp.infordigi.prestadorapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prestadorapp.infordigi.prestadorapp.R;
import com.prestadorapp.infordigi.prestadorapp.activity.PerfilPrestadorActivity;
import com.prestadorapp.infordigi.prestadorapp.adapter.AdapterPesquisa;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;
import com.prestadorapp.infordigi.prestadorapp.helper.RecyclerItemClickListener;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroPrestador;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisaFragment extends Fragment {

    //widget
    private SearchView searchViewPesquisar;
    private RecyclerView recyclerViewPesquisar;

    private List<CadastroPrestador> listaPrestador;
    private DatabaseReference prestadorReference;
    private AdapterPesquisa adapterPesquisa;

    public PesquisaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        searchViewPesquisar = view.findViewById(R.id.searchViewPesquisa);
        recyclerViewPesquisar = view.findViewById(R.id.recyclerViewPesquisa);

        //configurações iniciais
        listaPrestador = new ArrayList<>();
        prestadorReference = ConfiguracaoFirebase.getReferenceFirebase()
                .child("prestadores");

        //configurar recyclerview
        recyclerViewPesquisar.setHasFixedSize(true);
        recyclerViewPesquisar.setLayoutManager(new LinearLayoutManager(getActivity()));

        //configurar um adapter
        adapterPesquisa = new AdapterPesquisa(listaPrestador,getActivity());
        recyclerViewPesquisar.setAdapter(adapterPesquisa);

        //configurar evento de click
        recyclerViewPesquisar.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewPesquisar,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        CadastroPrestador prestadorselected = listaPrestador.get(position);
                        Intent intent = new Intent(getActivity(), PerfilPrestadorActivity.class);
                        intent.putExtra("prestadorSelecionado", prestadorselected);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        //configurar searchview
        searchViewPesquisar.setQueryHint("Buscar Por Profissão");
        searchViewPesquisar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String textoDigitado = newText.toUpperCase();
                pesquisarPrestador(textoDigitado);

                return true;
            }
        });

        return view;
    }

    private void pesquisarPrestador(String texto){

        //limpar lista
        listaPrestador.clear();

        //pesquisar prestador caso tenha um texto na pesquisa
        if(texto.length() >= 2){

            Query query = prestadorReference.orderByChild("pres_profissao")
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //limpar lista
                    listaPrestador.clear();

                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        //recupera prestador do firebase
                        listaPrestador.add(ds.getValue(CadastroPrestador.class));

                    }

                    adapterPesquisa.notifyDataSetChanged();

                    /*
                    int total = listaPrestador.size();
                    Log.i("totalPrestadores", "total: " + total);
                    */

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

}
