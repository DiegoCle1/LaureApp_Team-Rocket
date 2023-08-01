package it.uniba.dib.sms222312;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public class SchermataCaricamento {
    private Dialog dialog;

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
    public void show(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }
}
