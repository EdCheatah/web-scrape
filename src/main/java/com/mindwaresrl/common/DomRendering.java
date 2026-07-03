package com.mindwaresrl.common;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

@Slf4j
public class DomRendering {
    private static final int PROBE_TIMEOUT_MILLIS = 10000;

    /**
     * Tests if a website uses JavaScript to dynamically update its DOM
     * by comparing static HTML content with what a browser would render
     */
    public static boolean usesDynamicRendering(URL url) {
        try {
            return evaluatesAsDynamic(fetchStaticDocument(url));
        } catch (IOException e) {
            log.error("Error detecting dynamic rendering", e);
            return true;
        }
    }

    private static Document fetchStaticDocument(URL url) throws IOException {
        return Jsoup.connect(String.valueOf(url))
                .userAgent(FakeUserAgent.chrome())
                .timeout(PROBE_TIMEOUT_MILLIS)
                .get();
    }

    static boolean evaluatesAsDynamic(Document document) {
        return suspiciouslyEmpty(document) || hasSpaRoot(document);
    }

    private static boolean suspiciouslyEmpty(Document document) {
        int scriptTags = document.select("script").size();
        int bodyTextLength = document.body().text().trim().length();
        return bodyTextLength < 100 && scriptTags > 0;
    }

    private static boolean hasSpaRoot(Document document) {
        return !document.select("#root").isEmpty()
                || !document.select("#app").isEmpty()
                || !document.select("[data-reactroot]").isEmpty();
    }
}
