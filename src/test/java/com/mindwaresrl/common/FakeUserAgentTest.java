package com.mindwaresrl.common;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class FakeUserAgentTest {

    @Test
    void chromeReturnsNonEmptyChromeUserAgent() {
        assertThat(FakeUserAgent.chrome()).isNotBlank().contains("Chrome/");
    }

    @Test
    void firefoxReturnsNonEmptyFirefoxUserAgent() {
        assertThat(FakeUserAgent.firefox()).isNotBlank().contains("Firefox/");
    }

    @Test
    void safariReturnsNonEmptySafariUserAgent() {
        assertThat(FakeUserAgent.safari()).isNotBlank().contains("Version/");
    }

    @Test
    void edgeReturnsNonEmptyEdgeUserAgent() {
        assertThat(FakeUserAgent.edge()).isNotBlank().contains("Edg/");
    }

    @Test
    void randomNeverReturnsNullOrBlank() {
        IntStream.range(0, 50).forEach(i ->
                assertThat(FakeUserAgent.random()).isNotNull().isNotBlank());
    }
}
