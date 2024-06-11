import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document document = parser.parseInput(html, "");
 assert(document.title().text().equals("Test"));
 assert(document.body().children().get(0).tagName().equals("h1"));
 assert(document.body().children().get(0).text().equals("Hello World"));
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Test</p>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "");
 assert(nodes.size() == 1);
 assert(nodes.get(0).tagName().equals("p"));
 assert(nodes.get(0).text().equals("Test"));
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<root><child/></root>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "");
 assert(nodes.size() == 2);
 assert(nodes.get(0).tagName().equals("root"));
 assert(nodes.get(1).tagName().equals("child"));
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidHtmlWithTrackErrorsEnabled() {
 String invalidHtml = "<html><head><title>Test</title></title>"; // missing body tag
 parser.setTrackErrors(1);
 Document document = parser.parseInput(invalidHtml, "");
 assert(parser.getErrors().size() > 0);
 ParseError error = parser.getErrors().get(0);
 assertEquals(error.line(), 6);
 assertEquals(error.column(), 1);
 assertEquals(error.message(), "Expecting '>' after '<title>'");
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidHtmlWithTrackErrorsDisabled() {
 String invalidHtml = "<html><head><title>Test</title></title>"; // missing body tag
 parser.setTrackErrors(0);
 Document document = parser.parseInput(invalidHtml, "");
 assert(parser.getErrors() == null);
 }

 @Test
 public void testParseInputWithCustomTreeBuilder() {
 treeBuilder = new XmlTreeBuilder();
 parser = new Parser(treeBuilder);
 String xml = "<root><child/></root>";
 Document document = parser.parseInput(xml, "");
 assert(document.tagName().equals("root"));
 }

 @Test
 public void testParseSettings() {
 ParseSettings settings = parser.settings();
 settings.setTrimWhitespace(true);
 Document document = parser.parseInput("<html><body><p> Test </p></body></html>", "");
 assert(document.body().text().equals("Test"));
 }
}