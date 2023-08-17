package com.itblee.constant;

public final class SystemConstants {

    public static final boolean DEBUG_PRINT_RESULTS = true;

    public static final int ACTIVE_STATUS = 1;
    public static final int INACTIVE_STATUS = 0;
    public static final String SPRING_ROLE = "ROLE_";

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 3600000;
    public static final String SIGNING_KEY = "itblee";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";

    private SystemConstants() {
        throw new AssertionError();
    }
}
