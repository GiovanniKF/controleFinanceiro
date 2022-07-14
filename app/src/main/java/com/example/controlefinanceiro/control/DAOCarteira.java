package com.example.controlefinanceiro.control;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.controlefinanceiro.models.Carteira;
import com.example.controlefinanceiro.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAOCarteira {

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String userId;
    private Usuario u;

    public DAOCarteira() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = FirebaseAuth.getInstance().getUid();
    }

    public Task<Void> criaCarteira(Carteira c) {
        return databaseReference.child("usuarios").child(c.getUsuarioID()).child("carteira").setValue(c);
    }

    public Task<Void> atualiza(Double valor) {
        databaseReference.child("usuarios").child(userId).child("carteira")
            .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double saldoAtual = snapshot.child("saldo").getValue(Double.class);

                Double saldoNovo = saldoAtual + valor;

                databaseReference.child("usuarios").child(userId).child("carteira").child("saldo").setValue(saldoNovo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return null;
    }

    public Task<Void> atualizaDel(Double valor, String tipo) {
        databaseReference.child("usuarios").child(userId).child("carteira")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double saldoAtual = snapshot.child("saldo").getValue(Double.class);

                        Double saldoNovo = 0.0;

                        if (tipo == "Despesa") {
                            saldoNovo = saldoAtual + valor;
                        } else {
                            saldoNovo = saldoAtual - valor;
                        }

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("saldo", saldoNovo);

                        databaseReference.child("usuarios").child(userId).child("carteira").updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return null;
    }

    public String buscaSaldo(TextView txtSaldo) {
        databaseReference.child("usuarios").child(userId).child("carteira")
            .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double saldoAtual = snapshot.child("saldo").getValue(Double.class);

                String saldoFormatado = String.format("%,.2f", saldoAtual);

                txtSaldo.setText("R$ " + saldoFormatado);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return String.valueOf(txtSaldo);
    }

    public Task<Void> atualizaSaldo(String novoSaldo, Context context) {
        Double novoSaldoConv = Double.parseDouble(novoSaldo);

        databaseReference.child("usuarios").child(userId).child("carteira").child("saldo").setValue(novoSaldoConv)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Saldo alterado com sucesso.", Toast.LENGTH_SHORT).show();

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "e" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        return null;
    }

    public Task<Void> depositoFeito(String valor) {
        Double valorDeposito = Double.parseDouble(valor);

        databaseReference.child("usuarios").child(userId).child("carteira")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Double saldoAtual = snapshot.child("saldo").getValue(Double.class);

                    Double saldoNovo = saldoAtual - valorDeposito;

                    databaseReference.child("usuarios").child(userId).child("carteira").child("saldo").setValue(saldoNovo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        return null;
    }
}
