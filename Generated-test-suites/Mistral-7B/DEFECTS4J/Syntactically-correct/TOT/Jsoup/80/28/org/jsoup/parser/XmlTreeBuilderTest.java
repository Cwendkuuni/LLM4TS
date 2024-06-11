import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.CDataSection;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.ParseError;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseValidXml() {
 String xmlInput = "<root><element attr='value'>Content</element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://localhost");

 assertEquals("root", document.tagName());
 List<Node> elements = document.getElementsByTag("element");
 assertEquals(1, elements.size());
 Element element = (Element) elements.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseXmlWithComments() {
 String xmlInput = "<root><!-- comment1 --><!-- comment2 --><element attr='value'>Content</element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://localhost");
 List<Node> comments = document.getElementsByTag("comment");
 assertEquals(2, comments.size());
 for (Node node : comments) {
 if (node instanceof Comment) {
 assertEquals(node.text(), "-- comment" + (node.id() > 0 ? Integer.toString(node.id()) : ""));
 }
 }
 }

 @Test
 public void testParseXmlWithCdata() {
 String xmlInput = "<root><![CDATA[This is CData]]><element attr='value'>Content</element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://localhost");
 List<Node> cdataSections = document.getElementsByTag("cdata");
 assertEquals(1, cdataSections.size());
 Node cdataSection = cdataSections.get(0);
 assertTrue(cdataSection instanceof CDataSection);
 assertEquals("This is CData", cdataSection.text());
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr='value'>Content</element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://localhost");
 List<Node> doctypeNodes = document.getElementsByTag("!DOCTYPE");
 assertEquals(1, doctypeNodes.size());
 Node doctypeNode = doctypeNodes.get(0);
 assertTrue(doctypeNode instanceof DocumentType);
 assertEquals("root", doctypeNode.nodeName());
 List<Attributes> attributes = doctypeNode.attributes();
 assertEquals(1, attributes.size());
 assertEquals("root", attributes.get(0).getKey());
 assertEquals("element", attributes.get(0).getValue("element"));
 assertEquals("CDATA", attributes.get(0).getValue("ATTLIST"));
 assertEquals("REQUIRED", attributes.get(0).getValue("REQUIRED"));
 List<Node> elements = document.getElementsByTag("element");
 assertEquals(1, elements.size());
 Element element = (Element) elements.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseXmlWithError() {
 String xmlInput = "<root><element attr='value'>Content<script>alert('Hello')</script></element></root>";
 List<ParseError> parseErrors = new ArrayList<>();
 document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://localhost", parseErrors, xmlTreeBuilder.defaultSettings());
 assertTrue(parseErrors.size() > 0);
 assertEquals(ParseError.SYNTAX_ERROR, parseErrors.get(0).getType());
 assertEquals("Error parsing XML: Unexpected token script", parseErrors.get(0).getMessage());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr='value'>Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://localhost", new ParseErrorList(), xmlTreeBuilder.defaultSettings());
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseInvalidXml() { // This test case should cause an exception
 String xmlInput = "<root><script>alert('Hello')</script>";
 document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://localhost");
 Validate.fail("Test should have failed with a ParseException");
 }
}