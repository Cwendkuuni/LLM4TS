import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Doctype;
import org.junit.Assert;

import java.util.Stack;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(StartTag.class, null, null, "tagName", null, null, null, null, null);
 StartTag startTag = startTagToken.asStartTag();
 Element expectedElement = new Element("tagName", "", null);
 xmlTreeBuilder.insert(startTag);
 Element actualElement = xmlTreeBuilder.currentElement();
 Assert.assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(EndTag.class, null, null, "tagName", null, null, null, null, null);
 EndTag endTag = endTagToken.asEndTag();
 xmlTreeBuilder.popStackToClose(endTag);
 Assert.assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Character.class, null, null, null, "commentText", null, null, null, null);
 Comment expectedComment = new Comment("commentText", "");
 xmlTreeBuilder.insert(commentToken);
 Comment actualComment = (Comment) xmlTreeBuilder.lastChild();
 Assert.assertEquals(expectedComment.getData(), actualComment.getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Character.class, null, null, null, null, null, null, null, "characterText");
 TextNode expectedTextNode = new TextNode("characterText", "");
 xmlTreeBuilder.insert(characterToken);
 TextNode actualTextNode = (TextNode) xmlTreeBuilder.lastChild();
 Assert.assertEquals(expectedTextNode.text(), actualTextNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Doctype.class, null, null, "doctypeName", "publicId", "systemId", "");
 DocumentType expectedDocumentType = new DocumentType("doctypeName", "publicId", "systemId", "");
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType actualDocumentType = (DocumentType) xmlTreeBuilder.child(0);
 Assert.assertEquals(expectedDocumentType.getName(), actualDocumentType.getName());
 Assert.assertEquals(expectedDocumentType.getPublicIdentifier(), actualDocumentType.getPublicIdentifier());
 Assert.assertEquals(expectedDocumentType.getSystemIdentifier(), actualDocumentType.getSystemIdentifier());
 }

 @Test (expected = RuntimeException.class)
 public void testProcessUnexpectedTokenType() {
 Token unexpectedToken = new Token(123, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testProcessSelfClosingTag() {
 Token selfClosingStartTagToken = new Token(StartTag.class, null, null, "tagName", null, null, null, null, null, true);
 StartTag selfClosingStartTag = selfClosingStartTagToken.asStartTag();
 xmlTreeBuilder.insert(selfClosingStartTag);
 Element expectedElement = new Element("tagName", "", null);
 expectedElement.setTag(Tag.valueOf("tagName").setSelfClosing(true));
 Element actualElement = xmlTreeBuilder.currentElement();
 Assert.assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 Assert.assertTrue(expectedElement.isSelfClosing());
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 Token unknownEndTagToken = new Token(EndTag.class, null, null, "unknownTag", null, null, null, null, null);
 xmlTreeBuilder.popStackToClose(unknownEndTagToken);
 Element tagOnStack = xmlTreeBuilder.peek();
 Assert.assertFalse(tagOnStack.isSelfClosing());
 }
}