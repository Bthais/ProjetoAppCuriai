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

import com.example.projetoappcuriai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastrarActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_Email, editText_Senha, editText_SenhaRepetir;
    private Button button_Cadastrar, button_Cancelar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        editText_Email = (EditText) findViewById(R.id.editText_EmailCadastro);
        editText_Senha = (EditText) findViewById(R.id.editText_SenhaCadastro);
        editText_SenhaRepetir = (EditText) findViewById(R.id.editText_RepetirSenhaCadastro);

        button_Cadastrar = (Button) findViewById(R.id.button_cadastrarUsuario);
        button_Cancelar = (Button) findViewById(R.id.button_cancelar);


        button_Cadastrar.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.button_cadastrarUsuario:

                cadastrar();

                break;


        }
    }


    private void cadastrar() {

        String email = editText_Email.getText().toString().trim();
        String senha = editText_Senha.getText().toString().trim();
        String confirmaSenha = editText_SenhaRepetir.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {

            Toast.makeText(getBaseContext(), "Erro - Campo obrigatório não preenchido", Toast.LENGTH_LONG).show();

        } else {


            if (senha.contentEquals(confirmaSenha)) {

                if(Util.verfificarInternet(this)){

                    criarUsuario( email, senha);

                }else{

                    Toast.makeText(getBaseContext(), "Erro - Verifique sua conexão com a internet", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getBaseContext(), "Erro - Senhas diferentes", Toast.LENGTH_LONG).show();
            }
        }
    }


        private void criarUsuario (String email, String senha){

            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        startActivity(new Intent(getBaseContext(), MainActivityLogado.class));
                        Toast.makeText(getBaseContext(), "Cadastrado efetuado com sucesso", Toast.LENGTH_LONG).show();
                        finish();

                    } else {

                        String resposta = task.getException().toString();
                        Util.opcoesErro(getBaseContext(), resposta);
                    }

                }
            });
        }


}


