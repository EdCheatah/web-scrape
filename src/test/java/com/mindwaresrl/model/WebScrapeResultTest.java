package com.mindwaresrl.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class WebScrapeResultTest {

    @Test
    void blankMarkdownIsRejected() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new WebScrapeResult(" ", "title", Instant.now()));
    }

    @Test
    void blankTitleIsRejected() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new WebScrapeResult("markdown", " ", Instant.now()));
    }

    @Test
    void nullTimestampIsRejected() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new WebScrapeResult("markdown", "title", null));
    }

    @Test
    void emptyResultIsNotNullAndKeepsSentinelValues() {
        assertThat(WebScrapeResult.EMPTY_RESULT).isNotNull();
        assertThat(WebScrapeResult.EMPTY_RESULT.markdown()).isEqualTo("undefined");
        assertThat(WebScrapeResult.EMPTY_RESULT.title()).isEqualTo("undefined");
        assertThat(WebScrapeResult.EMPTY_RESULT.timestamp()).isEqualTo(Instant.EPOCH);
    }
}
