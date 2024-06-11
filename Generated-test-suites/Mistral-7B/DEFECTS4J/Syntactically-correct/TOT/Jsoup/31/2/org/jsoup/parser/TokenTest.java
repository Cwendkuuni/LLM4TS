import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("W3C");
 doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
 doctype.forceQuirks = true;
 Token expectedDoctype = new Doctype();
 expectedDoctype.name.append("html");
 expectedDoctype.publicIdentifier.append("W3C");
 expectedDoctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
 expectedDoctype.forceQuirks = true;

 Assert.assertEquals(expectedDoctype.tokenType(), doctype.tokenType());
 Assert.assertEquals(expectedDoctype.getClass().getSimpleName(), doctype.tokenType());
 Assert.assertEquals(expectedDoctype.getClass(), doctype.getClass());
 Assert.assertEquals(expectedDoctype.isDoctype(), doctype.isDoctype());
 Assert.assertEquals(expectedDoctype.getName(), doctype.getName());
 Assert.assertEquals(expectedDoctype.getPublicIdentifier(), doctype.getPublicIdentifier());
 Assert.assertEquals(expectedDoctype.getSystemIdentifier(), doctype.getSystemIdentifier());
 Assert.assertEquals(expectedDoctype.isForceQuirks(), doctype.isForceQuirks());
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag("html");
 startTag.newAttribute();
 startTag.appendAttributeName("id");
 startTag.appendAttributeValue("root");
 startTag.newAttribute();
 startTag.finaliseTag();

 Token expectedStartTag = new StartTag("html");
 expectedStartTag.name("html");
 expectedStartTag.getAttributes().put(new Attribute("id", "root"));

 Assert.assertEquals(expectedStartTag.tokenType(), startTag.tokenType());
 Assert.assertEquals(expectedStartTag.getClass().getSimpleName(), startTag.tokenType());
 Assert.assertEquals(expectedStartTag.getClass(), startTag.getClass());
 Assert.assertEquals(expectedStartTag.isStartTag(), startTag.isStartTag());
 Assert.assertEquals(expectedStartTag.getName(), startTag.getName());
 Assert.assertNotNull(expectedStartTag.getAttributes());
 Assert.assertEquals(1, expectedStartTag.getAttributes().size());
 Assert.assertEquals("root", expectedStartTag.getAttributes().get("id").getValue());
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag("body");
 endTag.newAttribute();
 endTag.appendAttributeName("class");
 endTag.appendAttributeValue("content");
 endTag.newAttribute();
 endTag.finaliseTag();

 Token expectedEndTag = new EndTag("body");
 expectedEndTag.name("body");
 expectedEndTag.getAttributes().put(new Attribute("class", "content"));

 Assert.assertEquals(expectedEndTag.tokenType(), endTag.tokenType());
 Assert.assertEquals(expectedEndTag.getClass().getSimpleName(), endTag.tokenType());
 Assert.assertEquals(expectedEndTag.getClass(), endTag.getClass());
 Assert.assertEquals(expectedEndTag.isEndTag(), endTag.isEndTag());
 Assert.assertEquals(expectedEndTag.getName(), endTag.getName());
 Assert.assertNotNull(expectedEndTag.getAttributes());
 Assert.assertEquals(1, expectedEndTag.getAttributes().size());
 Assert.assertEquals("content", expectedEndTag.getAttributes().get("class").getValue());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.data.append("This is a comment.");

 Token expectedComment = new Comment();
 expectedComment.data.append("This is a comment.");

 Assert.assertEquals(expectedComment.tokenType(), comment.tokenType());
 Assert.assertEquals(expectedComment.getClass().getSimpleName(), comment.tokenType());
 Assert.assertEquals(expectedComment.getClass(), comment.getClass());
 Assert.assertEquals(expectedComment.isComment(), comment.isComment());
 Assert.assertEquals(expectedComment.getData(), comment.getData());
 }

 @Test
 public void testCharacter() {
 Character character = new Character("&amp;");

 Token expectedCharacter = new Character("&amp;");

 Assert.assertEquals(expectedCharacter.tokenType(), character.tokenType());
 Assert.assertEquals(expectedCharacter.getClass().getSimpleName(), character.tokenType());
 Assert.assertEquals(expectedCharacter.getClass(), character.getClass());
 Assert.assertEquals(expectedCharacter.isCharacter(), character.isCharacter());
 Assert.assertEquals(expectedCharacter.getData(), character.getData());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 Token invalidToken = new Token();

 // This should throw an AssertionError as the type is not defined
 Assert.assertEquals(TokenType.Comment, invalidToken.tokenType());
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();

 Token expectedEOF = new EOF();

 Assert.assertEquals(expectedEOF.tokenType(), eof.tokenType());
 Assert.assertEquals(expectedEOF.getClass().getSimpleName(), eof.tokenType());
 Assert.assertEquals(expectedEOF.getClass(), eof.getClass());
 Assert.assertEquals(expectedEOF.isEOF(), eof.isEOF());
 }
}