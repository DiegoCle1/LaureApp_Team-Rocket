package it.uniba.dib.sms222312.utenti.studenti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.RichiestaTesi;

public class InvioRichiestaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invio_richiesta);

        String studente = getIntent().getStringExtra("Studente");
        String docente = getIntent().getStringExtra("Docente");
        String tesi = getIntent().getStringExtra("Tesi");

        EditText edtDescrizione = findViewById(R.id.edit_text);
        Button btnInvia = findViewById(R.id.invia);


        btnInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descrizione = edtDescrizione.getText().toString();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                RichiestaTesi richiestaTesi = new RichiestaTesi(studente, docente, tesi, descrizione);
                database.collection("richiestatesi").document().set(richiestaTesi).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(InvioRichiestaActivity.this,"Richiesta inviata", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(InvioRichiestaActivity.this,"Impossibile inviare richiesta", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}