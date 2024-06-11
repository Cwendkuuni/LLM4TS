import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testStartTag() {
 Token startTag = new Token.StartTag("html", null);
 Element root = xmlTreeBuilder.insert(startTag);
 assertEquals("html", root.nodeName());
 assertNotNull(root.baseUri());
 }

 @Test
 public void testEndTag() {
 Token endTag = new Token.EndTag("html");
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.peekStack());
 }

 @Test
 public void testComment() {
 Token comment = new Token.Comment("-- This is a comment");
 xmlTreeBuilder.insert(comment);
 Element commentNode = xmlTreeBuilder.peekStack();
 assertEquals("-- This is a comment", commentNode.text());
 }

 @Test
 public void testCharacter() {
 Token character = new Token.Character('A');
 xmlTreeBuilder.insert(character);
 Element textNode = xmlTreeBuilder.peekStack();
 assertEquals("A", textNode.text());
 }

 @Test
 public void testDoctype() {
 Token doctype = new Token.Doctype("html", null, null);
 xmlTreeBuilder.insert(doctype);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.peekStack();
 assertEquals("html", doctypeNode.nodeName());
 assertNull(doctypeNode.publicIdentifier());
 assertNull(doctypeNode.systemIdentifier());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 Token unexpected = new Token(-1, "Unexpected token");
 xmlTreeBuilder.process(unexpected);
 }

 @Test
 public void testSelfClosingTag() {
 Token selfClosingTag = new Token.StartTag("img", null, new Attribute("src", "image.jpg"), null, true);
 Element img = xmlTreeBuilder.insert(selfClosingTag);
 assertEquals("img", img.nodeName());
 assertNotNull(img.baseUri());
 }

 @Test
 public void testNestedTags() {
 Token startTag1 = new Token.StartTag("html", null);
 Element root = xmlTreeBuilder.insert(startTag1);

 Token startTag2 = new Token.StartTag("head", null);
 Element head = new Element("head", baseUri, null);
 root.appendChild(head);
 xmlTreeBuilder.insert(startTag2);

 Token startTag3 = new Token.StartTag("title", null);
 Element title = new Element("title", baseUri, null);
 head.appendChild(title);
 xmlTreeBuilder.insert(startTag3);
 xmlTreeBuilder.insert(new Token.Character('Test'));
 xmlTreeBuilder.insert(new Token.EndTag("title"));
 xmlTreeBuilder.insert(new Token.EndTag("head"));
 xmlTreeBuilder.insert(new Token.EndTag("html"));

 assertEquals(4, root.childNodes().size());
 assertEquals("Test", root.childNodes().get(0).text());
 }

 @Test
 public void testUnknownTag() {
 Token startTag = new Token.StartTag("unknown", null);
 xmlTreeBuilder.insert(startTag);
 Element unknown = xmlTreeBuilder.peekStack();
 assertEquals("unknown", unknown.nodeName());
 unknown.setSelfClosing(true);
 }
}