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
import android.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_emailLogin, editText_senhaLogin;
    private CardView cardView_recuperarSenha, cardView_loginFacebook, cardView_loginGoogle, cardView_entrarLogin;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginemail);


        getSupportActionBar().setTitle("Perfil");

        editText_emailLogin = (EditText) findViewById(R.id.editText_EmailLogin);
        editText_senhaLogin = (EditText) findViewById(R.id.editText_SenhaLogin);


        cardView_recuperarSenha = (CardView) findViewById(R.id.cardView_RecuperarSenha);
        cardView_entrarLogin = (CardView) findViewById(R.id.cardView_EntrarLogin);
        cardView_loginFacebook = (CardView) findViewById(R.id.cardView_LoginFacebook);
        cardView_loginGoogle = (CardView) findViewById(R.id.cardView_LoginGoogle);


        cardView_recuperarSenha.setOnClickListener(this);
        cardView_entrarLogin.setOnClickListener(this);
        cardView_loginFacebook.setOnClickListener(this);
        cardView_loginGoogle.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        servicosGoogle();
        servicosFacebook();

    }


//-------------------------------------SERVIÇOS LOGIN--------------------------------------------------------

    private void servicosFacebook(){

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                adicionarContaFacebookaoFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                Toast.makeText(getBaseContext(),"Cancelado",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(getBaseContext(),"Erro ao logar com Facebook",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void servicosGoogle(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

    }


//-------------------------------------TRATAMENTO DE CLICKS------------------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cardView_LoginFacebook:

                signInFacebook();

                break;

            case R.id.cardView_LoginGoogle:

                signInGoogle();

                break;
            case R.id.cardView_EntrarLogin:

                loginEmail();

                break;

            case R.id.cardView_RecuperarSenha:

                recuperarSenha();

                break;
        }

    }


//----------------------------------METODOS DE LOGIN-----------------------------------------------------

    private void signInFacebook(){

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }

    private void signInGoogle(){

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null){

            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, 555);

        }else{

            //ja existe alguem conectado pelo google

            startActivity(new Intent(getBaseContext(),MainActivityLogado.class));
            finish();

        }
    }


//-----------------------------------------AUTENTICACAO NO FIREBASE----------------------------------------

    private void adicionarContaFacebookaoFirebase(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getBaseContext(), "Usuário logado com sucesso", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getBaseContext(),MainActivityLogado.class));
                            finish();

                        } else {

                            Toast.makeText(getBaseContext(),"Erro ao logar com o Facebook",Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }


    private void adicionarContaGoogleaoFirebase (GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getBaseContext(), "Usuário logado com sucesso", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getBaseContext(),MainActivityLogado.class));
                            finish();

                        } else {

                            Toast.makeText(getBaseContext(),"Erro ao criar conta Google",Toast.LENGTH_LONG).show();

                        }


                    }
                });
    }


    //---------------------------------METODOS DA ACTIVITY-------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    //--------------------------------------METODOS DE LOGIN COM EMAIL E SENHA--------------------------------
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


    private void confirmarLoginEmail(String email, String senha) {

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Usuário logado com sucesso", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), MainActivityLogado.class));
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
