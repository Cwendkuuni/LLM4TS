import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private Document doc;
    private ParseErrorList errors;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(String name) {
                // Override this method for testing purpose
                System.out.println("Processing start tag: " + name);
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                // Override this method for testing purpose
                System.out.println("Processing end tag: " + name);
                return true;
            }
        };
        doc = null;
        errors = ParseErrorList.noTracking();
    }

    @Test
    public void testParseDocument() {
        String input = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        doc = treeBuilder.parse(input, "http://test.com");

        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertTrue(doc.body().text().equals("Hello World!"));
    }

    @Test
    public void testParseDocumentWithErrors() {
        String input = "<html><head><title>Test</title>"; // Missing closing tag
        errors = new ParseErrorList();
        doc = treeBuilder.parse(input, "http://test.com", errors);

        assertNotNull(doc);
        assertNull(doc.title());
        assertTrue(errors.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        treeBuilder.parse(null, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        treeBuilder.parse("", null);
    }

    @Test
    public void testProcessStartTag() {
        String input = "<html>";
        treeBuilder.processStartTag("html", new Attributes());

        Element current = treeBuilder.currentElement();
        assertNotNull(current);
        assertTrue(current.tagName().equals("html"));
    }

    @Test
    public void testProcessEndTag() {
        String input = "<html><body></body></html>";
        treeBuilder.initialiseParse(input, "http://test.com", ParseErrorList.noTracking());
        treeBuilder.runParser();

        Element body = treeBuilder.currentElement();
        treeBuilder.processEndTag("body");

        Element current = treeBuilder.currentElement();
        assertNull(current);
    }
}