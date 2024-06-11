import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;
import java.util.Stack;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;
 private Tokenizer tokeniser;
 private Stack<Element> stack;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 doc = builder.newDocument();
 stack = builder.stack;
 tokeniser = builder.tokeniser;
 }

 @Test
 public void testStartTag() {
 // Test valid start tag
 Token startTagToken = new Token(Token.StartTag.class, "<tag name='test' attr='value' />");
 Element testElement = builder.insert(startTagToken);
 Validate.assertEquals("test", testElement.nodeName());
 Validate.assertEquals("value", testElement.attributes().get("attr"));

 // Test unknown start tag
 Token unknownStartTagToken = new Token(Token.StartTag.class, "<unknownTag />");
 builder.insert(unknownStartTagToken);
 Validate.assertNull(builder.stack.peek());
 }

 @Test
 public void testEndTag() {
 // Test valid end tag
 Token endTagToken = new Token(Token.EndTag.class, "</tag>");
 builder.popStackToClose(endTagToken);
 Validate.assertEquals(0, builder.stack.size());

 // Test unknown end tag
 Token unknownEndTagToken = new Token(Token.EndTag.class, "</unknownTag>");
 builder.popStackToClose(unknownEndTagToken);
 Validate.assertEquals(0, builder.stack.size());
 }

 @Test
 public void testComment() {
 // Test valid comment
 Token commentToken = new Token(Token.Comment.class, "-- comment --");
 builder.insert(commentToken);
 Validate.assertNotNull(builder.stack.peek());
 Validate.assertEquals("comment", builder.stack.peek().nodeName());
 }

 @Test
 public void testCharacter() {
 // Test valid character
 Token characterToken = new Token(Token.Character.class, "test");
 builder.insert(characterToken);
 Element textElement = (Element) builder.stack.peek();
 Validate.assertEquals("test", textElement.text());
 }

 @Test
 public void testDoctype() {
 // Test valid doctype
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE html SYSTEM 'about:legacy-compat'>");
 builder.insert(doctypeToken);
 Validate.assertEquals("html", builder.stack.peek().nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testUnknownToken() {
 // Test unknown token type
 Token unknownToken = new Token(123, "test");
 builder.process(unknownToken);
 }

 @Test(expected = AssertionError.class)
 public void testEmptyInput() {
 // Test empty input
 builder.initialiseParse("", null, null);
 }
}