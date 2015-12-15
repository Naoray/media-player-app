package de.codematch.naoray.media_player_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
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
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class LiveStreamActivity extends AppCompatActivity {


    // Declare variables
    ProgressDialog pDialog;
    WifiManager wifiManager;
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
        Boolean landscapeDefaultValue = false;
        Boolean landscape = sPrefs.getBoolean(landscapePreferencesKey, landscapeDefaultValue);
        if (landscape) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        //WLAN-Check
        sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String WLANPreferencesKey = getString(R.string.wlan_preferences_key);
        Boolean WLANDefaultValue = false;
        Boolean wlancheck = sPrefs.getBoolean(WLANPreferencesKey, WLANDefaultValue);

        if (wlancheck) {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                // Fragen ob WLAN aktiviert werden soll
                DialogFragment newFragment = new WLANactivateDialogFragment();
                newFragment.show(getFragmentManager(), "WLAN");
            }
            NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    startStream();

                } else  {
                    // no WLAN available
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
            }
        }
    }







        public void startStream(){
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
    public NetworkInfo findWlan(ConnectivityManager connManager)
        {
            Network[] networks = connManager.getAllNetworks();
        NetworkInfo networkInfo;
        for (Network a : networks) {
            networkInfo = connManager.getNetworkInfo(a);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return networkInfo;
            }
        }
        return null;
    }
    public void activateWLAN(){
        wifiManager.setWifiEnabled(true);
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

