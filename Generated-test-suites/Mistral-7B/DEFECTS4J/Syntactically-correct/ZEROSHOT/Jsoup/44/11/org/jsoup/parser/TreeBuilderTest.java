import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private Document doc;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        errors = new ParseErrorList();
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return super.process(token);
            }

            @Override
            protected boolean processStartTag(String name) {
                System.out.println("Found start tag: " + name);
                return true;
            }

            @Override
            protected boolean processEndTag(String name) {
                System.out.println("Found end tag: " + name);
                return true;
            }
        };
        treeBuilder.initialiseParse(input, "http://example.com", errors);
    }

    @After
    public void tearDown() {
        treeBuilder = null;
        doc = null;
    }

    @Test
    public void testParse() {
        treeBuilder.runParser();
        doc = treeBuilder.parse(treeBuilder.reader.input, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.head().childNodes().size());
        Element title = doc.head().childNodes().get(0);
        assertEquals("Test", title.text());
        assertEquals(1, doc.body().childNodes().size());
        Element h1 = doc.body().childNodes().get(0);
        assertEquals("Hello World", h1.text());
        assertEquals(4, doc.numTokens());
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullInput() {
        treeBuilder.parse(null, "http://example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        treeBuilder.parse("", null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullErrors() {
        treeBuilder.parse("", "", (ParseErrorList) null);
    }

    @Test
    public void testProcessStartTag() {
        treeBuilder.processStartTag("html");
        Element html = treeBuilder.currentElement();
        assertNotNull(html);
        assertEquals("html", html.tagName());

        treeBuilder.processStartTag("head");
        Element head = treeBuilder.currentElement();
        assertNotNull(head);
        assertEquals("head", head.tagName());

        treeBuilder.processStartTag("title");
        Element title = treeBuilder.currentElement();
        assertNotNull(title);
        assertEquals("title", title.tagName());
    }

    @Test
    public void testProcessEndTag() {
        treeBuilder.processStartTag("html");
        treeBuilder.processStartTag("head");
        treeBuilder.processStartTag("title");
        treeBuilder.processEndTag("title");
        Element title = treeBuilder.currentElement();
        assertNull(title);

        treeBuilder.processEndTag("head");
        Element head = treeBuilder.currentElement();
        assertNull(head);

        treeBuilder.processEndTag("html");
        Element html = treeBuilder.currentElement();
        assertNull(html);
    }
}