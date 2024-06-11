import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseWithValidXml() {
 String xmlInput = "<root><element>Hello World</element></root>";
 doc = xmlTreeBuilder.parse(xmlInput, null);
 Element root = doc.getRootElement();
 Element element = root.getChild(0);
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseWithSelfClosingTag() {
 String xmlInput = "<root><img src=\"image.jpg\" /> </root>";
 doc = xmlTreeBuilder.parse(xmlInput, null);
 Element img = doc.getElementsByTag("img").first();
 assertEquals(1, img.children().size());
 }

 @Test
 public void testParseWithComment() {
 String xmlInput = "<root><!-- This is a comment --></root>";
 doc = xmlTreeBuilder.parse(xmlInput, null);
 Element comment = doc.getElementsByClass("comment").first();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseWithDoctype() {
 String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><element attr=\"value\"></element></root>";
 doc = xmlTreeBuilder.parse(xmlInput, null);
 Element element = doc.getChild(0);
 assertEquals("value", element.attr("attr"));
 }

 @Test
 public void testParseWithErrors() {
 String xmlInput = "<root><element><script>alert('Hello')</script></element></root>";
 ParseSettings settings = new ParseSettings();
 settings.setErrorHandler(new ParseErrorHandler() {
 @Override
 public void handleError(ParseError error) {
 System.out.println("Error: " + error.getMessage());
 }
 });
 doc = xmlTreeBuilder.parse(xmlInput, null, new ParseErrorList(), settings);
 Element element = doc.getChild(0);
 // Error handling logic here
 }

 @Test
 public void testParseFragment() {
 String xmlInput = "<element><subElement></subElement></element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList(), ParseSettings.preserveCase);
 Element element = (Element) nodes.get(0);
 Element subElement = (Element) element.child(0);
 assertEquals(1, subElement.children().size());
 }

 @Test
 public void testParseWithUnknownTag() {
 String xmlInput = "<root><unknownTag></unknownTag></root>";
 doc = xmlTreeBuilder.parse(xmlInput, null);
 Element unknownTag = doc.getElementsByTag("unknownTag").first();
 assertTrue(unknownTag.isUnknownTag());
 }
}