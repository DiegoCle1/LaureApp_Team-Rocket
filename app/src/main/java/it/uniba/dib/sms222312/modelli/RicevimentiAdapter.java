package it.uniba.dib.sms222312.modelli;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.uniba.dib.sms222312.R;

public class RicevimentiAdapter extends RecyclerView.Adapter<RicevimentiAdapter.myViewHolder> {

    Context context;
    ArrayList<Ricevimento> ricevimentoArrayList;

    public RicevimentiAdapter(Context context, ArrayList<Ricevimento> ricevimentoArrayList) {
        this.context = context;
        this.ricevimentoArrayList = ricevimentoArrayList;
    }

    @NonNull
    @Override
    public RicevimentiAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemricevimento,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RicevimentiAdapter.myViewHolder holder, int position) {

        Ricevimento ricevimento = ricevimentoArrayList.get(position);

        holder.data.setText(ricevimento.getData());

    }

    @Override
    public int getItemCount() {
        return ricevimentoArrayList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView data;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.dataRicevimento);
        }
    }
}
