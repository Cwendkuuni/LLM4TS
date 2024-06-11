import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Assert;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<test><element name='test' /><comment>test comment</comment></test>");
 }

 @Test
 public void testInitialiseParse() {
 Assert.assertNotNull(xmlTreeBuilder);
 Assert.assertNotNull(doc);
 Assert.assertEquals(doc, stack.peek(xmlTreeBuilder.stack));
 }

 @Test
 public void testProcessStartTag() {
 Element element = new Element("element", "baseUri", new Attributes());
 xmlTreeBuilder.insert(new Token.StartTag("element"));
 Assert.assertEquals(element, stack.peek(xmlTreeBuilder.stack));
 }

 @Test
 public void testProcessEndTag() {
 xmlTreeBuilder.popStackToClose(new Token.EndTag("element"));
 Assert.assertEquals(doc, stack.peek(xmlTreeBuilder.stack));
 }

 @Test
 public void testProcessComment() {
 Comment comment = new Comment("test comment", "baseUri");
 xmlTreeBuilder.insert(new Token.Comment("test comment"));
 Node commentNode = stack.peek(xmlTreeBuilder.stack);
 Assert.assertEquals(comment, commentNode);
 }

 @Test
 public void testProcessCharacter() {
 TextNode textNode = new TextNode("test character", "baseUri");
 xmlTreeBuilder.insert(new Token.Character('t'));
 xmlTreeBuilder.insert(new Token.Character('e'));
 xmlTreeBuilder.insert(new Token.Character('st'));
 xmlTreeBuilder.insert(new Token.Character(' '));
 xmlTreeBuilder.insert(new Token.Character('c'));
 xmlTreeBuilder.insert(new Token.Character('h'));
 xmlTreeBuilder.insert(new Token.Character('a'));
 xmlTreeBuilder.insert(new Token.Character('r'));
 xmlTreeBuilder.insert(new Token.Character('a'));
 xmlTreeBuilder.insert(new Token.Character('ct'));
 xmlTreeBuilder.insert(new Token.Character(' '));
 Node charNode = stack.peek(xmlTreeBuilder.stack);
 Assert.assertEquals(textNode, charNode);
 }

 @Test
 public void testProcessDoctype() {
 Doctype doctype = new Doctype("html", "", "", "baseUri");
 xmlTreeBuilder.insert(new Token.Doctype("html"));
 Node doctypeNode = stack.peek(xmlTreeBuilder.stack);
 Assert.assertEquals(doctype, doctypeNode);
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 xmlTreeBuilder.process(new Token(123));
 }

 @Test
 public void testPopStackToCloseNoneFound() {
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unknown"));
 Assert.assertEquals(doc, stack.peek(xmlTreeBuilder.stack));
 }
}