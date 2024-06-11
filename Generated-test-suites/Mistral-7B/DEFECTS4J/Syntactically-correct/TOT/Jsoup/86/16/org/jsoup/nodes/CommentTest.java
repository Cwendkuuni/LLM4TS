import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 Comment comment = new Comment("This is a comment.", "/test/page.html");
 assertEquals("#comment", comment.nodeName());
 assertNull(comment.getBaseUri());
 }

 @Test
 public void testIsXmlDeclarationWithValidXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is a comment -->");
 assertTrue(xmlComment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithInvalidXmlDeclaration() {
 Comment comment = new Comment("This is a comment.");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithEmptyComment() {
 Comment comment = new Comment("");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithCommentOnly() {
 Comment comment = new Comment("<!-- This is a comment -->");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithXmlDeclarationMissingDeclaration() {
 Comment comment = new Comment("<?\n<!-- This is a comment -->");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithXmlDeclarationMissingVersion() {
 Comment comment = new Comment("<?\nencoding=\"UTF-8\"\n<!-- This is a comment -->");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithXmlDeclarationMissingEncoding() {
 Comment comment = new Comment("<? version=\"1.0\"\n<!-- This is a comment -->");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithXmlDeclarationMissingDeclarationAndEncoding() {
 Comment comment = new Comment("<?\n<!-- This is a comment -->");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationWithValidXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is a comment -->");
 XmlDeclaration xmlDecl = xmlComment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertEquals("xml", xmlDecl.getNamespace());
 assertTrue(xmlDecl.isStandalone());
 }

 @Test
 public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
 Comment comment = new Comment("This is a comment.");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNull(xmlDecl);
 }

 @Test
 public void testAsXmlDeclarationWithEmptyComment() {
 Comment comment = new Comment("");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNull(xmlDecl);
 }

 @Test
 public void testAsXmlDeclarationWithCommentOnly() {
 Comment comment = new Comment("<!-- This is a comment -->");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNull(xmlDecl);
 }

 @Test
 public void testAsXmlDeclarationWithXmlDeclarationMissingDeclaration() {
 Comment comment = new Comment("<?\n<!-- This is a comment -->");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNull(xmlDecl);
 }

 @Test
 public void testAsXmlDeclarationWithXmlDeclarationMissingVersion() {
 Comment comment = new Comment("<?\nencoding=\"UTF-8\"\n<!-- This is a comment -->");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNull(xmlDecl);
 }

 @Test
 public void testAsXmlDeclarationWithXmlDeclarationMissingEncoding() {
 Comment comment = new Comment("<? version=\"1.0\"\n<!-- This is a comment -->");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNull(xmlDecl);
 }

 @Test
 public void testAsXmlDeclarationWithXmlDeclarationMissingDeclarationAndEncoding() {
 Comment comment = new Comment("<?\n<!-- This is a comment -->");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNull(xmlDecl);
 }
}