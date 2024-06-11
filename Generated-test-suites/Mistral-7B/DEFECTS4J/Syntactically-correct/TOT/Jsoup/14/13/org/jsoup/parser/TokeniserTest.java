import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes()));
 tokeniser = new Tokeniser(reader);
 errors = tokeniser.getErrors();
 }

 @Test
 public void testReadEmptyInputStream() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream(new byte[0])));
 Token token = tokeniser.read();
 assertTrue(token == null);
 }

 @Test
 public void testReadCharacter() {
 tokeniser.read(); // consume initial character
 assertEquals('<', tokeniser.reader.current());
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("h", token.data);
 }

 @Test
 public void testReadStartTag() {
 tokeniser.read(); // consume <
 tokeniser.read(); // consume h
 tokeniser.read(); // consume {
 Token tag = tokeniser.createTagPending(true);
 tag.tagName = "h";
 tag.attributes = new ArrayList<>();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals(tag, tokeniser.tagPending);
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 tokeniser.emitEndTag();
 assertEquals(null, tokeniser.tagPending);
 }

 @Test
 public void testReadEndTag() {
 tokeniser.read(); // consume <
 tokeniser.read(); // consume /
 tokeniser.read(); // consume h
 tokeniser.read(); // consume >
 Token tag = tokeniser.tagPending;
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("h", tag.tagName);
 tokeniser.emitEndTag();
 assertEquals(null, tokeniser.tagPending);
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.read(); // consume <
 tokeniser.read(); // consume img
 tokeniser.read(); // consume {
 tokeniser.acknowledgeSelfClosingFlag();
 Token tag = tokeniser.createTagPending(false);
 tag.tagName = "img";
 tag.selfClosing = true;
 tag.attributes = new ArrayList<>();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals(tag, tokeniser.tagPending);
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 tokeniser.emitEndTag();
 assertEquals(null, tokeniser.tagPending);
 }

 @Test
 public void testReadComment() {
 tokeniser.read(); // consume <
 tokeniser.read(); // consume !
 tokeniser.read(); // consume c
 tokeniser.read(); // consume d
 tokeniser.read(); // consume a
 tokeniser.read(); // consume t
 tokeniser.read(); // consume !
 tokeniser.read(); // consume >
 Token comment = tokeniser.commentPending;
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("comment", comment.data);
 tokeniser.emitCommentPending();
 assertEquals(null, tokeniser.commentPending);
 }

 @Test
 public void testReadDoctype() {
 tokeniser.read(); // consume <
 tokeniser.read(); // consume !
 tokeniser.read(); // consume D
 tokeniser.read(); // consume O
 tokeniser.read(); // consume C
 tokeniser.read(); // consume T
 tokeniser.read(); // consumeYPE
 tokeniser.read(); // consume >
 Token doctype = tokeniser.doctypePending;
 assertEquals(Token.TokenType.Doctype, token.type);
 assertEquals("DOCTYPE", doctype.data);
 tokeniser.emitDoctypePending();
 assertEquals(null, tokeniser.doctypePending);
 }

 @Test
 public void testReadInvalidCharacterReference() {
 tokeniser.read(); // consume <
 tokeniser.read(); // consume &
 tokeniser.read(); // consume w
 tokeniser.read(); // consume o
 tokeniser.read(); // consume o
 tokeniser.read(); // consume ;
 assertEquals(Token.TokenType.Character, tokeniser.read().type); // consume next character
 tokeniser.consumeCharacterReference('w', true); // try to consume woo
 assertNull(tokeniser.read()); // should return null as the next character is not w
 assertEquals(1, errors.size()); // there should be one error
 assertEquals("Invalid character reference", errors.get(0).getMessage());
 }

 @Test
 public void testReadInvalidCharacterReferenceWithAttribute() {
 tokeniser.read(); // consume <
 tokeniser.read(); // consume html
 tokeniser.read(); // consume {
 tokeniser.read(); // consume a
 tokeniser.read(); // consume t
 tokeniser.read(); // consume t
 tokeniser.read(); // consume r
 tokeniser.read(); // consume i
 tokeniser.read(); // consume b
 tokeniser.read(); // consume =
 tokeniser.read(); // consume "
 tokeniser.read(); // consume w
 tokeniser.read(); // consume o
 tokeniser.read(); // consume o
 tokeniser.read(); // consume "
 tokeniser.read(); // consume >
 assertEquals(Token.TokenType.Character, tokeniser.read().type); // consume next character
 tokeniser.consumeCharacterReference('w', false); // try to consume woo in an attribute
 assertNull(tokeniser.read()); // should return null as the next character is not w
 assertEquals(1, errors.size()); // there should be one error
 assertEquals("Invalid character reference", errors.get(0).getMessage());
 }

 @Test
 public void testReadEndOfFile() {
 tokeniser.read(); // consume <
 tokeniser.read(); // consume h
 tokeniser.read(); // consume t
 tokeniser.read(); // consume m
 tokeniser.read(); // consume l
 tokeniser.read(); // consume >
 tokeniser.read(); // consume EOF
 assertEquals(null, tokeniser.read()); // should return null as EOF
 assertEquals(1, errors.size()); // there should be one error
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
 }
}