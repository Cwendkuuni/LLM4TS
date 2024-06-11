import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Comment;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        Comment comment = new Comment("This is a comment", "baseUri");
        // Since this constructor is deprecated, we cannot test its functionality directly
        // Instead, we can test if an exception is thrown when using it
        try {
            comment = new Comment("This is a comment", "baseUri");
            fail("Exception expected");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    // Testing outerHtmlHead and outerHtmlTail methods would require a more complex test setup
    // as they involve Document.OutputSettings and Appendable objects

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!--<?xml version=\"1.0\" encoding=\"UTF-8\"?>-->\n<html>\n<body>\n<p>This is a comment</p>\n</body>\n</html>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!--<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());

        Comment comment2 = new Comment("This is a comment");
        assertNull(comment2.asXmlDeclaration());
    }
}