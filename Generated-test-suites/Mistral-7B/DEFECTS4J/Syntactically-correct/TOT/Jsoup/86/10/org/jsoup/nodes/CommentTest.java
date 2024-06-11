import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 Comment comment = new Comment("This is a comment", "http://example.com");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 assertEquals("http://example.com", comment.baseUri());
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
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertNotNull(xmlDeclaration.getVersion());
 assertNotNull(xmlDeclaration.getEncoding());
 }

 @Test(expected = NullPointerException.class)
 public void testCommentAsXmlDeclarationNullData() {
 Comment comment = new Comment(null);
 comment.asXmlDeclaration();
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Comment comment = new Comment("This is a comment");
 StringBuilder sb = new StringBuilder();
 Document doc = Jsoup.parseBodyElements(comment.outerHtml(), null);
 assertEquals(1, doc.childNodeSize());
 Element el = doc.child(0);
 assertEquals("#comment", el.nodeName());
 assertEquals("This is a comment", el.text());
 }

 @Test
 public void testCommentOuterHtmlWithPrettyPrint() throws IOException {
 Comment comment = new Comment("This is a comment");
 StringBuilder sb = new StringBuilder();
 Document doc = Jsoup.parseBodyElements(comment.outerHtml(new Document.OutputSettings(true)), null);
 assertEquals(1, doc.childNodeSize());
 Element el = doc.child(0);
 assertEquals("#comment", el.nodeName());
 assertEquals("<!--This is a comment-->", el.outerHtml());
 }
}