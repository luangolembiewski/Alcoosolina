package com.example.alcoosolina;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class CalculoMedia extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculo_media);
    }

    public void calcularMedia(View v){

    }
    public void calcularCusto(View v){
        setContentView(R.layout.calculo_custo);
    }
}
