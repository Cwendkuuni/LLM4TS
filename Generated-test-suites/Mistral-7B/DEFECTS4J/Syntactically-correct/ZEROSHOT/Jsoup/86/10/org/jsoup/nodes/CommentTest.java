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
 *
 * @author TestAutomator
 */
public class CommentTest {

    @ org.junit.Test
    public void testCommentCreation() {
        // Test creating a comment with a string
        Comment comment = new Comment("Hello, this is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("Hello, this is a comment.", comment.getData());

        // Test creating a comment with a deprecated constructor
        Comment commentWithBaseUri = new Comment("Hello, this is a comment.", "http://example.com");
        assertEquals("#comment", commentWithBaseUri.nodeName());
        assertEquals("Hello, this is a comment.", commentWithBaseUri.getData());
    }

    @ org.junit.Test
    public void testCommentOuterHtml() throws IOException {
        String html = "<html><head><!-- This is a comment --></head><body></body></html>";
        Document document = Jsoup.parse(html);
        Comment comment = (Comment) document.selectFirst("#comment");

        String expected = "<!--This is a comment-->";
        String actual = comment.outerHtml();
        assertEquals(expected, actual);
    }

    @ org.junit.Test
    public void testCommentToString() {
        Comment comment = new Comment("Hello, this is a comment.");
        String expected = "<!--Hello, this is a comment-->";
        String actual = comment.toString();
        assertEquals(expected, actual);
    }

    @ org.junit.Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head><title>Test</title>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>");
        boolean expected = true;
        boolean actual = comment.isXmlDeclaration();
        assertEquals(expected, actual);

        Comment commentWithoutXmlDeclaration = new Comment("Hello, this is a comment.");
        boolean actualWithoutXmlDeclaration = commentWithoutXmlDeclaration.isXmlDeclaration();
        assertFalse(actualWithoutXmlDeclaration);
    }

    @ org.junit.Test
    public void testCommentAsXmlDeclaration() throws IOException {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        Comment comment = new Comment(xmlDeclaration);

        XmlDeclaration expectedXmlDeclaration = new XmlDeclaration("1.0", "UTF-8");
        expectedXmlDeclaration.attributes().addAll(new Attribute("xmlns", "http://www.w3.org/1999/xhtml"));
        XmlDeclaration actualXmlDeclaration = comment.asXmlDeclaration();

        assertEquals(expectedXmlDeclaration.toString(), actualXmlDeclaration.toString());

        Comment commentWithoutXmlDeclaration = new Comment("Hello, this is a comment.");
        XmlDeclaration actualXmlDeclarationWithoutXmlDeclaration = commentWithoutXmlDeclaration.asXmlDeclaration();
        assertNull(actualXmlDeclarationWithoutXmlDeclaration);
    }
}