package com.example.controlefinanceiro.control;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.controlefinanceiro.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAOUsuario {

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String userId;

    public DAOUsuario() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<Void> add(Usuario u) {
        return databaseReference.child("usuarios").child(u.getId()).setValue(u);
    }

    public Task<Void> update(HashMap<String, Object> hashMap) {
        return databaseReference.child("usuarios").child(userId).updateChildren(hashMap);
    }

    public Task<Void> remove() {
        databaseReference.child("usuarios").child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.delete();
            }
        });

        return null;
    }

    public EditText buscaNomeAlt(EditText edtNomeAlt) {
        userId = user.getUid();

        databaseReference.child("usuarios").child(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nome = snapshot.child("nome").getValue(String.class);

                    edtNomeAlt.setText(nome);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        return edtNomeAlt;
    }

    public String buscaEmailAlt() {
        return user.getEmail();
    }

    public String buscaNome(TextView txtOlaUser) {
        userId = user.getUid();

        databaseReference.child("usuarios").child(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nome = snapshot.child("nome").getValue(String.class);

                txtOlaUser.setText("Olá, " + nome + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return String.valueOf(txtOlaUser);
    }

    public Task<Void> alteraSenha(String senhaAtual, String novaSenha, Context context) {
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), senhaAtual);
        user.reauthenticate(authCredential)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    user.updatePassword(novaSenha).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Senha foi atualizada com sucesso.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        return null;
    }
}
