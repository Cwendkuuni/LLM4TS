package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

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
 public void testCommentOuterHtml() throws IOException {
 Comment comment = new Comment("This is a comment");
 Document doc = Jsoup.parse("", Document.OutputSettings.prettyPrint());
 doc.head().append(comment);
 StringBuilder sb = new StringBuilder();
 comment.outerHtmlHead(sb, 0, Document.OutputSettings.prettyPrint());
 sb.append(comment.getData());
 sb.append("-->");
 String expected = "<!--This is a comment-->";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>This is a comment</body></html>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>This is a comment</body></html>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.data());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCommentAsXmlDeclarationFailure() {
 Comment comment = new Comment("This is a comment");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 fail("Should have thrown UnsupportedOperationException");
 }
}