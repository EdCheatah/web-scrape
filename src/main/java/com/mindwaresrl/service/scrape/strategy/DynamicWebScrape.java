package com.mindwaresrl.service.scrape.strategy;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import com.mindwaresrl.common.Conversion;
import com.mindwaresrl.common.FakeUserAgent;
import com.mindwaresrl.common.WebScrapePlaywrightManager;
import com.mindwaresrl.model.WebScrapeRequest;
import com.mindwaresrl.model.WebScrapeResult;
import com.mindwaresrl.service.scrape.WebScrape;

import java.io.IOException;

public class DynamicWebScrape implements WebScrape {

    @Override
    public WebScrapeResult execute(WebScrapeRequest webScrapeRequest) throws IOException {
        Browser browser = WebScrapePlaywrightManager.browser();
        try (BrowserContext context = newContext(browser)) {
            return Conversion.toWebScrapeResult(renderHtml(context, webScrapeRequest));
        }
    }

    private static BrowserContext newContext(Browser browser) {
        return browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setUserAgent(FakeUserAgent.chrome()));
    }

    private static String renderHtml(BrowserContext context, WebScrapeRequest webScrapeRequest) {
        Page page = context.newPage();
        page.navigate(String.valueOf(webScrapeRequest.url()), navigateOptions(webScrapeRequest));
        return page.content();
    }

    private static Page.NavigateOptions navigateOptions(WebScrapeRequest webScrapeRequest) {
        return new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                .setTimeout(webScrapeRequest.timeout().toMillis());
    }
}
