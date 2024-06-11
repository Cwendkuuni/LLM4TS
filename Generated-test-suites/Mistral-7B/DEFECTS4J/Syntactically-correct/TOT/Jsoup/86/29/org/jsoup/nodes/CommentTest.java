import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment.");
 Assert.assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentCreationWithDeprecatedConstructor() {
 Comment comment = new Comment("This is a comment.", null);
 Assert.assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentNodeName() {
 Comment comment = new Comment("Comment content.");
 Assert.assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testCommentIsXmlDeclarationFalse() {
 Comment comment = new Comment("This is a comment.");
 Assert.assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentIsXmlDeclarationTrue() {
 Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"--");
 Assert.assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() throws IOException {
 Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"--");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 Assert.assertNotNull(xmlDeclaration);
 Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"--", xmlDeclaration.getDeclaration());
 }

 @Test(expected = IOException.class)
 public void testCommentAsXmlDeclarationWithInvalidXml() throws IOException {
 Comment comment = new Comment("!InvalidXML");
 comment.asXmlDeclaration();
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Comment comment = new Comment("This is a comment.");
 Document doc = Jsoup.parse("<root>" + comment.outerHtml() + "</root>");
 Element commentElement = doc.selectFirst("#comment");
 Assert.assertEquals("<!--This is a comment.-->", commentElement.html());
 }

 @Test
 public void testCommentOuterHtmlWithPrettyPrint() throws IOException {
 Document doc = Jsoup.parse("<root>" + System.lineSeparator() +
         "  <!--This is a comment-->" + System.lineSeparator() +
         "</root>");
 Comment comment = doc.selectFirst("#comment");
 Assert.assertEquals("<!--This is a comment-->", comment.outerHtml());
 }
}