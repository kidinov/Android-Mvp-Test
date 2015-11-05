package uk.co.ribot.androidboilerplate.data.remote;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;
import uk.co.ribot.androidboilerplate.data.model.Ribot;

public interface RibotsService {

    String ENDPOINT = "https://api.ribot.io/";

    @GET("ribots")
    Observable<List<Ribot>> getRibots();
}
