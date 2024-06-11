import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.AttributeSelector;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Elements;
import org.jsoup.parser.NodeVisitor;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseWithValidInput() throws IOException {
 String xmlInput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://example.com/\">\n" +
 " <x:element>\n" +
 " <x:attribute name=\"attribute1\">value1</x:attribute>\n" +
 " </x:element>\n" +
 " <xsi:comment xml:space=\"preserve\">This is a comment</xsi:comment>\n" +
 " <x:element>\n" +
 " <x:childElement>\n" +
 " <x:anotherChildElement>\n" +
 " <x:grandChildElement>\n" +
 " <x:textNode>Hello World</x:grandChildElement>\n" +
 " </x:anotherChildElement>\n" +
 " </x:childElement>\n" +
 " </x:element>\n" +
 " <xsi:processingInstruction target=\"xml-data\" data=\"data\"/>\n" +
 " <x:doctype documentType=\"doctype\">\n" +
 " <![CDATA[ <!-- CDATA --> ]]>\n" +
 " </x:doctype>\n" +
 " </test>";

 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");

 assertEquals("test", doc.tagName());
 Element root = doc.child(0);
 assertEquals("element", root.child(0).tagName());
 assertEquals("attribute1", root.child(0).child(0).tagName());
 assertEquals("value1", root.child(0).child(0).child(0).text());
 assertEquals("This is a comment", doc.child(1).text());
 assertEquals("childElement", root.child(1).tagName());
 assertEquals("anotherChildElement", root.child(1).child(0).tagName());
 assertEquals("grandChildElement", root.child(1).child(0).child(0).tagName());
 assertEquals("Hello World", root.child(1).child(0).child(0).child(0).text());
 assertEquals("xml-data", doc.child(3).getAttributes().get("target"));
 assertEquals("data", doc.child(3).getAttributes().get("data"));
 assertEquals("doctype", root.child(4).tagName());
 assertTrue(root.child(4).childNode(0) instanceof CDataNode);
 }

 @Test
 public void testParseWithEmptyInput() throws IOException {
 Document doc = xmlTreeBuilder.parse(new StringReader(""), "");
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testParseWithEmptyXMLTag() throws IOException {
 String xmlInput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://example.com/\">\n" +
 " <x:element/>\n" +
 " </test>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 assertEquals("test", doc.tagName());
 Element root = doc.child(0);
 assertEquals("element", root.tagName());
 assertEquals(0, root.childSize());
 }

 @Test
 public void testParseWithInvalidInput() throws IOException {
 String xmlInput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://example.com/\">\n" +
 " <x:element>\n" +
 " <x:attribute name=\"attribute1\">value1</x:attribute>\n" +
 " <x:attribute name=\"attribute2\">\n" +
 " </test>";
 try {
 xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 fail("Expected ParseException but no exception was thrown");
 } catch (org.jsoup.parser.ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseFragment() throws IOException {
 String xmlFragment = "<x:element>\n" +
 " <x:attribute name=\"attribute1\">value1</x:attribute>\n" +
 " </x:element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ArrayList<ParseErrorList>() {{ add(new ParseErrorList()); }}, XmlTreeBuilder.ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);
 assertEquals("element", root.tagName());
 assertEquals("attribute1", root.child(0).tagName());
 assertEquals("value1", root.child(0).child(0).text());
 }

 @Test
 public void testProcessTokenStartTag() throws IOException {
 Token startTagToken = new Token.StartTag("test", null, null, null, null, null, null);
 Element startTagElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("test", startTagElement.tagName());
 }

 @Test
 public void testProcessTokenEndTag() throws IOException {
 Token endTagToken = new Token.EndTag("test", null, null, null, null, null, null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessTokenCharacter() throws IOException {
 Token characterToken = new Token.Character('A', false, false);
 TextNode characterNode = (TextNode) xmlTreeBuilder.insert(characterToken);
 assertEquals("A", characterNode.text());
 }

 @Test
 public void testProcessTokenComment() throws IOException {
 Token commentToken = new Token.Comment("This is a comment");
 Comment commentNode = (Comment) xmlTreeBuilder.insert(commentToken);
 assertEquals("This is a comment", commentNode.getData());
 }

 @Test
 public void testProcessTokenDoctype() throws IOException {
 Token doctypeToken = new Token.Doctype("doctype", null, null, null, null, null, null);
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals("doctype", documentTypeNode.tagName());
 }

 @Test
 public void testParseWithThatDoesNotContainError() throws IOException {
 String xmlInput = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://example.com/\">\n" +
 " <x:element>\n" +
 " <x:attribute name=\"attribute1\">value1</x:attribute>\n" +
 " <x:element>\n" +
 " <x:childElement>\n" +
 " <x:anotherChildElement>\n" +
 " <x:grandChildElement>\n" +
 " <x:textNode>Hello World</x:grandChildElement>\n" +
 " </x:anotherChildElement>\n" +
 " </x:childElement>\n" +
 " </x:element>\n" +
 " <xsi:processingInstruction target=\"xml-data\" data=\"data\"/>\n" +
 " <x:doctype documentType=\"doctype\">\n" +
 " <![CDATA[ <!-- CDATA --> ]]>\n" +
 " </x:doctype>\n" +
 " </test>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 List<Node> nodes = doc.childNodes();
 Element testElement = (Element) nodes.get(0);
 List<Node> children = testElement.childNodes();
 assertEquals(7, children.size());
 Element element = (Element) children.get(0);
 List<Node> attributeNodes = element.childNodes();
 assertEquals(1, attributeNodes.size());
 Attribute attribute = (Attribute) attributeNodes.get(0);
 assertEquals("attribute1", attribute.getName());
 assertEquals("value1", attribute.getValue());
 Element childElement = (Element) children.get(1);
 List<Node> childNodes = childElement.childNodes();
 assertEquals(2, childNodes.size());
 Element anotherChildElement = (Element) childNodes.get(0);
 List<Node> anotherChildNodes = anotherChildElement.childNodes();
 assertEquals(1, anotherChildNodes.size());
 TextNode textNode = (TextNode) anotherChildNodes.get(0);
 assertEquals("Hello World", textNode.text());
 DocumentType documentType = (DocumentType) children.get(4);
 assertEquals("doctype", documentType.tagName());
 assertEquals("CDATA[ <!-- CDATA --> ]]", documentType.getData());
 }
}