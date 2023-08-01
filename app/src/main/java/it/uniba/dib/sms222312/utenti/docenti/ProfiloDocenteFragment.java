package it.uniba.dib.sms222312.utenti.docenti;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import it.uniba.dib.sms222312.R;

public class ProfiloDocenteFragment extends Fragment {
    TextView nome,cognome,email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);


        ((HomeDocente) getActivity()).setToolbarTitle(getResources().getString(R.string.profilo));

        nome=view.findViewById(R.id.textView3);
        cognome=view.findViewById(R.id.textView4);
        email=view.findViewById(R.id.textView5);

        view.findViewById(R.id.textView16).setVisibility(View.GONE);
        view.findViewById(R.id.textView6).setVisibility(View.GONE);
        view.findViewById(R.id.textView17).setVisibility(View.GONE);
        view.findViewById(R.id.textView7).setVisibility(View.GONE);



        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();
        DocumentReference docRef=fStore.collection("utente").document(userId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                nome.setText(value.getString("nome"));
                cognome.setText(value.getString("cognome"));
                email.setText(value.getString("email"));
            }
        });
        return view;
    }

}



