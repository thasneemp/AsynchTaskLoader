package testing.muhammed.com.asynchtaskloader;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

/**
 * Created by muhammed on 10/13/2017.
 */

public class AppEntry {
    /**
     * Perform alphabetical comparison of application entry objects.
     */
    public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppEntry object1, AppEntry object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };
    //    private final AppListLoader mLoader;
    private final ApplicationInfo mInfo;
    private final File mApkFile;
    private final AppListLoader listLoader;
    private String mLabel;
    private Drawable mIcon;
    private boolean mMounted;

    public AppEntry(AppListLoader listLoader, ApplicationInfo info) {

        this.listLoader = listLoader;
        mInfo = info;
        mApkFile = new File(info.sourceDir);
    }

    public ApplicationInfo getApplicationInfo() {
        return mInfo;
    }

    public String getLabel() {
        return mLabel;
    }

    public Drawable getIcon() {
        if (mIcon == null) {
            if (mApkFile.exists()) {
//                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            // If the app wasn't mounted but is now mounted, reload
            // its icon.
            if (mApkFile.exists()) {
                mMounted = true;
//                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            }
        } else {
            return mIcon;
        }

//        return mLoader.getContext().getResources().getDrawable(
//                android.R.drawable.sym_def_app_icon);
        return null;
    }

    void loadLabel(Context context) {
        if (mLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mLabel = mInfo.packageName;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(context.getPackageManager());
                mLabel = label != null ? label.toString() : mInfo.packageName;
            }
        }
    }

    public static class InterestingConfigChanges {
        final Configuration mLastConfiguration = new Configuration();
        int mLastDensity;

        boolean applyNewConfig(Resources res) {
            int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
            boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
            if (densityChanged || (configChanges & (ActivityInfo.CONFIG_LOCALE
                    | ActivityInfo.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
                mLastDensity = res.getDisplayMetrics().densityDpi;
                return true;
            }
            return false;
        }
    }
}
