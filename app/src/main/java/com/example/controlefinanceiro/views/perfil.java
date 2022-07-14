package com.example.controlefinanceiro.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.controlefinanceiro.MainActivity;
import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOMetas;
import com.example.controlefinanceiro.control.DAOTransacao;
import com.example.controlefinanceiro.control.DAOUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;

public class perfil extends AppCompatActivity {

    private EditText edtNomePerfil, edtEmailPerfil;
    private ImageButton btnAltNome, btnAltEmail;
    private Button btnAlteraSenha, btnExcluiConta, btnAlteraPerfil;
    private FloatingActionButton fabAddTransacao;
    private BottomNavigationView bottomNavMenu;
    public DAOUsuario daoUsuario = new DAOUsuario();
    public DAOTransacao daoTransacao = new DAOTransacao();
    public DAOMetas daoMetas = new DAOMetas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        edtNomePerfil = findViewById(R.id.edtNomePerfil);
        edtEmailPerfil = findViewById(R.id.edtEmailPerfil);
        btnAltNome = findViewById(R.id.btnAltNome);
        btnAltEmail = findViewById(R.id.btnAltEmail);
        btnAlteraSenha = findViewById(R.id.btnAlteraSenha);
        btnExcluiConta = findViewById(R.id.btnExcluiConta);
        btnAlteraPerfil = findViewById(R.id.btnAlteraPerfil);
        fabAddTransacao = findViewById(R.id.fabAddTransacao);
        bottomNavMenu = findViewById(R.id.bottomNavMenu);
        bottomNavMenu.setSelectedItemId(R.id.miPerfil);

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
                    finish();
                    startActivity(new Intent(getApplicationContext(), metas.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.miPerfil:
                    return true;
            }

            return false;
        });

        daoUsuario.buscaNomeAlt(edtNomePerfil);
        edtEmailPerfil.setText(daoUsuario.buscaEmailAlt());

        //Botões para alterar Nome e Email
        btnAltNome.setOnClickListener(view -> {
            if (edtNomePerfil.isEnabled() == false) {
                edtNomePerfil.setEnabled(true);
            } else {
                edtNomePerfil.setEnabled(false);
            }
        });
        btnAltEmail.setOnClickListener(view -> {
            if (edtEmailPerfil.isEnabled() == false) {
                edtEmailPerfil.setEnabled(true);
            } else {
                edtEmailPerfil.setEnabled(false);
            }
        });

        //Alterando campos mostra botão para alterar perfil
        edtNomePerfil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnAlteraPerfil.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edtNomePerfil.isEnabled()) {
                    btnAlteraPerfil.setVisibility(View.VISIBLE);
                } else {
                    btnAlteraPerfil.setVisibility(View.INVISIBLE);
                }
            }
        });
        edtEmailPerfil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                  btnAlteraPerfil.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edtEmailPerfil.isEnabled()) {
                    btnAlteraPerfil.setVisibility(View.VISIBLE);
                } else {
                    btnAlteraPerfil.setVisibility(View.INVISIBLE);
                }
            }
        });

        fabAddTransacao.setOnClickListener(view -> {
            startActivity(new Intent(perfil.this, addTransacao.class));
        });

        btnAlteraSenha.setOnClickListener(view -> {
            abreDialogSenha();
        });

        btnAlteraPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtNomePerfil.getText().toString()) || TextUtils.isEmpty(edtEmailPerfil.getText().toString())) {
                    Toast.makeText(perfil.this, "Verifique os campos vazios!", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("nome", edtNomePerfil.getText().toString());
                    hashMap.put("email", edtEmailPerfil.getText().toString());

                    daoUsuario.update(hashMap);
                    Toast.makeText(perfil.this, "Perfil alterado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(perfil.this, home.class));
                }
            }
        });

        btnExcluiConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(perfil.this);
                builder.setTitle("Você tem certeza?");
                builder.setMessage("Esta ação não pode ser desfeita.");

                builder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        daoMetas.deleteUser();
                        daoTransacao.deleteUser();
                        daoUsuario.remove().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                startActivity(new Intent(perfil.this, MainActivity.class));
                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(perfil.this, "Ação cancelada.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    private void abreDialogSenha() {
        View view = LayoutInflater.from(perfil.this).inflate(R.layout.dialog_altera_senha, null);
        final EditText edtSenhaAtual = view.findViewById(R.id.edtSenhaAtual);
        final EditText edtNovaSenha = view.findViewById(R.id.edtNovaSenha);
        Button btnConfSenha = view.findViewById(R.id.btnConfSenha);

        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(perfil.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnConfSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String senhaAtual = edtSenhaAtual.getText().toString();
                String novaSenha = edtNovaSenha.getText().toString();

                if(TextUtils.isEmpty(senhaAtual) || TextUtils.isEmpty(novaSenha)) {
                    Toast.makeText(perfil.this, "Preencha os campos vazios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (novaSenha.length() < 6) {
                    Toast.makeText(perfil.this, "A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                daoUsuario.alteraSenha(senhaAtual, novaSenha, perfil.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}