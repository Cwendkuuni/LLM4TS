package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Comment class
 *
 * @author <YourName>
 */
public class CommentTest {

 @Test
 public void testCreateComment() {
 Comment comment = new Comment("This is a comment");
 assertEquals("#comment", comment.nodeName());
 assertNull(comment.baseUri());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCreateCommentWithBaseUri() {
 Comment comment = new Comment("This is a comment", "http://example.com");
 assertEquals("#comment", comment.nodeName());
 assertEquals("http://example.com", comment.baseUri());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n");
 assertTrue(comment.isXmlDeclaration());

 Comment comment2 = new Comment("This is a comment.");
 assertFalse(comment2.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertNotNull(xmlDeclaration.getAttributes());
 assertEquals(2, xmlDeclaration.getAttributes().size());
 assertEquals("version", xmlDeclaration.getAttributes().get(0).getKey());
 assertEquals("1.0", xmlDeclaration.getAttributes().get(0).getValue());
 assertEquals("encoding", xmlDeclaration.getAttributes().get(1).getKey());
 assertEquals("UTF-8", xmlDeclaration.getAttributes().get(1).getValue());
 }

 @Test
 public void testCommentAsString() {
 Comment comment = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", comment.toString());
 }
}