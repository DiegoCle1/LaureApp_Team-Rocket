package it.uniba.dib.sms222312.docenti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.ListaRichiesteInterface;
import it.uniba.dib.sms222312.modelli.Tesista;
import it.uniba.dib.sms222312.modelli.TesistiAdapter;
import it.uniba.dib.sms222312.studenti.ListaRicevimentiStudenteActivity;
import it.uniba.dib.sms222312.studenti.RichiestaRicevimentoActivity;

public class ListaRicevimentiDocenteActivity extends AppCompatActivity implements ListaRichiesteInterface {

    FirebaseFirestore db;
    RecyclerView recyclerView;
    ArrayList<Tesista> tesistaArrayList;
    TesistiAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ricevimenti_docente);

        Button btnRicevimento = findViewById(R.id.btnRichieste);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        List<String> documentIds = new ArrayList<>();
        Query query = db.collection("tesisti").whereEqualTo("relatore", userId);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String documentId = document.getId();
                        documentIds.add(documentId);
                    }

                    btnRicevimento.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ListaRicevimentiDocenteActivity.this, ListaRichiesteRicevimentoActivity.class);

                            intent.putStringArrayListExtra("Tesista",(ArrayList<String>) documentIds);

                            startActivity(intent);
                        }
                    });
                } else {
                    // Nessun documento corrispondente trovato
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}