package org.kidinov.mvp_test.injection.module;


import android.app.Application;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmList;
import io.realm.RealmObject;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.kidinov.mvp_test.BuildConfig;
import org.kidinov.mvp_test.data.local.realm.RealmString;
import org.kidinov.mvp_test.data.remote.RibotsService;
import org.kidinov.mvp_test.data.remote.RxErrorHandlingCallAdapterFactory;
import org.kidinov.mvp_test.data.remote.UnauthorisedInterceptor;
import org.kidinov.mvp_test.util.RxEventBus;

@Module
public class NetworkModule {
    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, UnauthorisedInterceptor unauthorisedInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        builder.addInterceptor(unauthorisedInterceptor);

        return builder
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    UnauthorisedInterceptor provideUnathorizedInterceptor(RxEventBus rxEventBus) {
        return new UnauthorisedInterceptor(rxEventBus);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        Type token = new TypeToken<RealmList<RealmString>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getDeclaringClass().equals(RealmObject.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        builder.registerTypeAdapter(token, new TypeAdapter<RealmList<RealmString>>() {

            @Override
            public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                out.beginArray();
                for (RealmString realmString : value) {
                    out.value(realmString.getVal());
                }
                out.endArray();
            }

            @Override
            public RealmList<RealmString> read(JsonReader in) throws IOException {
                RealmList<RealmString> list = new RealmList<>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(new RealmString(in.nextString()));
                }
                in.endArray();
                return list;
            }
        });
        return builder.create();
    }

    @Provides
    @Singleton
    RibotsService provideRibotsService(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RibotsService.ENDPOINT)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit.create(RibotsService.class);
    }
}
