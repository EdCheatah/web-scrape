package com.mindwaresrl.model;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class WebScrapeRequestTest {

    private static final URL VALID_URL = url("https://example.com");

    @Test
    void builderWithOnlyUrlUsesDefaultSixtySecondTimeout() {
        WebScrapeRequest request = WebScrapeRequest.builder().url(VALID_URL).build();

        assertThat(request.timeout()).isEqualTo(Duration.ofSeconds(60));
    }

    @Test
    void timeoutBelowThirtySecondsIsRejected() {
        var builder = WebScrapeRequest.builder().url(VALID_URL).timeout(Duration.ofSeconds(29));

        assertThatIllegalArgumentException().isThrownBy(builder::build);
    }

    @Test
    void timeoutAboveThreeHundredSecondsIsRejected() {
        var builder = WebScrapeRequest.builder().url(VALID_URL).timeout(Duration.ofSeconds(301));

        assertThatIllegalArgumentException().isThrownBy(builder::build);
    }

    @Test
    void timeoutOfThirtySecondsIsValid() {
        WebScrapeRequest request = WebScrapeRequest.builder().url(VALID_URL).timeout(Duration.ofSeconds(30)).build();

        assertThat(request.timeout()).isEqualTo(Duration.ofSeconds(30));
    }

    @Test
    void timeoutOfThreeHundredSecondsIsValid() {
        WebScrapeRequest request = WebScrapeRequest.builder().url(VALID_URL).timeout(Duration.ofSeconds(300)).build();

        assertThat(request.timeout()).isEqualTo(Duration.ofSeconds(300));
    }

    @Test
    void nullUrlIsRejected() {
        var builder = WebScrapeRequest.builder();

        assertThatIllegalArgumentException().isThrownBy(builder::build);
    }

    private static URL url(String value) {
        try {
            return URI.create(value).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }
}
