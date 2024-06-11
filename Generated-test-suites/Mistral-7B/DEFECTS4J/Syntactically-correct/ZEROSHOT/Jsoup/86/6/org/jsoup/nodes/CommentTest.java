package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Comment.java
 *
 * @author John Doe
 */
public class CommentTest {

 @Test
 public void testCommentConstruction() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentDeprecatedConstruction() {
 Comment comment = new Comment("This is a comment.", "baseUri");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("<!--This is a comment.-->", comment.toString());
 }

 @Test
 public void testCommentIsXmlDeclarationFalse() {
 Comment comment = new Comment("This is a comment.");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentIsXmlDeclarationTrue() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is a comment. -->");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a comment.");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclarationXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- <root><element>Value</element></root> -->");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
 }

 @Test
 public void testCommentOuterHtmlHead() throws IOException {
 Comment comment = new Comment("This is a comment.");
 Appendable accum = Jsoup.output().size();
 comment.outerHtmlHead(accum, 0, null);
 assertEquals("<!--This is a comment.-->\n", accum.toString());
 }

 @Test
 public void testCommentOuterHtmlTail() throws IOException {
 Comment comment = new Comment("This is a comment.");
 Appendable accum = Jsoup.output().size();
 comment.outerHtmlTail(accum, 0, null);
 assertEquals("", accum.toString());
 }

 @Test
 public void testParseComment() throws IOException {
 Document doc = Jsoup.parse("<html><!--This is a comment.--></html>");
 Comment comment = (Comment) doc.body();
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testParseXmlDeclarationComment() throws IOException {
 Document doc = Jsoup.parse("<html><!-- <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element>Value</element></root> --></html>");
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.body();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
 Comment comment = (Comment) xmlDeclaration.nextSibling();
 assertEquals("#comment", comment.nodeName());
 assertEquals("", comment.getData());
 }
}