package com.example.alcoosolina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicial);
    }

    public void calculoMedia(View v){
        setContentView(R.layout.calculo_media);
    }


    public void postoProximo(View v){
        setContentView(R.layout.mapa);
    }

}