package de.codematch.naoray.media_player_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    GridView gridView;
    Context context;
    private String[] menuItems = new String[2];
    private Intent intent;
    private SharedPreferences sPrefs;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager lmanager;
    private RecyclerView.Adapter CardAdapter;
    private ArrayList<Card> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Karten erstellen
        cards = new ArrayList<>();
        createCards();

        recycler = (RecyclerView) findViewById(R.id.recycler);
        lmanager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(lmanager);
        CardAdapter = new CardAdapter(cards, this);
        recycler.setAdapter(CardAdapter);
    }

    /**
     * Methode die aufgerufen wird, wenn auf eine Karte geklickt wird
     */
    public void onClick(int position) {
        switch (position) {
            //Programm1
            case 0:
                intent = new Intent(MainMenuActivity.this, LiveStreamActivity.class);
                break;
            //Programm2
            case 1:
                Toast.makeText(MainMenuActivity.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
                break;
            //Programm3
            case 2:
                Toast.makeText(MainMenuActivity.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
                break;
            //Programm4
            case 3:
                Toast.makeText(MainMenuActivity.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
                break;
            //Mediathek
            case 4:
                Toast.makeText(MainMenuActivity.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(MainMenuActivity.this, "An Error occurred", Toast.LENGTH_LONG).show();
        }

        if (intent != null) {
            startActivity(intent);
            intent = null;
        }

    }

    /**
     * Methode die aufgerufen wird, wenn auf die drei Punkte einer Karte geklickt wird
     */
    public void cardDescription(int position) {
        intent = new Intent(MainMenuActivity.this, CardDescriptionActivity.class);
        intent.putExtra("CardTitle", cards.get(position).getTitel());
        intent.putExtra("CardLargeDescription", cards.get(position).getLargeDescritption());
        intent.putExtra("CardColor", cards.get(position).getBackground());

        if (intent != null) {
            startActivity(intent);
            intent = null;
        }

    }

    /**
     * Methode zum Erstellen der Karten
     */
    public void createCards() {
        String largeDescription = "Dies ist eine lange Beschreibung. Dies ist eine lange Beschreibung. " +
                "Dies ist eine lange Beschreibung. Dies ist eine lange Beschreibung." +
                "Dies ist eine lange Beschreibung. Dies ist eine lange Beschreibung." +
                "Dies ist eine lange Beschreibung. Dies ist eine lange Beschreibung." +
                "Dies ist eine lange Beschreibung. Dies ist eine lange Beschreibung." +
                "Dies ist eine lange Beschreibung. Dies ist eine lange Beschreibung." +
                "Dies ist eine lange Beschreibung. Dies ist eine lange Beschreibung.";

        cards.add(new Card("Hauptprogramm", "Regiotainment", R.drawable.test, "#FF4081", largeDescription));
        cards.add(new Card("Programm 2", "Beschreibung 2", "#1976D2", largeDescription));
        cards.add(new Card("Programm 3", "Beschreibung 3", "#FF4081", largeDescription));
        cards.add(new Card("Programm 4", "Beschreibung 4", "#000000", largeDescription));
        cards.add(new Card("Mediathek", "Videos in Mediathek", "#CA0FEE", largeDescription));
    }

    //Fügt das Menü in die Activity ein
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_settings_menue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.menu_item_ausloggen:
                //Ruft eine neue Activity auf, löscht dabei den Back Stack und beendet die alte Activity komplett, damit man in der Login-Activity nicht durch Drücken auf "zurück" wieder in das Hauptmenü kommt
                if (sPrefs.getBoolean("LoginState", false)) {
                    SharedPreferences.Editor editor = sPrefs.edit();
                    editor.putBoolean("LoginState", false);
                    editor.apply();
                }
                Intent intent = new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                startActivity(intent);
                finish();
                break;
            case R.id.menu_item_einstellungen:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu_item_credits:
                startActivity(new Intent(this, CreditsActivity.class));
                break;
            case R.id.menu_item_hilfe:
                startActivity(new Intent(this, HelpActivity.class));

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Drückt man in dieser Activity 2x auf den Zurück-Pfeil in der Navigationsleiste(unten), so wird die App verlassen.
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //Die ursprüngliche Methode beendet die App
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.app_closing_warning), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
