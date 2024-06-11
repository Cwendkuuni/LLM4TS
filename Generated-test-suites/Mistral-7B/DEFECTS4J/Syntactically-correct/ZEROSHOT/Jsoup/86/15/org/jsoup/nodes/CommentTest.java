package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Test class for Comment.java
 */
public class CommentTest {

    @org.junit.BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    /**
     * Test Comment constructor with String argument
     */
    @org.junit.Test
    public void testCommentStringConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    /**
     * Test Comment constructor with String and String arguments (deprecated)
     */
    @org.junit.Test
    public void testCommentStringStringConstructor() {
        String data = "This is a comment";
        String baseUri = "http://example.com";
        Comment comment = new Comment(data, baseUri);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    /**
     * Test nodeName method
     */
    @org.junit.Test
    public void testNodeName() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
    }

    /**
     * Test getData method
     */
    @org.junit.Test
    public void testGetData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    /**
     * Test isXmlDeclaration method
     */
    @org.junit.Test
    public void testIsXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclaration);
        assertTrue(comment.isXmlDeclaration());

        String commentText = "This is a comment";
        Comment commentTextComment = new Comment(commentText);
        assertFalse(commentTextComment.isXmlDeclaration());
    }

    /**
     * Test asXmlDeclaration method
     */
    @org.junit.Test
    public void testAsXmlDeclaration() throws IOException {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDeclarationNode = comment.asXmlDeclaration();
        assertNotNull(xmlDeclarationNode);
        assertEquals("version", xmlDeclarationNode.getName());
        assertEquals("1.0", xmlDeclarationNode.getVersion());
        assertEquals("UTF-8", xmlDeclarationNode.getEncoding());
        assertEquals(xmlDeclaration, xmlDeclarationNode.toString());

        String commentText = "This is a comment";
        Comment commentTextComment = new Comment(commentText);
        assertNull(commentTextComment.asXmlDeclaration());
    }

    /**
     * Test outerHtmlHead method
     * @throws IOException
     */
    @org.junit.Test
    public void testOuterHtmlHead() throws IOException {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        String expected = "<!--" + data + "-->";

        Document doc = Jsoup.parse("<root>" + expected + "</root>");
        Element element = doc.selectFirst("#comment");
        String output = element.outerHtml();

        assertEquals(expected, comment.outerHtml());
    }

    /**
     * Test outerHtmlTail method
     */
    @org.junit.Test
    public void testOuterHtmlTail() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        String expected = "";

        assertEquals(expected, comment.outerHtmlTail());
    }

    /**
     * Test toString method
     */
    @org.junit.Test
    public void testToString() {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        String expected = "<!--" + data + "-->";
        assertEquals(expected, comment.toString());
    }
}