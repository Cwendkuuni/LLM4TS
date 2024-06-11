import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import java.io.IOException;
import java.util.Stack;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = Jsoup.parse("<root><child tag='test'/></root>", "http://test.com");
 xmlTreeBuilder.initialiseParse(doc.html(), "http://test.com", new ParseErrorList());
 }

 @Test
 public void testStartTag() {
 Token startTag = new Token(Token.StartTag.class, "<tag name='test'/>");
 xmlTreeBuilder.process(startTag);

 Element expectedElement = new Element("tag", "http://test.com", null);
 Element actualElement = xmlTreeBuilder.currentElement();

 Validate.isTrue(expectedElement.nodeName().equals(actualElement.nodeName()));
 Validate.isTrue(expectedElement.baseUri().equals(actualElement.baseUri()));
 }

 @Test
 public void testEndTag() {
 Token endTag = new Token(Token.EndTag.class, "</tag>");
 xmlTreeBuilder.process(endTag);

 Element parentElement = xmlTreeBuilder.stack.peek();
 Validate.isTrue(parentElement.childNodes().size() > 0);
 Validate.isTrue(parentElement.childNodes().get(parentElement.childNodes().size() - 1).nodeName().equals("tag"));
 xmlTreeBuilder.stack.remove();
 }

 @Test
 public void testComment() {
 Token commentToken = new Token(Token.Comment.class, "--comment--");
 xmlTreeBuilder.process(commentToken);

 Comment expectedComment = new Comment("--comment--", "http://test.com");
 Comment actualComment = (Comment) xmlTreeBuilder.currentNode();

 Validate.isTrue(expectedComment.getData().equals(actualComment.getData()));
 Validate.isTrue(expectedComment.baseUri().equals(actualComment.baseUri()));
 }

 @Test
 public void testCharacter() {
 Token characterToken = new Token(Token.Character.class, "test");
 xmlTreeBuilder.process(characterToken);

 TextNode expectedTextNode = new TextNode("test", "http://test.com");
 TextNode actualTextNode = (TextNode) xmlTreeBuilder.currentNode();

 Validate.isTrue(expectedTextNode.text().equals(actualTextNode.text()));
 Validate.isTrue(expectedTextNode.baseUri().equals(actualTextNode.baseUri()));
 }

 @Test
 public void testDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE root SYSTEM \"test.dtd\">");
 xmlTreeBuilder.process(doctypeToken);

 DocumentType expectedDocumentType = new DocumentType("root", null, "test.dtd", "http://test.com");
 DocumentType actualDocumentType = (DocumentType) xmlTreeBuilder.currentNode();

 Validate.isTrue(expectedDocumentType.getName().equals(actualDocumentType.getName()));
 Validate.isTrue(expectedDocumentType.getPublicIdentifier() == null && actualDocumentType.getPublicIdentifier() == null);
 Validate.isTrue(expectedDocumentType.getSystemIdentifier().equals(actualDocumentType.getSystemIdentifier()));
 Validate.isTrue(expectedDocumentType.baseUri().equals(actualDocumentType.baseUri()));
 }

 @Test
 public void testUnexpectedToken() {
 Token unexpectedToken = new Token(Token.Unknown.class, "unknown_token");
 xmlTreeBuilder.process(unexpectedToken);

 // This test should fail since the code should throw an exception in the class under test
 }

 @Test
 public void testSelfClosingTag() {
 Token startTag = new Token(Token.StartTag.class, "<tag tag='test'/>");
 xmlTreeBuilder.process(startTag);

 Validate.isTrue(xmlTreeBuilder.stack.peek().nodeName().equals("tag"));
 }

 @Test
 public void testNestedTags() {
 Token startTag1 = new Token(Token.StartTag.class, "<parent><child></child></parent>");
 xmlTreeBuilder.process(startTag1);

 Element parent = xmlTreeBuilder.stack.peek();
 Validate.isTrue(parent.childNodes().size() > 0);
 Validate.isTrue(parent.childNodes().get(0).nodeName().equals("child"));
 }

 @Test
 public void testErrorHandling() {
 Token startTag1 = new Token(Token.StartTag.class, "<parent><child>"); // missing end tag
 xmlTreeBuilder.process(startTag1);

 ParseErrorList parseErrorList = xmlTreeBuilder.getErrors();
 Validate.isTrue(parseErrorList.size() > 0);
 }
}