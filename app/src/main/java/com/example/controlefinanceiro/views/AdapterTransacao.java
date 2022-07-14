package com.example.controlefinanceiro.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOCarteira;
import com.example.controlefinanceiro.control.DAOTransacao;
import com.example.controlefinanceiro.models.Transacao;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AdapterTransacao extends FirebaseRecyclerAdapter<Transacao, AdapterTransacao.transacaoViewHolder > {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *  @param options
     * */

    public AdapterTransacao(@NonNull FirebaseRecyclerOptions<Transacao> options) {
        super(options);
    }

    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder(@NonNull transacaoViewHolder holder, int position, @NonNull Transacao model) {
        holder.descricao.setText(model.getDescricao());

        Date data = new Date(model.getTimestamp());
        SimpleDateFormat dataFormato = new SimpleDateFormat("dd/MM/yyyy");
        String apenasData = dataFormato.format(data);

        holder.data.setText(apenasData);

        Double valor = model.getValor();
        if (valor > 0) {
            holder.valor.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.valor.setTextColor(Color.parseColor("#DD2214"));
        }

        holder.valor.setText("R$ " + String.format("%,.2f", valor));

        int finalPosition = holder.getAbsoluteAdapterPosition();

        DAOTransacao daoTransacao = new DAOTransacao();
        DAOCarteira daoCarteira = new DAOCarteira();

        holder.btnAttTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemView.getContext())
                        .setContentHolder(new ViewHolder(R.layout.activity_upd_del_transacao))
                        .setExpanded(true, 1200)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText valor = view.findViewById(R.id.edtValor);
                EditText descricao = view.findViewById(R.id.edtDescricao);
                EditText data = view.findViewById(R.id.edtData);

                Button btnUpdate = view.findViewById(R.id.btnConfirmaAtt);

                valor.setText(model.getValor().toString());
                descricao.setText(model.getDescricao());
                data.setText(apenasData);

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long milliseconds = 0;

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date date = dateFormat.parse(data.getText().toString());
                            milliseconds = date.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("valor", Double.parseDouble(valor.getText().toString()));
                        map.put("descricao", descricao.getText().toString());
                        map.put("timestamp", milliseconds);

                        daoTransacao.update(getRef(finalPosition).getKey(), map).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 daoCarteira.atualiza(Double.parseDouble(valor.getText().toString()));
                                 Toast.makeText(holder.descricao.getContext(), "Transação atualizada.", Toast.LENGTH_SHORT).show();
                                 dialogPlus.dismiss();
                             }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.descricao.getContext(), "Erro ao tentar atualizar.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        holder.btnDelTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(holder.descricao.getContext());
                builder.setTitle("Você tem certeza?");
                builder.setMessage("Esta ação não pode ser desfeita.");

                builder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        daoTransacao.delete(getRef(finalPosition).getKey());
                        daoCarteira.atualizaDel(valor, model.getTipo());
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.descricao.getContext(), "Ação cancelada.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public transacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transacoes, parent, false);
        return new transacaoViewHolder(view);
    }

    public class transacaoViewHolder extends RecyclerView.ViewHolder {
        TextView descricao, data, valor;

        ImageButton btnAttTran, btnDelTran;

        public transacaoViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.txtDescricao);
            data = itemView.findViewById(R.id.txtData);
            valor = itemView.findViewById(R.id.txtValor);
            btnAttTran = itemView.findViewById(R.id.btnAttTran);
            btnDelTran = itemView.findViewById(R.id.btnDelTran);
        }
    }

}
