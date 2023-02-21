package it.uniba.dib.sms222312.studenti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.RecyclerViewInterface;
import it.uniba.dib.sms222312.modelli.Tesi;
import it.uniba.dib.sms222312.modelli.TesiAdapter;

public class CercaTesiActivity extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView recyclerView;
    FirebaseFirestore database;
    TesiAdapter myAdapter;
    ArrayList<Tesi> list;
    String corso = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_tesi);

        recyclerView = findViewById(R.id.listaTesi);
        database = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CercaTesiActivity.this));

        list = new ArrayList<Tesi>();
        myAdapter = new TesiAdapter(CercaTesiActivity.this,list,this);
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();
    }

    private void EventChangeListener(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getUid();
        DocumentReference docRef = database.collection("utente").document(user);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                corso = documentSnapshot.getString("corso");
                database.collection("tesi").whereEqualTo("corso",corso).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot valua, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        Log.d("pre for", "ci sono");
                        for(DocumentChange dc : valua.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                list.add(dc.getDocument().toObject(Tesi.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {
        //niente
    }
}