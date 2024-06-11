import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlDeclaration;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = null;
 }

 @Test
 public void testProcessStartTag() throws IOException {
 String xmlInput = "<testTag attr1=\"value1\" attr2=\"value2\"/>";
 document = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> fragment = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());
 Element root = (Element) fragment.get(0);

 Token startTagToken = new Token.StartTag("testTag", null, new Attribute[] { new Attribute("attr1", "value1"), new Attribute("attr2", "value2") });
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(xmlInput, null, new ParseErrorList());
 xmlTreeBuilder.tokeniser = new Tokenizer(xmlInput); // reset tokeniser

 assertEquals(root.nodeName(), xmlTreeBuilder.insert(startTagToken).nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlInput = "<testTag attr1=\"value1\">content</testTag>";
 document = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> fragment = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());
 Element root = (Element) fragment.get(0);

 Token startTagToken = new Token.StartTag("testTag", null, new Attribute[] { new Attribute("attr1", "value1") });
 Element startTagElement = xmlTreeBuilder.insert(startTagToken);
 Token endTagToken = new Token.EndTag("testTag");

 insertNode(startTagElement);
 assertEquals(root.childNodes().size(), 1);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(root.childNodes().size(), 0);
 }

 @Test
 public void testProcessComment() {
 String xmlInput = "<!-- this is a comment -->";
 document = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> fragment = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());
 Element root = (Element) fragment.get(0);

 Token commentToken = new Token.Comment("this is a comment");
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(xmlInput, null, new ParseErrorList());
 xmlTreeBuilder.tokeniser = new Tokenizer(xmlInput); // reset tokeniser

 Comment comment = (Comment) xmlTreeBuilder.insert(commentToken);
 Node insert = comment;
 if (commentToken.bogus) {
 String data = comment.getData();
 if (data.length() > 1 && (data.startsWith("!") || data.startsWith("?"))) {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(data, comment.baseUri(), data.startsWith("!"));
 insert = xmlDeclaration;
 }
 }
 assertEquals(root.childNodes().size(), 1);
 insertNode(insert);
 assertEquals(root.childNodes().size(), 1);
 assertEquals(insert.nodeName(), "#comment");
 }

 @Test
 public void testProcessCharacter() {
 String xmlInput = "<testTag>&lt;hello&gt;world</testTag>";
 document = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> fragment = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());
 Element root = (Element) fragment.get(0);

 Token characterToken = new Token.Character('&', "lt;");
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(xmlInput, null, new ParseErrorList());
 xmlTreeBuilder.tokeniser = new Tokenizer(xmlInput); // reset tokeniser

 TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);
 Node insert = textNode;
 assertEquals(root.childNodes().size(), 1);
 insertNode(insert);
 assertEquals(root.childNodes().size(), 1);
 assertEquals(insert.nodeName(), "#text");
 assertEquals(insert.text(), "&lt;hello&gt;world");
 }

 @Test
 public void testProcessDoctype() {
 String xmlInput = "<!DOCTYPE test [ <!ATTLIST test attr CDATA #IMPLIED> ]>";
 document = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> fragment = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());
 Element root = (Element) fragment.get(0);

 Token doctypeToken = new Token.Doctype("test", null, null, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(xmlInput, null, new ParseErrorList());
 xmlTreeBuilder.tokeniser = new Tokenizer(xmlInput); // reset tokeniser

 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctypetoken);
 assertEquals(root.childNodes().size(), 1);
 insertNode(documentType);
 assertEquals(root.childNodes().size(), 1);
 assertEquals(documentType.nodeName(), "#document-type-decl");
 }

 @Test
 public void testProcessUnexpectedTokenType() {
 String xmlInput = "<test>"; // missing tag name
 document = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> fragment = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());
 Element root = (Element) fragment.get(0);

 Token unexpectedToken = new Token(Token.START_TAG, null, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(xmlInput, null, new ParseErrorList());
 xmlTreeBuilder.tokeniser = new Tokenizer(xmlInput); // reset tokeniser

 try {
 xmlTreeBuilder.process(unexpectedToken);
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testEmptyInput() {
 document = Jsoup.parse("", Parser.xmlParser());
 List<Node> fragment = xmlTreeBuilder.parseFragment("", null, new ParseErrorList());
 assertEquals(document.childNodes().size(), 0);
 }
}