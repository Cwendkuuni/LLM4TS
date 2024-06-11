package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for Comment class.
 */
public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentDeprecatedCreation() {
 Comment comment = new Comment("This is a comment.", null);
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentOuterHtml() throws Exception {
 String commentData = "<!--This is a comment-->";
 Comment comment = new Comment(commentData);
 Document doc = Jsoup.parse("<p>" + commentData + "</p>");
 String expected = "<!--This is a comment-->";
 assertEquals(expected, comment.outerHtml());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 String xmlDeclaration = "<?xml version='1.0' encoding='UTF-8'?>";
 Comment comment = new Comment(xmlDeclaration);
 assertTrue(comment.isXmlDeclaration());

 String nonXmlDeclaration = "-->This is a comment<--";
 Comment nonXmlComment = new Comment(nonXmlDeclaration);
 assertFalse(nonXmlComment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() throws Exception {
 String xmlDeclaration = "<?xml version='1.0' encoding='UTF-8'?>";
 Comment comment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDeclarationNode = comment.asXmlDeclaration();
 assertNull(xmlDeclarationNode);

 String xmlDeclarationWithElement = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><!--<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<title>Test Page</title>\n</head>\n<body>\n<p>\n<!--<?xml version='1.0' encoding='UTF-8'?>\n<!--Test-->\n</p>\n</body>\n</html>";
 Comment xmlDeclarationComment = new Comment(xmlDeclarationWithElement);
 XmlDeclaration xmlDeclarationNodeExpected = new XmlDeclaration("html", true);
 assertEquals(xmlDeclarationNodeExpected, xmlDeclarationComment.asXmlDeclaration());
 }

}