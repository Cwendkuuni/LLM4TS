package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCommentCreationDeprecated() {
 Comment comment = new Comment("This is a comment", null);
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>");
 assertTrue(comment.isXmlDeclaration());

 Comment notXmlComment = new Comment("This is not an XML declaration");
 assertFalse(notXmlComment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>");
 XmlDeclaration xmlDeclaration = xmlComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());

 Comment notXmlComment = new Comment("This is not an XML declaration");
 assertNull(notXmlComment.asXmlDeclaration());
 }
}