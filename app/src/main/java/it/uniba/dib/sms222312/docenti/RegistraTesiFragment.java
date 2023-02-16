package it.uniba.dib.sms222312.docenti;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Tesi;



public class RegistraTesiFragment extends Fragment {
    private Tesi tesi;
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewa=inflater.inflate(R.layout.fragment_registra_tesi, container, false);
        db = FirebaseFirestore.getInstance();
        Spinner spinnerCourses = viewa.findViewById(R.id.spinner_dipartimento);
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
                                }
                                // Popola lo Spinner con la lista di corsi
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<>(uniqueValues));
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                Spinner spinnerC = viewa.findViewById(R.id.spinner_corso);
                                spinnerC.setAdapter(adapter);

                            }
                        });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Non viene effettuata alcuna selezione

            }
        });
        Button btnRegistry = viewa.findViewById(R.id.btn_register);
        btnRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtNome = viewa.findViewById(R.id.nome);
                String nome = edtNome.getText().toString();
                EditText edtOre = viewa.findViewById(R.id.durata);
                String ore = edtOre.getText().toString();
                EditText edtMedia = viewa.findViewById(R.id.media);
                String media = edtMedia.getText().toString();
                EditText edtDesc = viewa.findViewById(R.id.descrizione);
                String descrizione = edtDesc.getText().toString();
                Spinner coursesSpinner = viewa.findViewById(R.id.spinner_corso);
                Object selectedItem = coursesSpinner.getSelectedItem();
                String corso = selectedItem.toString();
                // Verifica che tutti i campi siano stati compilati
                if (nome.isEmpty() || ore.isEmpty() || media.isEmpty() || descrizione.isEmpty()) {
                    Toast.makeText(getActivity(), "Tutti i campi sono obbligatori", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(corso.equals("Seleziona corso")){
                    Toast.makeText(getActivity(), "Inserire il corso", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String user = auth.getCurrentUser().getUid();
                tesi = new Tesi(user,nome,corso,Integer.parseInt(ore),Integer.parseInt(media),descrizione);
                registraTesi(tesi);
            }
        });

        return viewa;
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<>(uniqueValues));
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCourses.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void registraTesi(Tesi tesi) {
        Map<String, Object> userDb = new HashMap<>();
        userDb.put("docente", tesi.getId());
        userDb.put("nome", tesi.getNome());
        userDb.put("corso", tesi.getCorso());
        userDb.put("ore", tesi.getOreDurata());
        userDb.put("media", tesi.getMediaVoti());
        userDb.put("descrizione", tesi.getDescrizione());
        db.collection("tesi").document().set(userDb)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                        getActivity().getFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Errore durante la registrazione", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }
