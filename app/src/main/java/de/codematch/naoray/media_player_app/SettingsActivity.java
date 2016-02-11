package de.codematch.naoray.media_player_app;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

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
        //Here the background color of the SettingsActivity can be changed
        getListView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.background_color));
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

    //Ist erforderlich, damit das funktioniert:
    //Der Zurück-Pfeil in der ActionBar sorgt dafür, dass man zur Parent-Activity zurück kommt
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

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            // Defines what the deleteHistoryButton does when clicked
            Preference deleteHistoryButton = findPreference(getString(R.string.delete_email_history_preferences_key));
            deleteHistoryButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // Clears the emailAutocompleteList and the last entered email, so that the email field is empty again
                    SharedPreferences sPref = getPreferenceManager().getSharedPreferences();
                    SharedPreferences.Editor editor = sPref.edit();
                    editor.putStringSet("emailAutocompleteList", null);
                    editor.putString(getString(R.string.e_mail_address_preferences_key), "");
                    editor.apply();
                    Toast.makeText(getActivity(), getString(R.string.delete_email_history_preferences_toast), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }


    }
}

