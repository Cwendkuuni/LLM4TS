import org.junit.Test;
import org.jsoup.nodes.Comment;
import static org.junit.Assert.*;

public class CommentTest {
    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentConstructorWithBaseUri() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testgetData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testouterHtmlHead() throws IOException {
        Comment comment = new Comment("Test comment");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlHead(sb, 0, null);
        assertEquals("<!--Test comment-->", sb.toString());
    }

    @Test
    public void testouterHtmlTail() throws IOException {
        Comment comment = new Comment("Test comment");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("--test");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>Test</element>\n</root>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getDeclaration());
        assertEquals("root", xmlDeclaration.getName());
    }
}