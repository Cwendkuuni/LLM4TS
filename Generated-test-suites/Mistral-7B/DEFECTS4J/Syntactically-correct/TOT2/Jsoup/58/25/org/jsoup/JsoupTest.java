import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class JsoupTest {
    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><body><a href=\"/test.html\">Test</a></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
        assertEquals("Test", document.select("a[href=\"/test.html\"]").text());
    }

    @Test
    public void testParseHtmlWithoutBaseUri() {
        String html = "<html><body><a href=\"/test.html\">Test</a></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals("Test", document.select("a[href=\"/test.html\"]").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        try {
            Document document = Jsoup.parse(html);
        } catch (IllegalArgumentException e) {
            assertEquals("HTML document must not be null", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testConnectToUrl() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        assertNotNull(document);
        assertEquals("Example Domain", document.title());
    }

    @Test(expected = MalformedURLException.class)
    public void testConnectToInvalidUrl() {
        String url = "invalid://example.com";
        try {
            Connection connection = Jsoup.connect(url);
        } catch (MalformedURLException e) {
            assertEquals("No protocol: invalid://", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><a href=\"/test.html\">Test</a></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(document);
        assertEquals("Test", document.select("a[href=\"/test.html\"]").text());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><body><a href=\"/test.html\">Test</a></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        try {
            Document document = Jsoup.parse(inputStream, "ISO-8859-1");
        } catch (IOException e) {
            assertEquals("Charset ISO-8859-1 is not a valid character encoding for this document", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParseFile() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        Document document = Jsoup.parse(file, "UTF-8");
        assertNotNull(document);
        assertEquals("Test", document.select("a[href=\"/test.html\"]").text());
    }

    @Test(expected = FileNotFoundException.class)
    public void testParseNonexistentFile() throws IOException {
        String filePath = "nonexistent.html";
        File file = new File(filePath);
        try {
            Document document = Jsoup.parse(file, "UTF-8");
        } catch (FileNotFoundException e) {
            assertEquals("file:" + filePath + " (No such file or directory)", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCleanHtml() {
        String html = "<html><body><script src=\"malicious.js\"></script></body></html>";
        Whitelist whitelist = Whitelist.defaultTextOnly();
        String cleanedHtml = Jsoup.clean(html, "", whitelist);
        assertNotNull(cleanedHtml);
        assertEquals("", cleanedHtml);
    }

    @Test
    public void testCleanHtmlWithBaseUri() {
        String html = "<html><body><script src=\"/malicious.js\"></script></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleTagNames("script");
        String cleanedHtml = Jsoup.clean(html, baseUri, whitelist);
        assertNotNull(cleanedHtml);
        assertEquals("", cleanedHtml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullHtml() {
        String html = null;
        try {
            String cleanedHtml = Jsoup.clean(html, "", new Whitelist());
        } catch (IllegalArgumentException e) {
            assertEquals("HTML document must not be null", e.getMessage());
            throw e;
        }
    }
}