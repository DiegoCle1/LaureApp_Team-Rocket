package it.uniba.dib.sms222312.studenti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import it.uniba.dib.sms222312.docenti.VisualizzaTesiActivity;
import it.uniba.dib.sms222312.modelli.RecyclerViewInterface;
import it.uniba.dib.sms222312.modelli.Tesi;
import it.uniba.dib.sms222312.modelli.TesiAdapter;

public class CercaTesiActivity extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView recyclerView;
    FirebaseFirestore database;
    TesiAdapter myAdapter;
    ArrayList<Tesi> list;
    String corso = null;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_tesi);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        recyclerView = findViewById(R.id.listaTesi);
        database = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CercaTesiActivity.this));

        list = new ArrayList<Tesi>();
        myAdapter = new TesiAdapter(CercaTesiActivity.this,list,this);
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();
    }

    private void filterList(String text) {
        ArrayList<Tesi> filteredList = new ArrayList<>();
        for(Tesi tesi : list){
            if(tesi.getNome().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(tesi);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            myAdapter.setFilteredList(filteredList);
        }
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
        Intent intent = new Intent(CercaTesiActivity.this, VisualizzaTesiStudenteActivity.class);

        intent.putExtra("Nome", list.get(position).getNome());
        intent.putExtra("Corso", list.get(position).getCorso());
        intent.putExtra("Descrizione", list.get(position).getDescrizione());
        intent.putExtra("Media", list.get(position).getMedia());
        intent.putExtra("Durata", list.get(position).getOre());
        intent.putExtra("Docente", list.get(position).getDocente());

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        //niente
    }
}