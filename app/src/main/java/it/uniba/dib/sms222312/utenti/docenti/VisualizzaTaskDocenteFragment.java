package it.uniba.dib.sms222312.utenti.docenti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import it.uniba.dib.sms222312.modelli.adapterFile.AdapterFileCard;
import it.uniba.dib.sms222312.modelli.TaskTesi;

public class VisualizzaTaskDocenteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((HomeDocente) getActivity()).setToolbarTitle(getString(R.string.visualizzaTask));
        View view = inflater.inflate(R.layout.fragment_visualizza_task, container, false);

        Bundle bundle = getArguments();
        TaskTesi taskTesi = (TaskTesi) bundle.getSerializable("task");

        String tesista = taskTesi.getTesista();
        String nome = taskTesi.getNome();
        String descrizione = taskTesi.getDescrizione();
        String stato = taskTesi.getStato();
        String scadenza = taskTesi.getScadenza();


        TextView txtNome = view.findViewById(R.id.nome);
        TextView txtDescrizione = view.findViewById(R.id.descrizione);
        TextView txtScadenza = view.findViewById(R.id.scadenza);
        TextView txtStato = view.findViewById(R.id.stato);
        Button btnIniziato = view.findViewById(R.id.impostaIniziato);
        Button btnCompletato = view.findViewById(R.id.impostaCompletato);

        txtNome.setText(nome);
        txtDescrizione.setText(descrizione);
        txtScadenza.setText(scadenza);
        txtStato.setText(stato);



        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterFileCard adapter = new AdapterFileCard(taskTesi.getFile());
        recyclerView.setAdapter(adapter);

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
                                            Toast.makeText(getContext(),"TaskTesi iniziato",Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Gestione dell'errore
                                            Toast.makeText(getContext(),"Impossibile impostare TaskTesi Iniziato",Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getContext(),"TaskTesi completato",Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Gestione dell'errore
                                            Toast.makeText(getContext(),"Impossibile impostare TaskTesi Completato",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Nessun documento corrispondente trovato
                        }

                    }
                });

            }
        });
return view;
    }

}