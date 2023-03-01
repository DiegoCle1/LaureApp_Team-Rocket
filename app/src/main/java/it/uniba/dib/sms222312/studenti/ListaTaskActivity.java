package it.uniba.dib.sms222312.studenti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.docenti.ListaTesistiActivity;
import it.uniba.dib.sms222312.docenti.VisualizzaTesistaActivity;
import it.uniba.dib.sms222312.modelli.ListaRichiesteInterface;
import it.uniba.dib.sms222312.modelli.ListaTaskAdapter;
import it.uniba.dib.sms222312.modelli.Task;

public class ListaTaskActivity extends AppCompatActivity implements ListaRichiesteInterface {

    FirebaseFirestore db;
    RecyclerView recyclerView;
    ArrayList<Task> taskArrayList;
    ListaTaskAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_task);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        Query query = db.collection("tesisti").whereEqualTo("studente", userId);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Ottieni il primo documento corrispondente
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                    // Ottieni l'ID del documento
                    String tesista = documentSnapshot.getId();

                    recyclerView = findViewById(R.id.recyclerTask);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListaTaskActivity.this));

                    db = FirebaseFirestore.getInstance();
                    taskArrayList = new ArrayList<Task>();
                    myAdapter = new ListaTaskAdapter(ListaTaskActivity.this, taskArrayList, ListaTaskActivity.this);
                    recyclerView.setAdapter(myAdapter);

                    EventChangeListener(tesista);
                } else {
                    // Nessun documento corrispondente trovato
                }
            }
        });
    }

    private void EventChangeListener(String tesista) {
        db.collection("task").whereEqualTo("tesista",tesista).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        taskArrayList.add(dc.getDocument().toObject(Task.class));
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}