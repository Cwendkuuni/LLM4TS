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
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = Jsoup.parse("<root><tag attr=\"value\"><subtag/><tag2/></tag></root>", "", Parser.xmlParser());
 }

 @Test
 public void testStartTag() {
 Token tagToken = new Token(Token.StartTag.class, "<tag", null, null);
 Element tagElement = xmlTreeBuilder.insert(tagToken);
 assertEquals("tag", tagElement.nodeName());
 }

 @Test
 public void testEndTag() {
 Token endTagToken = new Token(Token.EndTag.class, "</tag", null, null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testComment() {
 Token commentToken = new Token(Token.Comment.class, "<!-- comment -->", null, null);
 xmlTreeBuilder.insert(commentToken);
 assertEquals("comment", xmlTreeBuilder.doc.childNodes().get(0).nodeName());
 }

 @Test
 public void testCharacter() {
 Token characterToken = new Token(Token.Character.class, "text", null, "text");
 xmlTreeBuilder.insert(characterToken);
 Element textElement = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 TextNode textNode = (TextNode) textElement.childNodes().get(0);
 assertEquals("text", textNode.text());
 }

 @Test
 public void testDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE root SYSTEM \"file:file.dtd\" [>\n<!ATTLIST tag attr CDATA #REQUIRED>\n<!ENTITY e \"entity\">", null, null);
 XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlTreeBuilder.insert(doctypeToken);
 assertEquals("root", xmlDeclaration.getName());
 }

 @Test
 public void testUnknownTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<unknown>", null, null);
 xmlTreeBuilder.insert(startTagToken);
 Element unknownElement = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 Validate.isTrue(unknownElement.isUnknown());
 }

 @Test
 public void testSelfClosingTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<tag/>", null, null);
 xmlTreeBuilder.insert(startTagToken);
 Element tagElement = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 Validate.isTrue(tagElement.isSelfClosing());
 }

 @Test(expected = RuntimeException.class)
 public void testUnexpectedToken() {
 Token unexpectedToken = new Token(123, null, null, null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><tag attr=\"value\"><subtag/><tag2/></tag></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList());
 assertEquals(6, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());
 }
}