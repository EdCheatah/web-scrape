package com.mindwaresrl.model;

import lombok.Builder;

import java.net.URL;
import java.time.Duration;

@Builder
public record WebScrapeRequest(URL url, Duration timeout) {
    private static final Long MIN_TIMEOUT_SECONDS = 30L;
    private static final Long MAX_TIMEOUT_SECONDS = 300L;

    public static class WebScrapeRequestBuilder {
        private Duration timeout = Duration.ofSeconds(60);
    }

    public WebScrapeRequest {
        requireUrl(url);
        requireValidTimeout(timeout);
    }

    private static void requireUrl(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("Missing URL");
        }
    }

    private static void requireValidTimeout(Duration timeout) {
        if (timeout == null) {
            throw new IllegalArgumentException("Missing timeout");
        }
        if (timeout.compareTo(Duration.ofSeconds(MIN_TIMEOUT_SECONDS)) < 0) {
            throw new IllegalArgumentException("Timeout less than " + MIN_TIMEOUT_SECONDS + " seconds");
        }
        if (timeout.compareTo(Duration.ofSeconds(MAX_TIMEOUT_SECONDS)) > 0) {
            throw new IllegalArgumentException("Timeout greater than " + MAX_TIMEOUT_SECONDS + " seconds");
        }
    }
}
