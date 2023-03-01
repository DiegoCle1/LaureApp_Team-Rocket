package it.uniba.dib.sms222312.docenti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import it.uniba.dib.sms222312.R;

public class VisualizzaTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_task);

        String tesista = getIntent().getStringExtra("Tesista");
        String nome = getIntent().getStringExtra("Nome");
        String descrizione = getIntent().getStringExtra("Descrizione");
        String stato = getIntent().getStringExtra("Stato");
        String scadenza = getIntent().getStringExtra("Scadenza");

        TextView txtNome = findViewById(R.id.nome);
        TextView txtDescrizione = findViewById(R.id.descrizione);
        TextView txtScadenza = findViewById(R.id.scadenza);
        TextView txtStato = findViewById(R.id.stato);
        Button btnIniziato = findViewById(R.id.impostaIniziato);
        Button btnCompletato = findViewById(R.id.impostaCompletato);

        txtNome.setText(nome);
        txtDescrizione.setText(descrizione);
        txtScadenza.setText(scadenza);
        txtStato.setText(stato);

        btnIniziato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Query query = db.collection("task").whereEqualTo("nome", nome).whereEqualTo("descrizione", descrizione).whereEqualTo("scadenza", scadenza).whereEqualTo("stato", txtStato.getText().toString());
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Ottieni il primo documento corrispondente
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                            // Ottieni l'ID del documento
                            String documentId = documentSnapshot.getId();
                            DocumentReference taskRef = db.collection("task").document(documentId);

                            taskRef.update("stato", "Iniziato")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Modifica del campo "stato" completata con successo
                                            txtStato.setText("Iniziato");
                                            Toast.makeText(VisualizzaTaskActivity.this,"Task iniziato",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Gestione dell'errore
                                            Toast.makeText(VisualizzaTaskActivity.this,"Impossibile impostare Task Iniziato",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Nessun documento corrispondente trovato
                        }
                    }
                });

            }
        });

        btnCompletato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Query query = db.collection("task").whereEqualTo("nome", nome).whereEqualTo("descrizione", descrizione).whereEqualTo("scadenza", scadenza).whereEqualTo("stato", txtStato.getText().toString());
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Ottieni il primo documento corrispondente
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                            // Ottieni l'ID del documento
                            String documentId = documentSnapshot.getId();
                            DocumentReference taskRef = db.collection("task").document(documentId);

                            taskRef.update("stato", "Completato")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Modifica del campo "stato" completata con successo
                                            txtStato.setText("Completato");
                                            Toast.makeText(VisualizzaTaskActivity.this,"Task completato",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Gestione dell'errore
                                            Toast.makeText(VisualizzaTaskActivity.this,"Impossibile impostare Task Completato",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Nessun documento corrispondente trovato
                        }
                    }
                });

            }
        });

    }

}