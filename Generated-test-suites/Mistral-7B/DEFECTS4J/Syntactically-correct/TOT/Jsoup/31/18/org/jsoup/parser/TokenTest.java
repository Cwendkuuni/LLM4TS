import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

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
 startTagToken = new Token.StartTag("html");
 endTagToken = new Token.EndTag("html");
 commentToken = new Token.Comment("This is a comment");
 characterToken = new Token.Character("a");
 eofToken = new Token.EOF();
 }

 @Test
 public void testDoctypeToken() {
 assertNotNull(doctypeToken);
 assertEquals(TokenType.Doctype, doctypeToken.type);
 }

 @Test
 public void testStartTagToken() {
 assertNotNull(startTagToken);
 assertEquals(TokenType.StartTag, startTagToken.type);
 assertEquals("html", startTagToken.name());
 assertTrue(startTagToken.isStartTag());
 assertNull(startTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 assertNotNull(endTagToken);
 assertEquals(TokenType.EndTag, endTagToken.type);
 assertEquals("html", endTagToken.name());
 assertTrue(endTagToken.isEndTag());
 assertNull(endTagToken.getAttributes());
 }

 @Test
 public void testCommentToken() {
 assertNotNull(commentToken);
 assertEquals(TokenType.Comment, commentToken.type);
 assertEquals("This is a comment", commentToken.getData());
 assertTrue(commentToken.isComment());
 }

 @Test
 public void testCharacterToken() {
 assertNotNull(characterToken);
 assertEquals(TokenType.Character, characterToken.type);
 assertEquals("a", characterToken.getData());
 assertTrue(characterToken.isCharacter());
 }

 @Test
 public void testEOFToken() {
 assertNotNull(eofToken);
 assertEquals(TokenType.EOF, eofToken.type);
 assertTrue(eofToken.isEOF());
 }

 @Test
 public void testDoctypeWithAttributes() {
 doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().name.append("html");
 doctypeToken.asDoctype().name.append(" ");
 doctypeToken.asDoctype().name.append("version");
 doctypeToken.asDoctype().name.append("=");
 doctypeToken.asDoctype().name.append("\"5.0\"");
 doctypeToken.asDoctype().publicIdentifier.append("public ");
 doctypeToken.asDoctype().publicIdentifier.append("id");
 doctypeToken.asDoctype().publicIdentifier.append("=");
 doctypeToken.asDoctype().publicIdentifier.append("\"-//W3C//DTD XHTML 1.0 Strict//EN\"");
 doctypeToken.asDoctype().systemIdentifier.append("system ");
 doctypeToken.asDoctype().systemIdentifier.append("id");
 doctypeToken.asDoctype().systemIdentifier.append("=");
 doctypeToken.asDoctype().systemIdentifier.append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
 doctypeToken.asDoctype().forceQuirks = true;
assertEquals(TokenType.Doctype, doctypeToken.type);
 assertFalse(doctypeToken.isCharacter());
 assertTrue(doctypeToken.isDoctype());
 assertTrue(doctypeToken.asDoctype().name.toString().equals("html"));
 assertTrue(doctypeToken.asDoctype().publicIdentifier.toString().equals("public id=\"-//W3C//DTD XHTML 1.0 Strict//EN\""));
 assertTrue(doctypeToken.asDoctype().systemIdentifier.toString().equals("system id=\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\""));
 assertTrue(doctypeToken.asDoctype().forceQuirks);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidToken() {
 Token invalidToken = new Token();
 assertNotNull(invalidToken);
 assertEquals(TokenType.Doctype, invalidToken.type); // This should fail because the token is not an instance of Doctype
 }
}