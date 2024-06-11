import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document parsedDocument;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseValidXML() {
 String xmlString = "<root><element attr=\"value\"><subElement></subElement></element></root>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");

 Element rootElement = parsedDocument.select("root").first();
 Element element = rootElement.select("element").first();
 Element subElement = element.select("subElement").first();

 assertEquals("root", rootElement.tagName());
 assertEquals("element", element.tagName());
 assertEquals("subElement", subElement.tagName());
 assertEquals("value", element.attr("attr"));
 assertNull(subElement.attr("attr"));
 }

 @Test
 public void testParseEmptyXML() {
 String xmlString = "<root/>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");

 Element rootElement = parsedDocument.select("root").first();
 assertNull(rootElement);
 }

 @Test
 public void testParseInvalidXML() {
 String xmlString = "<root><element><subElement></subElement>"; // missing end tag
 parseErrorTest(xmlString, "Expected: END_TAG {/subElement} but was : EOF(-1)");
 }

 @Test
 public void testParseInvalidXML2() {
 String xmlString = "<root><element><subElement></subElement></subElement>"; // nested self-closing tag
 parseErrorTest(xmlString, "Expected: START_TAG {/subElement} but was : START_TAG {subElement}");
 }

 @Test
 public void testParseXMLWithDocumentType() {
 String xmlString = "<!DOCTYPE root [ <!ENTITY foo SYSTEM \"foo.bar\"> ]><root><element><subElement></subElement></element></root>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");

 DocumentType doctypeNode = parsedDocument.doctype();
 assertEquals("root", doctypeNode.nodeName());
 assertNotNull(doctypeNode.getSystemIdentifier());
 }

 @Test
 public void testParseXMLWithComments() {
 String xmlString = "<root><!-- this is a comment --><element><!-- this is a comment -->subElement</subElement></element></root>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");

 Comment comment = parsedDocument.select("root-->").first();
 assertEquals("this is a comment", comment.text());
 }

 @Test
 public void testParseXMLWithCData() {
 String xmlString = "<root><![CDATA[ <script>function helloWorld() { alert('Hello World'); }</script> ]]><element><![CDATA[ <script>function test() { console.log('Test'); }]]></element></root>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");

 Element scriptElement = parsedDocument.select("root root script").first();
 assertEquals("<script>function helloWorld() { alert('Hello World'); }</script>", scriptElement.outerHtml());
 }

 private void parseErrorTest(String xmlString, String errorMessage) {
 ParseErrorList errors = new ParseErrorList();
 DocParser parser = new DocParser(xmlTreeBuilder, errors);
 try {
 parser.parseFragment(new StringReader(xmlString), "baseUrl", ParseSettings.xmlParser());
 } catch (IOException e) {
 // expected
 }
 Validate.isTrue(errors.size() > 0);
 Element firstError = errors.get(0).element();
 assertEquals(errorMessage, firstError.text());
}
}