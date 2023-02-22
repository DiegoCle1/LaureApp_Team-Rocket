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

public class ClassificaAdapter extends RecyclerView.Adapter<ClassificaAdapter.MyViewHolder>{

    Context context;
    ArrayList<Classifica> classificaArrayList;

    public ClassificaAdapter(Context context, ArrayList<Classifica> classificaArrayList) {
        this.context = context;
        this.classificaArrayList = classificaArrayList;
    }

    @NonNull
    @Override
    public ClassificaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemtesi,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificaAdapter.MyViewHolder holder, int position) {

        Classifica classifica = classificaArrayList.get(position);

        holder.nome.setText(classifica.getNome());

    }

    @Override
    public int getItemCount() {
        return classificaArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeTesi);
        }
    }
}
