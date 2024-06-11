import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Doctype;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private ParseSettings settings;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 settings = builder.defaultSettings();
 }

 @Test
 public void testParseValidXml() {
 String xmlString = "<root><element>Hello World</element></root>";
 Document doc = builder.parse(new StringReader(xmlString));
 Element root = doc.rootElement();
 Element element = root.getFirstChild();
 TextNode textNode = (TextNode) element.getFirstChild();
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xmlString = "<root><img src=\"image.jpg\" /> <element>Hello World</element></root>";
 Document doc = builder.parse(new StringReader(xmlString));
 Element root = doc.rootElement();
 Element img = root.getFirstChild();
 assertTrue(img.hasAttr("src"));
 assertEquals("image.jpg", img.attr("src"));
 Element element = root.getChild(1);
 TextNode textNode = (TextNode) element.getFirstChild();
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseXmlWithBogusComment() {
 String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<!--<![CDATA[CDATA[-->\n<script type=\"text/javascript\">\nfunction test() {\n alert(\"Hello World\");\n}\n//]]>\n-->\n<title>Test Page</title>\n</head>\n<body>\n<button onclick=\"test()\">Click me</button>\n</body>\n</html>";
 Document doc = builder.parse(new StringReader(xmlString));
 XmlDeclaration xmlDeclaration = doc.head().getFirstChild();
 assertTrue(xmlDeclaration instanceof XmlDeclaration);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXmlWithMissingStartTag() {
 String xmlString = "<root<element>Hello World</element>";
 try {
 builder.parse(new StringReader(xmlString));
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXmlWithMissingEndTag() {
 String xmlString = "<root><element>Hello World<";
 try {
 builder.parse(new StringReader(xmlString));
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXmlWithMultipleRoots() {
 String xmlString = "<root><element>Hello World</element></root><root><element>Hello World</element></root>";
 try {
 builder.parse(new StringReader(xmlString));
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test
 public void testParseXmlFragment() {
 String xmlFragment = "<element><subElement>Sub Element</subElement></element>";
 List<Node> nodes = builder.parseFragment(xmlFragment, null, null, new Parser(builder));
 Node node = nodes.get(0);
 Element element = (Element) node;
 SubElement subElement = (SubElement) element.getFirstChild();
 assertEquals("Sub Element", subElement.text());
 }

 @Test
 public void testParseXmlFragmentWithContext() {
 String xmlFragment = "<newElement><subElement>Sub Element</subElement></newElement>";
 Document doc = new Document();
 doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 Element root = doc.addElement("root");
 root.appendChild(doc.parseFragment(xmlFragment, root, null, new Parser(builder)));
 List<Node> nodes = root.childNodes();
 Node node = nodes.get(0);
 Element element = (Element) node;
 SubElement subElement = (SubElement) element.getFirstChild();
 assertEquals("Sub Element", subElement.text());
 assertEquals("root", root.nodeName());
 }
}