package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Class to test Comment methods
 */
public class CommentTest {

    @org.junit.Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.outerHtml());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @org.junit.Test
    public void testDeprecatedCommentCreation() {
        Comment comment = new Comment("This is a comment", "/tmp/test.html");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.outerHtml());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("version", xmlDeclaration.attribute(0).getKey());
        assertEquals("1.0", xmlDeclaration.attribute(0).getValue());
    }

    @org.junit.Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @org.junit.Test
    public void testCommentOuterHtml() throws IOException {
        Comment comment = new Comment("This is a comment");
        Appendable appendable = new StringBuilder();
        comment.outerHtmlHead(appendable, 0, null);
        comment.outerHtmlTail(appendable, 0, null);
        assertEquals("<!--This is a comment-->", appendable.toString());
    }
}