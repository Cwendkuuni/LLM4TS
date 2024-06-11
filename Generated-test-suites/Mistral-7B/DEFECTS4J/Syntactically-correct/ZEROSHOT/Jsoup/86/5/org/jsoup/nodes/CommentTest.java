package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for Comment.java
 *
 * @author [Your Name]
 */
public class CommentTest {

 private Comment comment;

 @Test
 public void testCommentCreation() {
 comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 comment = new Comment("This is a comment.", "http://example.com");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 appendable accum = new StringBuffer();
 Document doc = new Document();
 doc.setOutputSettings(Document.OutputSettings.prettyPrint());
 comment.outerHtmlHead(accum, 0, doc.outputSettings());
 String expected = "<!--This is a comment.-->";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 appendable accum = new StringBuffer();
 Document doc = new Document();
 comment.outerHtmlTail(accum, 0, doc.outputSettings());
 assertEquals("", accum.toString());
 }

 @Test
 public void testToString() {
 String expected = "<!--This is a comment.-->";
 assertEquals(expected, comment.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(comment.isXmlDeclaration());
 comment = new Comment("This is a comment.");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 XmlDeclaration expected = new XmlDeclaration("1.0", "UTF-8", null);
 comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration actual = comment.asXmlDeclaration();
 assertEquals(expected, actual);
 comment = new Comment("This is a comment.");
 assertNull(comment.asXmlDeclaration());
 }
}