import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertTrue(document.title().equals("Test Page"));
        assertTrue(document.body().text().equals("Hello World"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyHtml() {
        String html = "";
        Document document = Jsoup.parse(html);
        fail("Expected IllegalArgumentException, but was " + document.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Document document = Jsoup.parse((String) null);
        fail("Expected IllegalArgumentException, but was " + document.toString());
    }

    @Test
    public void testParseWithInvalidHtml() {
        String html = "<html><body<p>Hello World</p>";
        try {
            Document document = Jsoup.parse(html);
            fail("Expected IllegalArgumentException, but was " + document.toString());
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMissingBaseUri() {
        String html = "<html><head><base href='/'><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        Document document = Jsoup.parse(html);
        try {
            Jsoup.parse(html, (String) null);
            fail("Expected IllegalArgumentException, but was not");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IOException.class)
    public void testParseWithIOException() throws IOException {
        File file = new File("non-existent.html");
        try {
            Document document = Jsoup.parse(file.getAbsolutePath());
            fail("Expected IOException, but was " + document.toString());
        } catch (IOException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedMimeTypeException.class)
    public void testParseWithUnsupportedMimeTypeException() throws IOException {
        File file = new File("image.jpg");
        try (InputStream inputStream = new FileInputStream(file)) {
            Document document = DataUtil.load(inputStream, null, file.getAbsolutePath());
            fail("Expected UnsupportedMimeTypeException, but was " + document.toString());
        } catch (UnsupportedMimeTypeException e) {
            // Expected
        }
    }

    @Test(expected = HttpStatusException.class)
    public void testParseWithHttpStatusException() throws IOException {
        URL url = new URL("http://example.com/error.html");
        try (Connection connection = HttpConnection.connect(url); InputStream inputStream = connection.inputStream()) {
            Document document = DataUtil.load(inputStream, null, url.toString());
            fail("Expected HttpStatusException, but was " + document.toString());
        } catch (HttpStatusException e) {
            // Expected
        }
    }
}