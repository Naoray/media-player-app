package de.codematch.naoray.media_player_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * DialogFragment, welches angezeigt wird,
 * wenn das Video nach dem Puffer Timeout nicht geladen werden konnte.
 */
public class VideoNotAvailableDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.videoNotAvailablePart1))
                .setPositiveButton(R.string.videoNotAvailableAccept_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User accepts the message
                        ((LiveStreamActivity) getActivity()).finish();
                    }
                });

        // Create the AlertDialog object, make sure it doesn't close after a click outside of it and return it
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
