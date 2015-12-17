package de.codematch.naoray.media_player_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Schmidt on 15.12.2015.
 */
public class WLANactivateDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.activateWlanMessage)
                .setPositiveButton(R.string.enableWLAN, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User wants to activate WLAN on device
                        ((LiveStreamActivity)getActivity()).activateWLAN();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        ((LiveStreamActivity)getActivity()).finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}