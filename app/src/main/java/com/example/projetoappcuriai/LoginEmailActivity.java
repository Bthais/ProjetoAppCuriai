package com.example.projetoappcuriai;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_emailLogin, editText_senhaLogin;
    private Button button_entrarLogin;
    private CardView cardView_recuperarSenha;
    private CardView cardView_loginGoogle;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginemail);

        editText_emailLogin = (EditText) findViewById(R.id.editText_EmailLogin);
        editText_senhaLogin = (EditText) findViewById(R.id.editText_SenhaLogin);

        button_entrarLogin = (Button) findViewById(R.id.button_EntrarLogin);
        cardView_recuperarSenha = (CardView) findViewById(R.id.cardView_RecuperarSenha);
        cardView_loginGoogle = (CardView) findViewById(R.id.cardView_LoginGoogle);


        button_entrarLogin.setOnClickListener(this);
        cardView_recuperarSenha.setOnClickListener(this);
        cardView_loginGoogle.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        servicosGoogle();

    }

    private void servicosGoogle(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cardView_LoginGoogle:

                signInGoogle();

                break;
            case R.id.button_EntrarLogin:

                loginEmail();

                break;

            case R.id.cardView_RecuperarSenha:

                recuperarSenha();

                break;
        }

    }

    private void signInGoogle(){

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null){

            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, 555);

        }else{

            //ja existe alguem conectado pelo google

            Toast.makeText(getBaseContext(),"Já logado",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getBaseContext(),MainActivityLogado.class));
            finish();

        }
    }

    private void adicionarContaGoogleaoFirebase (GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(getBaseContext(),MainActivityLogado.class));
                            finish();

                        } else {

                            Toast.makeText(getBaseContext(),"Erro ao criar conta Google",Toast.LENGTH_LONG).show();

                        }


                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 555){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                adicionarContaGoogleaoFirebase(account);


            }catch (ApiException e){

                Toast.makeText(getBaseContext(),"Erro ao logar com conta do Google",Toast.LENGTH_LONG).show();

            }

        }
    }

    private void recuperarSenha(){

        String email = editText_emailLogin.getText().toString().trim();

        if (email.isEmpty()) {

            Toast.makeText(getBaseContext(), "Seu e-mail deve ser informado para recuperação de senha", Toast.LENGTH_LONG).show();
        }else{

            enviarEmail(email);
        }

    }

    private void enviarEmail(String email){

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getBaseContext(),"Uma mesnsagem com um link de redefinição de senha foi enviada para o seu e-mail",
                        Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String erro = e.toString();
                Util.opcoesErro(getBaseContext(),erro);

            }
        });

    }

    private void loginEmail() {

        String email = editText_emailLogin.getText().toString().trim();
        String senha = editText_senhaLogin.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {

            Toast.makeText(getBaseContext(), "Preencha os campos obrigatórios", Toast.LENGTH_LONG).show();
        } else {

            if(Util.verfificarInternet(this)){

                confirmarLoginEmail(email, senha);
            }else{

                Toast.makeText(getBaseContext(), "Erro - Verifique sua conexão com a internet", Toast.LENGTH_LONG).show();

            }
        }
    }


    private void confirmarLoginEmail(String email, String senha){

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    startActivity(new Intent(getBaseContext(),MainActivityLogado.class));
                    Toast.makeText(getBaseContext(), "Usuário logado com sucesso", Toast.LENGTH_LONG).show();
                    finish();
                } else {

                    String resposta = task.getException().toString();
                    Util.opcoesErro(getBaseContext(), resposta);
                   // Toast.makeText(getBaseContext(), "Erro ao logar usuário", Toast.LENGTH_LONG).show();

                }

            }
        });
    }


}
