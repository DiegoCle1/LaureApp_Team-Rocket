package it.uniba.dib.sms222312.studenti;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Classifica;
import it.uniba.dib.sms222312.modelli.ClassificaAdapter;

public class VisualizzaClassificaFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Classifica> classificaArrayList;
    ClassificaAdapter myAdapter;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewa = inflater.inflate(R.layout.fragment_visualizza_classifica, container, false);

        recyclerView = viewa.findViewById(R.id.classifica);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();
        classificaArrayList = new ArrayList<Classifica>();
        myAdapter = new ClassificaAdapter(getActivity(), classificaArrayList);

        EventChangeListener();

        return viewa;
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
}