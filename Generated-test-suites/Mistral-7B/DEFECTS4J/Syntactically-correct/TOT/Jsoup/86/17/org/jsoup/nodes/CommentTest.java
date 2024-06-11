import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommentTest {

 // Test for constructor with valid data
 @Test
 public void testCommentConstructorValidData() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 // Test for constructor with deprecated baseUri argument
 @Test
 public void testCommentConstructorDeprecatedBaseUri() {
 String data = "This is a comment";
 Comment comment = new Comment(data, "");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 // Test for isXmlDeclaration method with valid XML declaration comment
 @Test
 public void testIsXmlDeclarationValidXmlDeclaration() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = new Comment(xmlDeclaration);
 assertTrue(comment.isXmlDeclaration());
 }

 // Test for isXmlDeclaration method with non-XML declaration comment
 @Test
 public void testIsXmlDeclarationNonXmlDeclaration() {
 String comment = "This is a regular comment";
 Comment commentNode = new Comment(comment);
 assertFalse(commentNode.isXmlDeclaration());
 }

 // Test for asXmlDeclaration method with valid XML declaration comment
 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.data());
 }

 // Test for asXmlDeclaration method with non-XML declaration comment
 @Test
 public void testAsXmlDeclarationNonXmlDeclaration() {
 String comment = "This is a regular comment";
 Comment commentNode = new Comment(comment);
 assertNull(commentNode.asXmlDeclaration());
 }

 // Test for outerHtml method with pretty print
 @Test
 public void testOuterHtmlWithPrettyPrint() throws IOException {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 Document doc = Jsoup.parse("", null, Parser.xmlParser());
 doc.appendElement(comment);
 String expectedResult = "<!--This is a comment-->\n";
 assertEquals(expectedResult, comment.outerHtml(new Document.OutputSettings(true)));
 }

 // Test for outerHtml method without pretty print
 @Test
 public void testOuterHtmlWithoutPrettyPrint() throws IOException {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 Document doc = Jsoup.parse("", null, Parser.xmlParser());
 doc.appendElement(comment);
 String expectedResult = "<!--This is a comment-->";
 assertEquals(expectedResult, comment.outerHtml(new Document.OutputSettings(false)));
 }

 // Test for toString method
 @Test
 public void testToString() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals("<!--This is a comment-->", comment.toString());
 }
}