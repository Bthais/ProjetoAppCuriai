package com.example.projetoappcuriai;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private CardView  cardView_login, cardView_cadastrar;
    private FirebaseAuth auth;
    private FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cardView_login = (CardView) findViewById(R.id.cardView_Login);
        cardView_cadastrar = (CardView) findViewById(R.id.cardView_Cadastrar);

        cardView_login.setOnClickListener(this);
        cardView_cadastrar.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();


        verificarLogin();


    }

    private void verificarLogin(){

        FirebaseUser user = auth.getCurrentUser();

        if(user != null){

            Log.d("testeCursoI M","Usuario Logado");

            startActivity(new Intent(this, MainActivityLogado.class));
            finish();

        }
    }
//-----------------------------------------SERVIÇOS LOGIN-----------------------------------------------------


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
        NavigationView nav= (NavigationView) findViewById(R.id.nav_view);
        Menu me=nav.getMenu();
        int id = item.getItemId();
        if (id == R.id.nav_home) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }else if (id == R.id.nav_explorar) {

        } else if (id == R.id.nav_categorias) {
            boolean bo=!me.findItem(R.id.nav_festas).isVisible();
            //setting submenus visible state
            me.findItem(R.id.nav_festas).setVisible(bo);
            me.findItem(R.id.nav_music).setVisible(bo);
            me.findItem(R.id.nav_aprender).setVisible(bo);
            me.findItem(R.id.nav_negocios).setVisible(bo);
            me.findItem(R.id.nav_cultura).setVisible(bo);
            me.findItem(R.id.nav_bemEstar).setVisible(bo);

            return true;
        }else if (id == R.id.nav_festas) {

        }else if (id == R.id.nav_music) {

        } else if (id == R.id.nav_aprender) {

        } else if (id == R.id.nav_negocios) {

        } else if (id == R.id.nav_cultura) {

        } else if (id == R.id.nav_bemEstar) {

        } else  if (id == R.id.nav_event) {
                boolean bo=!me.findItem(R.id.nav_meuEvent).isVisible();
                //setting submenus visible state
                me.findItem(R.id.nav_meuEvent).setVisible(bo);
                me.findItem(R.id.nav_criarEvent).setVisible(bo);
                return true;
        } else if (id == R.id.nav_com) {

            me.findItem(R.id.nav_avaliacao).setVisible(false);
            me.findItem(R.id.nav_ajuda).setVisible(false);
            me.findItem(R.id.nav_termo_uso).setVisible(false);
            me.findItem(R.id.nav_privacidade).setVisible(false);
            me.findItem(R.id.nav_sair).setVisible(false);

        } else if (id == R.id.nav_avaliacao) {

        } else if (id == R.id.nav_ajuda) {

        }else if (id == R.id.nav_termo_uso) {

        }else if (id == R.id.nav_privacidade) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//-----------------------------------TRATAMENTO DE CLICKS--------------------------------------------
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cardView_Login:

                signInEmail();

                break;

            case R.id.cardView_Cadastrar:

                startActivity(new Intent(this, CadastrarActivity.class));

                break;
        }


    }
//------------------------------------METODOS DE LOGIN---------------------------------------------------
    private void signInEmail(){

        FirebaseUser user = auth.getCurrentUser();

        if(user == null){

            Log.d("testeCursoI M","Usuario Não Logado");


            startActivity(new Intent(this, LoginEmailActivity.class));

        }else{
            Log.d("testeCursoI M","Usuario Logado");

            finish();
            startActivity(new Intent(this, MainActivityLogado.class));

        }

    }


}
