import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ParserTest {

    @Test
    public void testParseHtml() {
        String html = "<html><head></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://localhost");
        assertNotNull(doc);
    }
}