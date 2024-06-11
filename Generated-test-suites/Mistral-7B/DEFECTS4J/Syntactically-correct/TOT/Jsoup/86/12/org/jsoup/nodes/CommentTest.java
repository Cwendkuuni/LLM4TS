import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 private Comment comment;
 private Document doc;

 @Before
 public void setUp() {
 comment = new Comment("This is a comment");
 doc = Jsoup.parse("<html><head></head><body>" + comment.outerHtml() + "</body></html>");
 }

 @Test
 public void testCreateComment() {
 Comment comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCreateCommentWithBaseUri() {
 Comment comment = new Comment("This is a comment", "/index.html");
 comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 // baseUri should be ignored
 assertNull(comment.getBaseUri());
 }

 @Test
 public void testNodeName() {
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testOuterHtml() {
 Appendable acc = new StringBuilder();
 comment.outerHtml(acc, 0, null);
 String expected = "<!--This is a comment-->";
 assertEquals(expected, acc.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlComment = new Comment(xmlDeclaration);
 assertTrue(xmlComment.isXmlDeclaration());
 String nonXmlDeclaration = "This is a comment";
 Comment nonXmlComment = new Comment(nonXmlDeclaration);
 assertFalse(nonXmlComment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 Comment xmlComment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDeclarationNode = xmlComment.asXmlDeclaration();
 assertNotNull(xmlDeclarationNode);
 }

 @Test(expected = IllegalStateException.class)
 public void testAsXmlDeclarationWithNoRootElement() {
 String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tag>";
 Comment xmlComment = new Comment(xmlData);
 xmlComment.asXmlDeclaration();
 }
}