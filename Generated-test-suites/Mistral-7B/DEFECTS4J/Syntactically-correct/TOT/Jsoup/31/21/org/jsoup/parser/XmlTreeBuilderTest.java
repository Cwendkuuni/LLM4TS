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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;
 private Tokenizer tokenizer;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse("<test><tag attr='value' /></test>");
 tokenizer = new Tokenizer(document.outerHtml());
 }

 @Test
 public void testStartTag() {
 Token token = tokenizer.nextToken();
 Element el = xmlTreeBuilder.insert(token.asStartTag());
 assertEquals("tag", el.nodeName());
 assertEquals("value", el.attributes().get("attr"));
 }

 @Test
 public void testEndTag() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 assertNotNull(xmlTreeBuilder.stack.peek()); // Verify that the top element on the stack was not removed
 }

 @Test
 public void testComment() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.insert(token.asComment());
 String commentData = token.getData();
 assertEquals(commentData, xmlTreeBuilder.getLastNode().nodeName());
 }

 @Test
 public void testCharacter() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.insert(token.asCharacter());
 String characterData = token.getData();
 assertEquals(characterData, xmlTreeBuilder.getLastNode().nodeValue());
 }

 @Test
 public void testDoctype() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.insert(token.asDoctype());
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.getLastNode();
 assertEquals("test", doctypeNode.getPublicIdentifier());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 tokenizer = new Tokenizer("invalid input"); // Invalid input to cause unexpected token type
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testSelfClosingTag() {
 Token token = tokenizer.nextToken();
 assertEquals("selfClosingTag", token.name());
 token = tokenizer.nextToken(); // assume this is end tag
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 token = tokenizer.nextToken(); // assume this is start tag
 xmlTreeBuilder.insert(token.asStartTag());
 assertEquals("selfClosingTag", xmlTreeBuilder.getLastNode().nodeName());
 }

 @Test
 public void testNestedTags() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse("<outerTag><innerTag attr='value'><anotherTag /></innerTag></outerTag>");
 Element outerTag = document.select("outerTag").first();
 Element innerTag = outerTag.select("innerTag").first();
 Element anotherTag = innerTag.select("anotherTag").first();
 assertEquals(3, xmlTreeBuilder.stack.size());
 assertEquals("outerTag", xmlTreeBuilder.stack.peek().nodeName());
 }
}