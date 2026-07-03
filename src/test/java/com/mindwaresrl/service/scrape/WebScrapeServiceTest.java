package com.mindwaresrl.service.scrape;

import com.microsoft.playwright.PlaywrightException;
import com.mindwaresrl.model.WebScrapeRequest;
import com.mindwaresrl.model.WebScrapeResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class WebScrapeServiceTest {

    @Test
    void degradesToEmptyResultWhenStrategyThrowsIOException() throws Exception {
        var request = validRequest();
        WebScrape failingStrategy = req -> {
            throw new IOException("network unreachable");
        };

        var result = WebScrapeService.getWebScrapeResult(request, failingStrategy);

        assertThat(result).isSameAs(WebScrapeResult.EMPTY_RESULT);
    }

    @Test
    void degradesToEmptyResultWhenStrategyThrowsPlaywrightException() throws Exception {
        var request = validRequest();
        WebScrape failingStrategy = req -> {
            throw new PlaywrightException("navigation timeout");
        };

        var result = WebScrapeService.getWebScrapeResult(request, failingStrategy);

        assertThat(result).isSameAs(WebScrapeResult.EMPTY_RESULT);
    }

    private static WebScrapeRequest validRequest() throws Exception {
        return WebScrapeRequest.builder()
                .url(new URL("https://example.com"))
                .build();
    }
}
