import org.junit.After;
import org.junit.Before;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;

import java.util.ArrayList;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private Document doc;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        treeBuilder = new MyTreeBuilder(); // replace MyTreeBuilder with the actual subclass name
        errors = new ParseErrorList();
    }

    @After
    public void tearDown() {
        treeBuilder = null;
        doc = null;
    }

    @org.junit.Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        doc = treeBuilder.parse(input, "http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());

        Element html = doc.body();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());

        Element title = html.head().title();
        Assert.assertNotNull(title);
        Assert.assertEquals("Test", title.text());

        Element h1 = html.child(0);
        Assert.assertNotNull(h1);
        Assert.assertEquals("h1", h1.tagName());
        Assert.assertEquals("Hello", h1.text());
    }

    @org.junit.Test
    public void testProcessStartTag() {
        String input = "<html>";
        treeBuilder.initialiseParse(input, "http://example.com", errors);

        Token token = new Token();
        token.reset().type(Token.TokenType.START_TAG).name("html");
        treeBuilder.process(token);

        Element html = treeBuilder.currentElement();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
    }

    @org.junit.Test
    public void testProcessEndTag() {
        String input = "<html><body></html>";
        treeBuilder.initialiseParse(input, "http://example.com", errors);

        Element html = treeBuilder.parse(input, "http://example.com").body();
        Element body = treeBuilder.currentElement();

        treeBuilder.processStartTag("body");
        treeBuilder.processEndTag("body");

        Assert.assertNull(treeBuilder.currentElement());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testProcessError() {
        String input = "<html><body><p>Test<script>alert('Error')</script>";
        treeBuilder.initialiseParse(input, "http://example.com", errors);

        treeBuilder.runParser();
        Assert.assertFalse(errors.isEmpty());
    }
}

class MyTreeBuilder extends TreeBuilder {
    @Override
    protected boolean process(Token token) {
        if (token.type == Token.TokenType.START_TAG) {
            return processStartTag(token.name());
        } else if (token.type == Token.TokenType.END_TAG) {
            return processEndTag(token.name());
        } else {
            return false;
        }
    }

    @Override
    protected boolean processStartTag(String name) {
        return super.processStartTag(name);
    }

    @Override
    protected boolean processEndTag(String name) {
        return super.processEndTag(name);
    }
}