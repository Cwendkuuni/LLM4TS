package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructorWithData() {
        String data = "This is a test comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentConstructorWithEmptyData() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
        assertEquals("", comment.getData());
    }

    // Test other methods with basic cases and edge cases here...

    @Test
    public void testIsXmlDeclarationWithValidXmlDeclaration() {
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlData);
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithInvalidXmlDeclaration() {
        String invalidData = "!@#$%^&*()_+";
        Comment comment = new Comment(invalidData);
        assertFalse(comment.isXmlDeclaration());
    }

    // Test other methods with edge cases and exception handling here...

    @Test
    public void testAsXmlDeclarationWithValidXmlDeclaration() {
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>";
        Comment comment = new Comment(xmlData);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAsXmlDeclarationWithNullData() {
        Comment comment = new Comment(null);
        comment.asXmlDeclaration();
    }

    // Test other methods with edge cases and exception handling here...

}