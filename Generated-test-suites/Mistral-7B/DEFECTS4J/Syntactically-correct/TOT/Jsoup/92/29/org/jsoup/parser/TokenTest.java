import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 private Token doctypeToken;
 private Token startTagToken;
 private Token endTagToken;
 private Token commentToken;
 private Token characterToken;
 private Token cdataToken;
 private Token eofToken;

 @Before
 public void setUp() {
 doctypeToken = new Token.Doctype();
 startTagToken = new Token.StartTag();
 endTagToken = new Token.EndTag();
 commentToken = new Token.Comment();
 characterToken = new Token.Character();
 cdataToken = new Token.CData("test");
 eofToken = new Token.EOF();
 }

 @Test
 public void testDoctypeReset() {
 // Test reset method for Doctype token
 Doctype doctype = (Doctype) doctypeToken.asDoctype();
 doctype.getName().equals("html"); // Set initial value
 assertEquals("html", doctype.getName());
 doctype.reset();
 assertNull(doctype.getName());
 }

 @Test
 public void testStartTagReset() {
 // Test reset method for StartTag token
 StartTag startTag = (StartTag) startTagToken.asStartTag();
 startTag.name("html").attributes().put("class", "test"); // Set initial values
 assertEquals("html", startTag.name());
 assertEquals("test", startTag.getAttributes().get("class"));
 startTag.reset();
 assertNull(startTag.name());
 assertNull(startTag.getAttributes());
 }

 @Test
 public void testEndTagReset() {
 // Test reset method for EndTag token
 EndTag endTag = (EndTag) endTagToken.asEndTag();
 endTag.name("html"); // Set initial value
 assertEquals("html", endTag.name());
 startTag.reset();
 assertNull(endTag.name());
 }

 @Test
 public void testCommentReset() {
 // Test reset method for Comment token
 Comment comment = (Comment) commentToken.asComment();
 comment.getData().equals("test comment"); // Set initial value
 assertEquals("test comment", comment.getData());
 comment.reset();
 assertNull(comment.getData());
 }

 @Test
 public void testCharacterReset() {
 // Test reset method for Character token
 Character character = (Character) characterToken.asCharacter();
 character.data("test character"); // Set initial value
 assertEquals("test character", character.getData());
 character.reset();
 assertNull(character.getData());
 }

 @Test
 public void testCDataReset() {
 // Test reset method for CData token
 CData cdata = (CData) cdataToken.asCData();
 assertEquals("test", cdata.getData());
 cdata.reset();
 assertNull(cdata.getData());
 }

 @Test
 public void testEOFReset() {
 // Test reset method for EOF token
 EOF eof = (EOF) eofToken.asEOF();
 assertTrue(eof.isEOF());
 eof.reset();
 assertFalse(eof.isEOF());
 }

 @Test
 public void testTokenTypeIdentification() {
 // Test identification of token types
 Token token = new Token();
 assertTrue(token.isDoctype() ^ token.isStartTag() ^ token.isEndTag() ^ token.isComment() ^ token.isCharacter() ^ token.isCData() ^ token.isEOF());
 token = doctypeToken;
 assertTrue(token.isDoctype());
 token = startTagToken;
 assertTrue(token.isStartTag());
 token = endTagToken;
 assertTrue(token.isEndTag());
 token = commentToken;
 assertTrue(token.isComment());
 token = characterToken;
 assertTrue(token.isCharacter());
 token = cdataToken;
 assertTrue(token.isCData());
 token = eofToken;
 assertTrue(token.isEOF());
 }

 @Test
 public void testTokenTypeConversion() {
 // Test conversion between token types
 Token token = doctypeToken;
 assertSame(TokenType.Doctype, token.tokenType());
 token = startTagToken;
 assertSame(TokenType.StartTag, token.tokenType());
 token = endTagToken;
 assertSame(TokenType.EndTag, token.tokenType());
 token = commentToken;
 assertSame(TokenType.Comment, token.tokenType());
 token = characterToken;
 assertSame(TokenType.Character, token.tokenType());
 token = cdataToken;
 assertSame(TokenType.Character, token.tokenType()); // Note: CData is treated as an extension of Character in the library
 token = eofToken;
 assertSame(TokenType.EOF, token.tokenType());
 }

 @Test
 public void testTokenReset() {
 // Test reset method for various token types
 doctypeToken.reset();
 assertSame(TokenType.Doctype, doctypeToken.tokenType());
 assertNull(doctypeToken.asDoctype().getName());
 startTagToken.reset();
 assertSame(TokenType.StartTag, startTagToken.tokenType());
 assertNull(startTagToken.asStartTag().name());
 assertNull(startTagToken.asStartTag().getAttributes());
 endTagToken.reset();
 assertSame(TokenType.EndTag, endTagToken.tokenType());
 assertNull(endTagToken.asEndTag().name());
 commentToken.reset();
 assertSame(TokenType.Comment, commentToken.tokenType());
 assertNull(commentToken.asComment().getData());
 characterToken.reset();
 assertSame(TokenType.Character, characterToken.tokenType());
 assertNull(characterToken.asCharacter().getData());
 cdataToken.reset();
 assertSame(TokenType.Character, cdataToken.tokenType());
 assertNull(cdataToken.asCData().getData());
 eofToken.reset();
 assertSame(TokenType.EOF, eofToken.tokenType());
 }

 @Test
 public void testTokenFinaliseTag() {
 // Test finaliseTag method for StartTag token
 StartTag startTag = (StartTag) startTagToken.asStartTag();
 startTag.newAttribute().name("test").value("test");
 startTag.finaliseTag();
 assertNull(startTag.getAttributes());
 }
}