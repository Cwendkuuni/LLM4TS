package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link XmlTreeBuilder}
 */
public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String inputXml;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        inputXml = "<root><child><subChild/><text>Hello World</text><anotherChild/></child></root>";
    }

    @Test
    public void testParse() {
        Document doc = xmlTreeBuilder.parse(new StringReader(inputXml), "http://test.com");
        assertNotNull(doc);
        assertEquals("<root><child><subChild/><text>Hello World</text><anotherChild/></child></root>", doc.html());
        List<Node> nodes = doc.childNodes();
        assertEquals(2, nodes.size());
        Node firstChild = nodes.get(0);
        assertEquals("child", firstChild.nodeName());
        List<Node> childNodes = ((Element) firstChild).childNodes();
        assertEquals(3, childNodes.size());
        Node subChild = childNodes.get(0);
        assertNotNull(subChild);
        assertEquals("subChild", subChild.nodeName());
        assertNull(subChild.text());
        Node textNode = childNodes.get(1);
        assertEquals("text", textNode.nodeName());
        assertEquals("Hello World", textNode.text());
        Node anotherChild = childNodes.get(2);
        assertNotNull(anotherChild);
        assertEquals("anotherChild", anotherChild.nodeName());
    }

    @Test
    public void testProcessTokenStartTag() {
        Token startTagToken = new Token(Token.TokenType.StartTag, "<root>", 0, 0, null, null, null, null);
        Element rootElement = xmlTreeBuilder.insert(startTagToken.asStartTag());
        assertEquals("root", rootElement.nodeName());
        assertNull(rootElement.attributes());
    }

    @Test
    public void testProcessTokenStartTagWithAttributes() {
        Token startTagToken = new Token(Token.TokenType.StartTag, "<child attr1='value1' attr2='value2'/>", 0, 0, null, null, null, null);
        Element childElement = xmlTreeBuilder.insert(startTagToken.asStartTag());
        List<Attribute> attributes = childElement.attributes();
        assertEquals(2, attributes.size());
        Attribute attr1 = attributes.get(0);
        assertEquals("attr1", attr1.getKey());
        assertEquals("value1", attr1.getValue());
        Attribute attr2 = attributes.get(1);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
    }

    @Test
    public void testProcessTokenEndTag() {
        Token endTagToken = new Token(Token.TokenType.EndTag, "</root>", 0, 0, null, null, null, null);
        xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
        assertNull(xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testProcessTokenComment() {
        Token commentToken = new Token(Token.TokenType.Comment, "-- This is a comment --", 0, 0, null, null, null, null);
        Comment comment = (Comment) xmlTreeBuilder.insert(commentToken);
        assertEquals("-- This is a comment --", comment.text());
    }

    @Test
    public void testProcessTokenCharacterData() {
        Token characterToken = new Token(Token.TokenType.Character, "Hello World", 0, 0, null, null, null, null);
        TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);
        assertEquals("Hello World", textNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessTokenUnexpectedTokenType() {
        Token unexpectedToken = new Token(Token.TokenType.Unknown, "Invalid Token", 0, 0, null, null, null, null);
        xmlTreeBuilder.process(unexpectedToken);
    }

    @Test
    public void testParseFragment() {
        List<Node> fragments = xmlTreeBuilder.parseFragment(inputXml, null, "http://test.com", new Parser());
        Node root = fragments.get(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        assertEquals("child", root.childNodes().get(0).nodeName());
        assertEquals(3, root.childNodes().get(0).childNodes().size());
    }
}