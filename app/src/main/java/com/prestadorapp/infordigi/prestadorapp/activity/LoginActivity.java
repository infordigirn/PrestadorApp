package com.prestadorapp.infordigi.prestadorapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.prestadorapp.infordigi.prestadorapp.R;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroPrestador;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroUsuario;

public class LoginActivity extends AppCompatActivity {

    Spinner cadastrese;

    private EditText campoEmail, campoSenha;
    private Button buttonEntrar;
    private ProgressBar progressBar;

    private CadastroUsuario usuario;
    private CadastroPrestador prestador;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verifyIsLogado();
        inicializarComponentes();

        //fazer login do usuário
        progressBar.setVisibility(View.GONE);
        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recupera texto digitado
                String textoLoginEmail = campoEmail.getText().toString();
                String textoLoginSenha = campoSenha.getText().toString();

                //validação dos campos
                if(!textoLoginEmail.isEmpty()){
                    if(!textoLoginSenha.isEmpty()){

                            usuario = new CadastroUsuario();
                            usuario.setUsu_email(textoLoginEmail);
                            usuario.setUsu_senha(textoLoginSenha);

                            prestador = new CadastroPrestador();
                            prestador.setPres_email(textoLoginEmail);
                            prestador.setPres_senha(textoLoginSenha);

                            validarLogin(usuario, prestador);

                    }else{
                        Toast.makeText(LoginActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void verifyIsLogado(){
        auth = ConfiguracaoFirebase.getReferenceAuth();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void validarLogin(CadastroUsuario usuario, CadastroPrestador prestador){

        progressBar.setVisibility(View.VISIBLE);

        auth = ConfiguracaoFirebase.getReferenceAuth();
        auth.signInWithEmailAndPassword(
                usuario.getUsu_email(),
                usuario.getUsu_senha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this, "Erro ao tentar fazer login!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*auth.signInWithEmailAndPassword(
                prestador.getPres_email(),
                prestador.getPres_senha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this, "Erro ao tentar fazer login!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });*/

    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    public void abrirCadastroPrestador(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroPrestadorActivity.class);
        startActivity(intent);
    }

    private void inicializarComponentes(){

        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);

        buttonEntrar = findViewById(R.id.buttonEntrar);
        progressBar =findViewById(R.id.progressLogin);

    }
}
