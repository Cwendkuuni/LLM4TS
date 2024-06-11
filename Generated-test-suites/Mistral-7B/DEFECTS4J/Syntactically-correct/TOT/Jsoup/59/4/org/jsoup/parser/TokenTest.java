import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.pubSysKey = "DTD XHTML 1.0";
 doctype.publicIdentifier.append("Public");
 doctype.systemIdentifier.append("system/xhtml1_strict.dtd");
 doctype.forceQuirks = true;

 Token expected = new Doctype();
 expected.name.append("html");
 expected.pubSysKey = "DTD XHTML 1.0";
 expected.publicIdentifier.append("Public");
 expected.systemIdentifier.append("system/xhtml1_strict.dtd");
 expected.forceQuirks = true;

 Assert.assertEquals(expected.tokenType(), doctype.tokenType());
 Assert.assertEquals(expected.getClass(), doctype.getClass());
 Assert.assertEquals(expected.getName(), doctype.getName());
 Assert.assertEquals(expected.getPubSysKey(), doctype.getPubSysKey());
 Assert.assertEquals(expected.getPublicIdentifier(), doctype.getPublicIdentifier());
 Assert.assertEquals(expected.getSystemIdentifier(), doctype.getSystemIdentifier());
 Assert.assertEquals(expected.isForceQuirks(), doctype.isForceQuirks());

 doctype.reset();

 Assert.assertNull(doctype.getName());
 Assert.assertNull(doctype.getPubSysKey());
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.nameAttr("html", new Attributes().put("class", "page"));

 Token expected = new StartTag();
 expected.nameAttr("html", new Attributes().put("class", "page"));

 Assert.assertEquals(expected.tokenType(), startTag.tokenType());
 Assert.assertEquals(expected.getClass(), startTag.getClass());
 Assert.assertEquals(expected.getName(), startTag.getName());
 Assert.assertEquals(expected.getNormalName(), startTag.getNormalName());
 Assert.assertFalse(startTag.isSelfClosing());
 Assert.assertEquals(expected.getAttributes(), startTag.getAttributes());

 startTag.reset();

 Assert.assertNull(startTag.getName());
 Assert.assertNull(startTag.getNormalName());
 Assert.assertNull(startTag.getAttributes());
 }

 // Add tests for other token types and methods here

 @Test
 public void testResetAllTokens() {
 Doctype doctype = new Doctype();
 StartTag startTag = new StartTag();
 Comment comment = new Comment();
 Character character = new Character();
 EOF eof = new EOF();

 doctype.name.append("html");
 startTag.nameAttr("html", new Attributes().put("class", "page"));
 comment.data.append("This is a comment");
 character.data("&amp;");

 Assert.assertEquals(TokenType.Doctype, doctype.tokenType());
 Assert.assertEquals(TokenType.StartTag, startTag.tokenType());
 Assert.assertEquals(TokenType.Comment, comment.tokenType());
 Assert.assertEquals(TokenType.Character, character.tokenType());
 Assert.assertEquals(TokenType.EOF, eof.tokenType());

 doctype.reset();
 startTag.newAttribute();
 comment.newAttribute();
 character.newAttribute();
 eof.reset();

 Assert.assertEquals(TokenType.Doctype, doctype.tokenType());
 Assert.assertEquals(TokenType.StartTag, startTag.tokenType());
 Assert.assertEquals(TokenType.Comment, comment.tokenType());
 Assert.assertEquals(TokenType.Character, character.tokenType());
 Assert.assertEquals(TokenType.EOF, eof.tokenType());
 }

 @Test
 public void testInvalidToken() {
 // Create an invalid token and test if it throws an error
 try {
 InvalidToken invalidToken = new InvalidToken(); // InvalidToken is not a real token type in the given code
 } catch (Exception e) {
 Assert.assertEquals("InvalidToken is not a valid token type", e.getMessage());
 }
 }
}