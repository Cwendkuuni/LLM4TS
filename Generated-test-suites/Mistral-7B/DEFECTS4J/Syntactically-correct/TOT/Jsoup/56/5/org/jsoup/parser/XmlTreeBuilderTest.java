import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokeniser;
 private ParseSettings parseSettings;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokeniser = new Tokenizer();
 parseSettings = new ParseSettings();
 }

 @Test
 public void testParseBasicXml() {
 String xml = "<root><child>Hello World</child></root>";
 Document document = xmlTreeBuilder.parse(xml, null);
 assertEquals("root", document.tagName());
 Element root = document.child(0);
 assertEquals("child", root.tagName());
 assertEquals("Hello World", root.child(0).text());
 }

 @Test
 public void testParseWithComments() {
 String xml = "<root><!-- comment1 --><child>Hello World</child><!-- comment2 --></root>";
 Document document = xmlTreeBuilder.parse(xml, null);
 List<Node> nodes = document.childNodes();
 assertEquals(3, nodes.size());
 Node firstComment = nodes.get(0);
 assertTrue(firstComment instanceof Comment);
 assertEquals("comment1", firstComment.nodeName());
 Node child = nodes.get(1);
 assertEquals("child", child.tagName());
 assertEquals("Hello World", child.child(0).text());
 Node lastComment = nodes.get(2);
 assertTrue(lastComment instanceof Comment);
 assertEquals("comment2", lastComment.nodeName());
 }

 @Test
 public void testParseWithSelfClosingTag() {
 String xml = "<root><img src=\"image.jpg\" /> </root>";
 Document document = xmlTreeBuilder.parse(xml, null);
 List<Node> nodes = document.childNodes();
 assertEquals(2, nodes.size());
 Element root = document.child(0);
 assertEquals("img", root.tagName());
 assertNotNull(root.attributes().get("src"));
 }

 @Test(expected = ParseError.class)
 public void testParseWithInvalidXml() {
 String xml = "<root><child><!-- comment --></child>";
 Document document = null;
 try {
 document = xmlTreeBuilder.parse(xml, null);
 } catch (ParseError e) {
 // expected
 }
 assertNull(document);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child>Hello World</child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList(), parseSettings);
 Document document = new Document();
 document.appendChild(nodes.get(0));
 assertEquals("root", document.tagName());
 Element root = document.child(0);
 assertEquals("child", root.tagName());
 assertEquals("Hello World", root.child(0).text());
 }

 @Test
 public void testParseWithXmlDeclaration() {
 String xml = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root><child>Hello World</child></root>";
 Document document = xmlTreeBuilder.parse(xml, null);
 List<Node> nodes = document.childNodes();
 assertEquals(3, nodes.size());
 Node xmlDeclaration = nodes.get(0);
 assertTrue(xmlDeclaration instanceof XmlDeclaration);
 assertEquals("root", xmlDeclaration.childNodes().get(0).nodeName());
 Element root = (Element) nodes.get(1);
 assertEquals("child", root.tagName());
 assertEquals("Hello World", root.child(0).text());
 }
}