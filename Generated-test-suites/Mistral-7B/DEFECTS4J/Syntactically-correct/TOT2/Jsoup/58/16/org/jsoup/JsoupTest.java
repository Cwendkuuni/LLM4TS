import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;

public class JsoupTest {

    @Test
    public void testParseMethodWithStringAndBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals("<html><head><base href=\"http://example.com\"></base></head><body><h1>Hello World</h1></body></html>", document.html());
    }
}