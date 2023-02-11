package it.uniba.dib.sms222312;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
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

                // Verifica che tutti i campi siano stati compilati
                if (email.isEmpty() || password.isEmpty() || matricola.isEmpty() || nome.isEmpty() || cognome.isEmpty()) {
                    Toast.makeText(Registry.this, "Tutti i campi sono obbligatori", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Registry.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Creazione di un nuovo utente riuscita
                                    String utente = auth.getCurrentUser().getUid();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    Map<String, Object> userDb = new HashMap<>();
                                    userDb.put("email", email);
                                    userDb.put("matricola", matricola);
                                    userDb.put("nome", nome);
                                    userDb.put("cognome", cognome);
                                    db.collection("students").document(utente).set(userDb)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Registry.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Registry.this, MainActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Registry.this, "Errore durante la registrazione", Toast.LENGTH_SHORT).show();
                                                }
                                            });
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
}