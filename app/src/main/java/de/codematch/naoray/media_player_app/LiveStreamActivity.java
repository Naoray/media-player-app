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
        if (landscape) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        //WLAN-Check
        sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String WLANPreferencesKey = getString(R.string.wlan_preferences_key);
        Boolean wlancheck = sPrefs.getBoolean(WLANPreferencesKey, false);

        if (wlancheck) {
            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                // ask, if WLAN shoeld be activated
                DialogFragment newFragment = new WLANactivateDialogFragment();
                newFragment.show(getFragmentManager(), "WLAN");
            } else {
                if (checkWLANConnection()) {
                    startStream();
                } else {
                    noWLAN();
                }
            }
        } else {

            startStream();
        }
    }

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

    // no WLAN available
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

