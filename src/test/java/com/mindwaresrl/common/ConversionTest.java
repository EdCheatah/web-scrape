package com.mindwaresrl.common;

import com.mindwaresrl.model.WebScrapeResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConversionTest {

    @Test
    void convertsPageWithTitleAndBodyContent() {
        var html = "<html><head><title>Sample Page</title></head>"
                + "<body><h1>Hello</h1><p>Some body text</p></body></html>";

        var result = Conversion.toWebScrapeResult(html);

        assertThat(result.title()).isEqualTo("Sample Page");
        assertThat(result.markdown()).isNotBlank().contains("Some body text");
    }

    @Test
    void stripsScriptAndStyleContentFromMarkdown() {
        var html = "<html><head><title>Sample Page</title></head>"
                + "<body><p>Visible text</p>"
                + "<script>var secret = 'script-payload';</script>"
                + "<style>.hidden { color: red; } /* style-payload */</style>"
                + "</body></html>";

        var result = Conversion.toWebScrapeResult(html);

        assertThat(result.markdown())
                .contains("Visible text")
                .doesNotContain("script-payload")
                .doesNotContain("style-payload");
    }

    @Test
    void returnsEmptyResultForBlankBody() {
        var html = "<html><head><title>Sample Page</title></head><body></body></html>";

        var result = Conversion.toWebScrapeResult(html);

        assertThat(result).isSameAs(WebScrapeResult.EMPTY_RESULT);
    }

    @Test
    void returnsEmptyResultForMissingTitle() {
        var html = "<html><head></head><body><p>Some body text</p></body></html>";

        var result = Conversion.toWebScrapeResult(html);

        assertThat(result).isSameAs(WebScrapeResult.EMPTY_RESULT);
    }
}
