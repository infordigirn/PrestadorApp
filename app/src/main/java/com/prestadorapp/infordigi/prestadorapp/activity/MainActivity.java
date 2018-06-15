package com.prestadorapp.infordigi.prestadorapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.prestadorapp.infordigi.prestadorapp.R;
import com.prestadorapp.infordigi.prestadorapp.adapter.Common;
import com.prestadorapp.infordigi.prestadorapp.fragment.AvaliacaoFragment;
import com.prestadorapp.infordigi.prestadorapp.fragment.HomeFragment;
import com.prestadorapp.infordigi.prestadorapp.fragment.PerfilFragment;
import com.prestadorapp.infordigi.prestadorapp.fragment.PesquisaFragment;
import com.prestadorapp.infordigi.prestadorapp.fragment.SobreFragment;
import com.prestadorapp.infordigi.prestadorapp.helper.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recuperar token
        Common.currentToken = FirebaseInstanceId.getInstance().getToken();

        Log.i("CURRENTOKEN: ", Common.currentToken);

        //configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Prestador");
        toolbar.setTitleTextColor(R.color.white);
        setSupportActionBar(toolbar);

        //configurações de objeto
        auth = ConfiguracaoFirebase.getReferenceAuth();

        //configuração bottom navigation view
        configuraBottomNavigationView();

        //iniciar fragment home
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new PesquisaFragment()).commit();

    }

    /**
     * método responsável por criar o bottomNavigation
     */
    private void configuraBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

        //configurações iniciais
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);

        //habilitar navegação
        habilitarNavegacao(bottomNavigationViewEx);

        //configura item selecionado inicialmente
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

    }

    /**
     * método responsável por tratar eventos de click na bottomNavigation
     * @param viewEx
     */
    private void habilitarNavegacao(BottomNavigationViewEx viewEx){

        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId()){
                    case R.id.ic_home:
                        fragmentTransaction.replace(R.id.viewPager, new HomeFragment()).commit();
                        return true;
                    case R.id.ic_search:
                        fragmentTransaction.replace(R.id.viewPager, new PesquisaFragment()).commit();
                        return true;
                    case R.id.ic_about:
                        //fragmentTransaction.replace(R.id.viewPager, new SobreFragment()).commit();
                        startActivity(new Intent(getApplicationContext(), SobreActivity.class));
                        return true;
                    case R.id.ic_rate:
                        fragmentTransaction.replace(R.id.viewPager, new AvaliacaoFragment()).commit();
                        return true;
                    case R.id.ic_profile:
                        fragmentTransaction.replace(R.id.viewPager, new PerfilFragment()).commit();
                        return true;
                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sair:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){

        try{
            auth.signOut();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
