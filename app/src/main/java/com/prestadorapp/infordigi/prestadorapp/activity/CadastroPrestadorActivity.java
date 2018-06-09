package com.prestadorapp.infordigi.prestadorapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.prestadorapp.infordigi.prestadorapp.R;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;
import com.prestadorapp.infordigi.prestadorapp.helper.MaskFone;
import com.prestadorapp.infordigi.prestadorapp.helper.MaskUtil;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroPrestador;

public class CadastroPrestadorActivity extends AppCompatActivity {

    private EditText campoPresNome, campoPresCPF, campoPresCelular, campoPresProfissao, campoPresEmail, campoPresSenha;
    private Button buttonCadastrarPrestador;
    private ProgressBar progressBarPrestador;

    private CadastroPrestador prestador;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_prestador);

        inicializarComponentes();

        //setar mascara fone
        campoPresCelular.addTextChangedListener(MaskFone.insert(campoPresCelular, MaskFone.MaskType.FONE));

        //setar mascara cpf
        campoPresCPF.addTextChangedListener(MaskUtil.insert(campoPresCPF, MaskUtil.MaskType.CPF));

        //cadastrar prestador
        progressBarPrestador.setVisibility(View.GONE);
        buttonCadastrarPrestador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recupera o que foi digitado
                String textoPresNome = campoPresNome.getText().toString();
                String textoPresCPF = campoPresCPF.getText().toString();
                String textoPresCelular = campoPresCelular.getText().toString();
                String textoPresProfissao = campoPresProfissao.getText().toString();
                String textoPresEmail = campoPresEmail.getText().toString();
                String textoPresSenha = campoPresSenha.getText().toString();

                //validação de campos
                if(!textoPresNome.isEmpty()){
                    if(!textoPresCPF.isEmpty()){
                        if(!textoPresCelular.isEmpty()){
                            if(!textoPresProfissao.isEmpty()){
                                if(!textoPresEmail.isEmpty()){
                                    if(!textoPresSenha.isEmpty()){

                                        prestador = new CadastroPrestador();
                                        prestador.setPres_nome(textoPresNome);
                                        prestador.setPres_CPF(textoPresCPF);
                                        prestador.setPres_celular(textoPresCelular);
                                        prestador.setPres_profissao(textoPresProfissao);
                                        prestador.setPres_email(textoPresEmail);
                                        prestador.setPres_senha(textoPresSenha);
                                        cadastrarPrestador(prestador);

                                    }else{
                                        Toast.makeText(CadastroPrestadorActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(CadastroPrestadorActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(CadastroPrestadorActivity.this, "Preencha a profissão!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(CadastroPrestadorActivity.this, "preencha o número do celular!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroPrestadorActivity.this, "Preencha o CPF!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroPrestadorActivity.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * método responsável por cadastrar prestador com email e senha
     * e fazer validações ao cadastrar
     */
    public void cadastrarPrestador(final CadastroPrestador prestador){

        progressBarPrestador.setVisibility(View.VISIBLE);
        auth = ConfiguracaoFirebase.getReferenceAuth();
        auth.createUserWithEmailAndPassword(
                prestador.getPres_email(),
                prestador.getPres_senha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            progressBarPrestador.setVisibility(View.GONE);

                            //salvar dados prestador
                            String pres_id = task.getResult().getUser().getUid();
                            prestador.setPres_id(pres_id);
                            prestador.salvarPrestador();

                            Toast.makeText(CadastroPrestadorActivity.this, "Dados cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                            //envia o usuário para uma activity após cadastro
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }else{

                            progressBarPrestador.setVisibility(View.GONE);

                            String erroExecao = "";
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthWeakPasswordException e){
                                erroExecao = "Digite uma senha mais forte!";
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                erroExecao = "Por favor, digite um email válido!";
                            }catch(FirebaseAuthUserCollisionException e){
                                erroExecao = "Esta conta já está cadastrada!";
                            }catch(Exception e){
                                erroExecao = "ao cadastrar prestador: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroPrestadorActivity.this, "Erro: " + erroExecao, Toast.LENGTH_SHORT).show();

                        }

                    }
                }
        );

    }

    public void inicializarComponentes(){

        campoPresNome = findViewById(R.id.editCadPresNome);
        campoPresCPF = findViewById(R.id.editCadPresCPF);
        campoPresCelular = findViewById(R.id.editCadPresCelular);
        campoPresProfissao = findViewById(R.id.editCadPresProfissao);
        campoPresEmail = findViewById(R.id.editCadPresEmail);
        campoPresSenha = findViewById(R.id.editCadPresSenha);

        buttonCadastrarPrestador = findViewById(R.id.buttonCadastrarPrestador);
        progressBarPrestador = findViewById(R.id.progressPrestador);

    }
}
