package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * Test class for {@link XmlTreeBuilder}
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder.defaultSettings());
        assertTrue(xmlTreeBuilder.defaultSettings() instanceof ParseSettings);
    }

    @Test
    public void testParseEmptyString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("", "");
        assertNull(document);
    }

    @Test
    public void testParseEmptyDocumentWithBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("", "baseUri");
        assertNotNull(document);
        assertTrue(document.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.doc);
        assertTrue(xmlTreeBuilder.doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
        assertTrue(xmlTreeBuilder.stack.size() == 1);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token);
        Element rootElement = (Element) xmlTreeBuilder.stack.get(0);
        Element childElement = rootElement.child(0);
        assertNotNull(childElement);
        assertEquals("child", childElement.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token); // start tag
        token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token); // end tag
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!--comment-->";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token);
        Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertNotNull(comment);
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root>&gt;<child/></root>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token); // &gt; character
        TextNode textNode = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertNotNull(textNode);
        assertEquals(">", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE root SYSTEM \"file.dtd\">";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertNotNull(documentType);
        assertEquals("root", documentType.nodeName());
        assertEquals("file.dtd", documentType.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element rootElement = new Element("root", "", null);
        xmlTreeBuilder.insertNode(rootElement);
        Element childElement = new Element("child", "", null);
        xmlTreeBuilder.insertNode(childElement);
        List<Node> nodeList = rootElement.childNodes();
        assertNotNull(nodeList);
        assertTrue(nodeList.size() == 1);
        assertEquals(childElement, nodeList.get(0));
    }

    @Test
    public void testInsertElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/></root>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token); // start tag
        Element rootElement = (Element) xmlTreeBuilder.stack.get(0);
        Element childElement = xmlTreeBuilder.insert(token.asStartTag());
        assertNotNull(childElement);
        assertEquals("child", childElement.nodeName());
        assertEquals(1, rootElement.childNodes().size());
        assertEquals(childElement, rootElement.childNodes().get(0));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!--comment-->";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token);
        Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertNotNull(comment);
        assertEquals("comment", xmlTreeBuilder.insert(token).nodeName());
    }

    @Test
    public void testInsertTextNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root>&gt;<child/></root>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token); // &gt; character
        TextNode textNode = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertNotNull(textNode);
        assertEquals(">", xmlTreeBuilder.insert(token).text());
    }

    @Test
    public void testInsertDocumentType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE root SYSTEM \"file.dtd\">";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Token token = xmlTreeBuilder.tokeniser.next();
        assertNotNull(token);
        xmlTreeBuilder.process(token);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertNotNull(documentType);
        assertEquals("root", xmlTreeBuilder.insert(token).nodeName());
        assertEquals("file.dtd", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/><child/><child/></root>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        for (int i = 0; i < input.length(); i++) {
            Token token = xmlTreeBuilder.tokeniser.next();
            xmlTreeBuilder.process(token);
        }
        Token endChildToken = new Token.EndTag("child", null);
        xmlTreeBuilder.process(endChildToken);
        assertEquals(2, xmlTreeBuilder.stack.size());
        xmlTreeBuilder.popStackToClose(endChildToken);
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child/><child/><child/></root>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodeList = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);
        assertNotNull(nodeList);
        assertEquals(3, nodeList.size());
    }
}