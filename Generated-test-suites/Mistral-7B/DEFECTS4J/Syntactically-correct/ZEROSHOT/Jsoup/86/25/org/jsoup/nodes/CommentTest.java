package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for Comment.java
 */
public class CommentTest {

 @Test
 public void testCreateComment() {
 // Test default constructor
 Comment comment = new Comment("");
 assertEquals("#comment", comment.nodeName());
 assertNull(comment.getData());

 // Test constructor with data
 Comment commentWithData = new Comment("This is a comment");
 assertEquals("#comment", commentWithData.nodeName());
 assertEquals("This is a comment", commentWithData.getData());
 }

 @Test
 public void testIsXmlDeclaration() {
 // Test empty comment
 Comment emptyComment = new Comment("");
 assertFalse(emptyComment.isXmlDeclaration());

 // Test comment with XML Declaration
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(xmlDeclarationComment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 // Test empty comment
 Comment emptyComment = new Comment("");
 assertNull(emptyComment.asXmlDeclaration());

 // Test comment with XML Declaration
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getDeclaredContent());
 }

 @Test
 public void testOuterHtml() {
 // Test empty comment
 Comment emptyComment = new Comment("");
 assertEquals("<!---->", emptyComment.outerHtml());

 // Test comment with data
 Comment commentWithData = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", commentWithData.outerHtml());
 }

 @Test
 public void testToString() {
 // Test empty comment
 Comment emptyComment = new Comment("");
 assertEquals("", emptyComment.toString());

 // Test comment with data
 Comment commentWithData = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", commentWithData.toString());
 }

 @Test
 public void testCreateCommentWithDeprecatedConstructor() {
 // Test deprecated constructor
 Comment commentWithDataAndBaseUri = new Comment("This is a comment", "http://example.com");
 assertEquals("#comment", commentWithDataAndBaseUri.nodeName());
 assertEquals("This is a comment", commentWithDataAndBaseUri.getData());
 }
}