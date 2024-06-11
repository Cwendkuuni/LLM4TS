package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = Jsoup.parse("<root><child><grandchild/></child></root>", "http://example.com", Parser.xmlParser());
    }

    @After
    public void tearDown() {
        if (doc != null) {
            doc.detach();
        }
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child><grandchild/></child></root>";
        Document parsedDoc = xmlTreeBuilder.parse(input, "http://example.com");
        assertEquals(doc.html(), parsedDoc.html());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("root");
        Element el = xmlTreeBuilder.insert(startTag);
        assertEquals("root", el.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token endTag = new Token.EndTag("root");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Node comment = xmlTreeBuilder.currentElement().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('A');
        xmlTreeBuilder.insert(characterToken);
        Node characterNode = xmlTreeBuilder.currentElement().child(0);
        assertEquals("A", characterNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", null, null);
        xmlTreeBuilder.insert(doctypeToken);
        DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentElement();
        assertEquals("html", documentTypeNode.nodeName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<child><grandchild/></child>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element childElement = (Element) nodes.get(0);
        Element grandchildElement = (Element) childElement.child(0);
        assertEquals("child", childElement.nodeName());
        assertEquals("grandchild", grandchildElement.nodeName());
    }
}