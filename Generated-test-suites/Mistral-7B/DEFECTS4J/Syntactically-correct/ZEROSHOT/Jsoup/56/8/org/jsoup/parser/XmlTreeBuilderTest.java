package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
                "<tag attr1=\"value1\" attr2=\"value2\">Content</tag>" +
                "</xml>";

        Document xmlDoc = new XmlTreeBuilder().parse(xmlString, null);
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertNotNull(root.tagName());
        assertNotNull(root.baseUri());
        assertNotNull(root.childNodes());
        assertNotNull(root.childNodes().get(0).tagName());
        assertNotNull(root.childNodes().get(0).attributes());
        assertNotNull(root.childNodes().get(0).attributes().get("attr1"));
        assertNotNull(root.childNodes().get(0).attributes().get("attr2"));
        assertNull(root.childNodes().get(0).text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<tag attr1=\"value1\" attr2=\"value2\">Content</tag>";

        List<Node> fragmentNodes = new XmlTreeBuilder().parseFragment(xmlFragment, null, null, null);
        assertNotNull(fragmentNodes);
        assertNotNull(fragmentNodes.get(0).tagName());
        assertNotNull(fragmentNodes.get(0).attributes());
        assertNotNull(fragmentNodes.get(0).attributes().get("attr1"));
        assertNotNull(fragmentNodes.get(0).attributes().get("attr2"));
        assertNotNull(fragmentNodes.get(0).text());
        assertNull(fragmentNodes.get(0).baseUri());
        assertNull(fragmentNodes.get(0).doc());
        assertNull(fragmentNodes.get(0).parents());
        assertNull(fragmentNodes.get(0).nextSibling());
        assertNull(fragmentNodes.get(0).previousSibling());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIllegalInput() {
        new XmlTreeBuilder().parse("Invalid input", null);
    }

    @Test
    public void testProcessTokens() {
        String xmlString = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
                "<tag attr1=\"value1\" attr2=\"value2\">Content</tag>" +
                "</xml>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.setParser(new MyParser()); // replace the default Parser
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, null);

        // Test StartTag processing
        Token startTagToken = new Token.StartTag("tag", null, new AttributeSet());
        xmlTreeBuilder.process(startTagToken);
        Element root = xmlDoc.rootElement();
        Element tagElement = root.child(0);
        assertNotNull(tagElement);

        // Test EndTag processing
        Token endTagToken = new Token.EndTag("tag");
        xmlTreeBuilder.process(endTagToken);
        assertNull(tagElement);

        // Test Comment processing
        Token commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(commentToken);
        List<Node> comments = xmlDoc.childNodes();
        assertNotNull(comments);
        assertNotNull(comments.get(comments.size() - 1));
        assertEquals("This is a comment", comments.get(comments.size() - 1).text());

        // Test Character processing
        Token characterToken = new Token.Character('A');
        xmlTreeBuilder.process(characterToken);
        List<Node> textNodes = xmlDoc.childNodes();
        assertNotNull(textNodes);
        assertNotNull(textNodes.get(textNodes.size() - 1));
        assertEquals("A", textNodes.get(textNodes.size() - 1).text());

        // Test Doctype processing
        Token doctypeToken = new Token.Doctype("xml", null, null);
        xmlTreeBuilder.process(doctypeToken);
        DocumentType documentType = (DocumentType) xmlDoc.childNodes().get(0);
        assertNotNull(documentType);
        assertEquals("xml", documentType.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Test no element on stack
        xmlTreeBuilder.process(new Token.EndTag("tag"));
        assertNull(xmlTreeBuilder.stack.peek());

        // Test element on stack
        Element element = new Element("tag", null, null);
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
        assertNull(xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testInsertNodes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("tag", null, null);
        Token endTagToken = new Token.EndTag("tag");
        Token commentToken = new Token.Comment("This is a comment");
        Token characterToken = new Token.Character('A');
        Token doctypeToken = new Token.Doctype("xml", null, null);

        xmlTreeBuilder.process(startTagToken);
        Element tagElement = (Element) xmlTreeBuilder.stack.peek();
        xmlTreeBuilder.insertNode(tagElement);
        xmlTreeBuilder.process(endTagToken);
        xmlTreeBuilder.popStackToClose(endTagToken);

        xmlTreeBuilder.process(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.stack.peek();
        xmlTreeBuilder.insert(comment);
        xmlTreeBuilder.process(characterToken);
        TextNode textNode = (TextNode) xmlTreeBuilder.stack.peek();
        xmlTreeBuilder.insert(textNode);

        xmlTreeBuilder.process(doctypeToken);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.peek();
        xmlTreeBuilder.insert(documentType);
    }

    private class MyParser extends Parser {
        @Override
        public TreeBuilder treeBuilder() {
            return new XmlTreeBuilder();
        }
    }
}