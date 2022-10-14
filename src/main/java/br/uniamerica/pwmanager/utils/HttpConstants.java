package br.uniamerica.pwmanager.utils;

public final class HttpConstants {
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 300 * 60 * 60;

    public static final long REFRESH_TOKEN_VALIDITY_SECONDS = 1000 * 60 * 60;
    public static final String SIGNING_KEY = "secret";
    public static final String TOKEN_PREFIX = "Bearer ";

    private HttpConstants() { }
}
