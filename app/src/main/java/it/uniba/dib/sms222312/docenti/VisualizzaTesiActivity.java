package it.uniba.dib.sms222312.docenti;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import it.uniba.dib.sms222312.R;
import it.uniba.dib.sms222312.studenti.VisualizzaTesiStudenteActivity;

public class VisualizzaTesiActivity extends AppCompatActivity {

    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_tesi);

        String nome = getIntent().getStringExtra("Nome");
        String corso = getIntent().getStringExtra("Corso");
        String descrizione = getIntent().getStringExtra("Descrizione");
        String media = getIntent().getStringExtra("Media");
        String durata = getIntent().getStringExtra("Durata");

        TextView textNome = findViewById(R.id.nome);
        TextView textCorso = findViewById(R.id.corso);
        TextView textDescrizione = findViewById(R.id.descrizione);
        TextView textMedia = findViewById(R.id.media);
        TextView textDurata = findViewById(R.id.durata);
        ImageView imageQr = findViewById(R.id.qr);
        Button modificaButton = findViewById(R.id.modifica);
        Button condividiButton = findViewById(R.id.condividi);

        String text ="Nome: " + nome + " Corso: " + corso + " Media: " + media + " Durata: " + durata + " ore Descrizione: " + descrizione;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix matrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
            imageQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        textNome.setText(nome);
        textCorso.setText(corso);
        textDescrizione.setText(descrizione);
        textMedia.setText(media);
        textDurata.setText(durata);
        modificaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizzaTesiActivity.this, ModificaTesiActivity.class);

                intent.putExtra("Nome", nome);
                intent.putExtra("Corso", corso);
                intent.putExtra("Descrizione", descrizione);
                intent.putExtra("Media", media);
                intent.putExtra("Durata", durata);

                startActivity(intent);
            }
        });

        condividiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(VisualizzaTesiActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(VisualizzaTesiActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else{
                    // Codice per condividere un Bitmap su WhatsApp
                    String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Shared image", null);

                    Uri bitmapUri = Uri.parse(bitmapPath);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    //shareIntent.setPackage("com.whatsapp");

                    startActivity(Intent.createChooser(shareIntent, "Condividi immagine"));
                }

            }
        });

    }
}