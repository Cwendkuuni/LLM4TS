package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

/**
 * Test class for Comment.java
 *
 * @author John Doe
 */
public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testDeprecatedCommentCreation() {
        Comment comment = new Comment("This is a comment", "baseUri");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());

        comment = new Comment("!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentIsNotXmlDeclaration() {
        Comment comment = new Comment("This is a regular comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getXmlDeclarationString());

        comment = new Comment("!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
        xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"", xmlDeclaration.getXmlDeclarationString());

        comment = new Comment("This is a regular comment");
        xmlDeclaration = comment.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }

    @Test
    public void testCommentOuterHtml() throws IOException {
        Comment comment = new Comment("This is a comment");
        Document doc = Jsoup.parse("<html><head></head><body><!--This is a comment--></body></html>", "", Parser.htmlParser());
        String expected = "<!--This is a comment-->";

        StringBuilder sb = new StringBuilder();
        comment.outerHtmlHead(sb, 0, null);
        comment.outerHtmlTail(sb, 0, null);

        assertEquals(expected, comment.outerHtml());
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }
}