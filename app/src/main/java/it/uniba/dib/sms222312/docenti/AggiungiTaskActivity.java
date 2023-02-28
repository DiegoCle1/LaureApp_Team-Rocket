package it.uniba.dib.sms222312.docenti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.modelli.Task;
import it.uniba.dib.sms222312.registrazione.Registry;
import it.uniba.dib.sms222312.studenti.InvioRichiestaActivity;

public class AggiungiTaskActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    List<String> listaFile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_task);

        String tesista = getIntent().getStringExtra("Tesista");

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        EditText edtNome = findViewById(R.id.nomeTask);
        EditText edtDescrizione = findViewById(R.id.descrizioneTask);
        Button btnInvio = findViewById(R.id.aggiungiTask);
        Button btnUpload = findViewById(R.id.caricaFile);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); //tutti i tipi di file
                startActivityForResult(Intent.createChooser(intent, "Seleziona un file"), 111);

            }
        });

        btnInvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = edtNome.getText().toString();
                String descrizione = edtDescrizione.getText().toString();
                String data = dateButton.getText().toString();
                if (nome.isEmpty() || descrizione.isEmpty()) {
                    Toast.makeText(AggiungiTaskActivity.this, R.string.errorCampiObbligatori, Toast.LENGTH_SHORT).show();
                    return;
                }
                Task task = new Task(tesista, nome, descrizione, "Non iniziato", data, listaFile);
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection("task").document().set(task).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AggiungiTaskActivity.this,"Task aggiunto", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AggiungiTaskActivity.this,"Impossibile aggiungere task", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            // ottieni un riferimento al bucket di storage di Firebase
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // crea un riferimento al file in storage
            String fileName = null;
            if (fileUri.getScheme().equals("content")) {
                Cursor cursor = getContentResolver().query(fileUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex >= 0) {
                        fileName = cursor.getString(columnIndex);
                    } else {
                        fileName = "unknown_file_name";
                    }
                } else {
                    fileName = "unknown_file_name";
                }
                cursor.close();
            }
            if (fileName == null) {
                fileName = fileUri.getPath();
                int cut = fileName.lastIndexOf('/');
                if (cut != -1) {
                    fileName = fileName.substring(cut + 1);
                }
            }


            StorageReference fileRef = storageRef.child(fileName);

            // carica il file
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // il file è stato caricato con successo
                        // ottieni l'URL del file appena caricato
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // fai qualcosa con l'URL del file, ad esempio salvarlo su Firestore
                            listaFile.add(uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        // il caricamento del file è fallito
                        // gestisci l'errore
                    });

        }
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-10000);
    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch(month){
            case 1: return "GEN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAG";
            case 6: return "GIU";
            case 7: return "LUG";
            case 8: return "AGO";
            case 9: return "SET";
            case 10: return "OTT";
            case 11: return "NOV";
            case 12: return "DIC";
        }
        return "GEN";
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }
}