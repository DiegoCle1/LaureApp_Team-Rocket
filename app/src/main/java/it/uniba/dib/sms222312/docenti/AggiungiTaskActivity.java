package it.uniba.dib.sms222312.docenti;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.SchermataCaricamento;
import it.uniba.dib.sms222312.adapter.AdapterFileCard;
import it.uniba.dib.sms222312.modelli.Task;

public class AggiungiTaskActivity extends AppCompatActivity implements AdapterFileCard.OnItemClickListener1{

    private EditText edtNome;
    private EditText edtDescrizione;
    private TextInputEditText textViewDataScadenza;
    private Button buttonScegliFile;
    private Button buttonCaricaDati;
    private HashSet<Uri> fileUris = new HashSet<>();
    String dataScadenza = null;
    // Inizializza il ProgressBar
    SchermataCaricamento dialog;
    List<String> listaFile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_task);
        dialog = new SchermataCaricamento(this);



        // Inizializza le variabili
        edtNome = findViewById(R.id.nomeTask);
        edtDescrizione = findViewById(R.id.descrizioneTask);
        textViewDataScadenza = findViewById(R.id.date_picker_text_input_edit_text);
        buttonScegliFile = findViewById(R.id.caricaFile);
        buttonCaricaDati = findViewById(R.id.aggiungiTask);

        // Ottieni i dati inseriti dall'utente
        String tesista = getIntent().getStringExtra("Tesista");

        // Aggiungi un listener al pulsante per selezionare i file
        buttonScegliFile.setOnClickListener(view -> scegliFile());

        // Aggiungi un listener al pulsante per caricare i file
        buttonCaricaDati.setOnClickListener(view -> uploadData(dataScadenza, tesista));
    }

    private void uploadData(String dataScadenza, String tesista) {
        String nome = edtNome.getText().toString();
        String descrizione = edtDescrizione.getText().toString();
        Log.d("",dataScadenza+descrizione+nome);
        // Verifica che tutti i campi siano stati compilati
        if (nome.isEmpty() || descrizione.isEmpty() || dataScadenza.isEmpty()) {
            Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show();
            return;
        }

        //codice per inizializzare Firebase
        FirebaseApp.initializeApp(this);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();


        // Mostra il ProgressBar
        dialog.show();

        // Carica il file selezionato su Firebase Storage
        // carica il file
        if(!fileUris.isEmpty()) {
            for (Uri fileUri : fileUris) {
                StorageReference storageRef = storage.getReference().child(tesista + "/" + getFileName(fileUri));
                storageRef.putFile(fileUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // il file è stato caricato con successo
                            // ottieni l'URL del file appena caricato
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // fai qualcosa con l'URL del file, ad esempio salvarlo su Firestore
                                listaFile.add(uri.toString());
                                if (listaFile.size() == fileUris.size()) {
                                    // Crea un oggetto "Task" con i dati inseriti dall'utente e l'URL del file
                                    Task task = new Task(tesista, nome, descrizione, dataScadenza, "Non iniziato", listaFile);
                                    uploadTask(database, task);
                                }
                            });
                        })
                        .addOnFailureListener(e -> {
                            // Se si verifica un errore durante il caricamento su Firebase Storage, mostra un messaggio di errore
                            // Nascondi il ProgressBar

                            Toast.makeText(this, "Si è verificato un errore", Toast.LENGTH_SHORT).show();

                        });
            }
        }else{
            Task task = new Task(tesista, nome, descrizione, dataScadenza, "Non iniziato", listaFile);
            uploadTask(database, task);
        }


    }

    private void uploadTask(FirebaseFirestore database, Task task) {
        // Carica l'oggetto "Dato" su Firebase Database
        database.collection("task").document().set(task)
                .addOnSuccessListener(aVoid -> {
                    // Nascondi il ProgressBar
                    dialog.dismiss();
                    Toast.makeText(this, "Dati caricati con successo", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Se si verifica un errore durante il caricamento su Firebase Database, mostra un messaggio di errore
                    dialog.dismiss();
                    Toast.makeText(this, "Si è verificato un errore", Toast.LENGTH_SHORT).show();
                });
    }


    private static final String DATE_PICKER_TAG = "datePicker";

    public void showDatePickerDialog(View v) {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date");
        builder.setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);
        builder.setCalendarConstraints(new CalendarConstraints.Builder()
                .setStart(System.currentTimeMillis() - 1000)
                .setValidator(DateValidatorPointForward.now())
                .build());

        MaterialDatePicker<Long> materialDatePicker = builder.build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Converti la data selezionata in una stringa formattata
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                dataScadenza = formatter.format(new Date((Long) selection));
                textViewDataScadenza.setText(dataScadenza);
            }
        });

        materialDatePicker.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    private ActivityResultLauncher<String[]> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenMultipleDocuments(), uris -> {
                if (uris != null && uris.size() > 0) {
                    if (!fileUris.addAll(uris)) {
                        Toast.makeText(this, "Il file selezionato è già presente", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("a", fileUris.toString());
                    String testo = "File selezionati: " + fileUris.size();
                    buttonScegliFile.setText(testo);
                    updateSelectedFilesList();
                }
            });
    private void scegliFile() {
        filePickerLauncher.launch(new String[] {"*/*"});
    }

    private void updateSelectedFilesList() {

        ArrayList<String> myList = new ArrayList<>();
        ArrayList<Uri> myListUri = new ArrayList<>();
        for (Uri uri : fileUris) {
            myList.add(getFileName(uri));
            myListUri.add(uri);
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.removeAllViews();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterFileCard adapter = new AdapterFileCard(myList, myListUri, this);
        recyclerView.setAdapter(adapter);

        buttonScegliFile.setText("Scegli file");
    }

    public String getFileName(Uri uri) {
        String result = "unknown_file_name";
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (columnIndex >= 0) {
                    result = cursor.getString(columnIndex);
                }
            }
            cursor.close();
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onItemClick1(Uri file) {
        fileUris.remove((Uri) file);
        updateSelectedFilesList();
    }
}