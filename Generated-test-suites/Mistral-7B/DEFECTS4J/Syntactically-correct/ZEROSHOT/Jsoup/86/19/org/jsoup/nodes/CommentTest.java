package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Test class for Comment.java
 */
public class CommentTest {

    @org.junit.Test
    public void testCommentCreation() {
        // Test creation of a new comment with data
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());

        // Test creation of a new comment with deprecated constructor
        Comment commentWithBaseUri = new Comment("This is a comment", null);
        assertEquals("#comment", commentWithBaseUri.nodeName());
        assertEquals("This is a comment", commentWithBaseUri.getData());
    }

    @org.junit.Test
    public void testIsXmlDeclaration() {
        // Test comment without XML declaration
        Comment commentWithoutDeclaration = new Comment("This is a comment");
        assertFalse(commentWithoutDeclaration.isXmlDeclaration());

        // Test comment with XML declaration
        Comment commentWithDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(commentWithDeclaration.isXmlDeclaration());
    }

    @org.junit.Test
    public void testAsXmlDeclaration() throws IOException {
        // Test comment without XML declaration
        Comment commentWithoutDeclaration = new Comment("This is a comment");
        assertNull(commentWithoutDeclaration.asXmlDeclaration());

        // Test comment with XML declaration
        Comment commentWithDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = commentWithDeclaration.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("version", xmlDeclaration.name());
        assertEquals("1.0", xmlDeclaration.getVersion());
        assertEquals("UTF-8", xmlDeclaration.getEncoding());
    }
}