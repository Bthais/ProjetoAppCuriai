package com.example.projetoappcuriai;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivityLogado extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logado);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





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

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        Menu m = nv.getMenu();
        int id = item.getItemId();
        if (id == R.id.nav_home) {

            Intent intent = new Intent(this, MainActivityLogado.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_explorar) {

        } else if (id == R.id.nav_categorias) {
            boolean b = !m.findItem(R.id.nav_festas).isVisible();
            //setting submenus visible state
            m.findItem(R.id.nav_festas).setVisible(b);
            m.findItem(R.id.nav_music).setVisible(b);
            m.findItem(R.id.nav_aprender).setVisible(b);
            m.findItem(R.id.nav_negocios).setVisible(b);
            m.findItem(R.id.nav_cultura).setVisible(b);
            m.findItem(R.id.nav_bemEstar).setVisible(b);

            return true;
        } else if (id == R.id.nav_festas) {

        } else if (id == R.id.nav_music) {

        } else if (id == R.id.nav_aprender) {

        } else if (id == R.id.nav_negocios) {

        } else if (id == R.id.nav_cultura) {

        } else if (id == R.id.nav_bemEstar) {

        } else if (id == R.id.nav_event) {
            boolean b = !m.findItem(R.id.nav_meuEvent).isVisible();
            //setting submenus visible state
            m.findItem(R.id.nav_meuEvent).setVisible(b);
            m.findItem(R.id.nav_criarEvent).setVisible(b);
            return true;

        } else if (id == R.id.nav_com) {

            m.findItem(R.id.nav_avaliacao).setVisible(false);
            m.findItem(R.id.nav_ajuda).setVisible(false);
            m.findItem(R.id.nav_termo_uso).setVisible(false);
            m.findItem(R.id.nav_privacidade).setVisible(false);
            m.findItem(R.id.nav_sair).setVisible(false);

        } else if (id == R.id.nav_avaliacao) {

        } else if (id == R.id.nav_ajuda) {

        }else if (id == R.id.nav_termo_uso) {

        }else if (id == R.id.nav_privacidade) {

        }else if (id == R.id.nav_sair) {

            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            //finish();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            googleSignInClient = GoogleSignIn.getClient(this,gso);
            googleSignInClient.signOut();

            finish();
            startActivity(new Intent(getBaseContext(), MainActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
