package com.prestadorapp.infordigi.prestadorapp.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prestadorapp.infordigi.prestadorapp.R;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroPrestador;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.prestadorapp.infordigi.prestadorapp.R.color.cinza_medio;

public class PerfilPrestadorActivity extends AppCompatActivity {

    private CadastroPrestador prestadorSelected;
    private Button buttonAcaoPerfil;
    private CircleImageView imagePerfil;
    private TextView textAvaliacao, textContratacao, textSeguindo;

    private DatabaseReference prestadoresReference;
    private DatabaseReference prestadoreRef;
    private ValueEventListener valueEventListenerPerfilPrestador;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_prestador);

        //configurações iniciais
        prestadoresReference = ConfiguracaoFirebase.getReferenceFirebase().child("prestadores");

        //inicializar componentes
        inicializarComponentes();

        //configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        //recuperar prestador selecionado
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
           prestadorSelected = (CadastroPrestador) bundle.getSerializable("prestadorSelecionado");

            //configura o nome do prestador na actionbar
            getSupportActionBar().setTitle(prestadorSelected.getPres_nome());

            //recuperar foto do prestador
            String caminhoFoto = prestadorSelected.getPres_caminhoFoto();
            if(caminhoFoto != null){
                Uri url = Uri.parse(caminhoFoto);
                Glide.with(PerfilPrestadorActivity.this)
                        .load(url)
                        .into(imagePerfil);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosPerfilPrestador();
    }

    @Override
    protected void onStop() {
        super.onStop();
        prestadoreRef.removeEventListener(valueEventListenerPerfilPrestador);
    }

    private void recuperarDadosPerfilPrestador(){

        prestadoreRef = prestadoresReference.child(prestadorSelected.getPres_id());

        valueEventListenerPerfilPrestador = prestadoreRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        CadastroPrestador prestador = dataSnapshot.getValue(CadastroPrestador.class);

                        String seguidores = String.valueOf(prestador.getSeguidores());
                        String contratacoes = String.valueOf(prestador.getContratacoes());
                        String avaliacoes = String.valueOf(prestador.getAvaliacoes());

                        textSeguindo.setText(seguidores);
                        textContratacao.setText(contratacoes);
                        textAvaliacao.setText(avaliacoes);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void inicializarComponentes(){

        imagePerfil = findViewById(R.id.imagePerfil);
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        buttonAcaoPerfil.setText("Contratar");

        textAvaliacao = findViewById(R.id.textAvaliacao);
        textContratacao = findViewById(R.id.textContratacao);
        textSeguindo = findViewById(R.id.textSeguindo);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
