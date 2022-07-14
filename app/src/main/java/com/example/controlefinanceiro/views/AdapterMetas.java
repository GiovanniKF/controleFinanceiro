package com.example.controlefinanceiro.views;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.control.DAOCarteira;
import com.example.controlefinanceiro.control.DAOMetas;
import com.example.controlefinanceiro.models.Metas;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterMetas extends FirebaseRecyclerAdapter<Metas, AdapterMetas.metasViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public AdapterMetas(@NonNull FirebaseRecyclerOptions<Metas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull metasViewHolder holder, int position, @NonNull Metas model) {

        Date data = new Date(model.getTimestampMeta());
        SimpleDateFormat dataFormato = new SimpleDateFormat("dd/MM/yyyy");
        String apenasData = dataFormato.format(data);

        holder.metaDescricao.setText(model.getMetaDescricao());
        holder.dataLimite.setText(apenasData);
        holder.valorAcum.setText("R$ " + String.format("%,.2f", model.getValorAcumulado()));
        holder.valorMeta.setText("R$ " + String.format("%,.2f", model.getValorMeta()));

        int finalPosition = holder.getAbsoluteAdapterPosition();

        DAOMetas daoMetas = new DAOMetas();
        DAOCarteira daoCarteira = new DAOCarteira();

        holder.btnDepositar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.dialog_depositar_meta,null, false);
                final EditText edtValorDeposito = view1.findViewById(R.id.edtValorDeposito);
                TextView txtMetaDesc = view1.findViewById(R.id.txtMetaDesc);
                TextView txtValorAcum = view1.findViewById(R.id.txtValorAcum);
                Button btnConfDeposito = view1.findViewById(R.id.btnConfDeposito);

                txtMetaDesc.setText(holder.metaDescricao.getText().toString());
                txtValorAcum.setText(holder.valorAcum.getText().toString());

                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(holder.itemView.getContext());
                builder.setView(view1);

                final AlertDialog dialog = builder.create();
                dialog.show();

                btnConfDeposito.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valorDeposito = edtValorDeposito.getText().toString();

                        if(TextUtils.isEmpty(valorDeposito)) {
                            Toast.makeText(holder.itemView.getContext(), "Valor do depósito não pode ser vazio.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        dialog.dismiss();
                        daoMetas.atualizaMeta(valorDeposito, getRef(finalPosition).getKey(), holder.itemView.getContext());
                        daoCarteira.depositoFeito(valorDeposito);
                    }
                });
            }
        });

        holder.btnExcluirMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(holder.metaDescricao.getContext());
                builder.setTitle("Você tem certeza?");
                builder.setMessage("Esta ação não pode ser desfeita.");

                builder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        daoMetas.delete(getRef(finalPosition).getKey());
                        daoCarteira.atualiza(model.getValorAcumulado());
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.metaDescricao.getContext(), "Ação cancelada.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public metasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_metas, parent, false);
        return new metasViewHolder(view);
    }

    class metasViewHolder extends RecyclerView.ViewHolder {
        TextView metaDescricao, dataLimite, valorAcum, valorMeta;

        Button btnDepositar, btnExcluirMeta;


        public metasViewHolder(@NonNull View itemView) {
            super(itemView);

            metaDescricao = itemView.findViewById(R.id.txtMetaDescricao);
            dataLimite = itemView.findViewById(R.id.txtDataLimite);
            valorAcum = itemView.findViewById(R.id.txtValorAcum);
            valorMeta = itemView.findViewById(R.id.txtValorMeta);
            btnDepositar = itemView.findViewById(R.id.btnDepositar);
            btnExcluirMeta = itemView.findViewById(R.id.btnExcluirMeta);

        }
    }
}
