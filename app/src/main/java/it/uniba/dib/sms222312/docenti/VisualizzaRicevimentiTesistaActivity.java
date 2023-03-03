package it.uniba.dib.sms222312.docenti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.RicevimentiAdapter;
import it.uniba.dib.sms222312.modelli.Ricevimento;

public class VisualizzaRicevimentiTesistaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Ricevimento> ricevimentoArrayList;
    RicevimentiAdapter myAdapter;
    FirebaseFirestore db;
    private String tesista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_ricevimenti_tesista);

        tesista = getIntent().getStringExtra("Tesista");

        recyclerView = findViewById(R.id.recyclerRicevimenti);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        ricevimentoArrayList = new ArrayList<Ricevimento>();
        myAdapter = new RicevimentiAdapter(VisualizzaRicevimentiTesistaActivity.this, ricevimentoArrayList);
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();
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
}