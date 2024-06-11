import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokenTest {

 private Token doctypeToken;
 private Token startTagToken;
 private Token endTagToken;
 private Token commentToken;
 private Token characterToken;
 private Token eofToken;

 @Before
 public void setUp() {
 doctypeToken = new Token.Doctype();
 startTagToken = new Token.StartTag();
 endTagToken = new Token.EndTag();
 commentToken = new Token.Comment();
 characterToken = new Token.Character();
 eofToken = new Token.EOF();
 }

 @Test
 public void testDoctypeReset() {
 Doctype doctype = (Doctype) doctypeToken.asDoctype();
 doctype.getName().equals("html");
 doctype.reset();
 assertNull(doctype.getName());
 }

 @Test
 public void testStartTagReset() {
 StartTag startTag = (StartTag) startTagToken.asStartTag();
 startTag.name("html").attributes(new Attributes());
 startTag.toString().equals("<html>");
 startTag.reset();
 assertNull(startTag.tagName);
 assertNull(startTag.attributes);
 }

 @Test
 public void testEndTagReset() {
 EndTag endTag = (EndTag) endTagToken.asEndTag();
 endTag.name("html");
 endTag.toString().equals("</html>");
 endTag.reset();
 assertNull(endTag.tagName);
 }

 @Test
 public void testCommentReset() {
 Comment comment = (Comment) commentToken.asComment();
 comment.getData().equals("This is a comment");
 comment.reset();
 assertNull(comment.getData());
 }

 @Test
 public void testCharacterReset() {
 Character character = (Character) characterToken.asCharacter();
 character.data("!");
 character.toString().equals("!");
 character.reset();
 assertNull(character.getData());
 }

 @Test
 public void testEOFReset() {
 EOF eof = (EOF) eofToken.asEOF();
 eof.toString().equals("");
 eof.reset();
 assertSame(eofToken, eof);
 }

 @Test
 public void testDoctypeFinaliseTag() {
 Doctype doctype = (Doctype) doctypeToken.asDoctype();
 doctype.finaliseTag();
 assertFalse(doctype.isForceQuirks());
 }

 @Test(expected = IllegalStateException.class)
 public void testDoctypeFinaliseTagTwice() {
 Doctype doctype = (Doctype) doctypeToken.asDoctype();
 doctype.finaliseTag();
 doctype.finaliseTag();
 }

 @Test
 public void testStartTagNewAttribute() {
 StartTag startTag = (StartTag) startTagToken.asStartTag();
 startTag.newAttribute().put("id", "test");
 assertEquals("test", startTag.attributes().get("id").getValue());
 }

 @Test
 public void testStartTagNewAttributeWithEmptyValue() {
 StartTag startTag = (StartTag) startTagToken.asStartTag();
 startTag.newAttribute().put("class", "");
 assertEquals("class", startTag.attributes().getName());
 assertEquals("", startTag.attributes().getValue("class"));
 }

 @Test
 public void testStartTagNewAttributeWithValue() {
 StartTag startTag = (StartTag) startTagToken.asStartTag();
 startTag.newAttribute().put("style", "color:red;");
 assertEquals("style", startTag.attributes().getName());
 assertEquals("color:red;", startTag.attributes().getValue("style"));
 }

 @Test
 public void testStartTagNewAttributeWithBooleanValue() {
 StartTag startTag = (StartTag) startTagToken.asStartTag();
 startTag.newAttribute().put("disabled", true);
 assertEquals("disabled", startTag.attributes().getName());
 assertEquals("true", startTag.attributes().getValue("disabled"));
 }

 @Test
 public void testEndTagNewAttribute() {
 EndTag endTag = (EndTag) endTagToken.asEndTag();
 endTag.newAttribute();
 assertNull(endTag.attributes());
 }

 @Test(expected = IllegalStateException.class)
 public void testEndTagNewAttributeTwice() {
 EndTag endTag = (EndTag) endTagToken.asEndTag();
 endTag.newAttribute();
 endTag.newAttribute();
 }

 @Test
 public void testCommentAppendAttributeName() {
 Comment comment = (Comment) commentToken.asComment();
 comment.appendAttributeName("data-test");
 assertEquals("data-test", comment.pendingAttributeName);
 comment.reset();
 assertNull(comment.pendingAttributeName);
 }

 @Test
 public void testCommentAppendAttributeValue() {
 Comment comment = (Comment) commentToken.asComment();
 comment.appendAttributeValue("test");
 assertEquals("test", comment.pendingAttributeValue.toString());
 comment.reset();
 assertNull(comment.pendingAttributeValue);
 }

 @Test
 public void testCommentAppendAttributeValueWithEmptyString() {
 Comment comment = (Comment) commentToken.asComment();
 comment.setEmptyAttributeValue();
 assertEquals("", comment.pendingAttributeValue);
 comment.reset();
 assertNull(comment.pendingAttributeValue);
 }

 @Test
 public void testCharacterAppendValue() {
 Character character = (Character) characterToken.asCharacter();
 character.appendAttributeValue("!");
 assertEquals("!", character.getData());
 }

 @Test
 public void testCharacterAppendValueWithEmptyString() {
 Character character = (Character) characterToken.asCharacter();
 character.setEmptyAttributeValue();
 assertEquals("", character.getData());
 character.reset();
 assertNull(character.getData());
 }

 @Test
 public void testCharacterAppendValueWithCodepoint() {
 Character character = (Character) characterToken.asCharacter();
 character.appendAttributeValue('\u00A9');
 assertEquals("\u00A9", character.getData());
 }

 @Test
 public void testCharacterAppendValueWithCodepoints() {
 Character character = (Character) characterToken.asCharacter();
 character.appendAttributeValue(new int[]{'a', 'b', 'c'});
 assertEquals("abc", character.getData());
 }

 @Test
 public void testTokenTypes() {
 Token token = new Token();
 assertFalse(token.isDoctype());
 assertFalse(token.isStartTag());
 assertFalse(token.isEndTag());
 assertFalse(token.isComment());
 assertFalse(token.isCharacter());
 assertFalse(token.isEOF());
 }

 @Test
 public void testDoctypeTokenType() {
 Token token = doctypeToken;
 assertTrue(token.isDoctype());
 assertFalse(token.isStartTag());
 assertFalse(token.isEndTag());
 assertFalse(token.isComment());
 assertFalse(token.isCharacter());
 assertFalse(token.isEOF());
 }

 @Test
 public void testStartTagTokenType() {
 Token token = startTagToken;
 assertFalse(token.isDoctype());
 assertFalse(token.isEndTag());
 assertFalse(token.isComment());
 assertFalse(token.isCharacter());
 assertTrue(token.isStartTag());
 assertFalse(token.isEOF());
 }

 @Test
 public void testEndTagTokenType() {
 Token token = endTagToken;
 assertFalse(token.isDoctype());
 assertFalse(token.isStartTag());
 assertFalse(token.isComment());
 assertFalse(token.isCharacter());
 assertTrue(token.isEndTag());
 assertFalse(token.isEOF());
 }

 @Test
 public void testCommentTokenType() {
 Token token = commentToken;
 assertFalse(token.isDoctype());
 assertFalse(token.isStartTag());
 assertFalse(token.isEndTag());
 assertTrue(token.isComment());
 assertFalse(token.isCharacter());
 assertFalse(token.isEOF());
 }

 @Test
 public void testCharacterTokenType() {
 Token token = characterToken;
 assertFalse(token.isDoctype());
 assertFalse(token.isStartTag());
 assertFalse(token.isEndTag());
 assertFalse(token.isComment());
 assertFalse(token.isEOF());
 assertTrue(token.isCharacter());
 assertFalse(token.isEOF());
 }

 @Test
 public void testEOFTokenType() {
 Token token = eofToken;
 assertFalse(token.isDoctype());
 assertFalse(token.isStartTag());
 assertFalse(token.isEndTag());
 assertFalse(token.isComment());
 assertFalse(token.isCharacter());
 assertTrue(token.isEOF());
 }

 @Test
 public void testTokenToString() {
 Token token = doctypeToken;
 assertEquals("<doctype>", token.toString());
 token = startTagToken;
 assertEquals("<tag>", token.toString());
 token = endTagToken;
 assertEquals("</tag>", token.toString());
 token = commentToken;
 assertEquals("<!-- -->", token.toString());
 token = characterToken;
 assertEquals("!", token.toString());
 token = eofToken;
 assertEquals("", token.toString());
 }

 @Test
 public void testDoctypeToStringWithAttributes() {
 Doctype doctype = (Doctype) new Token.Doctype().name("html").attributes(new Attributes().put("xmlns:html", "http://www.w3.org/1999/xhtml"));
 assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/1999/xhtml\">", doctype.toString());
 }

 @Test
 public void testStartTagToStringWithAttributes() {
 StartTag startTag = (StartTag) new Token.StartTag().name("div").attributes(new Attributes().put("id", "test"));
 assertEquals("<div id=\"test\">", startTag.toString());
 }

 @Test
 public void testEndTagToString() {
 EndTag endTag = new Token.EndTag().name("div");
 assertEquals("</div>", endTag.toString());
 }

 @Test
 public void testCommentToStringWithData() {
 Comment comment = (Comment) new Token.Comment().getData("This is a comment");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

}