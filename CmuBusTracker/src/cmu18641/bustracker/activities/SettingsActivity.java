package cmu18641.bustracker.activities;

import cmu18641.bustracker.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_REMOTE = "pref_remote";
    public static final String KEY_FAVORITES = "pref_favorites";

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Implementing changing settings in PreferenceActivities is deprecated
        //   but the recommended way - introduced in API-11 class PreferenceFragment -
        //   is not allowed by project requirements, which say that API-10 must be supported.
        //   Hence, we need to stick to deprecated method
        addPreferencesFromResource(R.xml.preferences);
        
    }
}
