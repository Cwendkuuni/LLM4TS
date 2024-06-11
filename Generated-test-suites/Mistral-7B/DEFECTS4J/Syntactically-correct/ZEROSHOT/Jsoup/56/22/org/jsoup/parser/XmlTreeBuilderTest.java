import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParse() {
 String xmlInput = "<xml version='1.0'><root><child></child></root></xml>";
 doc = xmlTreeBuilder.parse(xmlInput, "");
 assertEquals("xml", doc.tagName());
 Element root = doc.child(0);
 assertEquals("root", root.tagName());
 assertEquals(1, root.childNodeSize());
 }

 @Test
 public void testProcessStartTag() {
 String xmlInput = "<xml version='1.0'><root><child/><child></child></root></xml>";
 doc = xmlTreeBuilder.parse(xmlInput, "");
 Element root = doc.child(0);
 Element child1 = root.child(0);
 Element child2 = root.child(1);
 assertEquals("root", root.tagName());
 assertEquals("child", child1.tagName());
 assertEquals("child", child2.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlInput = "<xml version='1.0'><root><child></child></root>";
 doc = xmlTreeBuilder.parse(xmlInput, "");
 Element root = doc.child(0);
 Element child = root.child(0);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("root"));
 assertEquals(0, root.childNodeSize());
 assertEquals(0, child.childNodeSize());
 }

 @Test
 public void testProcessComment() {
 String xmlInput = "<xml version='1.0'><!--comment--></xml>";
 doc = xmlTreeBuilder.parse(xmlInput, "");
 List<Node> commentNodes = doc.childNodes();
 assertEquals(1, commentNodes.size());
 assertEquals(Node.COMMENT_NODE, commentNodes.get(0).nodeType());
 }

 @Test
 public void testProcessCharacter() {
 String xmlInput = "<xml version='1.0'>text</xml>";
 doc = xmlTreebuilder.parse(xmlInput, "");
 List<Node> textNodes = doc.childNodes();
 assertEquals(1, textNodes.size());
 assertEquals(Node.TEXT_NODE, textNodes.get(0).nodeType());
 assertEquals("text", textNodes.get(0).text());
 }

 @Test
 public void testProcessDoctype() {
 String xmlInput = "<!DOCTYPE xml [ <!ATTLIST xml foo CDATA #REQUIRED> ]>";
 doc = xmlTreeBuilder.parse(xmlInput, "");
 DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
 assertEquals("xml", doctypeNode.getName());
 assertEquals("CDATA", doctypeNode.getPublicIdentifier());
 assertEquals(null, doctypeNode.getSystemIdentifier());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 String xmlInput = "<xml version='1.0'><root></root><";
 doc = xmlTreeBuilder.parse(xmlInput, "");
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child><grandchild/></child>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element child = (Element) fragmentNodes.get(0);
 assertEquals("child", child.tagName());
 assertEquals(1, child.childNodeSize());
 }
}