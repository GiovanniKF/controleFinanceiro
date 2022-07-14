package com.example.controlefinanceiro.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOCartao;
import com.example.controlefinanceiro.models.Cartao;

public class addCartao extends AppCompatActivity {

    private EditText edtCartaoId, edtVlrGastoCred, edtValorCred, edtValorDeb;
    private Button btnSalvarCartao;
    private Cartao cartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cartao);

        edtCartaoId = findViewById(R.id.edtCartaoId);
        edtValorCred = findViewById(R.id.edtValorCred);
        edtVlrGastoCred = findViewById(R.id.edtVlrGastoCred);
        edtValorDeb = findViewById(R.id.edtValorDeb);
        btnSalvarCartao = findViewById(R.id.btnSalvarCartao);

        DAOCartao daoCartao = new DAOCartao();

        btnSalvarCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtCartaoId.getText().toString().equals("") || edtValorCred.getText().toString().equals("") ||
                        edtVlrGastoCred.getText().toString().equals("") || edtValorDeb.getText().toString().equals("") ) {
                    Toast.makeText(addCartao.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    guardaDados();
                    daoCartao.add(cartao);
                    finish();
                    startActivity(new Intent(addCartao.this, cartoes.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(addCartao.this, cartoes.class));
    }

    public void guardaDados() {
        cartao = new Cartao();
        cartao.setCartaoID(edtCartaoId.getText().toString());
        cartao.setCredito(Double.parseDouble(edtValorCred.getText().toString()));
        cartao.setCreditoGasto(Double.parseDouble(edtVlrGastoCred.getText().toString()));
        cartao.setDebito(Double.parseDouble(edtValorDeb.getText().toString()));
    }
}