package org.kidinov.mvp_test.data.remote;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;
import org.kidinov.mvp_test.data.model.Ribot;

public interface RibotsService {
    String ENDPOINT = "https://api.io/";

    @GET("ribots")
    Observable<List<Ribot>> getRibots();
}
