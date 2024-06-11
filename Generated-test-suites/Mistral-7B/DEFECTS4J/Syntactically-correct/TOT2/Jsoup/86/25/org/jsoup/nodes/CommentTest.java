package org.junit.test;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class CommentTest {
    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        String data = comment.getData();
        assertEquals("This is a comment.", data);
    }

    @Test
    public void testCommentCreationWithEmptyString() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
        String data = comment.getData();
        assertEquals("", data);
    }

    @Test
    public void testCommentCreationWithNullData() {
        Comment comment = new Comment(null);
        assertEquals("#comment", comment.nodeName());
        String data = comment.getData();
        assertNull(data);
    }

    @Test
    public void testCommentCreationWithLongStringData() {
        String longStringData = "some long string data";
        for (int i = 0; i < 10000; i++) {
            longStringData += "some more data";
        }
        Comment comment = new Comment(longStringData);
        assertEquals("#comment", comment.nodeName());
        String data = comment.getData();
        assertEquals(longStringData, data);
    }

    @Test
    public void testIsXmlDeclarationWithXmlDeclarationCommentWithoutDOCTYPE() {
        String xmlDeclaration = "<?xml version=\"1.0\"?>";
        Comment comment = new Comment(xmlDeclaration);
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithCommentStartingWithExclamation() {
        String commentWithExclamation = "!-- This is a comment --";
        Comment comment = new Comment(commentWithExclamation);
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithCommentStartingWithQuestionMark() {
        String commentWithQuestionMark = "<? This is a comment ?>";
        Comment comment = new Comment(commentWithQuestionMark);
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithCommentThatStartsWithXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\"?>\n<!-- This is a comment -->";
        Comment comment = new Comment(xmlDeclaration.substring(xmlDeclaration.indexOf("\n") + 1));
        assertTrue(comment.isXmlDeclaration());
    }

    @Test(expected = AssertionError.class)
    public void testAsXmlDeclarationWithCommentThatDoesntStartWithXmlDeclaration() {
        String comment = "<!-- This is a comment -->";
        Comment commentNode = new Comment(comment);
        XmlDeclaration xmlDeclaration = commentNode.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }

    @Test
    public void testAsXmlDeclarationWithCommentThatContainsError() {
        String comment = "<!-- <error>This is an error</error> -->";
        Comment commentNode = new Comment(comment);
        assertNull(commentNode.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithCommentThatHasAttributes() {
        String comment = "<!-- id=\"myId\" class=\"myClass\" -->\n<html>\n<head>\n<title>My Title</title>\n</head>\n<body>\n<p id=\"otherId\">Other text</p>\n</body>\n</html>";
        Comment commentNode = new Comment(comment.substring(comment.indexOf("\n<html>\n") + "<html>\n<head>\n".length()));
        XmlDeclaration xmlDeclaration = commentNode.asXmlDeclaration();
        assertNull(xmlDeclaration);

        Element element = commentNode.asElement();
        assertEquals("ID", element.tagName());
        assertEquals("myId", element.attr("id"));
        assertEquals("class", element.tagName());
        assertEquals("myClass", element.attr("class"));
    }
}