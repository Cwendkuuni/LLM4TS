import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;
import org.jsoup.ParseError;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;
 private ParseErrorList parseErrors;

 @Before
 public void setup() {
 characterReader = new CharacterReader();
 parseErrors = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrors);
 }

 @Test
 public void testReadEmpty() {
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadWhitespace() {
 tokeniser.emit(" abc\tdef\r\n\rfgh");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals(" ", tokeniser.read().getData());
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("t", tokeniser.read().getData());
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("\n", tokeniser.read().getData());
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("\r", tokeniser.read().getData());
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("\f", tokeniser.read().getData());
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("g", tokeniser.read().getData());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadTag() {
 tokeniser.emit("<div>");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("div", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 assertEquals("div", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadComment() {
 tokeniser.emit("<!-- This is a comment -->");
 assertEquals(Token.TokenType.Comment, tokeniser.read().getType());
 assertEquals("This is a comment", tokeniser.read().getData().getData());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadScriptTag() {
 tokeniser.emit("<script src=\"script.js\"></script>");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("script", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.Attribute, tokeniser.read().getType());
 assertEquals("src", tokeniser.read().getData().getAttributeName());
 assertEquals("script.js", tokeniser.read().getData().getAttributeValue());
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 assertEquals("script", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadInvalidTag() {
 tokeniser.emit("<invalid>");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 assertEquals("Unexpected character '<' in input state [Data]", tokeniser.read().getData().getMessage());
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 assertEquals("Invalid start tag name", tokeniser.read().getData().getMessage());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertFalse(parseErrors.isEmpty());
 }

 @Test
 public void testReadCharacterReference() {
 tokeniser.emit("&amp;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("&", tokeniser.read().getData());
 assertEquals("amp;", tokeniser.read().getData());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 tokeniser.emit("&nbsp;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("nbsp", tokeniser.read().getData().getName());
 assertEquals("\u00A0", tokeniser.read().getData().getData());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadCharacterReferenceError() {
 tokeniser.emit("&invalid");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 assertEquals("Invalid character reference: invalid", tokeniser.read().getData().getMessage());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadCharacterReferenceNumeric() {
 tokeniser.emit("&#123;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("\u007B", tokeniser.read().getData().getData());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 tokeniser.emit("&#x7B;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("\u007B", tokeniser.read().getData().getData());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadCharacterReferenceErrorNumeric() {
 tokeniser.emit("&#x123;");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 assertEquals("Character outside of valid range", tokeniser.read().getData().getMessage());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadCharacterReferenceEntity() {
 tokeniser.emit("&gt;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals(">", tokeniser.read().getData());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 tokeniser.emit("&quot;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("\"", tokeniser.read().getData());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadCharacterReferenceEntityError() {
 tokeniser.emit("&lt;");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 assertEquals("Invalid character reference: lt", tokeniser.read().getData().getMessage());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadError() {
 tokeniser.emit("<script>");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("script", tokeniser.read().getData().getName());
 tokeniser.error(TokeniserState.Script);
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 assertEquals("Unexpected character '<' in input state [Script]", tokeniser.read().getData().getMessage());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertFalse(parseErrors.isEmpty());
 }

 @Test
 public void testReadEOFEvenInsideTag() {
 tokeniser.emit("<div>");
 tokeniser.emit("abc");
 tokeniser.emit("</div>");
 tokeniser.emit("def");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("div", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 assertEquals("div", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadEOFInsideTag() {
 tokeniser.emit("<div>");
 tokeniser.emit("abc");
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 assertEquals("div", tokeniser.read().getData().getName());
 assertNull(tokeniser.read());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadEOFWhileReadingTag() {
 tokeniser.emit("<div>");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("div", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertNull(tokeniser.read());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.emit("<img src=\"image.jpg\" />");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("img", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.Attribute, tokeniser.read().getType());
 assertEquals("src", tokeniser.read().getData().getAttributeName());
 assertEquals("image.jpg", tokeniser.read().getData().getAttributeValue());
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 assertEquals("img", tokeniser.read().getData().getName());
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());
 assertTrue(parseErrors.isEmpty());
 }

 @Test
 public void testUnescapeEntities() {
 assertEquals("&lt;p&gt;Hello &amp; World!&lt;/p&gt;", tokeniser.unescapeEntities("&lt;p&gt;Hello &amp; World!&lt;/p&gt;"));
 assertEquals("&apos;Hello&apos; World!", tokeniser.unescapeEntities("&apos;Hello&apos; World!"));
 assertEquals("&quot;Hello&quot; World!", tokeniser.unescapeEntities("&quot;Hello&quot; World!"));
 assertEquals("&eacute;l&oelig;o", tokeniser.unescapeEntities("&eacute;l&oelig;o"));
 assertEquals("&uuml;l&ouml;o", tokeniser.unescapeEntities("&uuml;l&ouml;o"));
 assertEquals("&nbsp;l&nbsp;o", tokeniser.unescapeEntities("&nbsp;l&nbsp;o"));
 }
}