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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] menuItems = new String[2];
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItems[0] = getString(R.string.menu_item_1);
        menuItems[1] = getString(R.string.menu_item_2);

        final ListView menuView = (ListView) findViewById(R.id.menu_list);
        ArrayAdapter<String> menuViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItems);
        menuView.setAdapter(menuViewAdapter);
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (menuItems[(int) id]) {
                    case "Live Stream":
                        intent = new Intent(MainActivity.this, LiveStreamActivity.class);
                        break;
                    case "Mediathek":
                        intent = new Intent(MainActivity.this, MediathekActivity.class);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "An Error occurred", Toast.LENGTH_LONG).show();
                }
                if (intent != null) {
                    startActivity(intent);
                }
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
        switch (id) {

            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.credits:
                startActivity(new Intent(this, CreditsActivity.class));
                break;
            case R.id.hilfe:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
