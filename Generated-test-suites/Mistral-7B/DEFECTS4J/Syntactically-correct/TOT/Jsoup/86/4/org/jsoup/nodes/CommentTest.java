import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 @Test
 public void testCreateComment() {
 Comment comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCreateCommentDeprecated() {
 Comment comment = new Comment("This is a comment", null);
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testNodeName() {
 Comment comment = new Comment("Test Comment");
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 Comment comment = new Comment("Test Comment");
 assertEquals("Test Comment", comment.getData());
 }

 @Test
 public void testOuterHtml() {
 Appendable accum = new StringBuilder();
 try {
 Comment comment = new Comment("Test Comment");
 comment.outerHtmlHead(accum, 0, null);
 comment.outerHtmlTail(accum, 0, null);
 assertTrue(accum.toString().contains("<!--Test Comment-->"));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<!--Test Comment-->");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a comment");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValid() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<test>< attribute='value' />\n<!--Test Comment-->");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("1.0", xmlDeclaration.getVersion());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAsXmlDeclarationInvalid() {
 Comment comment = new Comment("This is a comment");
 comment.asXmlDeclaration();
 }
}