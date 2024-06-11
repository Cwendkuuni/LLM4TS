import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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
 public void testIsXmlDeclarationFalse() {
 Comment comment = new Comment("This is a comment");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<comment>This is a comment</comment>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a comment");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head><title>The Dormouse's story</title>\n<meta charset=\"UTF-8\"/>\n<meta name=\"description\" content=\"An animated retelling of Lewis Carroll's 'Alice in Wonderland', as realized in Adobe Flash.\"/>\n<meta name=\"keywords\" content=\"Alice, in, Wonderland, animated, flash\"/>\n<meta name=\"author\" content=\"Disney\">\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n<style type=\"text/css\">@import url(\"styles.css\");</style>\n<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"favicon.ico\"/>\n<!--[if IE]>\n<script src=\"excanvas.min.js\"></script>\n<![endif]-->\n<script src=\"http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js\"></script>\n<script src=\"http://ajax.googleapis.com/ajax/libs/swfobject/2.2/flashplayer.js\"></script>\n<script src=\"script.js\"></script>\n</head>\n<body>\n<div id=\"container\"></div>\n</body>\n</html>\n<!--comment-->\n");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", xmlDeclaration.xmlData());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAsXmlDeclarationInvalidFormat() {
 Comment comment = new Comment("This is a comment");
 comment.asXmlDeclaration();
 }
}