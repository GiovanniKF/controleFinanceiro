package com.example.controlefinanceiro.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlefinanceiro.MainActivity;
import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOCarteira;
import com.example.controlefinanceiro.control.DAOTransacao;
import com.example.controlefinanceiro.control.DAOUsuario;
import com.example.controlefinanceiro.models.Carteira;
import com.example.controlefinanceiro.models.Transacao;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class home extends AppCompatActivity {

    private TextView txtOlaUser, txtSaldo;
    private ImageButton btnLogout;
    private FloatingActionButton fabAddTransacao;
    private BottomNavigationView bottomNavMenu;
    private RecyclerView recyclerView;
    private AdapterTransacao adapterTransacao;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private CardView viewCartao;
    private Carteira c;
    public DAOTransacao daoTransacao = new DAOTransacao();
    public DAOUsuario daoUsuario = new DAOUsuario();
    public DAOCarteira daoCarteira = new DAOCarteira();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogout = findViewById(R.id.btnLogout);
        txtOlaUser = findViewById(R.id.txtOlaUser);
        txtSaldo = findViewById(R.id.txtSaldo);
        fabAddTransacao = findViewById(R.id.fabAddTransacao);
        bottomNavMenu = findViewById(R.id.bottomNavMenu);
        viewCartao = findViewById(R.id.viewCartao);
        bottomNavMenu.setSelectedItemId(R.id.miHome);

        bottomNavMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.miHome:
                    return true;

                case R.id.miCartao:
                    finish();
                    startActivity(new Intent(getApplicationContext(), cartoes.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.miMetas:
                    finish();
                    startActivity(new Intent(getApplicationContext(), metas.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.miPerfil:
                    finish();
                    startActivity(new Intent(getApplicationContext(), perfil.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });

        daoUsuario.buscaNome(txtOlaUser);
        daoCarteira.buscaSaldo(txtSaldo);

        recyclerView = findViewById(R.id.lstTransacoes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Transacao> options =
                new FirebaseRecyclerOptions.Builder<Transacao>()
                        .setQuery(daoTransacao.selectAllOrderByDate(), Transacao.class).build();

        adapterTransacao = new AdapterTransacao(options);
        recyclerView.setAdapter(adapterTransacao);

        btnLogout.setOnClickListener(view -> msgSaida());

        fabAddTransacao.setOnClickListener(view -> {
            startActivity(new Intent(home.this, addTransacao.class));
        });

        viewCartao.setOnClickListener(view -> {
            abreDialogSaldo();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterTransacao.startListening();

        FirebaseUser user = Auth.getCurrentUser();
        if (Auth != null) {

        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterTransacao.stopListening();
    }

    @Override
    public void onBackPressed() {
        msgSaida();
    }

    private void msgSaida() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(home.this);

        builder.setMessage("Você deseja sair?");
        builder.setTitle("Alerta!");
        builder.setCancelable(false);

        builder.setPositiveButton("Sim", (dialog, which) -> {
            Auth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });

        builder.setNegativeButton("Não", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void abreDialogSaldo() {
        View view = LayoutInflater.from(home.this).inflate(R.layout.dialog_altera_saldo, null);
        final EditText edtNovoSaldo = view.findViewById(R.id.edtNovoSaldo);
        TextView txtSaldoAtual = view.findViewById(R.id.txtSaldoAtual);
        Button btnConfSaldo = view.findViewById(R.id.btnConfSaldo);

        txtSaldoAtual.setText(txtSaldo.getText().toString());

        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(home.this);
        builder.setView(view);

        final androidx.appcompat.app.AlertDialog dialog = builder.create();
        builder.show();

        btnConfSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String novoSaldo = edtNovoSaldo.getText().toString();

                if(TextUtils.isEmpty(novoSaldo)) {
                    Toast.makeText(home.this, "Novo saldo não pode ser vazio.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                daoCarteira.atualizaSaldo(novoSaldo, home.this);
                home.this.recreate();
            }
        });
    }

}