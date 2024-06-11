import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 Assert.assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"--");
 Assert.assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentIsNotXmlDeclaration() {
 Comment comment = new Comment("This is a regular comment");
 Assert.assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"--");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 Assert.assertNotNull(xmlDeclaration);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCommentWithNullData() {
 // This should throw an IllegalArgumentException
 Comment comment = new Comment(null);
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Document document = Jsoup.parse("<html><!--This is a comment--></html>");
 Comment comment = document.child(0).child(0);
 StringBuilder expected = new StringBuilder();
 expected.append("<!--");
 expected.append(comment.getData());
 expected.append("-->");
 StringBuilder actual = new StringBuilder();
 comment.outerHtml(actual, 0, null);
 Assert.assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment");
 Assert.assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testCommentIsXmlDeclarationWithEmptyString() {
 Comment comment = new Comment("");
 Assert.assertFalse(comment.isXmlDeclaration());
 }
}