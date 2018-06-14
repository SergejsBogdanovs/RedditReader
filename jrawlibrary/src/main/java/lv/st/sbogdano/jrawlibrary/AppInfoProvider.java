package lv.st.sbogdano.jrawlibrary;

import android.content.pm.PackageManager;

interface AppInfoProvider {

    AppInfo provide() throws PackageManager.NameNotFoundException;

}
