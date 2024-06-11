import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest {
    // Test for the constructor
    @Test
    public void testParserConstructor() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    // Test for the parseInput method with valid input
    @Test
    public void testParseInputValid() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", null);
        assertNotNull(document);
        assertEquals("Hello World", document.head().title().text());
    }

    // Test for the parseInput method with empty input
    @Test
    public void testParseInputEmpty() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("", null);
        assertNotNull(document);
        assertNull(document.body());
    }

    // Test for the parseInput method with invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testParseInputInvalid() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("", null); // empty input is valid, but this test checks for the IllegalArgumentException thrown for other invalid inputs
        assertThrows(IllegalArgumentException.class, () -> parser.parseInput("", null)); // test for empty input again for good measure
        assertThrows(IllegalArgumentException.class, () -> parser.parseInput("invalidInput", null));
    }

    // Test for the getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    // Test for the isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    // Test for the setTrackErrors method
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    // Test for the getErrors method
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
    }

    // Test for the parse static method
    @Test
    public void testParseStaticMethod() {
        Document document = Parser.parse("<html><body><h1>Hello World</h1></body></html>", null);
        assertNotNull(document);
        assertEquals("Hello World", document.head().title().text());
    }
}