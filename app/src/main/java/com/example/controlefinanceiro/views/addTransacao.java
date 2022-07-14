package com.example.controlefinanceiro.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOCartao;
import com.example.controlefinanceiro.control.DAOCarteira;
import com.example.controlefinanceiro.control.DAOTransacao;
import com.example.controlefinanceiro.models.Carteira;
import com.example.controlefinanceiro.models.Transacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class addTransacao extends AppCompatActivity {

    private RadioGroup rdgTipo;
    private RadioButton rdbReceita, rdbDespesa;
    private TextView txtSelecionaCartao;
    private EditText edtValor, edtData, edtDescricao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public String userId = user.getUid();
    private Spinner spiPagamento, spiCartao;
    private Button btnSalvarTran;
    private Transacao transacao;
    private ArrayList<String> arrayList = new ArrayList<>();
    public Double valorAtt;
    public String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transacao);

        rdgTipo = findViewById(R.id.rdgTipo);
        rdbReceita = findViewById(R.id.rdbReceita);
        rdbDespesa = findViewById(R.id.rdbDespesa);
        txtSelecionaCartao = findViewById(R.id.txtSelecionaCartao);
        edtValor = findViewById(R.id.edtValor);
        edtData = findViewById(R.id.edtData);
        edtDescricao = findViewById(R.id.edtDescricao);
        spiPagamento = findViewById(R.id.spiPagamento);
        spiCartao = findViewById(R.id.spiCartao);
        btnSalvarTran = findViewById(R.id.btnSalvarTran);

        DAOTransacao daoTransacao = new DAOTransacao();
        DAOCarteira daoCarteira = new DAOCarteira();
        DAOCartao daoCartao = new DAOCartao();

        //Pega string do tipo da transação
        int selecionado = rdgTipo.getCheckedRadioButtonId();
        RadioButton rbSelecionado = findViewById(selecionado);
        tipo = rbSelecionado.getText().toString();

        //Spinner de tipo de pagamentos
        ArrayAdapter<CharSequence> pagamentos = ArrayAdapter.createFromResource
                (this, R.array.pagamentos, android.R.layout.simple_spinner_dropdown_item);
        pagamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiPagamento.setAdapter(pagamentos);

        //Spinner de cartão
        buscaCartoes();

        spiPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spiPagamento.getSelectedItem().toString().equals("Dinheiro")) {
                    txtSelecionaCartao.setVisibility(View.INVISIBLE);
                    spiCartao.setVisibility(View.INVISIBLE);
                } else {
                    txtSelecionaCartao.setVisibility(View.VISIBLE);
                    spiCartao.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSalvarTran.setOnClickListener(view -> {
            if (edtValor.getText().toString().equals("") || edtData.getText().toString().equals("") || edtDescricao.getText().toString().equals("")) {
                Toast.makeText(addTransacao.this, "Você deve preencher todos os dados", Toast.LENGTH_SHORT).show();
            }
            else if (spiPagamento.getSelectedItem().toString().equals("Cartão (Crédito)") && rdbDespesa.isChecked()) {
                Toast.makeText(this, "Não é permitido adicionar crédito.", Toast.LENGTH_SHORT).show();
            } else {
                guardaDadosT();
                daoTransacao.add(transacao);

                if (spiPagamento.getSelectedItem().toString().equals("Dinheiro")) {
                    //Atualiza saldo atual na carteira
                    daoCarteira.atualiza(valorAtt);
                } else {
                    //Atualiza saldo do cartão selecionado
                    daoCartao.atualizaCartao(valorAtt, spiPagamento.getSelectedItem().toString(),spiCartao.getSelectedItem().toString(), this);
                }

                finish();
                startActivity(new Intent(addTransacao.this, home.class));
            }
        });

    }

    private void guardaDadosT() {
        converteValor();
        long data = dataToMilliseconds();

        transacao = new Transacao();
        transacao.setUsuarioID(userId);
        transacao.setTipo(tipo);
        transacao.setValor(valorAtt);
        transacao.setDescricao(edtDescricao.getText().toString());
        transacao.setPagamento(spiPagamento.getSelectedItem().toString());
        transacao.setCartaoPagamento(spiCartao.getSelectedItem().toString());
        transacao.setTimestamp(data);
    }

    private long dataToMilliseconds() {
        long milliseconds = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(edtData.getText().toString());
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }

    private void converteValor() {
        //Se for despesa converte o valor para negativo
        String valorString = "";
        if (rdbDespesa.isChecked()) {
            valorString = "-" + edtValor.getText().toString();

            valorAtt = Double.parseDouble(valorString);
        } else {
            valorAtt = Double.parseDouble(edtValor.getText().toString());
        }
    }

    private void buscaCartoes() {
        databaseReference.child("usuarios").child(userId).child("carteira").child("cartoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    arrayList.add(item.child("cartaoID").getValue(String.class));
                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(addTransacao.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayList);
                spiCartao.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}