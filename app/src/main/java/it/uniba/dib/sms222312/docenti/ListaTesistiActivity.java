package it.uniba.dib.sms222312.docenti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Tesista;
import it.uniba.dib.sms222312.modelli.TesistiAdapter;

public class ListaTesistiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Tesista> tesistaArrayList;
    TesistiAdapter myAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tesisti);

        recyclerView = findViewById(R.id.recyclerTesisti);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        tesistaArrayList = new ArrayList<Tesista>();
        myAdapter = new TesistiAdapter(ListaTesistiActivity.this, tesistaArrayList);
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getUid();


        db.collection("tesisti").whereEqualTo("relatore", user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        tesistaArrayList.add(dc.getDocument().toObject(Tesista.class));
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        db.collection("tesisti").whereEqualTo("corelatore", user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        tesistaArrayList.add(dc.getDocument().toObject(Tesista.class));
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}