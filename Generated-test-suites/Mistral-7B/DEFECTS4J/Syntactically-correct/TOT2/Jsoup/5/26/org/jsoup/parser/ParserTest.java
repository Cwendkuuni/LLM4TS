import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.jsoup.parser.Parser;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @BeforeEach
 public void setup() {
 parser = new Parser("", "", false);
 doc = parser.parse("<html><body></body></html>");
 }

 @Test
 public void testParseEmptyHTML() {
 assertEquals(HtmlTag.HTML, doc.tagName());
 assertNotNull(doc.body());
 }

 @Test
 public void testParseBodyFragmentEmptyHTML() {
 Document bodyDoc = Parser.parseBodyFragment("", "");
 assertEquals(HtmlTag.HTML, bodyDoc.tagName());
 assertNotNull(bodyDoc.body());
 assertNotNull(bodyDoc.head());
 }

 @Test
 public void testParseBodyFragmentRelaxedEmptyHTML() {
 Document bodyDoc = Parser.parseBodyFragmentRelaxed("", "");
 assertEquals(HtmlTag.HTML, bodyDoc.tagName());
 assertNotNull(bodyDoc.body());
 assertNotNull(bodyDoc.head());
 }

 @Test
 public void testParseHTMLWithText() {
 doc = parser.parse("<html><body><p>Hello World</p></body></html>");
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseBodyFragmentWithText() {
 Document bodyDoc = Parser.parseBodyFragment("<p>Hello World</p>", "");
 assertEquals("Hello World", bodyDoc.text());
 }

 @Test
 public void testParseHTMLWithTextAndTag() {
 doc = parser.parse("<html><body><p>Hello World</p><div>Test</div></body></html>");
 assertEquals("Hello World", doc.body().text());
 assertNotNull(doc.body().getElementsByTag("div").first());
 }

 @Test
 public void testParseBodyFragmentWithTag() {
 Document bodyDoc = Parser.parseBodyFragment("<div>Test</div>", "");
 assertNotNull(bodyDoc.body().getElementsByTag("div").first());
 }

 @Test
 public void testParseHTMLWithEmptyTag() {
 doc = parser.parse("<html><body><img src=\"test.jpg\"/></body></html>");
 assertNotNull(doc.body().getElementsByTag("img").first());
 assertEquals(0, doc.body().getElementsByTag("img").size());
 }

 @Test
 public void testParseHTMLWithEmptyTagAndText() {
 doc = parser.parse("<html><body><img src=\"test.jpg\"/><p>Test</p></body></html>");
 assertNotNull(doc.body().getElementsByTag("img").first());
 assertEquals(1, doc.body().getElementsByTag("img").size());
 assertEquals("Test", doc.body().text().replaceAll("[^\\p{Java Latin}\\s]", ""));
 }

 @Test
 public void testParseInvalidHTML() {
 assertThrows(RuntimeException.class, () -> parser.parse("<html><body><script>alert('Hello')</script></body></html>"));
 }

 @Test
 public void testParseInvalidHTMLWithBaseTag() {
 Document doc = Parser.parse("<html><base href=\"/test\"><body></body></html>");
 assertEquals("/test", doc.baseUri());
 }

 @Test
 public void testParseInvalidHTMLWithBaseTagAndScriptTag() {
 assertThrows(RuntimeException.class, () -> Parser.parse("<html><base href=\"/test\"><script>alert('Hello')</script></body></html>"));
 }
}