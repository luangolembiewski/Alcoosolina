package com.example.alcoosolina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class CalcMedia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_media);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void calcularMedia(View v){
        EditText litros = (EditText) findViewById(R.id.litros);
        EditText kmPercorridos = (EditText) findViewById(R.id.kmPercorridos);
        RadioButton rdGasolina = (RadioButton) findViewById(R.id.rdGasolina);
        RadioButton rdAlcool = (RadioButton) findViewById(R.id.rdAlcool);
        TextView resGasolina = (TextView) findViewById(R.id.resGasolina);
        TextView resAlcool =(TextView) findViewById(R.id.resAlcool);
        try {
            Double valKmPercorridos = Double.parseDouble(String.valueOf(kmPercorridos.getText()));
            Double valLitros = Double.parseDouble(String.valueOf(litros.getText()));

            if(!rdGasolina.isChecked() && !rdAlcool.isChecked()){
                AlertDialog.Builder alerta;
                alerta = new AlertDialog.Builder(CalcMedia.this);
                alerta.setTitle("Opção invalida!");
                alerta.setMessage("Escolha uma opção!");
                alerta.setNeutralButton("Ok", null);
                alerta.show();
            }else{
                if(valKmPercorridos > valLitros) {
                    Double kmL = valKmPercorridos/valLitros;
                    kmL = (Double) (Math.round(kmL*100.0)/100.0);
                    if(rdGasolina.isChecked()){
                        resGasolina.setText(String.valueOf(kmL));
                        rdGasolina.setChecked(false);
                    }
                    if(rdAlcool.isChecked()){
                        resAlcool.setText(String.valueOf(kmL));
                        rdAlcool.setChecked(false);
                    }
                }else{
                    AlertDialog.Builder alerta;
                    alerta = new AlertDialog.Builder(CalcMedia.this);
                    alerta.setTitle("Dados inválidos!");
                    alerta.setMessage("Quilometros percorridos é menor que Litros de combustivel!");
                    alerta.setNeutralButton("Ok", null);
                    alerta.show();
                }

            }
        }catch (Exception ex){
            AlertDialog.Builder alerta;
            alerta = new AlertDialog.Builder(CalcMedia.this);
            alerta.setTitle("Caracteres Inválidos!");
            alerta.setMessage("Preencha todos os campos com números");
            alerta.setNeutralButton("Ok", null);
            alerta.show();
        }

    }
    public void limparAlcool(View view){
        RadioButton rdAlcool = (RadioButton) findViewById(R.id.rdAlcool);
        TextView resAlcool =(TextView) findViewById(R.id.resAlcool);
        resAlcool.setText(String.valueOf(""));
        rdAlcool.setChecked(false);
    }
    public void limparGasolina(View view){
        RadioButton rdGasolina = (RadioButton) findViewById(R.id.rdGasolina);
        TextView resGasolina = (TextView) findViewById(R.id.resGasolina);
        resGasolina.setText(String.valueOf(""));
        rdGasolina.setChecked(false);
    }
    public void trocar(View view){
        TextView resGasolina = (TextView) findViewById(R.id.resGasolina);
        TextView resAlcool =(TextView) findViewById(R.id.resAlcool);
        try {
            Double valResGasolina = Double.parseDouble(String.valueOf(resGasolina.getText()));
            Double valResAlcool = Double.parseDouble(String.valueOf(resAlcool.getText()));

            if(!valResGasolina.isNaN() && !valResAlcool.isNaN()){
                Intent intent = new Intent(CalcMedia.this, CalcCustoBeneficio.class);
                intent.putExtra("gasolina", valResGasolina);
                intent.putExtra("alcool", valResAlcool);
                startActivity(intent);
            }
        }catch (Exception ex){
            AlertDialog.Builder alerta;
            alerta = new AlertDialog.Builder(CalcMedia.this);
            alerta.setTitle("Erro!");
            alerta.setMessage("Obrigatório inserir duas médias");
            alerta.setNeutralButton("Ok", null);
            alerta.show();
        }

    }


}
