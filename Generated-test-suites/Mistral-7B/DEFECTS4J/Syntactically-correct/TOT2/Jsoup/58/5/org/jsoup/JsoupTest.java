import org.junit.Test;
import org.jsoup.Jsoup;
import static org.junit.Assert.assertNotNull;

public class JsoupTest {
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
    }
}