import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 Assertions.assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!-- This is a comment -->");
 Assertions.assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!-- This is a comment -->");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 Assertions.assertNotNull(xmlDeclaration);
 }

 @Test
 public void testEmptyComment() {
 Comment comment = new Comment("");
 Assertions.assertEquals("", comment.getData());
 Assertions.assertFalse(comment.isXmlDeclaration());
 Assertions.assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testNullCommentData() {
 Assertions.assertThrows(NullPointerException.class, () -> new Comment(null));
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Comment comment = new Comment("This is a comment");
 Document document = Jsoup.parse("<html><body><!--" + comment.getData() + "--></body></html>");
 StringBuilder accum = new StringBuilder();
 comment.outerHtmlHead(accum, 0, null);
 String expected = "<!--This is a comment-->";
 Assertions.assertEquals(expected, accum.toString());
 }

 @Test
 public void testCommentOuterHtmlWithPrettyPrint() throws IOException {
 Comment comment = new Comment("This is a comment");
 Document document = Jsoup.parse("<html><body><!--" + comment.getData() + "--></body></html>");
 StringBuilder accum = new StringBuilder();
 comment.outerHtmlHead(accum, 0, new Document.OutputSettings().prettyPrint(true));
 String expected = "<!--\nThis is a comment\n-->";
 Assertions.assertEquals(expected, accum.toString());
 }

 @Test
 public void testCommentOuterHtmlWithTail() throws IOException {
 Comment comment = new Comment("This is a comment");
 Document document = Jsoup.parse("<html><body><!--" + comment.getData() + "--></body></html>");
 StringBuilder accum = new StringBuilder();
 comment.outerHtml(accum, 0, null);
 String expected = "<html><body><!--This is a comment--></body></html>";
 Assertions.assertEquals(expected, accum.toString());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment");
 String expected = "<!--This is a comment-->";
 Assertions.assertEquals(expected, comment.toString());
 }
}