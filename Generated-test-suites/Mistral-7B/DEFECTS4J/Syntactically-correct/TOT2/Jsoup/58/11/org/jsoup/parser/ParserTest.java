import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import static org.jsoup.parser.Parser.*;

public class ParserTest {
    @Test
    public void testParseInput() {
        // Given
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = htmlParser();
         // When
        Document document = parser.parseInput(html, baseUri);
         // Then
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseInput_TrackErrors() {
        // Given
        String html = "<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
        String baseUri = "http://example.com";
        Parser parser = htmlParser();
        parser.setTrackErrors(1);
         // When
        Document document = parser.parseInput(html, baseUri);
         // Then
        Assert.assertNotNull(document);
        Assert.assertEquals(1, parser.getErrors().size());
        Assert.assertEquals("Malformed HTML: unrecognized element: script", parser.getErrors().get(0).getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInput_NullHtml() {
        // Given
        Parser parser = htmlParser();
         // When & Then
        parser.parseInput(null, "http://example.com");
    }
}