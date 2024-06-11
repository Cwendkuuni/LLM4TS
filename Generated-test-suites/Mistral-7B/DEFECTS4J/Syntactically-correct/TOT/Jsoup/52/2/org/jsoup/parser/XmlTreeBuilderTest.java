import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testProcessStartTag() {
 // Test processing a valid start tag
 String xml = "<root><child attr='value'></child></root>";
 Document doc = Jsoup.parse(xml, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, null, null);
 Element root = (Element) nodes.get(0);
 Element child = (Element) root.childNodes().get(0);

 Token startTag = new Token.StartTag("child");
 Element el = xmlTreeBuilder.insert(startTag);
 Element expectedEl = new Element("child", null, null);
 assertEquals(expectedEl, el);
 }

 @Test
 public void testProcessEndTag() {
 // Test processing a valid end tag
 String xml = "<root><child></root>";
 Document doc = Jsoup.parse(xml, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, null, null);
 Element root = (Element) nodes.get(0);
 Element child = (Element) root.childNodes().get(0);

 Token endTag = new Token.EndTag("child");
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(0, root.childNodes().size());
 }

 @Test
 public void testProcessComment() {
 // Test processing a valid comment
 String xml = "<!-- This is a comment -->";
 Document doc = Jsoup.parse(xml, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, null, null);
 Comment comment = (Comment) nodes.get(0);

 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 Comment expectedComment = new Comment("This is a comment", null);
 assertEquals(expectedComment, comment);
 }

 @Test
 public void testProcessCharacter() {
 // Test processing a valid character
 String xml = "<root>Hello World</root>";
 Document doc = Jsoup.parse(xml, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, null, null);
 TextNode textNode = (TextNode) nodes.get(1);

 Token characterToken = new Token.Character('H');
 xmlTreeBuilder.insert(characterToken);
 TextNode expectedTextNode = new TextNode("Hello", null);
 assertEquals(expectedTextNode, textNode);
 }

 @Test
 public void testProcessDoctype() {
 // Test processing a valid doctype
 String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
 Document doc = Jsoup.parse(xml, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, null, null);
 DocumentType doctypeNode = (DocumentType) nodes.get(0);

 Token doctype = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctype);
 DocumentType expectedDoctypeNode = new DocumentType("html", null, null, null);
 assertEquals(expectedDoctypeNode, doctypeNode);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessUnexpectedTokenType() {
 // Test processing an unexpected token type
 String xml = "<root>Hello<world";
 Document doc = Jsoup.parse(xml, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, null, null);
 Token token = doc.childNodes().get(0);
Token unexpectedToken = new Token(3, "world");
 xmlTreeBuilder.process(unexpectedToken);
 // Validate should throw an exception
 }
}