import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

public class TokenTest {

 @Test
 public void testDoctypeTokenReset() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.reset();
 doctype.getName().equals("");
 }

 @Test
 public void testDoctypeTokenGetters() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.setForceQuirks(true);
 doctype.getForceQuirks();
 assertEquals(true, doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeTokenThrowsExceptionWhenForceQuirksIsSetTwice() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.isForceQuirks();
 doctype.isForceQuirks();
 // expected exception
 assertThrows(IllegalStateException.class, () -> doctype.isForceQuirks());
 }

 @Test
 public void testStartTagTokenReset() {
 StartTag startTag = new StartTag();
 startTag.name().equals("tagName");
 startTag.reset();
 startTag.tagName = null;
 }

 @Test
 public void testStartTagTokenSetName() {
 StartTag startTag = new StartTag();
 startTag.name().equals("tagName");
 startTag.name("newTagName");
 startTag.name().equals("newTagName");
 }

 @Test
 public void testStartTagTokenGetAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("tagName");
 startTag.newAttribute();
 Attribute attribute = startTag.getAttributes().get("attributeName");
 assertNull(attribute);
 }

 @Test
 public void testStartTagTokenGetAttributesWithValue() {
 StartTag startTag = new StartTag();
 startTag.name("tagName");
 startTag.newAttribute().set("attributeName", "attributeValue");
 Attribute attribute = startTag.getAttributes().get("attributeName");
 assertEquals("attributeValue", attribute.getValue());
 }

 @Test
 public void testStartTagTokenGetAttributesWithBooleanValue() {
 StartTag startTag = new StartTag();
 startTag.name("tagName");
 startTag.newAttribute().setAttribute("booleanAttribute");
 Attribute attribute = startTag.getAttributes().get("booleanAttribute");
 assertTrue(attribute instanceof BooleanAttribute);
 }

 @Test
 public void testStartTagTokenGetAttributesWithMultipleAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("tagName");
 startTag.newAttribute().set("attribute1", "attributeValue1");
 startTag.newAttribute().set("attribute2", "attributeValue2");
 Attribute[] attributes = startTag.getAttributes().asArray();
 assertEquals(2, attributes.length);
 assertEquals("attributeValue1", attributes[0].getValue());
 assertEquals("attributeValue2", attributes[1].getValue());
 }

 @Test
 public void testEndTagTokenReset() {
 EndTag endTag = new EndTag();
 endTag.name().equals("tagName");
 endTag.reset();
 endTag.tagName = null;
 }

 @Test
 public void testEndTagTokenToString() {
 EndTag endTag = new EndTag();
 endTag.name("tagName");
 assertEquals("</tagName>", endTag.toString());
 }

 @Test
 public void testCommentTokenReset() {
 Comment comment = new Comment();
 comment.data.equals("");
 comment.reset();
 comment.data.equals("");
 }

 @Test
 public void testCommentTokenSetData() {
 Comment comment = new Comment();
 comment.data.equals("");
 comment.data("commentData");
 comment.data.equals("commentData");
 }

 @Test
 public void testCommentTokenToString() {
 Comment comment = new Comment();
 comment.data.equals("commentData");
 assertEquals("<!--commentData-->", comment.toString());
 }

 @Test
 public void testCharacterTokenReset() {
 Character character = new Character();
 character.data.equals("");
 character.reset();
 character.data.equals("");
 }

 @Test
 public void testCharacterTokenSetData() {
 Character character = new Character();
 character.data.equals("");
 character.data("data");
 character.data.equals("data");
 }

 @Test
 public void testCharacterTokenToString() {
 Character character = new Character();
 character.data.equals("data");
 assertEquals("data", character.toString());
 }

 @Test
 public void testTokenizerCreateTokens() {
 String html = "<html>\n<head>\n<title>Title</title>\n<body>\n<p>Content</p>\n</body>\n</html>";
 Tokenizer tokenizer = new Tokenizer(html);
 Token token = null;
 while ((token = tokenizer.next()) != null) {
 switch (token.tokenType()) {
 case TokenType.Doctype:
 doctypeTokens++;
 break;
 case TokenType.StartTag:
 startTagsTokens++;
 break;
 case TokenType.EndTag:
 endTagsTokens++;
 break;
 case TokenType.Comment:
 commentTokens++;
 break;
 case TokenType.Character:
 characterTokens++;
 break;
 case TokenType.EOF:
 eofTokens++;
 break;
 default:
 // unexpected token type
 }
 }
 assertEquals(1, doctypeTokens);
 assertEquals(4, startTagsTokens);
 assertEquals(4, endTagsTokens);
 assertEquals(2, commentTokens);
 assertEquals(11, characterTokens);
 assertEquals(1, eofTokens);
 }
}