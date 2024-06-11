import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = Jsoup.parse("<test><element attr='value'/><selfClosingTag/></test>", "", Parser.xmlParser());
 }

 @Test
 public void testStartTag() {
 Tokenizer tokenizer = new Tokenizer(document.outerHtml());
 Token token = tokenizer.next();
 Element element = xmlTreeBuilder.insert(token.asStartTag());

 assertEquals("element", element.nodeName());
 assertEquals("value", element.tagInfo().attributes().get("attr"));
 }

 @Test
 public void testEndTag() {
 Tokenizer tokenizer = new Tokenizer(document.outerHtml());
 Token token = tokenizer.next(); // <test>
 token = tokenizer.next(); // <element attr='value'>
 token = tokenizer.next(); // </element>
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testComment() {
 Tokenizer tokenizer = new Tokenizer(document.outerHtml());
 Token token = tokenizer.next(); // <test>
 token = tokenizer.next(); // <!DOCTYPE test [
 token = tokenizer.next(); // <!--
 token = tokenizer.next(); // --->
 token = tokenizer.next(); // <element attr='value'>
 xmlTreeBuilder.insert(token.asComment());
 Element comment = (Element) xmlTreeBuilder.stack.peek();
 assertEquals("!--", comment.nodeName());
 }

 @Test
 public void testCharacter() {
 Tokenizer tokenizer = new Tokenizer(document.outerHtml());
 Token token = tokenizer.next(); // <test>
 token = tokenizer.next(); // <element attr='value'>
 token = tokenizer.next(); // <selfClosingTag/>
 token = tokenizer.next(); // <test>
 Element textNode = xmlTreeBuilder.insert(token.asCharacter());
 assertEquals(" ", textNode.nodeName());
 assertEquals("", textNode.text());
 }

 @Test
 public void testDoctype() {
 Tokenizer tokenizer = new Tokenizer(document.outerHtml());
 Token token = tokenizer.next(); // <test>
 token = tokenizer.next(); // <!DOCTYPE test [
 xmlTreeBuilder.insert(token.asDoctype());
 DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.peek();
 assertEquals("test", documentType.nodeName());
 }

 @Test
 public void testParseFragment() {
 List<Node> fragment = xmlTreeBuilder.parseFragment("<test><element attr='value'/><selfClosingTag/></test>", "", new ParseErrorList());
 assertNotNull(fragment);
 assertNotNull(fragment.get(0));
 assertNotNull(fragment.get(1));
 assertEquals(2, fragment.size());
 }

 @Test
 public void testUnexpectedTokenType() {
 Tokenizer tokenizer = new Tokenizer("<test><element attr='value'><!");
 xmlTreeBuilder.initialiseParse("<test><element attr='value'><!", "", new ParseErrorList());
 xmlTreeBuilder.process(tokenizer.next());
 xmlTreeBuilder.process(tokenizer.next());
 xmlTreeBuilder.process(tokenizer.next()); // <!
 assertNull(xmlTreeBuilder.stack.peek());
 }
}