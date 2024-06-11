import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseFragment_SimpleXML() {
 String inputFragment = "<root><element>Hello World</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, new ParseErrorList());

 Document doc = nodes.get(0).asDocument();
 Element root = doc.selectRootElements().get(0);
 Element element = root.selectElements("element").get(0);
 TextNode textNode = (TextNode) element.selectNodes("text()").get(0);

 assertEquals("Hello World", textNode.getText());
 }

 @Test
 public void testParseFragment_SelfClosingTag() {
 String inputFragment = "<tag />";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, new ParseErrorList());

 Element tag = nodes.get(0).asElement();
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testParseFragment_EmptyElement() {
 String inputFragment = "<element />";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, new ParseErrorList());

 Element element = nodes.get(0).asElement();
 assertTrue(element.text().isEmpty());
 }

 @Test
 public void testParseFragment_ElementsWithAttributes() {
 String inputFragment = "<root element='test' attribute='value'><element attribute='value2' /></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, new ParseErrorList());

 Document doc = nodes.get(0).asDocument();
 Element root = doc.selectRootElements().get(0);
 Element element = root.selectElements("element").get(0);

 assertEquals("test", element.attr("element"));
 assertEquals("value2", element.attr("attribute"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragment_InvalidXML() {
 String inputFragment = "<root<element />"; // missing end tag
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, new ParseErrorList());
 fail("Expected IllegalArgumentException but it did not occur.");
 }
}