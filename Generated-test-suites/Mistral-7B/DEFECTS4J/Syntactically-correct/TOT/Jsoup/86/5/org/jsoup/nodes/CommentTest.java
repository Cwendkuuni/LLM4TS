import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Comment class
 */
public class CommentTest {

 @Test
 public void testCreateComment() {
 // Test creating a comment with valid data
 Comment comment = new Comment("This is a comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());

 // Test creating a comment with empty data
 Comment emptyComment = new Comment("");
 assertEquals("#comment", emptyComment.nodeName());
 assertEquals("", emptyComment.getData());
 }

 @Test
 public void testIsXmlDeclaration() {
 // Test valid XML declaration
 Comment xmlDeclComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(xmlDeclComment.isXmlDeclaration());

 // Test invalid XML declaration
 Comment invalidXmlDeclComment = new Comment("!-- invalid xml declaration --");
 assertFalse(invalidXmlDeclComment.isXmlDeclaration());

 // Test comment without XML declaration
 Comment normalComment = new Comment("This is a normal comment");
 assertFalse(normalComment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 // Test valid XML declaration
 Comment xmlDeclComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration xmlDecl = xmlDeclComment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.toString());

 // Test invalid XML declaration
 Comment invalidXmlDeclComment = new Comment("!-- invalid xml declaration --");
 assertNull(invalidXmlDeclComment.asXmlDeclaration());

 // Test comment without XML declaration
 Comment normalComment = new Comment("This is a normal comment");
 assertNull(normalComment.asXmlDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCommentWithNullData() {
 // Test creating a comment with null data
 Comment comment = new Comment(null);
 fail("Expected IllegalArgumentException");
 }
}