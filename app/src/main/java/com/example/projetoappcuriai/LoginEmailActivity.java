package com.example.projetoappcuriai;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_emailLogin, editText_senhaLogin;
    private Button button_entrarLogin, button_recuperarSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginemail);

        editText_emailLogin = (EditText) findViewById(R.id.editText_EmailLogin);
        editText_senhaLogin = (EditText) findViewById(R.id.editText_SenhaLogin);

        button_entrarLogin = (Button) findViewById(R.id.button_EntrarLogin);
        button_recuperarSenha = (Button) findViewById(R.id.button_Recuperar);

        button_entrarLogin.setOnClickListener(this);
        button_recuperarSenha.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_EntrarLogin:

                loginEmail();

                break;

            case R.id.button_Recuperar:

                break;
        }

    }

    private void loginEmail() {
        String email = editText_emailLogin.getText().toString().trim();
        String senha = editText_senhaLogin.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {

            Toast.makeText(getBaseContext(), "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show();
        } else {

            if(verfificarInternet()){

                confirmarLoginEmail(email, senha);
            }else{

                Toast.makeText(getBaseContext(), "Erro - Verifique sua conexão com a internet", Toast.LENGTH_LONG).show();

            }
        }
    }

    private boolean verfificarInternet(){

        ConnectivityManager conexao = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo informacao = (conexao.getActiveNetworkInfo());

        if(informacao != null && informacao.isConnected()){

            return true;
        }else {

            return false;
        }
    }

    private void confirmarLoginEmail(String email, String senha){

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    startActivity(new Intent(getBaseContext(),MainActivity.class));
                    Toast.makeText(getBaseContext(), "Usuário logado com sucesso", Toast.LENGTH_LONG).show();
                    finish();
                } else {

                    String resposta = task.getException().toString();
                    opcoesErro(resposta);
                   // Toast.makeText(getBaseContext(), "Erro ao logar usuário", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void opcoesErro(String resposta) {

        if (resposta.contains("least 6 characters")) {

            Toast.makeText(getBaseContext(), "A senha deve conter pelo menos 6 caracteres", Toast.LENGTH_LONG).show();

        } else if (resposta.contains("address is badly")) {

            Toast.makeText(getBaseContext(), "Digite um e-mail válido", Toast.LENGTH_LONG).show();

        }else if(resposta.contains("interrupted connection")){

            Toast.makeText(getBaseContext(), "Sem conexão com o Servidor", Toast.LENGTH_LONG).show();

        }else if(resposta.contains("password is invalid")){

            Toast.makeText(getBaseContext(), "Senha inválida", Toast.LENGTH_LONG).show();

        } else if(resposta.contains("There is no user")){

            Toast.makeText(getBaseContext(), "E-mail não cadastrado", Toast.LENGTH_LONG).show();

        } else{
            Toast.makeText(getBaseContext(), resposta, Toast.LENGTH_LONG).show();
        }

    }
}
