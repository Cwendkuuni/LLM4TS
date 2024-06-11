package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Test for Comment class
 */
public class CommentTest {

    @org.junit.Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @org.junit.Test
    public void testCommentCreationWithBaseUri() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @org.junit.Test
    public void testCommentOuterHtml() throws IOException {
        Comment comment = new Comment("This is a comment");
        Document doc = Jsoup.parse("<html><body>" + comment.outerHtml() + "</body></html>");
        assertEquals("<!--This is a comment-->", doc.body().text());
    }

    @org.junit.Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @org.junit.Test
    public void testCommentIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentIsXmlDeclarationTrue() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\nThis is a comment");
        assertTrue(comment.isXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentAsXmlDeclarationXmlDeclaration() throws IOException {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://example.com/schema.xsd\"></test>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());
    }
}