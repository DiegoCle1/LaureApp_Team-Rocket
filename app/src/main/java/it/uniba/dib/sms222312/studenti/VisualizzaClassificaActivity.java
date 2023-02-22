package it.uniba.dib.sms222312.studenti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import java.util.Collections;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Classifica;
import it.uniba.dib.sms222312.modelli.ClassificaAdapter;

public class VisualizzaClassificaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Classifica> classificaArrayList;
    ClassificaAdapter myAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_classifica);

        recyclerView = findViewById(R.id.classifica);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(VisualizzaClassificaActivity.this));

        db = FirebaseFirestore.getInstance();
        classificaArrayList = new ArrayList<Classifica>();
        myAdapter = new ClassificaAdapter(VisualizzaClassificaActivity.this, classificaArrayList);
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void EventChangeListener() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getUid();

        db.collection("classifica").whereEqualTo("utente",user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error",error.getMessage());
                    return;
                }

                for(DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        classificaArrayList.add(dc.getDocument().toObject(Classifica.class));
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getBindingAdapterPosition();
            int toPosition = target.getBindingAdapterPosition();
            Collections.swap(classificaArrayList,fromPosition,toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}