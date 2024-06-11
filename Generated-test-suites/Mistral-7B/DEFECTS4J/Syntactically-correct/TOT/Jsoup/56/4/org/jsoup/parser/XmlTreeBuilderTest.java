import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.ParseError;
import org.jsoup.helper.Validate;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokenizer;
 private ParseError error;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokenizer = new Tokenizer();
 }

 @Test
 public void testParseSimpleXml() {
 String xmlInput = "<root><element>Hello World</element></root>";
 Document doc = xmlTreeBuilder.parse(xmlInput, null);
 assertNotNull(doc);
 Element root = doc.rootElement();
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseWithErrors() {
 String xmlInput = "<root><element><script src=\"javascript:alert('Hello')\"></script></element></root>";
 ParseSettings settings = ParseSettings.preserveHtml;
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList(), settings);
 assertNotNull(nodes);
 error = nodes.get(nodes.size() - 1);
 assertTrue(error instanceof ParseError);
 assertEquals("Unexpected tag name: script", error.getMessage());
 }

 @Test
 public void testParseSelfClosingTag() {
 String xmlInput = "<img src=\"image.jpg\" />";
 Document doc = xmlTreeBuilder.parse(xmlInput, null);
 assertNotNull(doc);
 Element img = doc.select("img").first();
 assertEquals("img", img.tagName());
 assertTrue(img.hasAttr("src"));
 }

 @Test
 public void testParseComment() {
 String xmlInput = "<!-- This is a comment -->";
 Document doc = xmlTreeBuilder.parse(xmlInput, null);
 assertNotNull(doc);
 Element comment = doc.body();
 assertTrue(comment.tagName().equals("#comment"));
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseDoctype() {
 String xmlInput = "<!DOCTYPE html SYSTEM \"About.dtd\">";
 Document doc = xmlTreeBuilder.parse(xmlInput, null);
 assertNotNull(doc.doctype());
 assertEquals("html", doc.doctype().name());
 }

 @Test
 public void testParseFragment() {
 String xmlInput = "<fragment><root><element>Hello World</element></root></fragment>";
 ParseSettings settings = ParseSettings.preserveCase;
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList(), settings);
 assertNotNull(nodes);
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);
 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("Hello World", element.text());
 }
}