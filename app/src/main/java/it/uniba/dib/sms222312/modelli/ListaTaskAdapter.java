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

public class ListaTaskAdapter extends RecyclerView.Adapter<ListaTaskAdapter.MyViewHolder> {

    private final ListaRichiesteInterface recyclerViewInterface;

    Context context;
    ArrayList<Task> taskArrayList;

    public ListaTaskAdapter(Context context, ArrayList<Task> taskArrayList, ListaRichiesteInterface listaRichiesteInterface) {
        this.context = context;
        this.taskArrayList = taskArrayList;
        this.recyclerViewInterface = listaRichiesteInterface;
    }

    @NonNull
    @Override
    public ListaTaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemtesi, parent, false);

        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaTaskAdapter.MyViewHolder holder, int position) {

        Task task = taskArrayList.get(position);

        holder.nome.setText(task.getNome());

    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome;

        public MyViewHolder(@NonNull View itemView, ListaRichiesteInterface recyclerViewInterface) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeTesi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
