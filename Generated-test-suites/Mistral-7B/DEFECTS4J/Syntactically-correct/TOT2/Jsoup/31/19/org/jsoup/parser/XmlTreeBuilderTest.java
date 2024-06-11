import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.ParseErrorList;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Test case 1: Basic functionality
        String input = "<root><child/></root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Document doc = xmlTreeBuilder.getDocument();
        assertEquals("root", doc.tagName());
        assertTrue(doc.childNodes().size() > 0);
        assertNull(errors.size());

        // Test case 2: Empty input
        input = "";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(0, xmlTreeBuilder.getDocument().childNodes().size());
        assertEquals(1, errors.size());

        // Test case 3: Invalid XML input
        input = "<root><child><";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(0, xmlTreeBuilder.getDocument().childNodes().size());
        assertEquals(1, errors.size());

        // Test case 4: Processing start tag
        XmlTreeBuilder xmlTreeBuilder2 = new XmlTreeBuilder();
        xmlTreeBuilder2.initialiseParse(input, baseUri, errors);
        Tokenizer tokenizer = new Tokenizer(input);
        Token token = tokenizer.nextToken();
        xmlTreeBuilder2.process(token);
        assertEquals(0, xmlTreeBuilder2.getDocument().childNodes().size());
        assertEquals(1, errors.size());
    }

    @Test
    public void testProcessStartTag() {
        // Test case 1: Basic functionality
        String input = "<root><child/></root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tokenizer tokenizer = new Tokenizer(input);
        Token token = tokenizer.nextToken();

        xmlTreeBuilder.process(token);

        Document doc = xmlTreeBuilder.getDocument();
        Element root = doc.getFirstElement();
        assertEquals("root", root.tagName());
        assertTrue(root.childNodes().size() > 0);
        assertNull(errors.size());

        // Test case 2: Unknown tag
        input = "<unknown/>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        tokenizer = new Tokenizer(input);
        token = tokenizer.nextToken();
        xmlTreeBuilder.process(token);
        Document doc2 = xmlTreeBuilder.getDocument();
        assertEquals(0, doc2.childNodes().size());
        assertEquals(1, errors.size());

        // Test case 3: Self-closing tag
        input = "<tag/>";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        tokenizer = new Tokenizer(input);
        token = tokenizer.nextToken();
        xmlTreeBuilder.process(token);
        Document doc3 = xmlTreeBuilder.getDocument();
        Element tag = doc3.getFirstElement();
        assertTrue(tag.isSelfClosing());
        assertEquals(0, doc3.childNodes().size());
        assertNull(errors.size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.getDocument();
        Element root = doc.addElement("root");

        Node node = new TextNode("Test");
        xmlTreeBuilder.insertNode(node);

        Element el = (Element) root.lastChild();
        assertEquals("Test", el.text());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.getDocument();
        Element root = doc.addElement("root");
        Element child = doc.addElement("child");
        Element grandChild = doc.addElement("grandChild");

        xmlTreeBuilder.pushElementToStack(child);
        xmlTreeBuilder.pushElementToStack(grandChild);

        xmlTreeBuilder.popStackToClose(child.tagName());

        assertEquals(1, doc.childNodes().size());
        assertEquals("root", doc.getFirstElement().tagName());
    }

    @Test(expected = RuntimeException.class)
    public void testPopStackToCloseStackUnderflow() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.getDocument();

        xmlTreeBuilder.popStackToClose("root");
    }

    @Test
    public void testProcessComment() {
        String input = "<!---Test-->";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tokenizer tokenizer = new Tokenizer(input);
        Token token = tokenizer.nextToken();

        while (token.type != Token.COMMENT) {
            xmlTreeBuilder.process(token);
            token = tokenizer.nextToken();
        }

        Document doc = xmlTreeBuilder.getDocument();
        Comment comment = (Comment) doc.getFirstChild();
        assertEquals("Test", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<root>&#x1F600;</root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tokenizer tokenizer = new Tokenizer(input);
        Token token = tokenizer.nextToken();

        while (token.type != Token.Character) {
            xmlTreeBuilder.process(token);
            token = tokenizer.nextToken();
        }

        Document doc = xmlTreeBuilder.getDocument();
        Element root = doc.getFirstElement();
        TextNode textNode = (TextNode) root.childNodes().get(0);
        assertEquals("🎉", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Tokenizer tokenizer = new Tokenizer(input);
        Token token = tokenizer.nextToken();

        while (token.type != Token.Doctype) {
            xmlTreeBuilder.process(token);
            token = tokenizer.nextToken();
        }

        Document doc = xmlTreeBuilder.getDocument();
        DocumentType doctypeNode = (DocumentType) doc.getFirstChild();
        assertEquals("html", doctypeNode.name());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctypeNode.publicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctypeNode.systemIdentifier());
    }
}