package testing.muhammed.com.asynchtaskloader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by muhammed on 10/13/2017.
 */

public class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {

    private final PackageManager packageManager;
    AppEntry.InterestingConfigChanges configChanges = new AppEntry.InterestingConfigChanges();
    List<AppEntry> mApps;
    private PackageIntentReceiver mPackageObserver;

    public AppListLoader(Context context) {
        super(context);
        packageManager = getContext().getPackageManager();
    }

    @Override
    public List<AppEntry> loadInBackground() {

        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);

        if (list == null) {
            list = new ArrayList<>();
        }


        List<AppEntry> entries = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            AppEntry appEntry = new AppEntry(this, list.get(i));

            appEntry.loadLabel(getContext());
            entries.add(appEntry);

        }

        Collections.sort(entries, AppEntry.ALPHA_COMPARATOR);

        return entries;
    }

    @Override
    public void deliverResult(List<AppEntry> data) {
        if (isReset()) {
            if (data != null) {
                onReleaseResources(data);
            }
        }

        List<AppEntry> oldApps = mApps;
        mApps = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldApps != null) {
            onReleaseResources(oldApps);
        }
    }

    @Override
    protected void onStartLoading() {

        if (mApps != null) {
            deliverResult(mApps);
        }

        if (mPackageObserver == null) {
            mPackageObserver = new PackageIntentReceiver(this);
        }

        boolean b = configChanges.applyNewConfig(getContext().getResources());

        if (takeContentChanged() || mApps == null || b) {
            forceLoad();
        }

    }

    protected void onReleaseResources(List<AppEntry> apps) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<AppEntry> data) {
        super.onCanceled(data);

        onReleaseResources(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        if (mApps != null) {
            onReleaseResources(mApps);
            mApps = null;
        }

        if (mPackageObserver != null) {
            getContext().unregisterReceiver(mPackageObserver);
            mPackageObserver = null;
        }
    }

}
