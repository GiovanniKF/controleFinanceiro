package com.example.controlefinanceiro.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOCartao;
import com.example.controlefinanceiro.models.Cartao;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class cartoes extends AppCompatActivity {

    private FloatingActionButton fabAddTransacao;
    private BottomNavigationView bottomNavMenu;
    private Button btnAddCartao;
    private RecyclerView recyclerView;
    private AdapterCartao adapterCartao;
    private DAOCartao daoCartao = new DAOCartao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoes);

        btnAddCartao = findViewById(R.id.btnAddCartao);
        fabAddTransacao = findViewById(R.id.fabAddTransacao);
        bottomNavMenu = findViewById(R.id.bottomNavMenu);
        bottomNavMenu.setSelectedItemId(R.id.miCartao);

        bottomNavMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.miHome:
                    finish();
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.miCartao:
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

        recyclerView = findViewById(R.id.lstCartao);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Cartao> options =
                new FirebaseRecyclerOptions.Builder<Cartao>()
                        .setQuery(daoCartao.selectAll(), Cartao.class).build();

        adapterCartao = new AdapterCartao(options);
        recyclerView.setAdapter(adapterCartao);

        btnAddCartao.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(cartoes.this, addCartao.class));
        });

        fabAddTransacao.setOnClickListener(view -> {
            startActivity(new Intent(cartoes.this, addTransacao.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterCartao.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterCartao.stopListening();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}