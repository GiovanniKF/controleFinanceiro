package com.example.controlefinanceiro.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOMetas;
import com.example.controlefinanceiro.models.Metas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addMetas extends AppCompatActivity {

    private EditText edtDescricao, edtValorMeta, edtDataMeta;
    private Button btnSalvarMeta;
    private Metas metas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_metas);

        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtValorMeta = (EditText) findViewById(R.id.edtValorMeta);
        edtDataMeta = (EditText) findViewById(R.id.edtDataMeta);
        btnSalvarMeta = (Button) findViewById(R.id.btnSalvarMeta);

        DAOMetas daoMetas = new DAOMetas();

        btnSalvarMeta.setOnClickListener(view -> {
            if (edtDescricao.getText().toString().equals("") || edtValorMeta.getText().toString().equals("") || edtDataMeta.getText().toString().equals("") ) {
                Toast.makeText(addMetas.this, "Você deve preencher todos os dados", Toast.LENGTH_SHORT).show();
            } else {
                guardaDados();
                daoMetas.add(metas);
                startActivity(new Intent(addMetas.this, com.example.controlefinanceiro.views.metas.class));
            }
        });

    }

    private void guardaDados() {
        long data = dataToMilliseconds();

        metas = new Metas();
        metas.setMetaDescricao(edtDescricao.getText().toString());
        metas.setValorAcumulado(0.0);
        metas.setValorMeta(Double.valueOf(edtValorMeta.getText().toString()));
        metas.setTimestampMeta(data);
    }

    private long dataToMilliseconds() {
        long milliseconds = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(edtDataMeta.getText().toString());
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(addMetas.this, metas.class));
    }
}