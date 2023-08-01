package it.uniba.dib.sms222312.utenti.docenti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Ricevimento;

public class VisualizzaRichiestaRicevimentoActivity extends DialogFragment {
    private FirebaseFirestore db;
    private AlertDialog alertDialog;

    private Context mContext;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_visualizza_richiesta_ricevimento, null);


        Bundle args = getArguments();

        String tesista = args.getString("Tesista");
        String task = args.getString("TaskTesi");
        String data = args.getString("Data");
        String dettagli = args.getString("Dettagli");

        TextView txtTask = view.findViewById(R.id.taskTesi);
        TextView txtDettagli = view.findViewById(R.id.dettagli);
        TextView txtData = view.findViewById(R.id.data);
        Button btnAccetta = view.findViewById(R.id.accetta);
        Button btnRifiuta = view.findViewById(R.id.rifiuta);

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
                                Toast.makeText(mContext,"Richiesta accettata", Toast.LENGTH_SHORT).show();
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
                                                            alertDialog.dismiss();


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("msg", "Error deleting document", e);
                                                            alertDialog.dismiss();

                                                        }
                                                    });
                                        } else {
                                            // Nessun documento corrispondente trovato
                                        }

                                    }
                                });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,"Impossibile accettare richiesta", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();

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
                                            Toast.makeText(mContext,"Richiesta rifiutata", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(mContext,"Impossibile eliminare richiesta", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();

                                        }
                                    });
                        } else {
                            // Nessun documento corrispondente trovato
                        }

                    }
                });

            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        return alertDialog;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
