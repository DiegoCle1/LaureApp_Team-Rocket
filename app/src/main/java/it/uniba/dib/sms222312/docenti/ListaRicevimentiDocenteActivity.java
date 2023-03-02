package it.uniba.dib.sms222312.docenti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.studenti.ListaRicevimentiStudenteActivity;
import it.uniba.dib.sms222312.studenti.RichiestaRicevimentoActivity;

public class ListaRicevimentiDocenteActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ricevimenti_docente);

        Button btnRicevimento = findViewById(R.id.btnRichieste);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        Query query = db.collection("tesisti").whereEqualTo("relatore", userId);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Ottieni il primo documento corrispondente
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                    // Ottieni l'ID del documento
                    String tesista = documentSnapshot.getId();

                    btnRicevimento.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ListaRicevimentiDocenteActivity.this, ListaRichiesteRicevimentoActivity.class);

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
}