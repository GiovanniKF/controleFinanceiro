package com.example.controlefinanceiro.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.models.Cartao;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterCartao extends FirebaseRecyclerAdapter<Cartao, AdapterCartao.cartaoViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public AdapterCartao(@NonNull FirebaseRecyclerOptions<Cartao> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull cartaoViewHolder holder, int position, @NonNull Cartao model) {
        Double credito = model.getCredito();
        Double creditoGasto = model.getCreditoGasto();

        Double creditoDisp = credito - creditoGasto;

        holder.cartaoID.setText(model.getCartaoID());
        holder.credito.setText("R$ " + String.format("%,.2f",creditoDisp));
        holder.debito.setText("R$ " + String.format("%,.2f",model.getDebito()));
    }

    @NonNull
    @Override
    public cartaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cartao, parent, false);
        return new cartaoViewHolder(view);
    }

    class cartaoViewHolder extends RecyclerView.ViewHolder {
        TextView cartaoID, credito, debito;

        public cartaoViewHolder(@NonNull View itemView) {
            super(itemView);

            cartaoID = itemView.findViewById(R.id.txtCartaoID);
            credito = itemView.findViewById(R.id.txtCredito);
            debito = itemView.findViewById(R.id.txtDebito);

        }
    }

}
