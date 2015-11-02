package de.codematch.naoray.media_player_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String[] menuItems = {"Live Stream", "Mediathek", "Credits"};
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView menuView = (ListView) findViewById(R.id.menu_list);
        ArrayAdapter<String> menuViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        menuView.setAdapter(menuViewAdapter);
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    intent = new Intent(MainActivity.this, LiveStreamActivity.class);
                } else if (id == 1) {
                    intent = new Intent(MainActivity.this, MediathekActivity.class);
                } else if (id == 2) {
                    intent = new Intent(MainActivity.this, CreditsActivity.class);
                }
                startActivity(intent);
                //Toast.makeText(MainActivity.this, parent + " - " + id, Toast.LENGTH_SHORT).show();
            }
        });

        // Die Einstellung wird zusammen mit den anderen App-Einstellungen in einer Default SharedPreferences-Datei gespeichert. Wenn der Nutzer eine Einstellung ändert, aktualisiert das System den zum angegebenen Schlüssel passenden Wert in der SharedPreferences-Datei.
        // Auf die SharedPreferences-Datei sollte nur lesend zugegriffen werden.Das Speichern übernimmt das Android System.
        // Liest die Default SharedPreferences-Datei ein und ließt den Wert, der vom passenden Key (Key-Value-Paare) referenziert wird aus
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String benutzernamePreferencesKey = getString(R.string.benutzername_preferences_key);
        String benutzernamePreferencesDefault = getString(R.string.benutzername_preferences_default);
        String aktuellerBenutzername = sPrefs.getString(benutzernamePreferencesKey, benutzernamePreferencesDefault);

        String wunschtext = getString(R.string.wunschtext);
        TextView willkommenstextTextView = (TextView) findViewById(R.id.willkommenstextTextView);
        willkommenstextTextView.setText(willkommenstextTextView.getText() + ", " + aktuellerBenutzername + "!" + "\n" + wunschtext);
    }

    //Fügt das Menü in die Activity ein
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
