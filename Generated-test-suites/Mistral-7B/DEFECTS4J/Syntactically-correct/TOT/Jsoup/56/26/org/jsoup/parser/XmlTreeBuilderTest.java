import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private ParseErrorList parseErrorList;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 parseErrorList = ParseErrorList.noTracking();
 }

 @Test
 public void testParseWithValidXml() {
 String xmlInput = "<root><child element=\"value\"/><anotherChild/></root>";
 document = xmlTreeBuilder.parse(xmlInput, "");

 Element root = document.rootElement();
 assertEquals("root", root.tagName());
 assertEquals(2, root.childNodeSize());

 Element child = root.getChild(0);
 assertEquals("child", child.tagName());
 assertEquals("value", child.attribute("element"));
 assertNull(child.childNodeSize());

 Element anotherChild = root.getChild(1);
 assertEquals("anotherChild", anotherChild.tagName());
 assertNull(anotherChild.attributeSize());
 }

 @Test
 public void testParseWithSelfClosingTag() {
 String xmlInput = "<root><img src=\"image.jpg\" /> </root>";
 document = xmlTreeBuilder.parse(xmlInput, "");

 Element root = document.rootElement();
 assertEquals("root", root.tagName());
 assertEquals(1, root.childNodeSize());

 Element img = root.getChild(0);
 assertEquals("img", img.tagName());
 assertTrue(img.isSelfClosing());
 assertNull(img.attributeSize());
 }

 @Test
 public void testParseWithComment() {
 String xmlInput = "<root><!-- this is a comment --></root>";
 document = xmlTreeBuilder.parse(xmlInput, "");

 Element root = document.rootElement();
 assertEquals("root", root.tagName());
 assertEquals(0, root.childNodeSize());

 Element comment = (Element) root.childNodes().get(0);
 assertEquals("--this is a comment --", comment.text());
 }

 @Test
 public void testParseWithDoctype() {
 String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root><child/></root>";
 document = xmlTreeBuilder.parse(xmlInput, "");

 DocumentType doctypeNode = document.doctype();
 assertEquals("root", doctypeNode.name());
 assertEquals("root.dtd", doctypeNode.systemIdentifier());
 assertEquals("", doctypeNode.publicIdentifier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidInput() {
 String xmlInput = "<root>"; // missing closing tag
 document = xmlTreeBuilder.parse(xmlInput, "");
 assertNull(document);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 String xmlInput = "";
 document = xmlTreeBuilder.parse(xmlInput, "");
 assertNull(document);
 }

 @Test
 public void testParseFragment() {
 String xmlInput = "<root><child/><anotherChild></anotherChild></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, "", parseErrorList, parseSettings);

 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 assertEquals(2, root.childNodeSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithEmptyInput() {
 String xmlInput = "";
 xmlTreeBuilder.parseFragment(xmlInput, "", parseErrorList, parseSettings);
 assertNull(document);
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.parse("<root><child><grandChild/></grandChild></child></root>", "", parseErrorList, parseSettings);
 document.childNodes().get(1).childNodes().get(0).childNodes().get(0).remove();
 xmlTreeBuilder.popStackToClose((Element) document.childNodes().get(1).childNodes().get(0));
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testPopStackToCloseWithNonExistingTag() {
 xmlTreeBuilder.parse("<root><child></child></root>", "", parseErrorList, parseSettings);
 xmlTreeBuilder.popStackToClose((Element) document.childNodes().get(0).childNodes().get(0));
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testProcessWithUnknownTag() {
 String xmlInput = "<root><unknownTag/></root>";
 document = xmlTreeBuilder.parse(xmlInput, "");
 Element root = document.rootElement();
 Element unknownTag = root.getChild(0);
 assertTrue(unknownTag.isUnknownTag());
 }
}