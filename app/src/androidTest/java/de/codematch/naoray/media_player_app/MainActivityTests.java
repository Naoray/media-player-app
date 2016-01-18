package de.codematch.naoray.media_player_app;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

/**
 * Created by Schmidt on 23.12.2015.
 */
public class MainActivityTests extends ActivityInstrumentationTestCase2<MainMenuActivity>  {

    public MainActivityTests() {
        super(MainMenuActivity.class);
    }

    public void testActivityExists() {
        MainMenuActivity activity = getActivity();
        assertNotNull(activity);

    }


}
