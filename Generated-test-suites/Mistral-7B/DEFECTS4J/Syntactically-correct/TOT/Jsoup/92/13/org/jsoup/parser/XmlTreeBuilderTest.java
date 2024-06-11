import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
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
 public void testParseSimpleXml() {
 String xml = "<root><element>Hello World</element></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xml));
 Element root = doc.rootElement();
 Element element = root.firstChild();
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xml = "<root><element /> </root>";
 doc = xmlTreeBuilder.parse(new StringReader(xml));
 Element root = doc.rootElement();
 Element element = root.firstChild();
 assertEquals("", element.text());
 }

 @Test
 public void testParseXmlWithComment() {
 String xml = "<root><!-- This is a comment --></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xml));
 assertEquals(0, doc.body().childNodes().size());
 }

 @Test
 public void testParseXmlWithError() {
 String xml = "<root><script type='text/javascript'>alert('Hello')</script>";
 doc = xmlTreeBuilder.parse(new StringReader(xml), null, new Parser());
 assertEquals(0, doc.body().childNodes().size());
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
 "<html><head></head><body></body></html>";
 doc = xmlTreeBuilder.parse(new StringReader(xml));
 DocumentType doctype = doc.doctype();
 assertEquals("html", doctype.name());
 }

 @Test
 public void testParseXmlWithFragment() {
 String xmlFragment = "<element1><element2>Hello</element2></element1>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, new Parser());
 Element element1 = (Element) nodes.get(0);
 Element element2 = (Element) element1.firstChild();
 assertEquals("Hello", element2.text());
 }

 @Test
 public void testParseXmlWithFragmentAndContext() {
 String xmlFragment = "<element1><element2>Hello</element2></element1>";
 Element context = new Element("root");
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, null, new Parser());
 Element element1 = (Element) nodes.get(0);
 Element element2 = (Element) element1.firstChild();
 assertEquals("Hello", element2.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateFail() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.Validate.fail("Test Failure");
 }

 @Test
 public void testParseEmptyXml() {
 doc = xmlTreeBuilder.parse(new StringReader(""), null, new Parser());
 assertEquals(0, doc.childNodes().size());
 }
}