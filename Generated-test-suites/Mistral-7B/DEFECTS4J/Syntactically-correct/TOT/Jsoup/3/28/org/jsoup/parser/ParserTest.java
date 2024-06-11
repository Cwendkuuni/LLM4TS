import org.junit.jupiter.api.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTest {

 private Document document;

 @BeforeAll
 public void setUp() {
 document = Parser.parse("<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 }

 @Test
 public void testParseHtml() {
 Element html = document.body();
 assertEquals("html", html.tagName());
 Element head = html.child(0);
 assertEquals("head", head.tagName());
 Element title = head.child(0);
 assertEquals("title", title.tagName());
 assertEquals("Test Page", title.text());
 Element body = html.child(1);
 assertEquals("body", body.tagName());
 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Document document = Parser.parseBodyFragment("<p>Test body fragment</p>", "http://example.com");
 Element body = document.body();
 Element p = body.child(0);
 assertEquals("p", p.tagName());
 assertEquals("Test body fragment", p.text());
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Document document = Parser.parseBodyFragment("", "http://example.com");
 assertEquals(0, document.body().childSize());
 }

 @Test
 public void testParseInvalidHtml() {
 try {
 Parser.parse("<html><head><title>Test Page</title></body></html>", "http://example.com");
 fail("Expected IllegalArgumentException when parsing invalid HTML");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseInvalidBaseUri() {
 try {
 Parser.parse("<html><head><title>Test Page</title></head><body><base href='javascript:void(0)'><a href='#'></a></body></html>", "http://example.com");
 fail("Expected IllegalArgumentException when parsing invalid base URI");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @AfterAll
 public void tearDown() {
 document = null;
 }

}