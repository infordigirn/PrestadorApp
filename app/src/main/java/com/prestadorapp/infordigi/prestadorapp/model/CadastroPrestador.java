package com.prestadorapp.infordigi.prestadorapp.model;

import com.google.firebase.database.DatabaseReference;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;

public class CadastroPrestador {

    private String pres_id;
    private String pres_nome;
    private String pres_CPF;
    private String pres_celular;
    private String pres_profissao;
    private String pres_email;
    private String pres_senha;

    public CadastroPrestador() {
    }

    public void salvarPrestador(){

        DatabaseReference databaseReference = ConfiguracaoFirebase.getReferenceFirebase();
        DatabaseReference prestadorReference = databaseReference.child("prestadores").child(getPres_id());
        prestadorReference.setValue(this);

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
        return pres_profissao;
    }

    public void setPres_profissao(String pres_profissao) {
        this.pres_profissao = pres_profissao;
    }

    public String getPres_email() {
        return pres_email;
    }

    public void setPres_email(String pres_email) {
        this.pres_email = pres_email;
    }

    public String getPres_senha() {
        return pres_senha;
    }

    public void setPres_senha(String pres_senha) {
        this.pres_senha = pres_senha;
    }
}
