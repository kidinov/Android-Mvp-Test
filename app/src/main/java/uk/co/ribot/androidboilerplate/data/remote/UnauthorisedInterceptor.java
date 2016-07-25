package uk.co.ribot.androidboilerplate.data.remote;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import uk.co.ribot.androidboilerplate.util.RxEventBus;
import uk.co.ribot.androidboilerplate.util.event.UnauthorizedResponse;

public class UnauthorisedInterceptor implements Interceptor {
    private final RxEventBus rxEventBus;

    public UnauthorisedInterceptor(RxEventBus rxEventBus) {
        this.rxEventBus = rxEventBus;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            rxEventBus.post(new UnauthorizedResponse());
        }
        return response;
    }
}
