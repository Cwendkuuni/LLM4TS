import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testParseValidXml() {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 Element root = doc.getRootElement();
 Element element = root.getFirstChild();

 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseEmptyXml() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader(""));
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseInvalidXml() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String invalidXml = "<root><element< attribute=\"value\">Content</element>";
 Document doc = null;
 try {
 doc = xmlTreeBuilder.parse(new StringReader(invalidXml));
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(doc);
 }

 @Test
 public void testParseWithBaseUri() {
 String xml = "<import href=\"http://example.com/style.xml\"/><root><element attr=\"value\">Content</element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
 Element root = doc.getRootElement();
 Element element = root.getFirstChild();

 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());

 String href = element.attr("href");
 assertEquals("http://example.com/style.xml", href);
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, new Parser(xmlTreeBuilder));
 Node node = nodes.get(0);

 assertEquals("element", node.tagName());
 assertEquals("value", node.attr("attr"));
 assertEquals("Content", node.text());
 }

 @Test
 public void testParseFragmentWithContext() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<element attr=\"value\">Content</element>";
 Element context = new Element("root");
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, null, new Parser(xmlTreeBuilder));
 Node node = nodes.get(0);

 assertEquals("element", node.tagName());
 assertEquals("value", node.attr("attr"));
 assertEquals("Content", node.text());

 context.appendChild(node);
 Element root = context;
 assertEquals("root", root.tagName());
 assertEquals(1, root.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithInvalidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> nodes = xmlTreeBuilder.parseFragment("", null, null, new Parser(xmlTreeBuilder));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> nodes = xmlTreeBuilder.parseFragment("", null, null, new Parser(xmlTreeBuilder));
 assertEquals(0, nodes.size());
 }
}