package it.uniba.dib.sms222312.docenti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.RecyclerViewInterface;
import it.uniba.dib.sms222312.modelli.Tesi;
import it.uniba.dib.sms222312.modelli.TesiAdapter;

public class ListaTesiFragment extends Fragment implements RecyclerViewInterface {

    RecyclerView recyclerView;
    FirebaseFirestore database;
    TesiAdapter myAdapter;
    ArrayList<Tesi> list;
    RegistraTesiFragment registraTesiFragment = new RegistraTesiFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewa = inflater.inflate(R.layout.fragment_lista_tesi, container, false);

        recyclerView = viewa.findViewById(R.id.listaTesi);
        database = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<Tesi>();
        myAdapter = new TesiAdapter(getActivity(),list,this);
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();
        Button btn = viewa.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, registraTesiFragment).commit();
            }
        });




        return viewa;
    }

    private void EventChangeListener(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getUid();
        database.collection("tesi").whereEqualTo("docente",user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        list.add(dc.getDocument().toObject(Tesi.class));
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), VisualizzaTesiActivity.class);

        intent.putExtra("Nome", list.get(position).getNome());
        intent.putExtra("Corso", list.get(position).getCorso());
        intent.putExtra("Descrizione", list.get(position).getDescrizione());
        intent.putExtra("Media", list.get(position).getMedia());
        intent.putExtra("Durata", list.get(position).getOre());

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        // Crea una query per cercare il documento con i dati specificati
        Query query = database.collection("tesi").whereEqualTo("corso", list.get(position).getCorso()).whereEqualTo("nome", list.get(position).getNome()).whereEqualTo("descrizione", list.get(position).getDescrizione()).whereEqualTo("media",list.get(position).getMedia()).whereEqualTo("ore",list.get(position).getOre()).whereEqualTo("docente",list.get(position).getDocente());

// Esegui la query e ottieni il primo documento corrispondente
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Ottieni il primo documento corrispondente
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                    // Ottieni l'ID del documento
                    String documentId = documentSnapshot.getId();

                    // Usa l'ID del documento per fare altre operazioni su di esso
                    database.collection("tesi").document(documentId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("msg", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("msg", "Error deleting document", e);
                                }
                            });
                } else {
                    // Nessun documento corrispondente trovato
                }
            }
        });
        list.remove(position);
        myAdapter.notifyItemRemoved(position);
    }
}