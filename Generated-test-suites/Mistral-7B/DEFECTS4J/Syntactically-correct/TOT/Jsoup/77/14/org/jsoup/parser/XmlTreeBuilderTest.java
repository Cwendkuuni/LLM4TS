import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 @Test
 public void testParseXmlDocument() {
 String xmlInput = "<root><child1><subchild1>Hello</subchild1></child1><child2>World</child2></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeParser(xmlInput, "baseUrl");

 // Check if the document is parsed correctly
 assertNotNull(xmlDoc);
 Element rootElement = xmlDoc.select("root").first();
 assertNotNull(rootElement);

 // Check if child elements are present
 Element child1 = rootElement.select("child1").first();
 assertNotNull(child1);
 Element subchild1 = child1.select("subchild1").first();
 assertNotNull(subchild1);
 assertTrue(subchild1.text().equals("Hello"));

 Element child2 = rootElement.select("child2").first();
 assertNotNull(child2);
 assertTrue(child2.text().equals("World"));
 }

 @Test
 public void testParseEmptyXmlDocument() {
 String xmlInput = "<root/>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlParser(xmlInput, "baseUrl");

 // Check if the document is parsed correctly
 assertNotNull(xmlDoc);
 Element rootElement = xmlDoc.select("root").first();
 assertNotNull(rootElement);
 assertTrue(rootElement.childNodes().size() == 0);
 }

 @Test
 public void testParseXmlDocumentWithInvalidInput() {
 String xmlInput = "<root><child1><subchild1>Hello</subchild1></child1><child2>World</child2></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 // Check if the document is parsed correctly with an invalid input
 try {
 Document xmlDoc = xmlParser("<invalid>", "baseUrl");
 Validate.fail("Expected ParseException to be thrown but it wasn't");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseXmlDocumentWithSelfClosingTags() {
 String xmlInput = "<root><child1><tag selfClosing='true'/><child2></child2></child1></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlParser(xmlInput, "baseUrl");

 // Check if the document is parsed correctly
 Element rootElement = xmlDoc.select("root").first();
 assertNotNull(rootElement);
 Element child1 = rootElement.select("child1").first();
 assertNotNull(child1);

 // Verify that self-closing tags are parsed as expected
 Element tagElement = child1.select("tag").first();
 assertNotNull(tagElement);
 assertTrue(tagElement.tagName().equals("tag"));
 assertTrue(tagElement.tagName().isSelfClosing());
 }

 // Add more test cases to cover edge cases and error scenarios if required

 private Document xmlParser(String xmlInput, String baseUri) {
 return new XmlTreeBuilder().parse(xmlInput, baseUri);
 }
}