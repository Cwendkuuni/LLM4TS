import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Given
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        Document document = parser.parseInput(html, baseUri);

        // Then
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseInputWithErrors() {
        // Given
        String html = "<html><body><script>throw new Error('Test Error');</script></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When & Then
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Test Error", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        // Given
        String html = "<html><body><script>throw new Error('Test Error 1');</script><script>throw new Error('Test Error 2');</script></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);

        // When
        Document document = parser.parseInput(html, baseUri);

        // Then
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(2, errors.size());
        Assert.assertEquals("Test Error 1", errors.get(0).getMessage());
        Assert.assertEquals("Test Error 2", errors.get(1).getMessage());
    }
}