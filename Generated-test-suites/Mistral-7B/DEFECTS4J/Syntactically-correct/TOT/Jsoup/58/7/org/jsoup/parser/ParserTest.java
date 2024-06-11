import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.xml.XmlTreeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private Parser xmlParser;

 @Before
 public void setUp() {
 parser = new Parser(new HtmlTreeBuilder());
 xmlParser = new Parser(new XmlTreeBuilder());
 }

 @Test
 public void testParseHtmlDocument() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document document = parser.parseInput(html, null);

 assertEquals("Hello World", document.getElementsByTag("h1").text());
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testParseHtmlFragment() {
 String fragmentHtml = "<h1>Hello World</h1>";
 Element context = Jsoup.parseBodyElements(Jsoup.parseBodyFragment("<html><body></body></html>", null)).get(0);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 assertEquals(1, nodes.size());
 assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 public void testParseHtmlWithErrors() {
 String htmlWithErrors = "<html><body><script>alert('Hello World');</script></body></html>";
 Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
 parserWithErrors.setTrackErrors(1);
 List<ParseError> errors = parserWithErrors.parseInput(htmlWithErrors, null).errors();
 assertFalse(parserWithErrors.isTrackErrors());
 assertEquals(1, errors.size());
 assertEquals("Unexpected '<', expecting '<!DOCTYPE'", errors.get(0).message());
 }

 @Test
 public void testParseXmlDocument() {
 String xml = "<root><element>Hello World</element></root>";
 Document document = xmlParser.parseInput(xml, null);
 assertEquals("Hello World", document.getElementsByTag("element").text());
 assertNotNull(document.head()); // XML documents have no head
 assertNull(document.body()); // XML documents have no body
 }
}