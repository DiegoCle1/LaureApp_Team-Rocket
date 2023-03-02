package it.uniba.dib.sms222312.docenti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Ricevimento;
import it.uniba.dib.sms222312.modelli.Tesista;

public class VisualizzaRichiestaRicevimentoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_richiesta_ricevimento);

        String tesista = getIntent().getStringExtra("Tesista");
        String task = getIntent().getStringExtra("Task");
        String data = getIntent().getStringExtra("Data");
        String dettagli = getIntent().getStringExtra("Dettagli");

        TextView txtTask = findViewById(R.id.task);
        TextView txtDettagli = findViewById(R.id.dettagli);
        TextView txtData = findViewById(R.id.data);
        Button btnAccetta = findViewById(R.id.accetta);
        Button btnRifiuta = findViewById(R.id.rifiuta);

        txtTask.setText(task);
        txtDettagli.setText(dettagli);
        txtData.setText(data);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        btnAccetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ricevimento ricevimento = new Ricevimento(tesista, task, data, dettagli);
                db.collection("ricevimenti").document().set(ricevimento).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(VisualizzaRichiestaRicevimentoActivity.this,"Richiesta accettata", Toast.LENGTH_SHORT).show();
                                Query query = db.collection("richiestaricevimento").whereEqualTo("tesista", tesista).whereEqualTo("task", task).whereEqualTo("data", data).whereEqualTo("dettagli", dettagli);
                                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            // Ottieni il primo documento corrispondente
                                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                                            // Ottieni l'ID del documento
                                            String documentId = documentSnapshot.getId();

                                            // Usa l'ID del documento per fare altre operazioni su di esso
                                            db.collection("richiestaricevimento").document(documentId)
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
                                setResult(RESULT_OK);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(VisualizzaRichiestaRicevimentoActivity.this,"Impossibile accettare richiesta", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnRifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = db.collection("richiestaricevimento").whereEqualTo("tesista", tesista).whereEqualTo("task", task).whereEqualTo("data", data).whereEqualTo("dettagli", dettagli);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Ottieni il primo documento corrispondente
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                            // Ottieni l'ID del documento
                            String documentId = documentSnapshot.getId();

                            // Usa l'ID del documento per fare altre operazioni su di esso
                            db.collection("richiestaricevimento").document(documentId)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(VisualizzaRichiestaRicevimentoActivity.this,"Richiesta rifiutata", Toast.LENGTH_SHORT).show();
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(VisualizzaRichiestaRicevimentoActivity.this,"Impossibile eliminare richiesta", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Nessun documento corrispondente trovato
                        }
                    }
                });
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}