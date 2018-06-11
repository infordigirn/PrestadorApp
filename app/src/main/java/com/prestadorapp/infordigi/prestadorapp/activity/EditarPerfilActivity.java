package com.prestadorapp.infordigi.prestadorapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prestadorapp.infordigi.prestadorapp.R;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;
import com.prestadorapp.infordigi.prestadorapp.helper.UsuarioFirebase;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroPrestador;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroUsuario;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView imageEditarPerfil;
    private TextView textAlterarFoto;
    private TextInputEditText editNomePerfil, editEmailPerfil;
    private Button buttonSalvarAlteracao;
    private String identificadorUsuario;

    private CadastroUsuario usuarioLogado;
    private CadastroPrestador prestadorLogado;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //configurações iniciais
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        prestadorLogado = UsuarioFirebase.getDadosPrestadorLogado();
        storageReference = ConfiguracaoFirebase.getReferenceStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        //configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        inicializarComponentes();

        //recuperar dados do usuario
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        editNomePerfil.setText(usuarioPerfil.getDisplayName());
        editEmailPerfil.setText(usuarioPerfil.getEmail());

        Uri url = usuarioPerfil.getPhotoUrl();
        if(url != null){
            Glide.with(EditarPerfilActivity.this)
                    .load(url)
                    .into(imageEditarPerfil);
        }else {
            imageEditarPerfil.setImageResource(R.drawable.avatar);
        }

        //salvar alterações do nome
        buttonSalvarAlteracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeAtualizado = editNomePerfil.getText().toString();

                //atualizar nome no perfil
                UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);

                if(usuarioLogado.getUsu_perfil() == "cliente") {
                    //atualizar nome no banco
                    usuarioLogado.setUsu_nome(nomeAtualizado);
                    usuarioLogado.atualizarUsuario();

                    Toast.makeText(EditarPerfilActivity.this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();

                }else if(prestadorLogado.getPres_perfil() == "prestador"){
                    prestadorLogado.setPres_nome(nomeAtualizado);
                    prestadorLogado.atualizarPrestador();

                    Toast.makeText(EditarPerfilActivity.this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //alterar foto usuários
        textAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_GALERIA);
                }

            }
        });

    }

    /**
     * método realiza troca da imagem do perfil
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try{

                //seleção da foto
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                //caso tenha sido escolhida a imagem
                if(imagem != null){

                    //configurar imagem na tela
                    imageEditarPerfil.setImageBitmap(imagem);


                    //recuperar imagem
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    if(usuarioLogado.getUsu_perfil() == "cliente"){
                        //salvar imagem usuário no banco
                        StorageReference imagemUsuario = storageReference
                                .child("usu_imagens")
                                .child("perfil")
                                .child(identificadorUsuario + ".jpeg");
                        UploadTask uploadTask = imagemUsuario.putBytes(dadosImagem);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditarPerfilActivity.this, "Erro ao tentar fazer upload da imagem!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                //recuperar local da foto
                                Uri url = taskSnapshot.getDownloadUrl();
                                atualizarFotoUsuario(url);

                                Toast.makeText(EditarPerfilActivity.this, "Sucesso ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    if(prestadorLogado.getPres_perfil() == "prestador") {
                        //salvar imagem prestador no banco
                        StorageReference imagemPrestador = storageReference
                                .child("pres_imagens")
                                .child("perfil")
                                .child(identificadorUsuario + ".jpeg");
                        UploadTask uploadTask = imagemPrestador.putBytes(dadosImagem);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditarPerfilActivity.this, "Erro ao tentar fazer upload da imagem!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                //recuperar local da foto
                                Uri url = taskSnapshot.getDownloadUrl();
                                atualizarFotoPrestador(url);

                                Toast.makeText(EditarPerfilActivity.this, "Sucesso ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    private void atualizarFotoUsuario(Uri url){
        //atalizar foto no perfil
        UsuarioFirebase.atualizarFotoUsuario(url);

        //atualizar foto no firebase
        usuarioLogado.setUsu_caminhoFoto(url.toString());
        usuarioLogado.atualizarUsuario();

        Toast.makeText(EditarPerfilActivity.this, "Foto alterada com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void atualizarFotoPrestador(Uri url){

        UsuarioFirebase.atualizarFotoUsuario(url);

        prestadorLogado.setPres_caminhoFoto(url.toString());
        prestadorLogado.atualizarPrestador();

        Toast.makeText(EditarPerfilActivity.this, "Foto alterada com sucesso!", Toast.LENGTH_SHORT).show();
    }

    public void inicializarComponentes(){

        imageEditarPerfil = findViewById(R.id.imageEditarPerfil);
        textAlterarFoto = findViewById(R.id.textAlterarFoto);
        editNomePerfil = findViewById(R.id.editNomePerfil);
        editEmailPerfil = findViewById(R.id.editEmailPerfil);
        buttonSalvarAlteracao = findViewById(R.id.buttonSalvarAlteracao);
        editEmailPerfil.setFocusable(false);

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;
    }
}
