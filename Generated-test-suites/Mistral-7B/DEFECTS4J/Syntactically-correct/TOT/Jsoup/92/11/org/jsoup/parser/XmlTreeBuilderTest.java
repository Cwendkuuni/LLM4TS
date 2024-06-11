import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseXmlWithValidInput() {
 String xmlInput = "<root><element1>Test</element1><element2>Value</element2></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput));

 Element rootElement = doc.rootElement();
 List<Element> elements = rootElement.children();

 Validate.isTrue(elements.size() == 2);
 Validate.isTrue(rootElement.tagName().equals("root"));
 Validate.isTrue(elements.get(0).tagName().equals("element1"));
 Validate.isTrue(elements.get(1).tagName().equals("element2"));
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xmlInput = "<root><element selfClosing/></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput));

 Element rootElement = doc.rootElement();
 List<Element> elements = rootElement.children();

 Validate.isTrue(elements.size() == 1);
 Validate.isTrue(rootElement.tagName().equals("root"));
 Validate.isTrue(elements.get(0).tagName().equals("element"));
 Validate.isTrue(elements.get(0).tagName().isSelfClosing());
 }

 @Test
 public void testParseXmlWithEmptyElement() {
 String xmlInput = "<root><element elementName/></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput));

 Element rootElement = doc.rootElement();
 List<Element> elements = rootElement.children();

 Validate.isTrue(elements.size() == 1);
 Validate.isTrue(rootElement.tagName().equals("root"));
 Validate.isTrue(elements.get(0).tagName().equals("element"));
 Validate.isTrue(elements.get(0).tagName().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidInput() {
 String xmlInput = "<root><script>alert('Hello!')</script>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput));
 }

 @Test
 public void testParseXmlWithComment() {
 String xmlInput = "<root><!-- This is a comment --><element>Test</element></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput));

 Element rootElement = doc.rootElement();
 List<Element> elements = rootElement.children();

 Validate.isTrue(elements.size() == 1);
 Validate.isTrue(rootElement.tagName().equals("root"));
 Validate.isTrue(elements.get(0).tagName().equals("element"));
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xmlInput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput));

 DocumentType doctype = doc.doctype();
 Validate.isTrue(doctype != null);
 Validate.isTrue(doctype.name().equals("html"));
 Validate.isTrue(doctype.getPublicIdentifier().equals("-//W3C//DTD XHTML 1.0 Strict//EN"));
 Validate.isTrue(doctype.getSystemIdentifier().equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"));
 }

 @Test
 public void testParseXmlWithCData() {
 String xmlInput = "<root><![CDATA[Hello World!]]></root>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput));

 Element rootElement = doc.rootElement();
 List<Node> nodes = rootElement.childNodes();

 Validate.isTrue(nodes.size() == 1);
 Validate.isTrue(nodes.get(0) instanceof CDataNode);
 Validate.isTrue(((CDataNode) nodes.get(0)).getData().equals("Hello World!"));
 }

 @Test
 public void testParseXmlWithEmptyDocument() {
 String xmlInput = "<root/>";
 doc = xmlTreeBuilder.parse(new StringReader(xmlInput));

 Validate.isTrue(doc != null);
 Validate.isTrue(doc.childNodes().isEmpty());
 }
}