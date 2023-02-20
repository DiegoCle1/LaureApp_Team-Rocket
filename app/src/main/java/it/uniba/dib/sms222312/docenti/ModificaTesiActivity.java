package it.uniba.dib.sms222312.docenti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import it.uniba.dib.sms222312.R;

public class ModificaTesiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_tesi);

        String nome = getIntent().getStringExtra("Nome");
        String corso = getIntent().getStringExtra("Corso");
        String descrizione = getIntent().getStringExtra("Descrizione");
        String media = getIntent().getStringExtra("Media");
        String durata = getIntent().getStringExtra("Durata");

        EditText editNome = findViewById(R.id.nome);
        EditText editDurata = findViewById(R.id.durata);
        EditText editMedia = findViewById(R.id.media);
        EditText editDescrizione = findViewById(R.id.descrizione);
        Button btnModifica = findViewById(R.id.btn_register);

        editNome.setText(nome);
        editDurata.setText(durata);
        editMedia.setText(media);
        editDescrizione.setText(descrizione);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String docente = auth.getCurrentUser().getUid();


        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = database.collection("tesi").whereEqualTo("corso", corso).whereEqualTo("nome", nome).whereEqualTo("descrizione", descrizione).whereEqualTo("media",media).whereEqualTo("ore",durata).whereEqualTo("docente",docente);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Ottieni il primo documento corrispondente
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                            // Ottieni l'ID del documento
                            String documentId = documentSnapshot.getId();

                            // Usa l'ID del documento per fare altre operazioni su di esso
                            DocumentReference docRef = database.collection("tesi").document(documentId);
                            String newNome = editNome.getText().toString();
                            String newDurata = editDurata.getText().toString();
                            String newMedia = editMedia.getText().toString();
                            String newDescrizione = editDescrizione.getText().toString();

                            docRef.update("nome",newNome,"ore",newDurata,"media",newMedia,"descrizione",newDescrizione)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ModificaTesiActivity.this,"Tesi modificata con successo",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ModificaTesiActivity.this,"Impossibile modificare la tesi",Toast.LENGTH_SHORT).show();
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