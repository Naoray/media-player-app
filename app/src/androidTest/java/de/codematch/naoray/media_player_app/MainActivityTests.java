package de.codematch.naoray.media_player_app;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTests extends ActivityInstrumentationTestCase2<MainMenuActivity> {

    public MainActivityTests() {
        super(MainMenuActivity.class);
    }

    public void testActivityExists() {
        MainMenuActivity activity = getActivity();
        assertNotNull(activity);

    }
}
