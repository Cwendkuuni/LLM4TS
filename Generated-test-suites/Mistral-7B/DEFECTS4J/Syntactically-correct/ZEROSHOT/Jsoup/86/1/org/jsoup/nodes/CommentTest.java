package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Comment.java
 */
public class CommentTest {

 @Test
 public void testCreateNewComment() {
 Comment comment = new Comment("This is a comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCreateNewCommentWithBaseUri() {
 Comment comment = new Comment("This is a comment", "baseUri");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 assertEquals("baseUri", comment.baseUri());
 }

 @Test
 public void testIsXmlDeclaration() {
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(xmlDeclarationComment.isXmlDeclaration());
 Comment simpleComment = new Comment("This is a simple comment");
 assertFalse(simpleComment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
 Comment simpleComment = new Comment("This is a simple comment");
 assertNull(simpleComment.asXmlDeclaration());
 }

 @Test
 public void testToString() {
 Comment comment = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Comment comment = new Comment("This is a comment");
 Document doc = Jsoup.parseBodyElements(comment.outerHtml());
 Appendable accum = new StringBuilder();
 comment.outerHtmlHead(accum, 0, null);
 assertEquals("<!--This is a comment-->", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Comment comment = new Comment("This is a comment");
 Document doc = Jsoup.parseBodyElements(comment.outerHtml());
 Appendable accum = new StringBuilder();
 comment.outerHtmlTail(accum, 0, null);
 assertTrue("".equals(accum.toString()));
 }

 @Test
 public void testCreateCommentFromXmlDeclaration() throws IOException {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = Jsoup.parseBodyElements(xmlDeclaration).get(0);
 XmlDeclaration xmlDeclarationObj = comment.asXmlDeclaration();
 assertNotNull(xmlDeclarationObj);
 }

 @Test
 public void testCreateCommentWithAttributes() {
 Comment comment = new Comment("This is a comment", new Attribute("id", "commentId"), new Attribute("class", "commentClass"));
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 assertEquals("commentId", comment.attribute(0).getAttributeName());
 assertEquals("commentId", comment.attribute(0).getValue());
 assertEquals(2, comment.attributes().size());
 }
}