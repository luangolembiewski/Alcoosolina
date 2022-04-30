package com.example.alcoosolina;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class CalcMedia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_media);
    }
    public void calcularMedia(View v){
        EditText litros = (EditText) findViewById(R.id.litros);
        EditText kmPercorridos = (EditText) findViewById(R.id.kmPercorridos);
        RadioButton rdGasolina = (RadioButton) findViewById(R.id.rdGasolina);
        RadioButton rdAlcool = (RadioButton) findViewById(R.id.rdAlcool);
        TextView resGasolina = (TextView) findViewById(R.id.resGasolina);
        TextView resAlcool =(TextView) findViewById(R.id.resAlcool);

        Double kmL = Double.parseDouble(String.valueOf(kmPercorridos))/(Double.parseDouble(String.valueOf(litros)));

        if(!rdGasolina.isChecked() && !rdAlcool.isChecked()){
            AlertDialog.Builder alerta;
            alerta = new AlertDialog.Builder(CalcMedia.this);
            alerta.setTitle("Opção invalida!");
            alerta.setMessage("Escolha uma opção!");
            alerta.show();
        }else{
            if(rdGasolina.isChecked()){
                resGasolina.setText("Gasolina");
            }
            if(rdAlcool.isChecked()){
                resAlcool.setText("Alcool");
            }
        }
    }
    public void trocar(View view){
        Intent intent1 = new Intent(getApplicationContext(), CalcCustoBeneficio.class);
        startActivity(intent1);
    }


}
