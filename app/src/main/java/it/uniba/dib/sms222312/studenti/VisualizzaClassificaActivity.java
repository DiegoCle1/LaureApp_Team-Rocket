package it.uniba.dib.sms222312.studenti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Classifica;
import it.uniba.dib.sms222312.modelli.ClassificaAdapter;
import it.uniba.dib.sms222312.modelli.ComparaDurata;
import it.uniba.dib.sms222312.modelli.ComparaMedia;

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
        Button button = findViewById(R.id.popup_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(classificaArrayList,fromPosition,toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                case ItemTouchHelper.RIGHT:
                    Query query = db.collection("classifica").whereEqualTo("nome", classificaArrayList.get(position).getNome()).whereEqualTo("media", classificaArrayList.get(position).getMedia()).whereEqualTo("durata", classificaArrayList.get(position).getDurata()).whereEqualTo("utente", classificaArrayList.get(position).getUtente());
                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Ottieni il primo documento corrispondente
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                                // Ottieni l'ID del documento
                                String documentId = documentSnapshot.getId();

                                // Usa l'ID del documento per fare altre operazioni su di esso
                                db.collection("classifica").document(documentId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("msg", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("msg", "Error deleting document", e);
                                            }
                                        });
                            } else {
                                // Nessun documento corrispondente trovato
                            }
                        }
                    });
                    classificaArrayList.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    break;
            }

        }
    };

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.ordinamento_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.order_by_media:
                        ComparaMedia comparator = new ComparaMedia();
                        Collections.sort(classificaArrayList, comparator);
                        myAdapter.notifyDataSetChanged();
                        break;
                    case R.id.order_by_durata:
                        ComparaDurata comparatord = new ComparaDurata();
                        Collections.sort(classificaArrayList, comparatord);
                        myAdapter.notifyDataSetChanged();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        popup.show();
    }


}