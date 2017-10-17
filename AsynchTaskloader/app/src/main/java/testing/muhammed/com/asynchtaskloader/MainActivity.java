package testing.muhammed.com.asynchtaskloader;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AppEntry>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportLoaderManager().initLoader(0, null, this);

    }


    @Override
    public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
        return new AppListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {

        for (AppEntry appEntry : data) {
            String label = appEntry.getLabel();

            Log.d("APP_LABEL", "onLoadFinished: " + label);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<AppEntry>> loader) {

    }
}
