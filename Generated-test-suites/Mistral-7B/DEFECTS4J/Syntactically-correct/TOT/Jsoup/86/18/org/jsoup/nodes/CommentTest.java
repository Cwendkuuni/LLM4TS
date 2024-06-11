import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 assertTrue(comment.getNodeName().equals("#comment"));
 assertSame(comment.getData(), "This is a comment");
 }

 @Test
 public void testCommentDeprecatedCreation() {
 Comment comment = new Comment("This is a comment", null);
 assertTrue(comment.getNodeName().equals("#comment"));
 assertSame(comment.getData(), "This is a comment");
 }

 @Test
 public void testCommentIsXmlDeclarationFalse() {
 Comment comment = new Comment("This is a comment");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentIsXmlDeclarationTrue() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a comment");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclarationXmlDeclaration() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<root><element>Value</element></root>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertSame(xmlDeclaration.getVersion(), "1.0");
 assertSame(xmlDeclaration.getEncoding(), "UTF-8");
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Comment comment = new Comment("This is a comment");
 Document document = Jsoup.parse("<root>" + comment.outerHtml() + "</root>");
 Element commentElement = document.selectFirst("#comment");
 assertSame(comment.getData(), commentElement.text());
 }
}