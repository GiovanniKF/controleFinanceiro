package com.example.controlefinanceiro.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOMetas;
import com.example.controlefinanceiro.models.Metas;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class metas extends AppCompatActivity {

    private FloatingActionButton fabAddTransacao;
    private BottomNavigationView bottomNavMenu;
    private Button btnAddMeta;
    private AdapterMetas adapterMetas;
    private RecyclerView recyclerView;
    private DAOMetas daoMetas = new DAOMetas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

        btnAddMeta = findViewById(R.id.btnAddMeta);
        fabAddTransacao = findViewById(R.id.fabAddTransacao);
        bottomNavMenu = findViewById(R.id.bottomNavMenu);
        bottomNavMenu.setSelectedItemId(R.id.miMetas);

        bottomNavMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.miHome:
                    finish();
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.miCartao:
                    finish();
                    startActivity(new Intent(getApplicationContext(), cartoes.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.miMetas:
                    return true;

                case R.id.miPerfil:
                    finish();
                    startActivity(new Intent(getApplicationContext(), perfil.class));
                    overridePendingTransition(0, 0);
                    return true;
            }

            return false;
        });

        recyclerView = findViewById(R.id.lstMetas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Metas> options =
                new FirebaseRecyclerOptions.Builder<Metas>()
                        .setQuery(daoMetas.selectAllOrderByDate(), Metas.class).build();

        adapterMetas = new AdapterMetas(options);
        recyclerView.setAdapter(adapterMetas);

        fabAddTransacao.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(metas.this, addTransacao.class));
        });

        btnAddMeta.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(metas.this, addMetas.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterMetas.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterMetas.stopListening();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}