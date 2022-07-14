package com.example.controlefinanceiro.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOCarteira;
import com.example.controlefinanceiro.control.DAOUsuario;
import com.example.controlefinanceiro.models.Carteira;
import com.example.controlefinanceiro.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class cadastrar extends AppCompatActivity {

    private EditText edtNome, edtSenha, edtConfSenha, edtEmail;
    private Button btnConfirmar;
    private FirebaseAuth Auth;
    private FirebaseUser user;
    private Usuario u;
    private Carteira c;
    private DAOUsuario daoUsuario = new DAOUsuario();
    private DAOCarteira daoCarteira = new DAOCarteira();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNome = findViewById(R.id.edtNome);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfSenha = findViewById(R.id.edtConfSenha);
        edtEmail = findViewById(R.id.edtEmail);
        btnConfirmar = findViewById(R.id.btnConfirmar);

        Auth = FirebaseAuth.getInstance();

        btnConfirmar.setOnClickListener(view -> {
            if(edtNome.getText().toString().equals("") ||
                    edtEmail.getText().toString().equals("") ||
                    edtSenha.getText().toString().equals("") ||
                    edtConfSenha.getText().toString().equals("")) {
                Toast.makeText(cadastrar.this, "Você deve preencher todos os dados", Toast.LENGTH_SHORT).show();
            } else if (!edtSenha.getText().toString().equals(edtConfSenha.getText().toString())) {
                Toast.makeText(cadastrar.this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
            } else if (edtSenha.getText().toString().length() < 6){
                Toast.makeText(cadastrar.this,"A senha deve ter no mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
            } else {
                guardaDados();
                Auth.createUserWithEmailAndPassword(u.getEmail(), u.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            u.setId(Auth.getUid());
                            daoUsuario.add(u);

                            montaCarteira();
                            daoCarteira.criaCarteira(c);

                            startActivity(new Intent(cadastrar.this, home.class));
                            finish();
                        } else {
                            Toast.makeText(cadastrar.this, "Erro ao tentar se cadastrar.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void guardaDados() {
        u = new Usuario();
        u.setNome(edtNome.getText().toString());
        u.setSenha(edtSenha.getText().toString());
        u.setEmail(edtEmail.getText().toString());
    }

    private void montaCarteira() {
        c = new Carteira();
        c.setUsuarioID(Auth.getUid());
        c.setSaldo(00.00);
    }

}