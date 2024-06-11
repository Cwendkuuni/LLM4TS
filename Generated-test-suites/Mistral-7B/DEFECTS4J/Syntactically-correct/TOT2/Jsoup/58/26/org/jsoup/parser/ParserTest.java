import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class ParserTest {
    @Test
    public void testParseInput() {
        // Basic test case
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = new Parser(new HtmlTreeBuilder()).parseInput(html, null);
        assertNotNull(document);

        // Edge case: empty input
        Document emptyDocument = new Parser(new HtmlTreeBuilder()).parseInput("", null);
        assertNotNull(emptyDocument);

        // Edge case: null input
        try {
            new Parser(new HtmlTreeBuilder()).parseInput(null, null);
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Edge case: invalid input (malformed HTML)
        String invalidHtml = "<html<body>";
        try {
            new Parser(new HtmlTreeBuilder()).parseInput(invalidHtml, null);
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Exception handling: parse errors
        String parseErrorHtml = "<html><body><script>throw new Error('Test Error')</script></body></html>";
        List<ParseError> parseErrors = new Parser(new HtmlTreeBuilder())
                .setTrackErrors(1)
                .parseInput(parseErrorHtml, null)
                .getErrors();
        assert parseErrors.size() == 1;
    }
}