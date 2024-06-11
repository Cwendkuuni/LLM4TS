package org.jsoup.nodes.tests;

import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.*;
import org.jsoup.nodes.*;

public class CommentTest {

    @Test
    public void testComment() {
        Comment comment = new Comment("This is a comment.");
        String data = comment.getData();
        String expected = "This is a comment.";
        assertEquals(expected, data);
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tag>Content</tag>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment.");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tag>Content</tag>");
        XmlDeclaration expected = new XmlDeclaration("1.0", "UTF-8");
        XmlDeclaration actual = comment.asXmlDeclaration();
        assertEquals(expected.toString(), actual.toString());

        Comment comment2 = new Comment("This is a comment.");
        assertNull(comment2.asXmlDeclaration());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment.");
        String expected = "#comment";
        assertEquals(expected, comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment.");
        String expected = "This is a comment.";
        assertEquals(expected, comment.getData());
    }

    @Test
    public void testEmptyCommentData() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testLongCommentData() {
        String longData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis nec odio. Sed non mi. Praesent blandit. Fusce ut placerat orci. Nullam mollis auctor tellus. Nullam quis ante.";
        Comment comment = new Comment(longData);
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testSpecialCharactersAndEntities() {
        Comment comment = new Comment("&lt;!DOCTYPE html&gt;<html><head><title>Test</title></head><body><p>Test &amp;amp; Test</p></body></html>");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!--&lt;!DOCTYPE html&gt;<html><head><title>Test</title></head><body><p>Test &amp; Test</p></body></html>-->", accum.toString());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullCommentData() {
        new Comment(null);
    }

    @Test
    public void testNullBaseUriComment() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testEmptyBaseUriComment() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testSpecialCharactersAndEntitiesInBaseUri() {
        Comment comment = new Comment("This is a comment.", "http://example.com/&test=123");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!--This is a comment.-->", accum.toString());
        assertEquals("http://example.com/&test=123", comment.baseUri());
    }

}