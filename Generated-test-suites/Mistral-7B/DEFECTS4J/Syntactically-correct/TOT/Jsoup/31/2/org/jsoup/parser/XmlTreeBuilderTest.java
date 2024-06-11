import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.newDocument();
 assertNotNull(doc);
 xmlTreeBuilder.initialiseParse("<xml></xml>", "", new ParseErrorList());
 assertEquals(doc, xmlTreeBuilder.getCurrentRootElement());
 stack.add(doc); // verify stack initialization
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.newDocument();
 Element root = xmlTreeBuilder.initialiseParse("<xml></xml>", "", new ParseErrorList()).createElement("xml");
 xmlTreeBuilder.processStartTag(new Token.StartTag("<root>", null, null, null, null, 1));
 Element rootElement = xmlTreeBuilder.getCurrentRootElement();
 assertEquals(root, rootElement);
 stack.add(rootElement);
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.newDocument();
 Element root = xmlTreeBuilder.initialiseParse("<xml></xml>", "", new ParseErrorList()).createElement("xml");
 Element rootElement = xmlTreeBuilder.insert(new Token.StartTag("<root>", null, null, null, null, 1));
 xmlTreeBuilder.processEndTag(new Token.EndTag("root"));
 assertEquals(root, xmlTreeBuilder.getCurrentRootElement());
 Stack<Element> stack = new Stack<>();
 assertEquals(rootElement, stack.pop());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.newDocument();
 Element root = xmlTreeBuilder.initialiseParse("<xml></xml>", "", new ParseErrorList()).createElement("xml");
 xmlTreeBuilder.processComment(new Token.Comment("<!-- test -->"));
 Comment comment = (Comment) xmlTreeBuilder.getCurrentRootElement().childNodes().get(0);
 assertEquals("test", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.newDocument();
 Element root = xmlTreeBuilder.initialiseParse("<xml></xml>", "", new ParseErrorList()).createElement("xml");
 xmlTreeBuilder.processCharacter(new Token.Character(' '));
 TextNode textNode = (TextNode) xmlTreeBuilder.getCurrentRootElement().childNodes().get(0);
 assertEquals("", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.newDocument();
 Element root = xmlTreeBuilder.initialiseParse("<xml></xml>", "", new ParseErrorList()).createElement("xml");
 xmlTreeBuilder.processDoctype(new Token.Doctype("xml", null, null, null));
 DocumentType doctype = (DocumentType) xmlTreeBuilder.getCurrentRootElement().childNodes().get(0);
 assertEquals("xml", doctype.nodeName());
 }

 @Test
 public void testPopStackToCloseWrongTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.newDocument();
 Element root = xmlTreeBuilder.initialiseParse("<xml></xml>", "", new ParseErrorList()).createElement("xml");
 xmlTreeBuilder.insert(new Token.StartTag("<root>", null, null, null, null, 1));
 xmlTreeBuilder.insert(new Token.EndTag("body")); // wrong tag name
 assertFalse(xmlTreeBuilder.popStackToClose(new Token.EndTag("body")));
 assertEquals(root, xmlTreeBuilder.getCurrentRootElement());
 }

 @Test
 public void testPopStackToCloseCorrectTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.newDocument();
 Element root = xmlTreeBuilder.initialiseParse("<xml></xml>", "", new ParseErrorList()).createElement("xml");
 xmlTreeBuilder.insert(new Token.StartTag("<root>", null, null, null, null, 1));
 xmlTreeBuilder.insert(new Token.EndTag("root"));
 assertTrue(xmlTreeBuilder.popStackToClose(new Token.EndTag("root")));
 assertEquals(doc, xmlTreeBuilder.getCurrentRootElement());
 }
}