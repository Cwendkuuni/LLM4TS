import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import static org.jsoup.parser.Parser.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parse(html, baseUri);

        assertEquals(htmlTag, doc.tagName());
        assertEquals(baseUri, doc.baseUri());

        Element head = doc.head();
        assertEquals(titleTag, head.tagName());
        assertEquals("Test", head.text());

        Element body = doc.body();
        assertEquals(h1, body.tagName());
        assertEquals("Hello", body.text());
    }
}