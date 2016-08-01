package org.kidinov.mvp_test.data.remote;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.kidinov.mvp_test.util.RxEventBus;
import org.kidinov.mvp_test.util.event.UnauthorizedResponse;

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
