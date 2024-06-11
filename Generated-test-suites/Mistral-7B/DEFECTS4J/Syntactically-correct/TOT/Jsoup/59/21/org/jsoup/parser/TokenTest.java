import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("_pub_");
 doctypeToken.publicIdentifier("ISOM DTD HTML4.01 Transitional//EN");
 doctypeToken.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
 doctypeToken.forceQuirks(false);

 Doctype expectedDoctypeToken = new Doctype();
 expectedDoctypeToken.name("html");
 expectedDoctypeToken.pubSysKey("_pub_");
 expectedDoctypeToken.publicIdentifier("ISOM DTD HTML4.01 Transitional//EN");
 expectedDoctypeToken.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
 expectedDoctypeToken.forceQuirks(false);

 Assert.assertEquals(expectedDoctypeToken, doctypeToken);
 }

 @Test
 public void testDoctypeTokenReset() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("_pub_");
 doctypeToken.publicIdentifier("ISOM DTD HTML4.01 Transitional//EN");
 doctypeToken.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
 doctypeToken.forceQuirks(false);

 Doctype resetDoctypeToken = doctypeToken.reset();

 Assert.assertNotEquals(doctypeToken, resetDoctypeToken);
 Assert.assertTrue(resetDoctypeToken instanceof Doctype);
 Assert.assertNull(resetDoctypeToken.getName());
 Assert.assertNull(resetDoctypeToken.getPubSysKey());
 Assert.assertNull(resetDoctypeToken.getPublicIdentifier());
 Assert.assertNull(resetDoctypeToken.getSystemIdentifier());
 Assert.assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 Tag startTagToken = new StartTag();
 startTagToken.name("html");

 StartTag expectedStartTagToken = new StartTag();
 expectedStartTagToken.name("html");

 Assert.assertEquals(expectedStartTagToken, startTagToken);
 }

 @Test
 public void testStartTagTokenReset() {
 Tag startTagToken = new StartTag();
 startTagToken.name("html");

 Tag resetStartTagToken = startTagToken.reset();

 Assert.assertNotEquals(startTagToken, resetStartTagToken);
 Assert.assertTrue(resetStartTagToken instanceof Tag);
 Assert.assertNull(resetStartTagToken.getName());
 }

 @Test
 public void testTagTokenAttributes() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html");
 startTagToken.attributes().put("id", "root");
 startTagToken.attributes().put("class", "html");

 String id = startTagToken.getAttributes().get("id");
 String className = startTagToken.getAttributes().get("class");

 Assert.assertEquals("root", id);
 Assert.assertEquals("html", className);
 }

 @Test
 public void testTagTokenResetAttributes() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html");
 startTagToken.attributes().put("id", "root");
 startTagToken.attributes().put("class", "html");

 startTagToken.reset();

 Assert.assertNull(startTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 Tag endTagToken = new EndTag();
 endTagToken.name("html");

 EndTag expectedEndTagToken = new EndTag();
 expectedEndTagToken.name("html");

 Assert.assertEquals(expectedEndTagToken, endTagToken);
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data("This is a comment");

 Comment expectedCommentToken = new Comment();
 expectedCommentToken.data("This is a comment");

 Assert.assertEquals(expectedCommentToken, commentToken);
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("!");

 Character expectedCharacterToken = new Character();
 expectedCharacterToken.data("!");

 Assert.assertEquals(expectedCharacterToken, characterToken);
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 EOF expectedEOFToken = new EOF();

 Assert.assertEquals(expectedEOFToken, eofToken);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidToken() {
 Token invalidToken = new Token(); // this class should be abstract and cannot be instantiated
 }

 @Test
 public void testTokenTypes() {
 Token doctypeToken = new Doctype();
 Token startTagToken = new StartTag();
 Token endTagToken = new EndTag();
 Token commentToken = new Comment();
 Token characterToken = new Character();
 Token eofToken = new EOF();

 Assert.assertEquals(TokenType.Doctype, doctypeToken.tokenType());
 Assert.assertEquals(TokenType.StartTag, startTagToken.tokenType());
 Assert.assertEquals(TokenType.EndTag, endTagToken.tokenType());
 Assert.assertEquals(TokenType.Comment, commentToken.tokenType());
 Assert.assertEquals(TokenType.Character, characterToken.tokenType());
 Assert.assertEquals(TokenType.EOF, eofToken.tokenType());
 }

 @Test
 public void testTokenReset() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("_pub_");
 doctypeToken.publicIdentifier("ISOM DTD HTML4.01 Transitional//EN");
 doctypeToken.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
 doctypeToken.forceQuirks(false);
 doctypeToken.reset();

 Assert.assertNull(doctypeToken.getName());
 Assert.assertNull(doctypeToken.getPubSysKey());
 Assert.assertNull(doctypeToken.getPublicIdentifier());
 Assert.assertNull(doctypeToken.getSystemIdentifier());
 Assert.assertFalse(doctypeToken.isForceQuirks());
 }
}