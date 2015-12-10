package de.codematch.naoray.media_player_app;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    private Preference benutzernamePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.preferences);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //Der Zurück-Pfeil sorgt dafür, dass man zur Parent-Activity zurück kommt
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

/*    //Mit dem Rückgabewert "true" wird die Einstellung übernommen, mit "false" wird sie verworfen
    @Override
    public boolean onPreferenceChange(Preference preference, Object object) {
        if (preference == benutzernamePreference) {
            if (object.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.LeererBenutzernameEingegebenBenachrichtigung), Toast.LENGTH_LONG).show();
                return false;
            } else {
                //Mit String.format wird hier die Variable %1$s im String "Einstellung %1$s wurde geändert" durch den Titel der Einstellung ersetzt
                Toast.makeText(this, String.format(getString(R.string.BenutzernamenÄnderungsBenachrichtigung), getString(R.string.benutzername_preferences_title)), Toast.LENGTH_LONG).show();
                return true;
            }
        } else {
            return false;
        }
    }*/

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }

    }
}

