package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.Iterator;

/**
 * XmlTreeBuilderTest class file to test the XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    /**
     * Test the initialiseParse method.
     */
    @Test
    public void testInitialiseParse() {
        String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
                "<root>\n" +
                "<child attr=\"value\">Content</child>\n" +
                "</root>\n";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals("xml", xmlTreeBuilder.doc.nodeName());
    }

    /**
     * Test the process method with different token types.
     */
    @Test
    public void testProcessToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
                "<root>\n" +
                "<child attr=\"value\">Content</child>\n" +
                "</root>\n";

        Token token;
        while ((token = xmlTreeBuilder.tokeniser.next()) != null) {
            xmlTreeBuilder.process(token);
        }

        // Start tag testing
        token = xmlTreeBuilder.tokeniser.next();
        assertEquals(Token.StartTag.class, token.getClass());
        xmlTreeBuilder.process(token);
        Element root = xmlTreeBuilder.doc.child(0);
        assertEquals("root", root.nodeName());

        // End tag testing
        token = xmlTreeBuilder.tokeniser.next();
        assertEquals(Token.EndTag.class, token.getClass());
        xmlTreeBuilder.process(token);
        assertEquals(0, xmlTreeBuilder.stack.size());

        // Comment testing
        token = xmlTreeBuilder.tokeniser.next();
        assertEquals(Token.Comment.class, token.getClass());
        xmlTreeBuilder.process(token);
        Comment comment = xmlTreeBuilder.doc.child(1);
        assertNotNull(comment);
        assertEquals("-- Comment --", comment.text());

        // Character testing
        token = xmlTreeBuilder.tokeniser.next();
        assertEquals(Token.Character.class, token.getClass());
        xmlTreeBuilder.process(token);
        TextNode characterNode = (TextNode) xmlTreeBuilder.doc.child(2);
        assertEquals("Content", characterNode.text());

        // Doctype testing
        token = xmlTreeBuilder.tokeniser.next();
        assertEquals(Token.Doctype.class, token.getClass());
        xmlTreeBuilder.process(token);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.child(0);
        assertEquals("xml", doctypeNode.nodeName());
        assertEquals("1.0", doctypeNode.getData());

        // EOF testing
        token = xmlTreeBuilder.tokeniser.next();
        assertEquals(Token.EOF.class, token.getClass());
        xmlTreeBuilder.process(token);
        assertTrue(xmlTreeBuilder.stack.isEmpty());

        // Unknown token testing
        token = new Token(255, null, null, null, null);
        xmlTreeBuilder.process(token);
        Validate.fail("Unexpected test passed.");
    }

    /**
     * Test the insertNode method.
     */
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root", null);
        xmlTreeBuilder.insertNode(root);
        assertEquals(root, xmlTreeBuilder.doc.child(0));
    }

    /**
     * Test the insert method for start tag.
     */
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("child", null, new Attribute("attr", "value"));
        Element childElement = xmlTreeBuilder.insert(startTag);
        assertEquals("child", childElement.nodeName());
        assertEquals("value", childElement.attribute(0).getValue());
    }

    /**
     * Test the insert method for comment.
     */
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("-- Comment --");
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.doc.child(1);
        assertEquals("-- Comment --", comment.text());
    }

    /**
     * Test the insert method for character.
     */
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('C');
        xmlTreeBuilder.insert(characterToken);
        TextNode characterNode = (TextNode) xmlTreeBuilder.doc.child(1);
        assertEquals("C", characterNode.text());
    }

    /**
     * Test the insert method for doctype.
     */
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("xml", null, null, null, null);
        xmlTreeBuilder.insert(doctypeToken);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.child(0);
        assertEquals("xml", doctypeNode.nodeName());
    }

    /**
     * Test the popStackToClose method.
     */
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(new Element("root"));
        xmlTreeBuilder.stack.add(new Element("child1"));
        xmlTreeBuilder.stack.add(new Element("child2"));

        xmlTreeBuilder.popStackToClose(new Token.EndTag("child1"));
        assertEquals(2, xmlTreeBuilder.stack.size());

        xmlTreeBuilder.popStackToClose(new Token.EndTag("root"));
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
}