package lv.st.sbogdano.redditreader.ui.login;

import android.content.pm.PackageManager;

interface AppInfoProvider {

    AppInfo provide() throws PackageManager.NameNotFoundException;

}
