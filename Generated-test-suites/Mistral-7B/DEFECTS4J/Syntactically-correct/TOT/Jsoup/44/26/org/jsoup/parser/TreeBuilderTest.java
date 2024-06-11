import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Tokenizer;
import org.jsoup.helper.ParseErrorList;
import org.jsoup.helper.CharSequenceInput;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TreeBuilderTest {
    private TreeBuilder builder;

    @Before
    public void setUp() {
        String input = " <html><head></head><body><h1 id='test'>Test</h1></body></html>";
        String baseUri = "http://example.com/";
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        builder.initialiseParse(input, baseUri, ParseErrorList.noTracking());
    }

    @Test
    public void testParseWithValidInput() {
        builder.runParser();
        Document doc = builder.doc;
        Element root = doc.body();
        Element h1 = root.getElementById("test");

        assertEquals("Test", h1.text());
    }

    @Test
    public void testParseWithBaseUri() {
        String input = " <img src='image.png' alt='Test'>";
        String baseUri = "http://example.com/images/";
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.StartTag && token.name.equals("img")) {
                    Element img = (Element) token.getChild(0);
                    assertEquals("image.png", img.attr("src"));
                }
                return true;
            }
        };
        builder.initialiseParse(input, baseUri, ParseErrorList.noTracking());
        builder.runParser();

        Document doc = builder.doc;
        Element img = doc.select("img").first();
        assertEquals("image.png", img.attr("src"));
    }

    @Test
    public void testParseWithErrors() {
        String input = " <html><body><script type='text/javascript'>alert('Hello World')</script></body></html>";
        String baseUri = "http://example.com/";
        builder = new TreeBuilder() {
            @Override
            protected ParseErrorList errors;

            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.StartTag && token.name.equals("script")) {
                    errors.add(new ParseError("Scripting is not allowed", token.getLineNumber(), token.getColumnNumber()));
                }
                return true;
            }
        };
        builder.initialiseParse(input, baseUri, new ParseErrorList());
        builder.runParser();

        ArrayList<ParseError> parseErrors = (ArrayList<ParseError>) builder.errors;
        assertEquals(1, parseErrors.size());
        ParseError error = parseErrors.get(0);
        assertEquals("Scripting is not allowed", error.getMessage());
        assertEquals(1, error.getLineNumber());
        assertEquals(12, error.getColumnNumber());
    }

    @Test
    public void testProcessStartTagWithValidAttributes() {
        String input = "<button class='button' id='myButton' onclick='handleClick()'>Click me</button>";
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.StartTag && token.name.equals("button")) {
                    Element button = (Element) token.getChild(0);
                    Attributes attrs = button.attributes();
                    assertEquals("button", attrs.getClass().getName());
                    assertEquals("myButton", attrs.get("id"));
                    assertEquals("handleClick()", attrs.get("onclick"));
                }
                return true;
            }
        };
        builder.initialiseParse(input, "", ParseErrorList.noTracking());
        builder.runParser();

        Document doc = builder.doc;
        Element button = doc.select("button").first();
        Attributes attrs = button.attributes();
        assertEquals("myButton", attrs.get("id"));
        assertEquals("handleClick()", attrs.get("onclick"));
    }

    @Test
    public void testProcessStartTagWithEmptyAttributes() {
        String input = "<div></div>";
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.StartTag && token.name.equals("div")) {
                    Element div = (Element) token.getChild(0);
                    Attributes attrs = div.attributes();
                    assertTrue(attrs.isEmpty());
                }
                return true;
            }
        };
        builder.initialiseParse(input, "", ParseErrorList.noTracking());
        builder.runParser();

        Document doc = builder.doc;
        Element div = doc.select("div").first();
        Attributes attrs = div.attributes();
        assertTrue(attrs.isEmpty());
    }

    @Test
    public void testProcessEndTagWithValidName() {
        String input = "<html><head></head><body><h1 id='test'>Test</h1></body></html>";
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EndTag) {
                    Element parent = currentElement();
                    if (token.name.equals("h1")) {
                        assertEquals("h1", parent.tagName());
                    }
                }
                return true;
            }
        };
        builder.initialiseParse(input, "", ParseErrorList.noTracking());
        builder.runParser();

        Document doc = builder.doc;
        Element h1 = doc.select("h1").first();
        assertEquals("h1", h1.tagName());
    }

    @Test
    public void testProcessEndTagWithInvalidName() {
        String input = "<html><head></head><body><h1 id='test'>Test</h1></body></html>";
        builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EndTag) {
                    Element parent = currentElement();
                    if (token.name.equals("p")) {
                        assertNull(parent);
                    }
                }
                return true;
            }
        };
        builder.initialiseParse(input, "", ParseErrorList.noTracking());
        builder.runParser();

        Document doc = builder.doc;
        Element h1 = doc.select("h1").first();
        assertNull(h1.getParent());
    }
}