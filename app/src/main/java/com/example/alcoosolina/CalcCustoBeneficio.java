package com.example.alcoosolina;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CalcCustoBeneficio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_custo_beneficio);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView mediaGasolina =(TextView) findViewById(R.id.mediaGasolina);
        TextView mediaAlcool = (TextView) findViewById(R.id.mediaAlcool);

        Bundle extras = getIntent().getExtras();
        Double gasolina = extras.getDouble("gasolina");
        Double alcool = extras.getDouble("alcool");

        mediaGasolina.setText(String.valueOf(gasolina));
        mediaAlcool.setText(String.valueOf(alcool));
    }
    public void calcularCusto(View view){
        TextView mediaGasolina =(TextView) findViewById(R.id.mediaGasolina);
        TextView mediaAlcool = (TextView) findViewById(R.id.mediaAlcool);
        TextView resCusto = (TextView) findViewById(R.id.resCusto);
        EditText precoGasolina =(EditText) findViewById(R.id.precoGasolina);
        EditText precoAlcool = (EditText) findViewById(R.id.precoAlcool);
        try {
            Double valMediaGasolina = Double.parseDouble(String.valueOf(mediaGasolina.getText()));
            Double valMediaAlcool = Double.parseDouble(String.valueOf(mediaAlcool.getText()));
            Double valPrecoGasolina = Double.parseDouble(String.valueOf(precoGasolina.getText()));
            Double valPrecoAlcool = Double.parseDouble(String.valueOf(precoAlcool.getText()));
            Double kmRealGasolina = valMediaGasolina/valPrecoGasolina;
            Double kmRealAlcool = valMediaAlcool/valPrecoAlcool;

            if(kmRealGasolina > kmRealAlcool) {
                resCusto.setText("Gasolina");
            }else if(kmRealGasolina < kmRealAlcool){
                resCusto.setText("Álcool");
            }else{
                resCusto.setText("Gasto Igual");
            }

        }catch (Exception ex) {
            AlertDialog.Builder alerta;
            alerta = new AlertDialog.Builder(CalcCustoBeneficio.this);
            alerta.setTitle("Caracteres Inválidos!");
            alerta.setMessage("Preencha todos os campos com números");
            alerta.setNeutralButton("Ok", null);
            alerta.show();
        }
    }

    public void limparPrecoAlcool(View view){
        EditText precoAlcool = (EditText) findViewById(R.id.precoAlcool);
        TextView resCusto = (TextView) findViewById(R.id.resCusto);
        precoAlcool.setText(String.valueOf(""));
        resCusto.setText("");
    }
    public void limparPrecoGasolina(View view){
        EditText precoGasolina =(EditText) findViewById(R.id.precoGasolina);
        TextView resCusto = (TextView) findViewById(R.id.resCusto);
        precoGasolina.setText(String.valueOf(""));
        resCusto.setText("");
    }
    public void voltarInicio(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}