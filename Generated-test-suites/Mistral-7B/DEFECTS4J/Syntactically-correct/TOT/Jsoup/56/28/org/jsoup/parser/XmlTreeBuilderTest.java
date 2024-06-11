import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokeniser;
 private ParseSettings settings;
 private ParseErrorList errorList;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokeniser = xmlTreeBuilder.tokeniser();
 settings = xmlTreeBuilder.defaultSettings();
 errorList = new ParseErrorList();
 }

 @Test
 public void testParse_ValidXml() throws Exception {
 String xmlInput = "<root><element attr=\"value\">Hello World</element></root>";
 document = xmlTreeBuilder.parse(xmlInput, null);
 Element root = document.rootElement();
 Element element = root.child(0);
 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Hello World", element.child(0).text());
 }

 @Test
 public void testParse_EmptyXml() throws Exception {
 String xmlInput = "<root></root>";
 document = xmlTreeBuilder.parse(xmlInput, null);
 Element root = document.rootElement();
 assertNull(document.childNodes().size());
 assertEquals("root", root.tagName());
 }

 @Test
 public void testParse_InvalidXml() throws Exception {
 String xmlInput = "<root><element<attr=\"value\">Hello World<";
 try {
 document = xmlTreeBuilder.parse(xmlInput, null);
 fail("Expected ParseException");
 } catch (Exception e) {
 assertEquals(ParseErrorList.class, e.getClass());
 assertNotNull(e.getCause());
 }
 }

 @Test
 public void testParseFragment_ValidXml() throws Exception {
 String xmlFragment = "<element attr=\"value\">Hello World</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errorList, settings);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Hello World", element.child(0).text());
 }

 @Test
 public void testParseFragment_EmptyXml() throws Exception {
 String xmlFragment = "<root></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errorList, settings);
 assertNull(nodes.size());
 }

 @Test
 public void testParseFragment_InvalidXml() throws Exception {
 String xmlFragment = "<root><element<attr=\"value\">Hello World<";
 try {
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, errorList, settings);
 fail("Expected ParseException");
 } catch (Exception e) {
 assertEquals(ParseErrorList.class, e.getClass());
 assertNotNull(e.getCause());
 }
 }

 @Test
 public void testPopStackToClose_ValidClosingTag() throws Exception {
 Element root = new Element("root");
 Element element1 = new Element("element1");
 Element element2 = new Element("element2");
 xmlTreeBuilder.insert(new Token.StartTag("root"));
 xmlTreeBuilder.insert(new Token.StartTag("element1"));
 xmlTreeBuilder.insert(new Token.StartTag("element2"));
 xmlTreeBuilder.insert(new Token.EndTag("element2"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("element1"));
 assertEquals(2, stack.size());
 }

 @Test
 public void testPopStackToClose_InvalidClosingTag() throws Exception {
 Element root = new Element("root");
 Element element1 = new Element("element1");
 Element element2 = new Element("element3"); // different tag name
 xmlTreeBuilder.insert(new Token.StartTag("root"));
 xmlTreeBuilder.insert(new Token.StartTag("element1"));
 xmlTreeBuilder.insert(new Token.StartTag("element2"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("element1"));
 assertEquals(3, stack.size());
 }
}