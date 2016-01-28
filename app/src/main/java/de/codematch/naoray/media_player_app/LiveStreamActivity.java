package de.codematch.naoray.media_player_app;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class LiveStreamActivity extends AppCompatActivity {


    // Declare variables
    ProgressDialog pDialog;
    WifiManager wifiManager;
    ConnectivityManager connManager;
    VideoView videoview;
    // Insert your Video URL
    String VideoURL = "http://regiotainment.mni.thm.de:3000/videostorage/playliststorage/5623ca95e6cc3b74106e1bba/mainpanel/streams.m3u8";
    //"http://regiotainment.mni.thm.de/MobileVideos/Regiotainment.mp4";
    //"http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
    private SharedPreferences sPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout from video_main.xml
        setContentView(R.layout.activity_live_stream);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Preferences
        sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String landscapePreferencesKey = getString(R.string.landscape_preferences_key);
        Boolean landscape = sPrefs.getBoolean(landscapePreferencesKey, false);
        //checks if landscape mode in the settings is turned on
        if (landscape) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        //WLAN-Check
        sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String WLANPreferencesKey = getString(R.string.wlan_preferences_key);
        Boolean wlancheck = sPrefs.getBoolean(WLANPreferencesKey, false);

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // checks if the Wifi-only Option is turned on
        if (wlancheck) {

            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
           // checks if Wifi is enabled on the device
            if (!wifiManager.isWifiEnabled()) {
                // ask, if WLAN should be activated
                DialogFragment wlanActivateDialog = new WLANactivateDialogFragment();
                //makes sure, that the dialog is NOT closed after the user clicks on the back button in the navigation bar
                wlanActivateDialog.setCancelable(false);
                wlanActivateDialog.show(getFragmentManager(), "WLAN");
            } else {
                if (checkWLANConnection()) {
                    startStream();
                } else {
                    noWLAN();
                }
            }
        } else {
            NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                startStream();
            } else {
                DialogFragment noConnectionDialog = new NoConnectionDialogFragment();
                //makes sure, that the dialog is NOT closed after the user clicks on the back button in the navigation bar
                noConnectionDialog.setCancelable(false);
                noConnectionDialog.show(getFragmentManager(), "No connection");
            }
        }
    }

    /**
     * checks if the device is connected to a Wifi-Network
     * @return true if Connection exists, false if no connection exists
     */
    public boolean checkWLANConnection() {
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI &&
                    activeNetwork.isConnected()) {
                // connected to wifi
                return true;


            }
        }
        return false;
    }

    /**
     * calls a dialog to inform the user, that no Wifi-network is available
     */
    public void noWLAN() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.noWlan)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * starts the stream; is called when a network (Wifi-network if Wifi-only-option is turned on) is available
     */
    public void startStream() {
        // Find your VideoView in your video_main.xml layout
        videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask
        // Create a progressbar
        pDialog = new ProgressDialog(LiveStreamActivity.this);
        // Set progressbar title
        pDialog.setTitle(getString(R.string.title_activity_live_stream));
        // Set progressbar message
        pDialog.setMessage(getString(R.string.buffer_message));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    LiveStreamActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });

    }

    /**
     * activates the Wifi on the device, waits up to 10 seconds for a Wifi to connect.
     * if a Connection is available, then startStream() is called and this method returns;
     * If no connection is available within 10 seconds, noWLAN() is called to inform the user,
     * the Stream will not start then
     */
    public void activateWLAN() {
        wifiManager.setWifiEnabled(true);
        int i = 0;
        while (i < 10) {
            if (checkWLANConnection()) {
                startStream();
                return;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }

            i++;
        }
        noWLAN();
    }


    //Notification- und Navigation-Leiste werden ausgeblendet. Die Activity geht somit quasi in einen Vollbild-Modus
    //Wischt der User aus Notification- bzw. Navigation-Leiste, so werden diese für einen kurzen Moment wieder eingeblendet
    //LEIDER WERDEN HIER DIE STEUERUNGSELEMENTE DES VIDEOS ÜBERDECKT!!
  /*  @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = this.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }*/
}

