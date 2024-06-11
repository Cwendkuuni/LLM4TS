import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.Parser;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");
 Element root = document.rootElement();

 // Test root element
 Validate.equals("root", root.tagName());
 List<Element> children = root.children();
 Validate.assertEquals(2, children.size());

 // Test child elements
 Element child1 = children.get(0);
 Validate.equals("child1", child1.tagName());
 Text textNode = child1.child(0);
 Validate.assertEquals("Hello", textNode.text());

 Element child2 = children.get(1);
 Validate.equals("child2", child2.tagName());
 List<Element> grandChildren = child2.children();
 Validate.assertEquals(1, grandChildren.size());

 Element grandChild = grandChildren.get(0);
 Validate.equals("grandChild", grandChild.tagName());
 Text grandChildTextNode = grandChild.child(0);
 Validate.assertEquals("World", grandChildTextNode.text());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child><grandChild>World</grandChild></child>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element child = (Element) nodes.get(0);
 Element grandChild = (Element) child.child(0);
 Validate.assertEquals("child", child.tagName());
 Validate.assertEquals("grandChild", grandChild.tagName());
 Validate.assertEquals("World", grandChild.child(0).text());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 String invalidXmlString = "<html><body><div<p>"; // missing '>' in tag
 xmlTreeBuilder.parse(new StringReader(invalidXmlString), "baseUrl");
 }
}