package it.uniba.dib.sms222312;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

public class SchermataCaricamento {
    private Dialog dialog;
    private AlertDialog.Builder dialogC;
    private final boolean[] conf = new boolean[1];

    public SchermataCaricamento(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.schermata_caricamento);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.1f);
        }
        dialog.setCancelable(false);
    }

    public SchermataCaricamento(){}


    public AlertDialog.Builder conferma(Context context, String s1, String s2){

        dialogC = new AlertDialog.Builder(context);
        // Imposta il titolo del dialogo
        dialogC.setTitle("Conferma");

        // Imposta il messaggio del dialogo
        dialogC.setMessage("Sei sicuro di voler procedere?");



        dialogC.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogC.setCancelable(false);
        return dialogC;
    }

    public void show(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }
}
