package com.prestadorapp.infordigi.prestadorapp.helper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroPrestador;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroUsuario;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual(){

        FirebaseAuth auth = ConfiguracaoFirebase.getReferenceAuth();
        return auth.getCurrentUser();

    }

    public static String getIdentificadorUsuario(){
        return getUsuarioAtual().getUid();
    }


    public static void atualizarNomeUsuario(String nome){

        try{

            //usuario logado no app
            FirebaseUser usuarioLogado = getUsuarioAtual();

            //configurar objeto para alteração do perfil
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(nome)
                    .build();
            usuarioLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao tentar atualizar nome de perfil!");
                    }
                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void atualizarFotoUsuario(Uri url){

        try{

            //usuario logado no app
            FirebaseUser usuarioLogado = getUsuarioAtual();

            //configurar objeto para alteração do perfil
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setPhotoUri(url)
                    .build();
            usuarioLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao tentar atualizar foto de perfil!");
                    }
                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static CadastroUsuario getDadosUsuarioLogado(){

        FirebaseUser firebaseUser = getUsuarioAtual();

        CadastroUsuario usuario = new CadastroUsuario();
        usuario.setUsu_email(firebaseUser.getEmail());
        usuario.setUsu_id(firebaseUser.getUid());
        usuario.setUsu_nome(firebaseUser.getDisplayName());

        if(firebaseUser.getPhotoUrl() == null){
            usuario.setUsu_caminhoFoto("");
        }else{
            usuario.setUsu_caminhoFoto(firebaseUser.getPhotoUrl().toString());
        }

        return usuario;

    }

    public static CadastroPrestador getDadosPrestadorLogado(){

        FirebaseUser firebaseUser = getUsuarioAtual();

        CadastroPrestador prestador = new CadastroPrestador();
        prestador.setPres_id(firebaseUser.getUid());
        prestador.setPres_email(firebaseUser.getEmail());
        prestador.setPres_nome(firebaseUser.getDisplayName());

        if(firebaseUser.getPhotoUrl() == null){
            prestador.setPres_caminhoFoto("");
        }else{
            prestador.setPres_caminhoFoto(firebaseUser.getPhotoUrl().toString());
        }

        return prestador;
    }

}
