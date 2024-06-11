import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

 // Test constructor with valid data
 @Test
 public void testCommentConstructorWithValidData() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 // Test constructor with empty data
 @Test
 public void testCommentConstructorWithEmptyData() {
 Comment comment = new Comment("");
 assertEquals("#comment", comment.nodeName());
 assertNull(comment.getData());
 }

 // Test isXmlDeclaration method with valid XML declaration comment
 @Test
 public void testIsXmlDeclarationWithValidXmlDeclarationComment() {
 String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = new Comment(data);
 assertTrue(comment.isXmlDeclaration());
 }

 // Test isXmlDeclaration method with non-XML declaration comment
 @Test
 public void testIsXmlDeclarationWithNonXmlDeclarationComment() {
 String data = "This is a non-XML declaration comment";
 Comment comment = new Comment(data);
 assertFalse(comment.isXmlDeclaration());
 }

 // Test asXmlDeclaration method with valid XML declaration comment
 @Test
 public void testAsXmlDeclarationWithValidXmlDeclarationComment() {
 String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>";
 Comment comment = new Comment(data);
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
 }

 // Test asXmlDeclaration method with invalid XML declaration comment
 @Test (expected = IllegalArgumentException.class)
 public void testAsXmlDeclarationWithInvalidXmlDeclarationComment() {
 String data = "!xml version=\"1.0\" encoding=\"UTF-8\"";
 Comment comment = new Comment(data);
 comment.asXmlDeclaration();
 }

 // Test getData method with valid comment data
 @Test
 public void testgetDataWithValidCommentData() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals(data, comment.getData());
 }

 // Test getData method with empty comment data
 @Test
 public void testgetDataWithEmptyCommentData() {
 Comment comment = new Comment("");
 assertNull(comment.getData());
 }

 // Test outerHtml method with normal comment
 @Test
 public void testouterHtmlWithNormalComment() throws IOException {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 Document doc = Jsoup.parse("<html><body><!--" + data + "--></body></html>");
 assertEquals("<!--This is a comment-->", comment.outerHtml());
 }

 // Test outerHtml method with XML declaration comment
 @Test
 public void testouterHtmlWithXmlDeclarationComment() throws IOException {
 String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = new Comment(data);
 Document doc = Jsoup.parse("<html><body><!--" + data + "--></body></html>");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", comment.outerHtml());
 }

 // Test toString method with normal comment
 @Test
 public void testToStringWithNormalComment() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 // Test toString method with XML declaration comment
 @Test
 public void testToStringWithXmlDeclarationComment() {
 String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = new Comment(data);
 assertEquals(data, comment.toString());
 }

 // Test addAttribute method with normal comment
 @Test
 public void testAddAttributeWithNormalComment() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 Attribute attr = new Attribute("class", "my_class");
 comment.addAttribute(attr);
 assertEquals("<!--This is a comment class=\"my_class-->", comment.outerHtml());
 }
}