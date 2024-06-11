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

 // Test for Doctype token
 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html");
 doctype.reset();
 }

 // Test for getting name, publicIdentifier, systemIdentifier, and forceQuirks
 @Test
 public void testDoctypeTokenProperties() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html");
 doctype.setName("html5");
 doctype.getPublicIdentifier().equals("XHTML 1.0 Strict");
 doctype.setPublicIdentifier("XHTML 1.0 Transitional");
 doctype.getSystemIdentifier().equals("index.html");
 doctype.setSystemIdentifier("index.html?version=1.0");
 doctype.isForceQuirks().equals(false);
 doctype.setForceQuirks(true);
 doctype.isForceQuirks().equals(true);
 doctype.reset();
 }

 // Test for resetting Doctype token
 @Test
 public void testDoctypeTokenReset() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html");
 doctype.setName("html5");
 doctype.getPublicIdentifier().equals("XHTML 1.0 Strict");
 doctype.setPublicIdentifier("XHTML 1.0 Transitional");
 doctype.getSystemIdentifier().equals("index.html");
 doctype.setSystemIdentifier("index.html?version=1.0");
 doctype.isForceQuirks().equals(false);
 doctype.setForceQuirks(true);
 doctype.isForceQuirks().equals(true);
 doctype.reset();
 doctype.getName().equals("html");
 doctype.reset();
 }

 // Test for StartTag token
 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.name().equals("html");
 startTag.name("body").normalName().equals("body");
 startTag.isSelfClosing().equals(false);
 startTag.reset();
 }

 // Test for getting name, attributes and toString for StartTag token
 @Test
 public void testStartTagTokenProperties() {
 StartTag startTag = new StartTag();
 startTag.name().equals("html");
 startTag.setName("body");
 startTag.name("body").normalName().equals("body");
 Attributes attributes = new Attributes();
 attributes.put(new Attribute("class", "myclass"));
 startTag.getAttributes().equals(attributes);
 startTag.toString().equals("<html><body class=\"myclass\"></body>");
 startTag.reset();
 }

 // Test for resetting StartTag token
 @Test
 public void testStartTagTokenReset() {
 StartTag startTag = new StartTag();
 startTag.name().equals("html");
 startTag.setName("body");
 startTag.name("body").normalName().equals("body");
 Attributes attributes = new Attributes();
 attributes.put(new Attribute("class", "myclass"));
 startTag.getAttributes().equals(attributes);
 startTag.toString().equals("<html><body class=\"myclass\"></body>");
 startTag.reset();
 startTag.name().equals("html");
 startTag.reset();
 }

 // Test for EndTag token
 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name().equals("body");
 endTag.toString().equals("</body>");
 endTag.reset();
 }

 // Test for Comment token
 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.getData().equals("This is a comment");
 comment.bogus = true;
 comment.reset();
 }

 // Test for getting data and toString for Comment token
 @Test
 public void testCommentTokenProperties() {
 Comment comment = new Comment();
 comment.getData().equals("This is a comment");
 comment.setData("Another comment");
 comment.toString().equals("<!--Another comment-->");
 comment.reset();
 }

 // Test for resetting Comment token
 @Test
 public void testCommentTokenReset() {
 Comment comment = new Comment();
 comment.getData().equals("This is a comment");
 comment.setData("Another comment");
 comment.toString().equals("<!--Another comment-->");
 comment.reset();
 comment.getData().equals("This is a comment");
 comment.reset();
 }

 // Test for Character token
 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.getData().equals("Hello World!");
 character.reset();
 }

 // Test for getting data and toString for Character token
 @Test
 public void testCharacterTokenProperties() {
 Character character = new Character();
 character.getData().equals("Hello World!");
 character.setData("Hello, Test!");
 character.toString().equals("Hello, Test!");
 character.reset();
 }

 // Test for resetting Character token
 @Test
 public void testCharacterTokenReset() {
 Character character = new Character();
 character.getData().equals("Hello World!");
 character.setData("Hello, Test!");
 character.toString().equals("Hello, Test!");
 character.reset();
 character.getData().equals("Hello World!");
 character.reset();
 }

 // Test for EOF token
 @Test
 public void testEOFToken() {
 EOF eof = new EOF();
 eof.reset();
 }

 // Test for isDoctype, isStartTag, isEndTag, isComment, isCharacter, and isEOF methods for Token
 @Test
 public void testTokenTypes() {
 Token token = new Doctype();
 Assert.assertEquals(TokenType.Doctype, token.isDoctype());
 token = new StartTag();
 Assert.assertEquals(TokenType.StartTag, token.isStartTag());
 token = new EndTag();
 Assert.assertEquals(TokenType.EndTag, token.isEndTag());
 token = new Comment();
 Assert.assertEquals(TokenType.Comment, token.isComment());
 token = new Character();
 Assert.assertEquals(TokenType.Character, token.isCharacter());
 token = new EOF();
 Assert.assertEquals(TokenType.EOF, token.isEOF());
 token = new Token(); // An empty token
 Assert.assertFalse(token.isDoctype());
 Assert.assertFalse(token.isStartTag());
 Assert.assertFalse(token.isEndTag());
 Assert.assertFalse(token.isComment());
 Assert.assertFalse(token.isCharacter());
 Assert.assertFalse(token.isEOF());
 }
}