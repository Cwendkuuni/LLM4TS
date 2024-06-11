import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private ParseErrorList errorList;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 errorList = ParseErrorList.noTracking();
 }

 @Test
 public void testParseBasicXml() {
 String xmlInput = "<root><element>Hello World</element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Element root = xmlDoc.getRootElement();
 Element element = root.getFirstChild();
 TextNode textNode = (TextNode) element.getFirstChild();

 assertEquals("Hello World", textNode.text());
 assertEquals("root", root.tagName());
 assertEquals("", root.baseUri());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xmlInput = "<root><img src=\"image.jpg\" /> </root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Element root = xmlDoc.getRootElement();
 Element img = root.getFirstChild();
 assertNotNull(img);
 assertEquals("img", img.tagName());
 assertEquals("", img.baseUri());
 }

 @Test
 public void testParseXmlWithComment() {
 String xmlInput = "<root><!-- This is a comment --></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Element root = xmlDoc.getRootElement();
 List<Node> children = root.childNodes();
 assertEquals(1, children.size());
 assertEquals(Node.COMMENT_NODE, children.get(0).nodeType());
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xmlInput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 DocumentType doctype = (DocumentType) xmlDoc.getFirstChild();
 assertEquals("html", doctype.nodeName());
 assertNotNull(doctype.getPublicIdentifier());
 assertNotNull(doctype.getSystemIdentifier());
 }

 @Test(expected = RuntimeException.class)
 public void testParseXmlWithUnexpectedToken() {
 String xmlInput = "<root><html>"; // This should cause an error as 'html' is not a valid start tag in XML
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");
 Validate.fail("Unexpected test passing.");
 }

 @Test
 public void testParseXmlFragment() {
 String xmlInput = "<root><element>Hello World</element></root>";
 List<Node> xmlFragment = xmlTreeBuilder.parseFragment(xmlInput, "", errorList, parseSettings);

 Element root = (Element) xmlFragment.get(0);
 Element element = (Element) xmlFragment.get(1);
 TextNode textNode = (TextNode) element.getFirstChild();

 assertEquals("Hello World", textNode.text());
 assertEquals("root", root.tagName());
 assertEquals("", root.baseUri());
 }

 @Test
 public void testParseXmlFragmentWithError() {
 String xmlInput = "<root><invalid>"; // This should cause a parse error
 List<Node> xmlFragment = xmlTreeBuilder.parseFragment(xmlInput, "", errorList, parseSettings);
 assertNull(xmlFragment);
 }
}