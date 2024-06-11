import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 assertTrue(comment.nodeName().equals("#comment"));
 assertTrue(comment.getData().equals("This is a comment"));
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 Comment comment = new Comment("This is a comment", "http://example.com");
 assertTrue(comment.nodeName().equals("#comment"));
 assertTrue(comment.getData().equals("This is a comment"));
 assertNull(comment.getBaseUri());
 }

 @Test
 public void testEmptyCommentCreation() {
 Comment comment = new Comment("");
 assertTrue(comment.nodeName().equals("#comment"));
 assertTrue(comment.getData().equals(""));
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentIsNotXmlDeclaration() {
 Comment comment = new Comment("This is a comment");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://example.com/test.xsd\">Test</test>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertTrue(xmlDeclaration.getVersion().equals("1.0"));
 assertTrue(xmlDeclaration.getEncoding().equals("UTF-8"));
 }

 @Test
 public void testCommentAsXmlDeclarationWithNoDeclaration() {
 Comment comment = new Comment("This is a comment");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment");
 assertTrue(comment.toString().startsWith("<!--This is a comment-->"));
 }
}