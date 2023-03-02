package it.uniba.dib.sms222312.docenti;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms222312.MainActivity;
import it.uniba.dib.sms222312.menuHome.MyAdapterMenu;
import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.menuHome.cardHome;

public class HomeDocenteFragment extends Fragment implements MyAdapterMenu.OnItemClickListener {
    private RecyclerView recyclerView;
    private MyAdapterMenu adapter;
    private List<cardHome> menuItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_1);
        View view = inflater.inflate(R.layout.fragment_home_docente, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numColumns = (int) (dpWidth / 120);
        getMenuItems(); // Recupera la lista degli elementi del menu
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        // Imposta l'adattatore per il RecyclerView
        MyAdapterMenu adapter = new MyAdapterMenu(menuItems, getContext(), this);
        recyclerView.setAdapter(adapter);
        return view;

    }



    private void getMenuItems() {
        if(menuItems.size()==0) {
            // Aggiungi gli elementi del menu
            menuItems.add(new cardHome(R.drawable.icona_chat, "Chat" , HomeDocente.class.getName()));
            menuItems.add(new cardHome(R.drawable.icona_ricevimenti, "Ricevimenti", ListaRicevimentiDocenteActivity.class.getName()));
            menuItems.add(new cardHome(R.drawable.icona_mie_tesi, "Mie Tesi",ListaTesiFragment.class.getName()));
            menuItems.add(new cardHome(R.drawable.icona_richieste_tesi, "Richiesta Tesi", ListaRichiesteActivity.class.getName()));
            menuItems.add(new cardHome(R.drawable.icona_tesi_avviate, "Tesi Avviate", ListaTesistiActivity.class.getName()));
        }
    }

    @Override
    public void onItemClick(cardHome menuItem) {
        try{
            Log.d("",menuItem.getactivityName());
        Intent intent = new Intent(getActivity(), Class.forName(menuItem.getactivityName())); // Replace with the name of your activity
        // Add any extras or data to the intent if needed
        startActivity(intent);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    }
}