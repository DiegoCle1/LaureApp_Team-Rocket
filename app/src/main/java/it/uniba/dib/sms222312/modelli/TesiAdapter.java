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

public class TesiAdapter extends RecyclerView.Adapter<TesiAdapter.TesiViewHolder> {

    Context context;
    ArrayList<Tesi> list;

    public TesiAdapter(Context context, ArrayList<Tesi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TesiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemtesi,parent,false);
        return new TesiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TesiViewHolder holder, int position) {

        Tesi tesi = list.get(position);
        holder.nomeTesi.setText(tesi.getNome());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TesiViewHolder extends RecyclerView.ViewHolder{

        TextView nomeTesi;

        public TesiViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeTesi = itemView.findViewById(R.id.nomeTesi);

        }
    }
}
