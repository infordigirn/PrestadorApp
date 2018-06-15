package com.prestadorapp.infordigi.prestadorapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.prestadorapp.infordigi.prestadorapp.R;

import mehdi.sakout.aboutpage.AboutPage;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sobre);

        View sobre = new AboutPage(this)
                .setImage(R.drawable.prestador_mini)
                .addGroup("Fale Conosco")
                .addWebsite("www.prestadorapp2016.com.br")
                .addEmail("prestador.app2016@gmail.com")
                .addGroup("Redes Sociais")
                .addFacebook("www.facebook.com/prestadorapp")
                .addInstagram("@prestadorapp")
                .addTwitter("@prestadorapp")
                .addYoutube("www.yputube.com/prestadorapp")
                .create();
        setContentView(sobre);
    }
}
