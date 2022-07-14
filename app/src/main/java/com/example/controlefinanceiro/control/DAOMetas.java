package com.example.controlefinanceiro.control;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.controlefinanceiro.models.Metas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DAOMetas {

    private DatabaseReference databaseReference;
    private String userId;

    public DAOMetas() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Task<Void> add(Metas metas) {
        return databaseReference.child("metas").child(userId).push().setValue(metas);
    }

    public Task<Void> delete(String key) {
        return databaseReference.child("metas").child(userId).child(key).removeValue();
    }

    public Task<Void> deleteUser() {
        return databaseReference.child("metas").child(userId).removeValue();
    }

    public Query selectAllOrderByDate() {
        return databaseReference.child("metas").child(userId).orderByChild("timestampMeta");
    }

    public Task<Void> atualizaMeta(String valorDeposito, String key, Context context) {
        Double valor = Double.parseDouble(valorDeposito);

        databaseReference.child("metas").child(userId).child(key)
            .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double valorAcumulado = snapshot.child("valorAcumulado").getValue(Double.class);

                Double novoValor = valor + valorAcumulado;

                databaseReference.child("metas").child(userId).child(key).child("valorAcumulado").setValue(novoValor);

                Toast.makeText(context, "Depósito feito com sucesso.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Ocorreu um erro durante o depósito.", Toast.LENGTH_SHORT).show();
            }
        });

        return null;
    }
}
