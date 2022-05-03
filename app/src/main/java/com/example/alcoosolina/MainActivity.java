package com.example.alcoosolina;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicial);
    }

    public void calculoMedia(View view){
        Intent calculoM = new Intent(this, CalcMedia.class);
        startActivity(calculoM);
    }


    public void postoProximo(View v){

    }


}