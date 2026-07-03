package com.mindwaresrl.common;

import com.mindwaresrl.model.WebScrapeResult;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

import java.time.Instant;

public class Conversion {

    public static WebScrapeResult toWebScrapeResult(String htmlContent) {
        var document = Jsoup.parse(htmlContent);
        var title = document.title();
        var markdown = htmlToMarkdown(extractCleanBody(document));

        if (markdown.isBlank() || title.isBlank()) {
            return WebScrapeResult.EMPTY_RESULT;
        }
        return new WebScrapeResult(markdown, title, Instant.now());
    }

    //Clean HTML using jsoup (remove scripts, events, unsafe tags)
    private static String extractCleanBody(Document document) {
        var bodyHtml = document.body().html();
        return Jsoup.clean(bodyHtml, Safelist.relaxed());
    }

    //Convert cleaned HTML → Markdown with Flexmark
    private static String htmlToMarkdown(String cleanBodyHtml) {
        FlexmarkHtmlConverter converter = FlexmarkHtmlConverter.builder().build();
        return converter.convert(cleanBodyHtml);
    }
}
