import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Comment;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    
    public void testIsXmlDeclarationEdgeCases() {
        Comment comment1 = new Comment("!");
        Comment comment2 = new Comment("?xml version=\"1.0\" encoding=\"UTF-8\"?");
        Comment comment3 = new Comment("Comment with only !");
        Comment comment4 = new Comment("?");
        Comment comment5 = new Comment("");
        Comment comment6 = new Comment(" ");

        boolean result1 = comment1.isXmlDeclaration();
        boolean result2 = comment2.isXmlDeclaration();
        boolean result3 = comment3.isXmlDeclaration();
        boolean result4 = comment4.isXmlDeclaration();
        boolean result5 = comment5.isXmlDeclaration();
        boolean result6 = comment6.isXmlDeclaration();

        // Expected results:
        // comment1, comment3 and comment4 are expected to return true, the others false
        assertTrue(result1);
        assertTrue(result3);
        assertFalse(result2);
        assertFalse(result4);
        assertFalse(result5);
        assertFalse(result6);
    }

    @Test
    public void testAsXmlDeclarationNullResult() {
        Comment comment = new Comment("This is a comment");
        XmlDeclaration result = comment.asXmlDeclaration();
        assertNull(result);
    }

    @Test
    public void testAsXmlDeclarationValidResult() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element>Value</element></root>");
        XmlDeclaration result = comment.asXmlDeclaration();
        assertNotNull(result);
        assertEquals("xml", result.getName());
        assertTrue(result.isStandalone());
    }

    
}