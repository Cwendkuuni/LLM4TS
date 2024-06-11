import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.exceptions.JsoupException;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() throws JsoupException {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        Element title = doc.title();
        assertEquals("Hello World", title.text());
    }

    @Test
    public void testParseInput_TrackErrors() throws JsoupException {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = null; // should throw an exception
        List<ParseError> errors = parser.getErrors();
        assertEquals("Expected number of parse errors: 1", 1, errors.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInput_NullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "http://example.com");
    }

    // Add more test cases for other methods here...
}