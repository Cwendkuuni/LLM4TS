import org.junit.Test;
import org.jsoup.nodes.Comment;
import static org.junit.Assert.assertEquals;

public class CommentTest {
    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    
    public void testIsXmlDeclarationEdgeCases() {
        Comment comment1 = new Comment("<!--  XML Declaration -->");
        Comment comment2 = new Comment("!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
        Comment comment3 = new Comment("!");
        Comment comment4 = new Comment("?xml version=\"1.0\" encoding=\"UTF-8\" ?");

        assertEquals(true, comment1.isXmlDeclaration());
        assertEquals(true, comment2.isXmlDeclaration());
        assertEquals(false, comment3.isXmlDeclaration());
        assertEquals(true, comment4.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationEdgeCases() {
        Comment comment1 = new Comment("<!--  XML Declaration -->");
        Comment comment2 = new Comment("!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
        Comment comment3 = new Comment("!");
        Comment comment4 = new Comment("?xml version=\"1.0\" encoding=\"UTF-8\" ?");

        Comment expectedComment1 = new Comment("<!--  XML Declaration -->", null);
        Comment expectedComment2 = new XmlDeclaration("html", false);
        Comment expectedComment3 = null;
        Comment expectedComment4 = new XmlDeclaration("xml version=\"1.0\" encoding=\"UTF-8\"", true);

        assertEquals(expectedComment1, comment1.asXmlDeclaration());
        assertEquals(expectedComment2, comment2.asXmlDeclaration());
        assertEquals(expectedComment3, comment3.asXmlDeclaration());
        assertEquals(expectedComment4, comment4.asXmlDeclaration());
    }
    
}