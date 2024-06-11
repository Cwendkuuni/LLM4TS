import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseFragment_ValidXml() {
 String xmlInput = "<root><element1 attr1=\"value1\"> content1 </element1><element2 attr2=\"value2\"/></root>";
 List<Node> expectedNodes = new Element("root").add(
 new Element("element1", null, new Attribute("attr1", "value1"))
 .add(new TextNode(" content1 "))
 .add(new Element("element2", null, new Attribute("attr2", "value2")))
 );
 List<Node> actualNodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());
 assertEquals(expectedNodes, actualNodes);
 }

 @Test
 public void testParseFragment_EmptyInput() {
 List<Node> expectedNodes = new Element("root").empty();
 List<Node> actualNodes = xmlTreeBuilder.parseFragment("", null, new ParseErrorList());
 assertEquals(expectedNodes, actualNodes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragment_NullInput() {
 xmlTreeBuilder.parseFragment(null, null, new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragment_InvalidXml() {
 String invalidXml = "<root><element <error/>";
 try {
 xmlTreeBuilder.parseFragment(invalidXml, null, new ParseErrorList());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseFragment_BogusXmlDeclaration() {
 String xmlWithDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element1 attr1=\"value1\"> content1 </element1><element2 attr2=\"value2\"/></root>";
 List<Node> expectedNodes = new Element("root").add(
 new XmlDeclaration("1.0", null, false)
 .add(new Element("element1", null, new Attribute("attr1", "value1")))
 .add(new TextNode(" content1 "))
 .add(new Element("element2", null, new Attribute("attr2", "value2")))
 );
 List<Node> actualNodes = xmlTreeBuilder.parseFragment(xmlWithDeclaration, null, new ParseErrorList());
 assertEquals(expectedNodes, actualNodes);
 }

 @Test
 public void testParseFragment_SelfClosingTag() {
 String xmlWithSelfClosingTag = "<root><element tag=\"selfClosing\"/></root>";
 List<Node> expectedNodes = new Element("root").add(new Element("element", null, new Attribute("tag", "selfClosing"))) ;
 List<Node> actualNodes = xmlTreeBuilder.parseFragment(xmlWithSelfClosingTag, null, new ParseErrorList());
 assertEquals(expectedNodes, actualNodes);
 }
}