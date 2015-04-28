package appewtc.masterung.carabaorest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by masterUNG on 4/28/15 AD.
 */
public class MyAlertDialog {

    public void dialogError(Context context, String strTitle, String strMessage) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(context);
        objBuilder.setIcon(R.drawable.icon_question);
        objBuilder.setTitle(strTitle);
        objBuilder.setMessage(strMessage);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();
    }

}   // Main Class
