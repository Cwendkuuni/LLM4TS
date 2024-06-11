import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder parser;
 private Document doc;
 private Tokenizer tokeniser;

 @Before
 public void setup() {
 parser = new XmlTreeBuilder();
 doc = parser.parse("<xml><tag attr='val'><subtag/></tag></xml>");
 tokeniser = new Tokenizer();
 }

 @Test
 public void testInitialiseParse() {
 assertEquals("xml", doc.tagName());
 assertTrue(doc.hasAttr("xmlns:xml"));
 assertNull(parser.stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 Token tagToken = new Token.StartTag("tag", null, null, null);
 Element tagElement = parser.insert(tagToken);
 assertEquals("tag", tagElement.tagName());
 assertEquals("xml", tagElement.baseUri());
 assertNotNull(tagElement.attributes());
 assertFalse(parser.stack.isEmpty());
 assertEquals(tagElement, parser.stack.peek());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("tag");
 parser.popStackToClose(endTagToken);
 assertFalse(parser.stack.contains(tagElement));
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 parser.insert(commentToken);
 Element comment = parser.stack.peek();
 assertEquals("comment", comment.nodeName());
 assertTrue(comment instanceof Comment);
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 Node characterNode = parser.insert(characterToken);
 assertEquals("A", characterNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token docTypeToken = new Token.Doctype("html", null, null);
 DocumentType docTypeNode = parser.insert(docTypeToken);
 assertEquals("html", docTypeNode.name());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token invalidToken = new Token(12345);
 parser.process(invalidToken);
 }
}