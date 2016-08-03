package org.kidinov.mvp_test.data.remote;

import org.kidinov.mvp_test.data.model.InstaFeed;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface InstaService {
    String ENDPOINT = "https://www.instagram.com/";

    @GET("divers_ru/media/")
    Observable<InstaFeed> getInstaFeed(@Query("max_id") String maxId);
}
