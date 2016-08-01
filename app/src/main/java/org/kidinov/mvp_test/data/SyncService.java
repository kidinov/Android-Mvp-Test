package org.kidinov.mvp_test.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import javax.inject.Inject;

import rx.Subscription;
import timber.log.Timber;
import org.kidinov.mvp_test.App;
import org.kidinov.mvp_test.data.remote.RetrofitException;
import org.kidinov.mvp_test.util.AndroidComponentUtil;
import org.kidinov.mvp_test.util.NetworkUtil;
import org.kidinov.mvp_test.util.RxUtil;

public class SyncService extends Service {
    @Inject
    DataManager dataManager;
    private Subscription subscription;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, SyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Timber.i("Starting sync...");

        if (!NetworkUtil.isNetworkConnected(this)) {
            Timber.i("Sync canceled, connection not available");
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        RxUtil.unsubscribe(subscription);
        subscription = dataManager.syncRibots()
                .doAfterTerminate(() -> stopSelf(startId))
                .subscribe(x -> {
                }, e -> {
                    RetrofitException retrofitException = (RetrofitException) e;
                    if (retrofitException.getKind() == RetrofitException.Kind.HTTP) {
                        Timber.e(e, "Server Error");
                    } else {
                        Timber.w(e, "Network Error");
                    }
                });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (subscription != null) subscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Timber.i("Connection is now available, triggering sync...");
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }

}