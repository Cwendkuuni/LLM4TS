import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

class TokenTest {

 @Test
 void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.publicIdentifier.append("Public ID:");
 doctypeToken.systemIdentifier.append("System ID:");
 doctypeToken.forceQuirks = true;

 Token expectedDoctypeToken = new Doctype();
 expectedDoctypeToken.name.append("html");
 expectedDoctypeToken.publicIdentifier.append("Public ID:");
 expectedDoctypeToken.systemIdentifier.append("System ID:");
 expectedDoctypeToken.forceQuirks = true;

 assertEquals(expectedDoctypeToken, doctypeToken);

 Token resetDoctypeToken = doctypeToken.reset();
 assertNotSame(doctypeToken, resetDoctypeToken);
 assertEquals(TokenType.Doctype, resetDoctypeToken.tokenType());
 }

 @Test
 void testResetStringBuilder() {
 StringBuilder sb = new StringBuilder("Initial value");
 Token.reset(sb);
 assertEquals("", sb.toString());
 }

 @Test
 void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html");
 startTagToken.attributes = new Attributes();
 startTagToken.attributes.put("class", "testClass");
 startTagToken.attributes.put("id", "testId");

 Token expectedStartTagToken = new StartTag();
 expectedStartTagToken.name("html");
 expectedStartTagToken.attributes = new Attributes();
 expectedStartTagToken.attributes.put("class", "testClass");
 expectedStartTagToken.attributes.put("id", "testId");

 assertEquals(expectedStartTagToken, startTagToken);

 startTagToken = startTagToken.reset();
 assertNotSame(startTagToken, startTagToken);
 assertEquals(TokenType.StartTag, startTagToken.tokenType());
 }

 @Test
 void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("html");

 Token expectedEndTagToken = new EndTag();
 expectedEndTagToken.name("html");

 assertEquals(expectedEndTagToken, endTagToken);

 endTagToken = endTagToken.reset();
 assertNotSame(endTagToken, endTagToken);
 assertEquals(TokenType.EndTag, endTagToken.tokenType());
 }

 @Test
 void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");
 commentToken.bogus = true;

 Token expectedCommentToken = new Comment();
 expectedCommentToken.data.append("This is a comment");
 expectedCommentToken.bogus = true;

 assertEquals(expectedCommentToken, commentToken);

 commentToken = commentToken.reset();
 assertNotSame(commentToken, commentToken);
 assertEquals(TokenType.Comment, commentToken.tokenType());
 }

 @Test
 void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&amp;");

 Token expectedCharacterToken = new Character();
 expectedCharacterToken.data("&amp;");

 assertEquals(expectedCharacterToken, characterToken);

 characterToken = characterToken.reset();
 assertNotSame(characterToken, characterToken);
 assertEquals(TokenType.Character, characterToken.tokenType());
 }

 @Test
 void testEOFToken() {
 EOF eofToken = new EOF();

 Token expectedEOFToken = new EOF();
 assertEquals(expectedEOFToken, eofToken);

 eofToken = eofToken.reset();
 assertNotSame(eofToken, eofToken);
 assertEquals(TokenType.EOF, eofToken.tokenType());
 }

 @Test
 void testTokenTypes() {
 Token token = new Doctype();
 assertTrue(token.isDoctype());
 token = new StartTag();
 assertFalse(token.isDoctype());
 assertTrue(token.isStartTag());
 token = new EndTag();
 assertTrue(token.isEndTag());
 token = new Comment();
 assertTrue(token.isComment());
 token = new Character();
 assertTrue(token.isCharacter());
 token = new EOF();
 assertTrue(token.isEOF());
 }

 @Test
 void testTokenTypeEnum() {
 assertEquals(TokenType.Doctype, TokenType.valueOf("Doctype"));
 assertEquals(TokenType.Comment, TokenType.valueOf("Comment"));
 assertEquals(TokenType.Character, TokenType.valueOf("Character"));
 assertEquals(TokenType.StartTag, TokenType.valueOf("StartTag"));
 assertEquals(TokenType.EndTag, TokenType.valueOf("EndTag"));
 assertEquals(TokenType.EOF, TokenType.valueOf("EOF"));
 }

 @Test
 void testClassName() {
 Token token = new Doctype();
 assertEquals("Doctype", token.tokenType().name());
 token = new StartTag();
 assertEquals("StartTag", token.tokenType().name());
 token = new EndTag();
 assertEquals("EndTag", token.tokenType().name());
 token = new Comment();
 assertEquals("Comment", token.tokenType().name());
 token = new Character();
 assertEquals("Character", token.tokenType().name());
 token = new EOF();
 assertEquals("EOF", token.tokenType().name());
 }

 @Test
 void testGetAttributes() {
 StartTag startTagToken = new StartTag();
 Attributes attributes = new Attributes();
 attributes.put("class", "testClass");
 attributes.put("id", "testId");
 startTagToken.attributes = attributes;

 assertEquals(attributes, startTagToken.getAttributes());
 }

 @Test
 void testNullAttributes() {
 StartTag startTagToken = new StartTag();
 assertNull(startTagToken.getAttributes());
 }
}