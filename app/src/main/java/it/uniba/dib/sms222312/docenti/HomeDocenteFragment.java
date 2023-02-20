package it.uniba.dib.sms222312.docenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms222312.MyAdapterMenu;
import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.cardHome;

public class HomeDocenteFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<cardHome> menuItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_docente, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Aggiungi gli elementi del menu
        menuItems.add(new cardHome(R.drawable.icona_chat, "Chat"));
        menuItems.add(new cardHome(R.drawable.icona_ricevimenti, "Ricevimenti"));
        menuItems.add(new cardHome(R.drawable.icona_mie_tesi, "Mie Tesi"));
        menuItems.add(new cardHome(R.drawable.icona_richieste_tesi, "Richiesta Tesi"));
        menuItems.add(new cardHome(R.drawable.icona_info_tesi, "Info Tesi"));
        menuItems.add(new cardHome(R.drawable.icona_tesi_avviate, "Tesi Avviate"));

        // Imposta l'adattatore per il RecyclerView
        MyAdapterMenu adapter = new MyAdapterMenu(menuItems, getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }

}