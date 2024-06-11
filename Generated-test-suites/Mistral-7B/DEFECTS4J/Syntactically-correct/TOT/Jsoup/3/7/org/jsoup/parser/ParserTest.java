import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html>\n" +
 "<head>\n" +
 "<title>Test Page</title>\n" +
 "</head>\n" +
 "<body>\n" +
 "<h1>Test Heading</h1>\n" +
 "<p>Test Paragraph</p>\n" +
 "<img src=\"image.jpg\" alt=\"Test Image\">\n" +
 "<script src=\"script.js\"></script>\n" +
 "</body>\n" +
 "</html>";
 String baseUri = "http://test.com";
 this.parser = new Parser(html, baseUri, false);
 this.doc = parser.parse();
 }

 @Test
 public void testParseHtml() {
 // Test parsing HTML into a Document
 Element htmlElement = doc.head();
 assertEquals(htmlTag.toString(), htmlElement.tagName());
 Element headElement = htmlElement.child(0);
 assertEquals(headTag.toString(), headElement.tagName());
 Element titleElement = headElement.child(0);
 assertEquals(titleTag.toString(), titleElement.tagName());
 Element bodyElement = doc.body();
 assertEquals(bodyTag.toString(), bodyElement.tagName());
 Element h1Element = bodyElement.child(0);
 assertEquals(h1Tag.toString(), h1Element.tagName());
 Element pElement = bodyElement.child(1);
 assertEquals(pTag.toString(), pElement.tagName());
 Element imgElement = bodyElement.child(2);
 assertEquals(imgTag.toString(), imgElement.tagName());
 }

 @Test
 public void testParseBodyFragment() {
 // Test parsing a fragment of HTML into the body of a Document
 String bodyHtml = "<img src=\"image.jpg\" alt=\"Test Image\">\n" +
 "<script src=\"script.js\"></script>";
 Document bodyDoc = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertEquals(doc.tagName(), bodyDoc.tagName());
 Element bodyElement = bodyDoc.body();
 assertEquals(bodyElement.tagName(), bodyTag.toString());
 Element imgElement = bodyElement.child(0);
 assertEquals(imgTag.toString(), imgElement.tagName());
 Element scriptElement = bodyElement.child(1);
 assertEquals(scriptTag.toString(), scriptElement.tagName());
 }

 @Test
 public void testParseCdata() {
 // Test parsing CDATA sections
 String htmlWithCdata = "<html>\n" +
 "<head>\n" +
 "<title>Test Page</title>\n" +
 "<![CDATA[script type=\"text/javascript\" src=\"script.js\"]]>\n" +
 "</head>\n" +
 "<body>\n" +
 "</body>\n" +
 "</html>";
 Parser parserWithCdata = new Parser(htmlWithCdata, baseUri, false);
 Document docWithCdata = parserWithCdata.parse();
 Element scriptElement = docWithCdata.head().child(0);
 assertTrue(scriptElement.data().contains("script type=\"text/javascript\" src=\"script.js\""));
 }

 @Test
 public void testParseComment() {
 // Test parsing HTML comments
 String htmlWithComment = "<html>\n" +
 "<!-- This is a comment -->\n" +
 "<head>\n" +
 "<title>Test Page</title>\n" +
 "</head>\n" +
 "<body>\n" +
 "</body>\n" +
 "</html>";
 Parser parserWithComment = new Parser(htmlWithComment, baseUri, false);
 Document docWithComment = parserWithComment.parse();
 Element commentElement = docWithComment.head().child(0);
 assertTrue(commentElement.tagName().equals(Comment.class.getSimpleName()));
 assertEquals("This is a comment", commentElement.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 // Test parsing null HTML
 String html = null;
 String baseUri = "http://test.com";
 try {
 Parser parser = new Parser(html, baseUri, false);
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 // Test parsing with null base URI
 String html = "<html>\n" +
 "<head>\n" +
 "<title>Test Page</title>\n" +
 "</head>\n" +
 "<body>\n" +
 "</body>\n" +
 "</html>";
 String baseUri = null;
 try {
 Parser parser = new Parser(html, baseUri, false);
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyHtml() {
 // Test parsing empty HTML
 String html = "";
 String baseUri = "http://test.com";
 try {
 Parser parser = new Parser(html, baseUri, false);
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testParseEmptyBodyFragment() {
 // Test parsing an empty fragment of HTML into the body of a Document
 String bodyHtml = "";
 Document bodyDoc = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertEquals(doc.tagName(), bodyDoc.tagName());
 Element bodyElement = bodyDoc.body();
 assertTrue(bodyElement.getChildren().isEmpty());
 }
}