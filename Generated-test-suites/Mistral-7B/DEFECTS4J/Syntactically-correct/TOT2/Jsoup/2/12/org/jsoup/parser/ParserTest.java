import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Title</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("Title", doc.title().text());
        assertEquals("Hello World", doc.body().text());
    }
}