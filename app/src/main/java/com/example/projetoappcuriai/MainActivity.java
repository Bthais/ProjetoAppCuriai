package com.example.projetoappcuriai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Button button_login, button_cadastrar;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private  FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        button_login = (Button) findViewById(R.id.button_Login);
        button_cadastrar = (Button) findViewById(R.id.button_Cadastrar);

        button_login.setOnClickListener(this);
        button_cadastrar.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        estadoAutenticacao();
    }

    private void estadoAutenticacao(){

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user!=null){

                    Toast.makeText(getBaseContext(),"usuario"+ " "+user.getEmail()+" "+"está logado" , Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(),MainActivityLogado.class));
                    finish();
                    //aqui posso colocar o que o usuario pode fazer se ele estiver logado
                }else{

                   // Toast.makeText(getBaseContext(),"usuario" + "Não está logado" , Toast.LENGTH_LONG).show();

                }


            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_categorias) {

        } else if (id == R.id.nav_explorar) {

        } else if (id == R.id.nav_avaliacao) {

        } else if (id == R.id.nav_ajuda) {

        } else if (id == R.id.nav_termo_uso) {

        } else if (id == R.id.nav_privacidade) {


        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_Login:

                user = auth.getCurrentUser();

                if(user == null){

                    startActivity(new Intent(this, LoginEmailActivity.class));
                    finish();

                }else{

                    startActivity(new Intent(this, MainActivityLogado.class));
                    finish();
                }

                break;

            case R.id.button_Cadastrar:

                startActivity(new Intent(this, CadastrarActivity.class));

                break;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null){

            auth.removeAuthStateListener(authStateListener);
        }
    }
}
