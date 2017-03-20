package com.ufonaut.twittertestapp.api;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.ufonaut.twittertestapp.Constants.AUTH_HEADER;
import static com.ufonaut.twittertestapp.Constants.BASIC_SUFFIX;
import static com.ufonaut.twittertestapp.Constants.BEARER_SUFFIX;
import static com.ufonaut.twittertestapp.Constants.BLANK;
import static com.ufonaut.twittertestapp.Constants.LOGIN_URL;

public class Headers implements Interceptor {

    private String accessToken;
    private static final String BEARER_TOKEN_CREDENTIALS_ENCODED =
            "ZlpLQk8wMW9qV3FhMnJyaGgwQjd1c1BzaTp1WG8wWGlFTnRLalVPdUdEWktTb3RLekNYOEMzU2pnQ3VHbmlQOE9ueng4WUIwR25BNQ==";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        if (original.urlString().contains(LOGIN_URL))
            builder.addHeader(AUTH_HEADER, BASIC_SUFFIX + BLANK + BEARER_TOKEN_CREDENTIALS_ENCODED);
        if (accessToken != null)
            builder.addHeader(AUTH_HEADER, BEARER_SUFFIX + BLANK + accessToken);
        return chain.proceed(builder.build());
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean hasAccessToken() {
        return accessToken != null;
    }
}
