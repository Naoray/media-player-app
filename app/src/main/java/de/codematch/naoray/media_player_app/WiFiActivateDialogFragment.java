package de.codematch.naoray.media_player_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class WiFiActivateDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.activateWiFiMessagePart1) + "\n" + getString(R.string.activateWiFiMessagePart2))
                .setPositiveButton(R.string.enableWiFi, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User wants to activate WiFi on device
                        ((LiveStreamActivity) getActivity()).activateWiFi();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        ((LiveStreamActivity) getActivity()).finish();
                    }
                });

        // Create the AlertDialog object, make sure it doesn't close after a click outside of it and return it
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}