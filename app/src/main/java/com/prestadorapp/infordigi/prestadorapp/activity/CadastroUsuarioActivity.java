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
import com.prestadorapp.infordigi.prestadorapp.helper.UsuarioFirebase;
import com.prestadorapp.infordigi.prestadorapp.model.CadastroUsuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText campoUsuNome, campoUsuCPF, campoUsuCelular,campoUsuEmail, campoUsuSenha;
    private Button buttonCadastrarUsuario;
    private ProgressBar progressBarUsuario;

    private CadastroUsuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        inicializarComponentes();

        //setar mascara fone
        campoUsuCelular.addTextChangedListener(MaskFone.insert(campoUsuCelular, MaskFone.MaskType.FONE));

        //setar mascara cpf
        campoUsuCPF.addTextChangedListener(MaskUtil.insert(campoUsuCPF, MaskUtil.MaskType.CPF));

        //cadastrar usuario
        progressBarUsuario.setVisibility(View.GONE);
        buttonCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recuperar texto digitado
                String textoUsuNome = campoUsuNome.getText().toString();
                String textoUsuCPF = campoUsuCPF.getText().toString();
                String textoUsuCelular = campoUsuCelular.getText().toString();
                String textoUsuEmail = campoUsuEmail.getText().toString();
                String textoUsuSenha = campoUsuSenha.getText().toString();

                if(!textoUsuNome.isEmpty()){
                    if(!textoUsuCPF.isEmpty()){
                        if(!textoUsuCelular.isEmpty()){
                            if(!textoUsuEmail.isEmpty()){
                                if(!textoUsuSenha.isEmpty()){

                                    usuario = new CadastroUsuario();
                                    usuario.setUsu_nome(textoUsuNome);
                                    usuario.setUsu_cpf(textoUsuCPF);
                                    usuario.setUsu_celular(textoUsuCelular);
                                    usuario.setUsu_email(textoUsuEmail);
                                    usuario.setUsu_senha(textoUsuSenha);
                                    cadastrarUsuario(usuario);

                                }else{
                                    Toast.makeText(CadastroUsuarioActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(CadastroUsuarioActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(CadastroUsuarioActivity.this, "Preencha o número de celular!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroUsuarioActivity.this, "Preencha o CPF!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroUsuarioActivity.this,"Preencha o nome!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * método responsável por cadastrar usuário com email e senha
     * e fazer validações ao cadastrar
     */
    public void cadastrarUsuario(final CadastroUsuario usuario){

        progressBarUsuario.setVisibility(View.VISIBLE);
        auth = ConfiguracaoFirebase.getReferenceAuth();
        auth.createUserWithEmailAndPassword(
                usuario.getUsu_email(),
                usuario.getUsu_senha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){


                            try{

                                progressBarUsuario.setVisibility(View.GONE);

                                //salvar dados do usuário
                                String usu_id = task.getResult().getUser().getUid();
                                usuario.setUsu_id(usu_id);
                                usuario.salvarUsuario();

                                //salvar dados usuário do profile do firebase
                                UsuarioFirebase.atualizarNomeUsuario(usuario.getUsu_nome());

                                Toast.makeText(CadastroUsuarioActivity.this, "Dados cadastrados com sucesso!", Toast.LENGTH_SHORT).show();

                                //envia o usuário para uma activity após cadastro
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();

                            }catch(Exception e){
                                e.printStackTrace();
                            }


                        }else{

                            progressBarUsuario.setVisibility(View.GONE);

                            String erroExecao = "";
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthWeakPasswordException e){
                                erroExecao = "Digite uma senha mais forte!";
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                erroExecao = "Por favor,digite um e-mail válido!";
                            }catch(FirebaseAuthUserCollisionException e){
                                erroExecao = "Esta conta já está cadastrada!";
                            }catch(Exception e){
                                erroExecao = "ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExecao, Toast.LENGTH_SHORT).show();

                        }

                    }
                }
        );

    }

    public void inicializarComponentes(){

        campoUsuNome = findViewById(R.id.editCadUsuNome);
        campoUsuCPF = findViewById(R.id.editCadUsuCPF);
        campoUsuCelular = findViewById(R.id.editCadUsuCelular);
        campoUsuEmail = findViewById(R.id.editCadUsuEmail);
        campoUsuSenha = findViewById(R.id.editCadUsuSenha);

        buttonCadastrarUsuario = findViewById(R.id.buttonCadastrarUsuario);
        progressBarUsuario = findViewById(R.id.progressUsuario);


    }

}
