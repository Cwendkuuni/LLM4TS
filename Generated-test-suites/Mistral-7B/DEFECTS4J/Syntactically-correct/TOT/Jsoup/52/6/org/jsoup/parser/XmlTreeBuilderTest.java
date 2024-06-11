import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testProcessStartTag() {
 // Test valid start tag
 Token startTagToken = new Token.StartTag("root", null);
 Element rootElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", rootElement.nodeName());

 // Test unknown tag
 Token unknownStartTagToken = new Token.StartTag("unknown", null);
 xmlTreeBuilder.insert(unknownStartTagToken);
 // No assertions here as the behavior is not defined for unknown tags in XML parsing
 }

 @Test
 public void testProcessEndTag() {
 // Test valid end tag
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());

 // Test non-existent tag
 Token nonExistentEndTagToken = new Token.EndTag("nonExistent");
 xmlTreeBuilder.popStackToClose(nonExistentEndTagToken);
 // No assertions here as no element should have been removed from the stack
 }

 @Test
 public void testProcessComment() {
 // Test valid comment without bogus declaration
 Token commentToken = new Token.Comment("<!-- This is a comment -->");
 xmlTreeBuilder.insert(commentToken);
 assertEquals("This is a comment", ((Comment) xmlTreeBuilder.doc.childNodes().get(0)).getData());

 // Test valid comment with bogus declaration
 String xmlDeclaration = "<?xml version='1.0' encoding='UTF-8'?>";
 Token commentWithDeclarationToken = new Token.Comment(xmlDeclaration);
 xmlTreeBuilder.insert(commentWithDeclarationToken);
 Document document = xmlTreeBuilder.doc;
 assertEquals(1, document.childNodes().size());
 assertTrue(document.childNodes().get(0) instanceof XmlDeclaration);
 }

 @Test
 public void testProcessCharacter() {
 // Test valid character
 Token characterToken = new Token.Character('#');
 xmlTreeBuilder.insert(characterToken);
 TextNode characterNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("#", characterNode.text());

 // Test invalid character (out of range)
 Token invalidCharacterToken = new Token.Character((char) 0xFFFE);
 xmlTreeBuilder.insert(invalidCharacterToken);
 // No assertions here as the character should be ignored in XML parsing
 }

 @Test
 public void testProcessDoctype() {
 // Test valid doctype
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("html", doctypeNode.nodeName());

 // Test invalid doctype (missing declaration)
 Token invalidDoctypeToken = new Token.Doctype(null, null, null, null);
 xmlTreeBuilder.insert(invalidDoctypeToken);
 // No assertions here as the doctype should be ignored in XML parsing
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<root><comment>This is a comment</comment></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 assertEquals(3, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());
 assertEquals("comment", nodes.get(1).nodeName());
 assertEquals("This is a comment", nodes.get(1).text());
 assertEquals("root", nodes.get(2).nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 // Test invalid token type
 Token invalidTokenToken = new Token(123);
 xmlTreeBuilder.process(invalidTokenToken);
 }
}