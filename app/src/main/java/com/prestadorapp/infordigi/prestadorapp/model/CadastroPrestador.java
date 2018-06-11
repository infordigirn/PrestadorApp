package com.prestadorapp.infordigi.prestadorapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CadastroPrestador implements Serializable {

    private String pres_id;
    private String pres_nome;
    private String pres_CPF;
    private String pres_celular;
    private String pres_profissao;
    private String pres_email;
    private String pres_senha;
    private String pres_perfil = "prestador";
    private String pres_caminhoFoto;
    private int seguidores = 0;
    private int contratacoes = 0;
    private float avaliacoes = 0;

    public CadastroPrestador() {
    }

    public void salvarPrestador(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getReferenceFirebase();
        DatabaseReference prestadorReference = databaseReference
                .child("prestadores")
                .child(getPres_id());
        prestadorReference.setValue(this);

    }

    public void atualizarPrestador(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getReferenceFirebase();
        DatabaseReference prestadorReference = databaseReference
                .child("prestadores")
                .child(getPres_id());

        Map<String, Object> valorPrestador = convertMap();
        prestadorReference.updateChildren(valorPrestador);

    }

    /**
     *
     *método mapea objeto prestador
     *
     */
    public Map<String, Object> convertMap(){

        HashMap<String, Object> prestadorMap = new HashMap<>();
        prestadorMap.put("pres_id", getPres_id());
        prestadorMap.put("pres_nome", getPres_nome());
        prestadorMap.put("pres_email", getPres_email());
        prestadorMap.put("pres_perfil", getPres_perfil());
        prestadorMap.put("pres_caminhoFoto", getPres_caminhoFoto());
        prestadorMap.put("seguidores", getSeguidores());
        prestadorMap.put("avaliacoes", getAvaliacoes());
        prestadorMap.put("contratacoes", getContratacoes());

        return prestadorMap;
    }

    public String getPres_id() {
        return pres_id;
    }

    public void setPres_id(String pres_id) {
        this.pres_id = pres_id;
    }

    public String getPres_nome() {
        return pres_nome;
    }

    public void setPres_nome(String pres_nome) {
        this.pres_nome = pres_nome;
    }

    public String getPres_CPF() {
        return pres_CPF;
    }

    public void setPres_CPF(String pres_CPF) {
        this.pres_CPF = pres_CPF;
    }

    public String getPres_celular() {
        return pres_celular;
    }

    public void setPres_celular(String pres_celular) {
        this.pres_celular = pres_celular;
    }

    public String getPres_profissao() {
        return pres_profissao.toUpperCase();
    }

    public void setPres_profissao(String pres_profissao) {
        this.pres_profissao = pres_profissao.toUpperCase();
    }

    public String getPres_email() {
        return pres_email;
    }

    public void setPres_email(String pres_email) {
        this.pres_email = pres_email;
    }

    @Exclude
    public String getPres_senha() {
        return pres_senha;
    }

    public void setPres_senha(String pres_senha) {
        this.pres_senha = pres_senha;
    }

    public String getPres_perfil() {
        return pres_perfil;
    }

    public String getPres_caminhoFoto() {
        return pres_caminhoFoto;
    }

    public void setPres_caminhoFoto(String pres_caminhoFoto) {
        this.pres_caminhoFoto = pres_caminhoFoto;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getContratacoes() {
        return contratacoes;
    }

    public void setContratacoes(int contratacoes) {
        this.contratacoes = contratacoes;
    }

    public float getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(float avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}
