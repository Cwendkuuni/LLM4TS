import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementUtils;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Element rootElement;

 @Before
 public void setUp() {
 xmlTree Builder = new XmlTreeBuilder();
 String input = "<root><child><subchild/></child></root>";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 rootElement = xmlTreeBuilder.getDocument();
 }

 @Test
 public void testStartTag() {
 Token startTag = new Token.StartTag("child", null, null, null);
 xmlTreeBuilder.process(startTag);
 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 }

 @Test
 public void testEndTag() {
 Token endTag = new Token.EndTag("child");
 xmlTreeBuilder.process(endTag);
 assertEquals(0, rootElement.childSize());
 }

 @Test
 public void testComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.process(commentToken);
 assertEquals(1, rootElement.childSize());
 assertInstanceOf(Comment.class, rootElement.child(0));
 }

 @Test
 public void testCharacter() {
 Token characterToken = new Token.Character('&');
 xmlTreeBuilder.process(characterToken);
 Element textNode = rootElement.child(0);
 assertEquals("&", textNode.text());
 }

 @Test
 public void testDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctype = rootElement.child(0);
 assertEquals("html", doctype.getName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(1234);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testSelfClosingTag() {
 Token startTag = new Token.StartTag("img", null, null, null, true);
 xmlTreeBuilder.process(startTag);
 assertEquals(1, rootElement.childSize());
 assertInstanceOf(Element.class, rootElement.child(0));
 }

 @Test
 public void testNestedElements() {
 String input = "<root><child><subchild><grandchild/></grandchild></subchild></child></root>";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 Element root = xmlTreeBuilder.getDocument();
 Element child = root.child(0);
 Element subchild = child.child(0);
 Element grandchild = subchild.child(0);
 assertEquals("grandchild", grandchild.tagName());
 }

 @Test
 public void testEmptyElement() {
 String input = "<img src='image.jpg'/>";
 xmlTreeBuilder.initialiseParse(input, "", new ParseErrorList());
 Element imgElement = xmlTreeBuilder.getDocument().child(0);
 assertTrue(imgElement.hasAttrs());
 assertTrue(imgElement.tagName().equals("img"));
 assertTrue(imgElement.tagName().isSelfClosing());
 }
}