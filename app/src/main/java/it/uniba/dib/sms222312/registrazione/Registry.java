package it.uniba.dib.sms222312.registrazione;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniba.dib.sms222312.Login;
import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Studente;

public class Registry extends AppCompatActivity {
private Studente studente;
private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        db = FirebaseFirestore.getInstance();
        Spinner spinnerCourses = findViewById(R.id.spinner_dipartimento);
        updateSpinner(spinnerCourses);
        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Recupero l'elemento selezionato
                String selectedItem = parent.getItemAtPosition(position).toString();
                Set<String> uniqueValues = new LinkedHashSet<>();
                uniqueValues.add("Seleziona corso");

                db.collection("corsi")
                        .whereEqualTo("Dipartimento", selectedItem)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                                // Per ogni documento, recupera il nome del corso e lo aggiunge alla lista
                                for (DocumentSnapshot document : documents) {
                                    uniqueValues.add(document.getString("Corso"));
                                    Toast.makeText(Registry.this, selectedItem, Toast.LENGTH_SHORT).show();
                                }
                                // Popola lo Spinner con la lista di corsi
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(Registry.this, android.R.layout.simple_spinner_item, new ArrayList<>(uniqueValues));
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                Spinner spinnerC = findViewById(R.id.spinner_corso);
                                spinnerC.setAdapter(adapter);

                            }
                        });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Non viene effettuata alcuna selezione

            }
        });
        Button btnRegistry = findViewById(R.id.btn_register);
        btnRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtEmail = findViewById(R.id.email);
                String email = edtEmail.getText().toString();
                EditText edtPassword = findViewById(R.id.password);
                String password = edtPassword.getText().toString();
                EditText edtMatricola = findViewById(R.id.matricola);
                String matricola = edtMatricola.getText().toString();
                EditText edtNome = findViewById(R.id.edt_name);
                String nome = edtNome.getText().toString();
                EditText edtCognome = findViewById(R.id.edt_surname);
                String cognome = edtCognome.getText().toString();
                Spinner coursesSpinner = findViewById(R.id.spinner_corso);
                Object selectedItem = coursesSpinner.getSelectedItem();
                String corso = selectedItem.toString();
                // Verifica che tutti i campi siano stati compilati
                if (email.isEmpty() || password.isEmpty() || matricola.isEmpty() || nome.isEmpty() || cognome.isEmpty()) {
                    Toast.makeText(Registry.this, "Tutti i campi sono obbligatori", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(corso.equals("Seleziona corso")){
                    Toast.makeText(Registry.this, "Inserire il corso", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Registry.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Creazione di un nuovo utente riuscita
                                    String user = auth.getCurrentUser().getUid();
                                    studente = new Studente(user,matricola,email,nome,cognome,corso);
                                    registraUtente(user, email, matricola, nome, cognome, corso);
                                } else {
                                    // Creazione di un nuovo utente non riuscita, mostra un messaggio di errore.
                                    Toast.makeText(Registry.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }

    private void updateSpinner(Spinner spinnerCourses) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("corsi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Recupera tutti i documenti dalla query
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            // Crea una lista di stringhe per contenere i nomi dei corsi
                            Set<String> uniqueValues = new LinkedHashSet<>();
                            uniqueValues.add("Seleziona dipartimento");
                            // Per ogni documento, recupera il nome del corso e lo aggiunge alla lista
                            for (DocumentSnapshot document : documents) {
                                uniqueValues.add(document.getString("Dipartimento"));
                            }
                            // Popola lo Spinner con la lista di corsi
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Registry.this, android.R.layout.simple_spinner_item, new ArrayList<>(uniqueValues));
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCourses.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void registraUtente(String user, String email, String matricola, String nome, String cognome, String corso) {
        Map<String, Object> userDb = new HashMap<>();
        userDb.put("email", email);
        userDb.put("matricola", matricola);
        userDb.put("nome", nome);
        userDb.put("cognome", cognome);
        userDb.put("corso", corso);
        userDb.put("tipo", "studente");
        db.collection("utente").document(user).set(userDb)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Registry.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registry.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registry.this, "Errore durante la registrazione", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}