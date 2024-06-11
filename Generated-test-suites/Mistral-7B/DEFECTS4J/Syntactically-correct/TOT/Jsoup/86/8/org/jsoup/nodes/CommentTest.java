import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentCreationWithDeprecatedConstructor() {
 Comment comment = new Comment("This is a comment.");
 // Deprecated constructor does not take baseUri as an argument, so it should be empty.
 assertEquals("", comment.getBaseUri());
 }

 @Test
 public void testCommentNodeName() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetCommentData() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testOuterHtmlMethod() throws IOException {
 Document doc = Jsoup.parse("<html><head><!--Comment--></head><body></body></html>");
 Comment comment = doc.selectFirst("html head comment");
 String expected = "<!--Comment-->";
 assertEquals(expected, comment.outerHtml());
 }

 @Test
 public void testIsXmlDeclarationMethod() {
 Comment comment = new Comment("<!--xml version=\"1.0\" encoding=\"UTF-8\"-->");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationMethodWithEmptyString() {
 Comment comment = new Comment("");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationMethodWithNonXmlDeclarationString() {
 Comment comment = new Comment("This is not an XML declaration.");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationMethod() throws IOException {
 Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"-->");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.data());
 }

 @Test
 public void testAsXmlDeclarationMethodWithEmptyString() {
 Comment comment = new Comment("");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationMethodWithNonXmlDeclarationString() {
 Comment comment = new Comment("This is not an XML declaration.");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationMethodWithInvalidXmlDeclarationString() {
 Comment comment = new Comment("!--xml version=\"1.0\" encoding="); // Incomplete declaration
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testCommentWithAttributes() {
 Comment comment = new Comment("<!-- This is a comment with attribute id=\"myId\" -->");
 assertEquals("myId", comment.getAttributes().keySet().iterator().next().toString());
 }

 @Test
 public void testCommentWithMultipleAttributes() {
 Comment comment = new Comment("<!-- This is a comment with id=\"myId1\" and class=\"myClass\" -->");
 Set<String> attributes = comment.getAttributes().keySet();
 assertEquals(2, attributes.size());
 assertTrue(attributes.contains("id"));
 assertTrue(attributes.contains("class"));
 }
}