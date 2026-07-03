package com.mindwaresrl.common;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomRenderingTest {

    @Test
    void detectsSpaRootById() {
        var document = Jsoup.parse("<html><body><div id=\"root\"></div></body></html>");

        assertThat(DomRendering.evaluatesAsDynamic(document)).isTrue();
    }

    @Test
    void detectsAppRootById() {
        var document = Jsoup.parse("<html><body><div id=\"app\"></div></body></html>");

        assertThat(DomRendering.evaluatesAsDynamic(document)).isTrue();
    }

    @Test
    void detectsReactRootAttribute() {
        var document = Jsoup.parse("<html><body><div data-reactroot=\"\"></div></body></html>");

        assertThat(DomRendering.evaluatesAsDynamic(document)).isTrue();
    }

    @Test
    void detectsSuspiciouslyEmptyBodyWithScript() {
        var document = Jsoup.parse("<html><body>Loading...<script>doStuff();</script></body></html>");

        assertThat(DomRendering.evaluatesAsDynamic(document)).isTrue();
    }

    @Test
    void treatsSubstantialArticleWithoutScriptOrSpaRootAsStatic() {
        var articleText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
                + "Ut enim ad minim veniam, quis nostrud exercitation ullamco.";
        var document = Jsoup.parse("<html><body><p>" + articleText + "</p></body></html>");

        assertThat(DomRendering.evaluatesAsDynamic(document)).isFalse();
    }
}
