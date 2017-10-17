package testing.muhammed.com.asynchtaskloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by muhammed on 10/17/2017.
 */

public class PackageIntentReceiver extends BroadcastReceiver {
    private AppListLoader appListLoader;

    public PackageIntentReceiver(AppListLoader appListLoader) {

        this.appListLoader = appListLoader;
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        appListLoader.getContext().registerReceiver(this, filter);
        // Register for events related to sdcard installation.
        IntentFilter sdFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        appListLoader.getContext().registerReceiver(this, sdFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        appListLoader.onContentChanged();
    }
}
