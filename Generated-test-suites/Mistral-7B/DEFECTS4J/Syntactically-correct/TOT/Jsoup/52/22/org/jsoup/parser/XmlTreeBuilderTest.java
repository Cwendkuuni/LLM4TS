import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testXmlTreeBuilder() {
 // Testing initialisation and empty XML document
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String emptyXml = "<xml/>";
 Document xmlDocument = Jsoup.parse(emptyXml, "", xmlTreeBuilder);

 // Asserting document is empty
 List<Node> nodes = xmlDocument.childNodes();
 Assert.assertEquals(0, nodes.size());

 // Testing parsing of simple XML document
 String simpleXml = "<root><child>Hello World</child></root>";
 nodes = Jsoup.parse(simpleXml, "", xmlTreeBuilder).childNodes();
 Element root = (Element) nodes.get(0);
 Element child = (Element) nodes.get(1);

 // Asserting element names and text content
 Assert.assertEquals("root", root.nodeName());
 Assert.assertEquals("child", child.nodeName());
 Assert.assertEquals("Hello World", child.text());

 // Testing parsing of XML document with self-closing tag
 String selfClosingXml = "<root><child/></root>";
 nodes = Jsoup.parse(selfClosingXml, "", xmlTreeBuilder).childNodes();
 Element rootSelfClosing = (Element) nodes.get(0);
 Element childSelfClosing = (Element) nodes.get(1);

 // Asserting self-closing tag is parsed correctly
 Assert.assertEquals("root", rootSelfClosing.nodeName());
 Assert.assertEquals("child", childSelfClosing.nodeName());
 Assert.assertNull(childSelfClosing.text());

 // Testing parsing of XML document with comments
 String xmlWithComments = "<root><child><!-- This is a comment --></child></root>";
 nodes = Jsoup.parse(xmlWithComments, "", xmlTreeBuilder).childNodes();
 Element rootWithComments = (Element) nodes.get(0);
 Element childWithComments = (Element) nodes.get(1);
 Comment comment = (Comment) rootWithComments.childNodes().get(0);

 // Asserting comment is parsed correctly
 Assert.assertEquals("root", rootWithComments.nodeName());
 Assert.assertEquals("child", childWithComments.nodeName());
 Assert.assertEquals("-- This is a comment --", comment.getData());

 // Testing parsing of XML document with document type declaration
 String xmlWithDoctype = "<!DOCTYPE root SYSTEM \"test.dtd\">\n<root></root>";
 nodes = Jsoup.parse(xmlWithDoctype, "", xmlTreeBuilder).childNodes();
 DocumentType doctype = (DocumentType) nodes.get(0);
 Element rootWithDoctype = (Element) nodes.get(1);

 // Asserting document type declaration is parsed correctly
 Assert.assertEquals("root", rootWithDoctype.nodeName());
 Assert.assertEquals("root", doctype.nodeName());
 Assert.assertEquals("test.dtd", doctype.getSystemIdentifier());

 // Testing handling of empty input
 String emptyInput = "";
 nodes = Jsoup.parse(emptyInput, "", xmlTreeBuilder).childNodes();
 // Asserting document is empty
 Assert.assertEquals(0, nodes.size());

 // Testing handling of malformed input
 String malformedInput = "<xml><root><child><!--";
 nodes = Jsoup.parse(malformedInput, "", xmlTreeBuilder).childNodes();
 // Asserting document is empty
 Assert.assertEquals(0, nodes.size());
 }
}