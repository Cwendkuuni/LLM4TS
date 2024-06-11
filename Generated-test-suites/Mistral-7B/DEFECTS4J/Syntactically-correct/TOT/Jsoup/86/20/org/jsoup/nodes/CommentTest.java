import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCommentCreationDeprecated() {
 Comment comment = new Comment("This is a comment", "http://example.com");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Comment comment = new Comment("This is a comment");
 Appendable accum = new StringBuilder();
 comment.outerHtmlHead(accum, 0, null);
 comment.outerHtmlTail(accum, 0, null);
 assertEquals("<!--This is a comment-->", accum.toString());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testCommentIsXmlDeclarationFalse() {
 Comment comment = new Comment("This is a comment");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentIsXmlDeclarationTrue() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--This is a comment-->");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a comment");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclarationValid() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n<!--This is a comment-->\n</body>\n</html>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("1.0", xmlDeclaration.getVersion());
 assertTrue(xmlDeclaration.isStandalone());
 }
}