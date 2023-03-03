package it.uniba.dib.sms222312.docenti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.ListaRichiesteInterface;
import it.uniba.dib.sms222312.modelli.Ricevimento;
import it.uniba.dib.sms222312.modelli.RichiesteRicevimentoAdapter;

public class ListaRichiesteRicevimentoActivity extends AppCompatActivity implements ListaRichiesteInterface {

    private List<String> tesisti;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ArrayList<Ricevimento> ricevimentoArrayList;
    RichiesteRicevimentoAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_richieste_ricevimento);

        tesisti = getIntent().getStringArrayListExtra("Tesista");

        recyclerView = findViewById(R.id.recyclerRichieste);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        ricevimentoArrayList = new ArrayList<Ricevimento>();
        myAdapter = new RichiesteRicevimentoAdapter(ListaRichiesteRicevimentoActivity.this, ricevimentoArrayList, this);
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();


    }

    private void EventChangeListener() {

        db.collection("richiestaricevimento").whereIn("tesista", tesisti).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        Intent intent = new Intent(ListaRichiesteRicevimentoActivity.this, VisualizzaRichiestaRicevimentoActivity.class);

        intent.putExtra("Tesista", ricevimentoArrayList.get(position).getTesista());
        intent.putExtra("Task", ricevimentoArrayList.get(position).getTask());
        intent.putExtra("Data", ricevimentoArrayList.get(position).getData());
        intent.putExtra("Dettagli", ricevimentoArrayList.get(position).getDettagli());

        startActivityForResult(intent, position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Codice per "restartare" l'activity
            Log.d("qualcosa","si");
            ricevimentoArrayList.remove(requestCode);
            myAdapter.notifyDataSetChanged();
        }
    }
}