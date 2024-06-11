import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class ParserTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<html><head></head><body><h1>Hello, World!</h1></body></html>");
 parser = new Parser("", doc.baseUri(), false);
 }

 @Test
 public void testParseHtml() {
 Document parsedDoc = Parser.parse(doc.body().html(), doc.baseUri());
 assertEquals(doc, parsedDoc);
 }

 @Test
 public void testParseBodyFragment() {
 Document parsedDoc = Parser.parseBodyFragment("<!-- comment --><p>A fragment.</p>", doc.baseUri());
 Element body = parsedDoc.body();
 assertEquals(1, body.childNodeSize());
 assertEquals("A fragment.", body.child(0).text());
 }

 @Test
 public void testParseBodyFragmentWithRelaxedParsing() {
 Document parsedDoc = Parser.parseBodyFragment("<p><span>A fragment.</span></p>", doc.baseUri());
 Element body = parsedDoc.body();
 assertEquals(1, body.childNodeSize());
 Element span = body.child(0);
 assertEquals(1, span.childNodeSize());
 assertEquals("A fragment.", span.child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, doc.baseUri(), false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("", null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyHtml() {
 Parser parser = new Parser("", doc.baseUri(), false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTagName() {
 parser.tq = new TokenQueue("<!-- MyInvalidTagName -->");
 parser.parseComment();
 fail("Should have thrown IllegalArgumentException for invalid tag name.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeName() {
 parser.tq = new TokenQueue("<p foo='bar'>Hello, World!</p>");
 parser.parseStartTag();
 parser.parseAttribute();
 fail("Should have thrown IllegalArgumentException for invalid attribute name.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeValue() {
 parser.tq = new TokenQueue("<p myattr='<script>alert(\"XSS\");'>Hello, World!</p>");
 parser.parseStartTag();
 parser.parseAttribute();
 parser.parseAttribute();
 fail("Should have thrown IllegalArgumentException for invalid attribute value.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndTag() {
 parser.tq = new TokenQueue("</foo>");
 parser.parseEndTag();
 fail("Should have thrown IllegalArgumentException for invalid end tag.");
 }
}