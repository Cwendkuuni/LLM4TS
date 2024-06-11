import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 // Test for Doctype token
 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();

 doctypeToken.name.append("HTML");
 doctypeToken.publicIdentifier.append("Public ID");
 doctypeToken.systemIdentifier.append("System ID");
 doctypeToken.forceQuirks = true;

 Token expectedDoctypeToken = new Doctype();
 expectedDoctypeToken.name.append("HTML");
 expectedDoctypeToken.publicIdentifier.append("Public ID");
 expectedDoctypeToken.systemIdentifier.append("System ID");
 expectedDoctypeToken.forceQuirks = true;

 assertEquals(expectedDoctypeToken.tokenType(), doctypeToken.tokenType());
 assertEquals(expectedDoctypeToken.getClass().getSimpleName(), doctypeToken.tokenType());
 assertEquals("Doctype", doctypeToken.tokenType());

 // Test reset method
 Doctype resetDoctypeToken = doctypeToken.reset();

 assertEquals(TokenType.Doctype, resetDoctypeToken.type);
 assertEquals("", resetDoctypeToken.name.toString());
 assertEquals("", resetDoctypeToken.publicIdentifier.toString());
 assertEquals("", resetDoctypeToken.systemIdentifier.toString());
 assertFalse(resetDoctypeToken.forceQuirks);

 // Test getters
 assertEquals("HTML", doctypeToken.getName());
 assertEquals("Public ID", doctypeToken.getPublicIdentifier());
 assertEquals("System ID", doctypeToken.getSystemIdentifier());
 assertTrue(doctypeToken.isForceQuirks());
 }

 // Test for StartTag token
 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();

 startTagToken.nameAttr("html", null);

 Token expectedStartTagToken = new StartTag();
 expectedStartTagToken.tagName = "html";
 expectedStartTagToken.normalName = "html";

 assertEquals(expectedStartTagToken.tokenType(), startTagToken.tokenType());
 assertEquals(expectedStartTagToken.getClass().getSimpleName(), startTagToken.tokenType());
 assertEquals("StartTag", startTagToken.tokenType());

 // Test reset method
 StartTag resetStartTagToken = startTagToken.reset();

 assertEquals(TokenType.StartTag, resetStartTagToken.type);
 assertEquals(null, resetStartTagToken.tagName);
 assertEquals(null, resetStartTagToken.normalName);

 // Test name method
 startTagToken.name("HEAD");

 assertEquals("HEAD", startTagToken.name());
 assertEquals("head", startTagToken.normalName());
 }

 // Test for EndTag token
 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();

 endTagToken.name("html");

 Token expectedEndTagToken = new EndTag();
 expectedEndTagToken.tagName = "html";
 expectedEndTagToken.normalName = "html";

 assertEquals(expectedEndTagToken.tokenType(), endTagToken.tokenType());
 assertEquals(expectedEndTagToken.getClass().getSimpleName(), endTagToken.tokenType());
 assertEquals("EndTag", endTagToken.tokenType());

 // Test reset method
 EndTag resetEndTagToken = endTagToken.reset();

 assertEquals(TokenType.EndTag, resetEndTagToken.type);
 assertEquals(null, resetEndTagToken.tagName);
 assertEquals(null, resetEndTagToken.normalName);
 }

 // Test for Comment token
 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();

 commentToken.data.append("This is a comment");

 Token expectedCommentToken = new Comment();
 expectedCommentToken.data.append("This is a comment");

 assertEquals(expectedCommentToken.tokenType(), commentToken.tokenType());
 assertEquals(expectedCommentToken.getClass().getSimpleName(), commentToken.tokenType());
 assertEquals("Comment", commentToken.tokenType());

 // Test reset method
 Comment resetCommentToken = commentToken.reset();

 assertEquals(TokenType.Comment, resetCommentToken.type);
 assertEquals("", resetCommentToken.data.toString());
 }

 // Test for Character token
 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();

 characterToken.data("&amp;");

 Token expectedCharacterToken = new Character();
 expectedCharacterToken.data = "&amp;";

 assertEquals(expectedCharacterToken.tokenType(), characterToken.tokenType());
 assertEquals(expectedCharacterToken.getClass().getSimpleName(), characterToken.tokenType());
 assertEquals("Character", characterToken.tokenType());

 // Test reset method
 Character resetCharacterToken = characterToken.reset();

 assertEquals(TokenType.Character, resetCharacterToken.type);
 assertEquals(null, resetCharacterToken.data);
 }

 // Test for EOF token
 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 assertEquals(TokenType.EOF, eofToken.tokenType());
 assertEquals(EOF.class.getSimpleName(), eofToken.tokenType());
 assertEquals("EOF", eofToken.tokenType());
 }

 // Test reset method for all tokens
 @Test
 public void testResetAllTokens() {
 Doctype doctypeToken = new Doctype();
 StartTag startTagToken = new StartTag();
 EndTag endTagToken = new EndTag();
 Comment commentToken = new Comment();
 Character characterToken = new Character();
 EOF eofToken = new EOF();

 doctypeToken.reset();
 startTagToken.reset();
 endTagToken.reset();
 commentToken.reset();
 characterToken.reset();
 eofToken.reset();

 assertEquals(TokenType.Doctype, doctypeToken.type);
 assertEquals(TokenType.StartTag, startTagToken.type);
 assertEquals(TokenType.EndTag, endTagToken.type);
 assertEquals(TokenType.Comment, commentToken.type);
 assertEquals(TokenType.Character, characterToken.type);
 assertEquals(TokenType.EOF, eofToken.type);
 }
}