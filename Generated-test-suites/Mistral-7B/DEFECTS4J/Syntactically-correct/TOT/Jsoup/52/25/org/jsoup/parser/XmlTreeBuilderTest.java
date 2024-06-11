import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParsingSimpleXML() throws IOException {
 String xml = "<root><element>Hello World</element></root>";
 Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 assertEquals("root", doc.rootElement().nodeName());
 assertEquals(1, nodes.size());
 assertEquals("element", nodes.get(0).nodeName());
 assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 public void testParsingXMLWithSelfClosingTag() throws IOException {
 String xml = "<root><element /></root>";
 Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 assertEquals("root", doc.rootElement().nodeName());
 assertEquals(1, nodes.size());
 assertEquals("element", nodes.get(0).nodeName());
 assertTrue(nodes.get(0).hasAttr("")); // check for empty attribute
 }

 @Test
 public void testParsingXMLWithAttributes() throws IOException {
 String xml = "<root elementAttr=\"value\"><element attr1=\"value1\" attr2=\"value2\"/></root>";
 Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 assertEquals("root", doc.rootElement().nodeName());
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("element", root.childNodes().get(0).nodeName());
 assertEquals("value", root.attr("elementAttr"));
 assertEquals("value1", root.childNodes().get(0).attr("attr1"));
 assertEquals("value2", root.childNodes().get(0).attr("attr2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParsingXMLWithInvalidTagName() throws IOException {
 String xml = "<root><x></x></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 fail("Expected an IllegalArgumentException, but got: " + nodes.toString());
 }

 @Test
 public void testParsingXMLWithDoctype() throws IOException {
 String xml = "<!DOCTYPE root SYSTEM \"about:legacy-compat\"><root><element></root>";
 Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 DocumentType doctype = (DocumentType) nodes.get(0);
 assertEquals("root", doc.rootElement().nodeName());
 assertEquals(1, nodes.size());
 assertEquals("!DOCTYPE", doctype.nodeName());
 }

 @Test
 public void testParsingXMLWithComment() throws IOException {
 String xml = "<!-- This is a comment --><root><element></root>";
 Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 Comment comment = (Comment) nodes.get(0);
 assertEquals("root", doc.rootElement().nodeName());
 assertEquals(1, nodes.size());
 assertEquals("-- This is a comment --", comment.getData());
 }

 @Test
 public void testParsingEmptyXML() throws IOException {
 String xml = "";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 assertEquals(0, nodes.size());
 }

 @Test
 public void testParsingMalformedXML() throws IOException {
 String xml = "<root>"; // missing closing tag
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 assertTrue(nodes.isEmpty());
 }
}