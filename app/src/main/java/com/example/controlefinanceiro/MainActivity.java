package com.example.controlefinanceiro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlefinanceiro.models.Usuario;
import com.example.controlefinanceiro.views.home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private TextView txtCadastrar;
    private Button btnEntrar;
    private FirebaseAuth Auth;
    private FirebaseUser user;
    private Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        confereUsuario();

        Auth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        txtCadastrar = findViewById(R.id.txtCadastrar);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(view -> {
            if(edtEmail.getText().toString().equals("") || edtSenha.getText().toString().equals("") ) {
                Toast.makeText(MainActivity.this, "Há campos vazios!", Toast.LENGTH_SHORT).show();
            } else {
                receberDados();
                logar();
            }
        });

        txtCadastrar.setOnClickListener(view -> {
            Intent cadastrar = new Intent(MainActivity.this, com.example.controlefinanceiro.views.cadastrar.class);
            MainActivity.this.startActivity(cadastrar);
        });
    }

    private void logar() {
        Auth.signInWithEmailAndPassword(u.getEmail(), u.getSenha())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        user = Auth.getCurrentUser();

                        Intent intent = new Intent(MainActivity.this, home.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Autenticação falhou. Verifique os dados inseridos.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void confereUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, home.class));
            finish();
        } else {
            return;
        }
    }

    private void receberDados() {
        u = new Usuario();
        u.setEmail(edtEmail.getText().toString());
        u.setSenha(edtSenha.getText().toString());
    }
}