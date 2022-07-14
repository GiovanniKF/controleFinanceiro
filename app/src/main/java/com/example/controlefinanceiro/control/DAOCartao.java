package com.example.controlefinanceiro.control;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.controlefinanceiro.models.Cartao;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DAOCartao {

    private DatabaseReference databaseReference;
    private String userId;

    public DAOCartao() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference();
        userId = FirebaseAuth.getInstance().getUid();
    }

    public Task<Void> add(Cartao cartao) {
        return databaseReference.child("usuarios").child(userId).child("carteira").child("cartoes").child(cartao.getCartaoID()).setValue(cartao);
    }

    public Query selectAll() {
        return databaseReference.child("usuarios").child(userId).child("carteira").child("cartoes").orderByChild("cartaoID");
    }

    public Task<Void> atualizaCartao(Double valor, String tipoPag, String tipoCartao, Context context) {
        databaseReference.child("usuarios").child(userId).child("carteira").child("cartoes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (tipoPag.equals("Cartão (Débito)")) {
                    Double saldoAtual = snapshot.child(tipoCartao).child("debito").getValue(Double.class);

                    Double saldoNovo = saldoAtual + valor;

                    databaseReference.child("usuarios").child(userId).child("carteira").child("cartoes").child(tipoCartao).child("debito").setValue(saldoNovo);
               }

               if (tipoPag.equals("Cartão (Crédito)")) {
                    Double saldoAtual = snapshot.child(tipoCartao).child("credito").getValue(Double.class);

                    if (saldoAtual > valor) {
                        Toast.makeText(context, "Não é possível fazer essa transação, verifique o seu crédito.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Double saldoNovo = saldoAtual + valor;

                        databaseReference.child("usuarios").child(userId).child("carteira").child("cartoes").child(tipoCartao).child("creditoGasto").setValue(saldoNovo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return null;
    }
}
