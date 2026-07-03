# web-scrape: uso como dependencia

Librería genérica que convierte una URL en Markdown limpio. Elige sola entre estrategia estática (JSoup) y dinámica (Playwright, para páginas que renderizan con JavaScript). No sigue enlaces: procesa la URL que se le pasa.

## Requisitos

- Java 21 o superior
- Maven 3.9 o superior
- Navegadores de Playwright instalados (solo para la estrategia dinámica)

## Instalar como dependencia

Desde la raíz del fork, compilar e instalar en el repositorio local `.m2`:

```bash
mvn clean install
```

Eso deja el artefacto disponible para otros proyectos. En el `pom.xml` del proyecto consumidor:

```xml
<dependency>
    <groupId>com.mindwaresrl</groupId>
    <artifactId>web-scrape</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Navegadores de Playwright

La estrategia dinámica lanza Chromium. Configurar la ruta de navegadores e instalarlos una vez (el README tiene el detalle):

```bash
# variable de entorno
PLAYWRIGHT_BROWSERS_PATH=<ruta-a-navegadores>

# instalación (basta Chromium, que es el navegador que se usa)
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"
```

Sin este paso, las URLs que requieran render dinámico fallan y devuelven un resultado vacío.

## Uso

Punto de entrada: `WebScrapeService.scrape(WebScrapeRequest)` devuelve un `WebScrapeResult`.

```java
URL url = URI.create("https://example.org").toURL();

var request = WebScrapeRequest.builder()
        .url(url)
        .timeout(Duration.ofSeconds(90))   // opcional, por defecto 60s
        .build();

var result = new WebScrapeService().scrape(request);

if (result == WebScrapeResult.EMPTY_RESULT) {
    // la fuente falló o no tenía contenido usable: se descarta, el resto sigue
} else {
    String markdown = result.markdown();
    String title = result.title();
    Instant extractedAt = result.timestamp();
}
```

## Timeout

Se pasa en el builder como `Duration`. Rango válido: 30 a 300 segundos. Por defecto, 60 segundos. Un valor fuera de rango lanza `IllegalArgumentException` al construir el request, no en tiempo de scraping.

## Semántica de fallo

El servicio nunca rompe al llamador por un fallo de scraping: degrada a `WebScrapeResult.EMPTY_RESULT` y lo registra en el log. Devuelven `EMPTY_RESULT`:

- Errores de red o de navegación: `IOException` (HTTP no exitoso, timeout de JSoup) y `PlaywrightException` (timeout de navegación, errores de render).
- Páginas sin contenido usable: body vacío o página sin `<title>`.

El fallo se detecta comparando por identidad: `result == WebScrapeResult.EMPTY_RESULT`.

## Concurrencia

La librería usa un único `Browser` Chromium compartido. Playwright Java no es thread-safe: `Playwright`, `Browser`, `BrowserContext` y `Page` deben usarse en el hilo que creó `Playwright`. Por eso conviene hacer un scrape dinámico a la vez sobre el browser compartido, y no paralelizar scrapes dinámicos sobre esa instancia. La ruta estática (JSoup) sí es segura para uso concurrente y paraleliza sin problema.
