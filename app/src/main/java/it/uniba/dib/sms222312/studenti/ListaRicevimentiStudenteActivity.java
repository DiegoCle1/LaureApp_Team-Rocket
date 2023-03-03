package it.uniba.dib.sms222312.studenti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import it.uniba.dib.sms222312.docenti.ClickRicevimentoTesistaActivity;
import it.uniba.dib.sms222312.docenti.VisualizzaRicevimentiTesistaActivity;
import it.uniba.dib.sms222312.modelli.ListaRichiesteInterface;
import it.uniba.dib.sms222312.modelli.ListaTaskAdapter;
import it.uniba.dib.sms222312.modelli.RicevimentiAdapter;
import it.uniba.dib.sms222312.modelli.Ricevimento;
import it.uniba.dib.sms222312.modelli.Task;

public class ListaRicevimentiStudenteActivity extends AppCompatActivity implements ListaRichiesteInterface {

    FirebaseFirestore db;
    String tesista;
    RecyclerView recyclerView;
    ArrayList<Ricevimento> ricevimentoArrayList;
    RicevimentiAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ricevimenti_studente);

        Button btnRicevimento = findViewById(R.id.btnRicevimento);

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
                    tesista = documentSnapshot.getId();

                    recyclerView = findViewById(R.id.listaRicevimenti);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListaRicevimentiStudenteActivity.this));

                    db = FirebaseFirestore.getInstance();
                    ricevimentoArrayList = new ArrayList<Ricevimento>();
                    myAdapter = new RicevimentiAdapter(ListaRicevimentiStudenteActivity.this, ricevimentoArrayList, ListaRicevimentiStudenteActivity.this);
                    recyclerView.setAdapter(myAdapter);

                    EventChangeListener();

                    btnRicevimento.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ListaRicevimentiStudenteActivity.this, RichiestaRicevimentoActivity.class);

                            intent.putExtra("Tesista", tesista);

                            startActivity(intent);
                        }
                    });
                } else {
                    // Nessun documento corrispondente trovato
                }
            }
        });
    }

    private void EventChangeListener() {
        db.collection("ricevimenti").whereEqualTo("tesista", tesista).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        ricevimentoArrayList.add(dc.getDocument().toObject(Ricevimento.class));
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ListaRicevimentiStudenteActivity.this, ClickRicevimentoTesistaActivity.class);

        intent.putExtra("Task", ricevimentoArrayList.get(position).getTask());
        intent.putExtra("Data", ricevimentoArrayList.get(position).getData());
        intent.putExtra("Dettagli", ricevimentoArrayList.get(position).getDettagli());

        startActivity(intent);
    }
}