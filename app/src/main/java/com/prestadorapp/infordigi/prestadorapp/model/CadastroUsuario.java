package com.prestadorapp.infordigi.prestadorapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.Map;

public class CadastroUsuario {

    private String Usu_id;
    private String Usu_nome;
    private String Usu_cpf;
    private String Usu_celular;
    private String Usu_email;
    private String Usu_senha;
    private String Usu_caminhoFoto;

    public CadastroUsuario() {
    }

    public void salvarUsuario(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getReferenceFirebase();
        DatabaseReference usuarioReference = databaseReference
                .child("usuarios")
                .child(getUsu_id());
        usuarioReference.setValue(this);
    }

    public void atualizarUsuario(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getReferenceFirebase();
        DatabaseReference usuarioReference = databaseReference
                .child("usuarios")
                .child(getUsu_id());

        Map<String, Object> valorUsuario = convertMap();
        usuarioReference.updateChildren(valorUsuario);

    }

    /**
     * método mapea objeto usuário
     *
     */
    public Map<String, Object> convertMap(){

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("usu_email", getUsu_email());
        usuarioMap.put("usu_nome", getUsu_nome());
        usuarioMap.put("usu_id", getUsu_id());
        usuarioMap.put("usu_caminhoFoto", getUsu_caminhoFoto());

        return usuarioMap;
    }

    public String getUsu_id() {
        return Usu_id;
    }

    public void setUsu_id(String usu_id) {
        Usu_id = usu_id;
    }

    public String getUsu_nome() {
        return Usu_nome;
    }

    public void setUsu_nome(String usu_nome) {
        Usu_nome = usu_nome;
    }

    public String getUsu_cpf() {
        return Usu_cpf;
    }

    public void setUsu_cpf(String usu_cpf) {
        Usu_cpf = usu_cpf;
    }

    public String getUsu_celular() {
        return Usu_celular;
    }

    public void setUsu_celular(String usu_celular) {
        Usu_celular = usu_celular;
    }

    @Exclude
    public String getUsu_senha() {
        return Usu_senha;
    }

    public void setUsu_senha(String usu_senha) {
        Usu_senha = usu_senha;
    }

    public String getUsu_caminhoFoto() {
        return Usu_caminhoFoto;
    }

    public void setUsu_caminhoFoto(String usu_caminhoFoto) {
        Usu_caminhoFoto = usu_caminhoFoto;
    }

    public String getUsu_email() {
        return Usu_email;
    }

    public void setUsu_email(String usu_email) {
        Usu_email = usu_email;
    }
}
