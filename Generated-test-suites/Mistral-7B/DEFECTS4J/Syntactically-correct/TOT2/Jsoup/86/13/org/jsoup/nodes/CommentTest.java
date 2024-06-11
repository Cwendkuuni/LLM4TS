package org.junit.test;

import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class CommentTest {

    // Test case for method Comment(String data)
    @Test
    public void testCommentConstruction() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    // Test case for method isXmlDeclaration()
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<!-- Comment -->");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment.");
        assertFalse(comment2.isXmlDeclaration());
    }

    // Test case for method asXmlDeclaration()
    @Test
    public void testAsXmlDeclaration() {
        String xmlData = "<?xml version='1.0' encoding='UTF-8'?>\n<!-- Comment -->";
        Comment comment = new Comment(xmlData);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version='1.0' encoding='UTF-8'?>", xmlDeclaration.getDeclaration());
        assertEquals("comment", xmlDeclaration.getName());

        Comment comment2 = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<title>page</title>\n");
        assertNull(comment2.asXmlDeclaration());
    }

    // Test case for method getData()
    @Test
    public void testGetData() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    // Test case for method nodeName()
    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
    }

    // Test case for method toString()
    @Test
    public void testToString() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals("<!--" + data + "-->", comment.toString());
    }
}
//