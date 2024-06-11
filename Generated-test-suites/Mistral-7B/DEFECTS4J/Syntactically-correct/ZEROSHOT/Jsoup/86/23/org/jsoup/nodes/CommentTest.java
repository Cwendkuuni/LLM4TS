import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 // Test creating a new comment with data
 Comment comment = new Comment("This is a comment");
 // Test creating a new comment with deprecated constructor
 Comment commentWithBaseUri = new Comment("This is a comment", null);

 // Test nodeName method
 assertEquals("#comment", comment.nodeName());
 assertEquals("#comment", commentWithBaseUri.nodeName());
 }

 @Test
 public void testCommentGetData() {
 Comment comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Comment comment = new Comment("This is a comment");
 Appendable appendable = new StringBuilder();
 comment.outerHtmlHead(appendable, 0, null);
 assertEquals("<!--This is a comment-->", appendable.toString());
 }

 @Test
 public void testOuterHtmlTail() throws Exception {
 Comment comment = new Comment("This is a comment");
 Appendable appendable = new StringBuilder();
 comment.outerHtmlTail(appendable, 0, null);
 assertEmpty(appendable.toString());
 }

 @Test
 public void testToString() {
 Comment comment = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 // Test comment with XML declaration data
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<html>\r\n<body>\r\n<!--This is a comment-->\r\n</body>\r\n</html>");
 assertTrue(xmlDeclarationComment.isXmlDeclaration());
 // Test comment without XML declaration data
 Comment notXmlDeclarationComment = new Comment("This is a comment");
 assertFalse(notXmlDeclarationComment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 // Test comment with valid XML declaration data
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<html>\r\n<body>\r\n<!--This is a comment-->\r\n</body>\r\n</html>");
 XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.data());
 // Test comment without XML declaration data
 Comment notXmlDeclarationComment = new Comment("This is a comment");
 assertNull(notXmlDeclarationComment.asXmlDeclaration());
 }
}