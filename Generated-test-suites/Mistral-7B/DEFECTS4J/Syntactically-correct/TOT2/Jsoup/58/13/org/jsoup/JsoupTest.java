import org.junit.Test;
import org.jsoup.Jsoup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsoupTest {

    @Test
    public void testParseMethodWithBaseUri() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Jsoup.parse(html, baseUri);
        assertEquals("Test Page", document.title());
        assertEquals("Hello World", document.select("h1").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMethodWithNullBaseUri() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = null;

        try {
            Jsoup.parse(html, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri cannot be null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMethodWithEmptyBaseUri() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "";

        try {
            Jsoup.parse(html, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri cannot be empty", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParseMethodWithEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";

        Document document = Jsoup.parse(html, baseUri);
        assertNull(document);
    }

    @Test
    public void testParseMethodWithInvalidHtml() {
        String html = "<html><body>"; // missing head tag
        String baseUri = "http://example.com";

        try {
            Jsoup.parse(html, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("Error parsing input as HTML: no root element", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IOException.class)
    public void testParseMethodWithIOException() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        try {
            Jsoup.parse(new File("non-existent-file.html"), baseUri);
        } catch (IOException e) {
            assertEquals("non-existent-file.html (No such file or directory)", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParseMethodWithNullDocument() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = null;
        try {
            document = Jsoup.parse(html, baseUri);
        } catch (IllegalArgumentException e) {
            // This should not happen as html and baseUri are valid
        }

        assertNull(document);
    }
}