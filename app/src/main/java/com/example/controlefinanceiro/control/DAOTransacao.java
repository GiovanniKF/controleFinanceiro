package com.example.controlefinanceiro.control;

import androidx.annotation.NonNull;

import com.example.controlefinanceiro.models.Transacao;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAOTransacao {

    private DatabaseReference databaseReference;
    private String userId;

    public DAOTransacao() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Task<Void> add(Transacao T) {
        return databaseReference.child("transacao").child(userId).push().setValue(T);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child("transacao").child(userId).child(key).updateChildren(hashMap);
    }

    public Task<Void> delete(String key) {
        return databaseReference.child("transacao").child(userId).child(key).removeValue();
    }

    public Task<Void> deleteUser() {
        return databaseReference.child("transacao").child(userId).removeValue();
    }

    public Query selectAllOrderByDate() {
        return databaseReference.child("transacao").child(userId).orderByChild("timestamp");
    }

}
