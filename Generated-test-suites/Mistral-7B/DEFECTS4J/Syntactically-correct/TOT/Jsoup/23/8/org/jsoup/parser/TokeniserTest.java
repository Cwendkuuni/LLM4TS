import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = Mockito.mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadEmptyInput() {
 Mockito.when(reader.isEmpty()).thenReturn(true);
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadCharacter() {
 Mockito.when(reader.current()).thenReturn((char) 'a');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit('a');
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("a", token.data);
 }

 @Test
 public void testReadStartTag() {
 Mockito.when(reader.current()).thenReturn('<');
 Mockito.when(reader.consume()).thenReturn('h').thenReturn('i').thenReturn('m');
 Mockito.when(reader.consume()).thenReturn('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.emit('<');
 tokeniser.emit('h').emit('i').emit('m');
 tokeniser.emit('>');
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("him", token.tagName);
 assertFalse(token.selfClosing);
 }

 @Test
 public void testReadEndTag() {
 Mockito.when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('/').thenReturn('h').thenReturn('i').thenReturn('m');
 Mockito.when(reader.consume()).thenReturn('>');
 tokeniser.emit(' ').emit(' ').emit('<').emit('/').emit('/').emit('h').emit('i').emit('m').emit('>');
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("him", token.tagName);
 assertFalse(token.selfClosing);
 }

 @Test
 public void testReadSelfClosingTag() {
 Mockito.when(reader.current()).thenReturn('<').thenReturn('h').thenReturn('i').thenReturn('m').thenReturn('/').thenReturn('>');
 tokeniser.emit(' ').emit(' ').emit('<').emit('h').emit('i').emit('m').emit('/').emit('>');
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("him", token.tagName);
 assertTrue(token.selfClosing);
 }

 @Test
 public void testReadErrorOnUnexpectedCharacter() throws IOException {
 Mockito.when(reader.current()).thenReturn('&').thenReturn(';'); // semicolon should not be consumed
 tokeniser.emit(' ').emit(' ');
 tokeniser.error("Unexpected character '%s' in input state [Data]", "&");
 List<ParseError> errors = this.errors.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected character '&' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testReadErrorOnMissingSemicolon() throws IOException {
 Mockito.when(reader.current()).thenReturn('#').thenReturn('x').thenReturn('>'); // semicolon should be consumed
 tokeniser.emit(' ').emit(' ').emit('#').emit('x');
 tokeniser.error("missing semicolon");
 List<ParseError> errors = this.errors.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Invalid character reference: missing semicolon", errors.get(0).getMessage());
 }

 @Test
 public void testReadErrorOnInvalidCharacter() throws IOException {
 Mockito.when(reader.current()).thenReturn('\u0000'); // null character
 tokeniser.error("Self closing flag not acknowledged");
 List<ParseError> errors = this.errors.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected character '\u0000' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testReadErrorOnInappropriateEndTag() {
 tokeniser.createTagPending(false).emitTagPending();
 tokeniser.createTagPending(true).emitTagPending(); // should be an img tag
 tokeniser.read(); // h1 tag is read
 List<ParseError> errors = new ArrayList<>();
 tokeniser.setErrors(errors);
 tokeniser.error("Attributes incorrectly present on end tag");
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(new ParseError(0, "Attributes incorrectly present on end tag"));
 assertEquals(expectedErrors, errors);
 }

 @Test
 public void testReadString() {
 Mockito.when(reader.current()).thenReturn('H').thenReturn('e').thenReturn('l').thenReturn('l').thenReturn('o').thenReturn(' ').thenReturn('w').thenReturn('o').thenReturn('r').thenReturn('d').thenReturn('\n');
 tokeniser.emit(' ').emit(' ').emit('H').emit('e').emit('l').emit('l').emit('o').emit(' ').emit('w').emit('o').emit('r').emit('d').emit('\n');
 String str = tokeniser.readString();
 assertEquals("Hello world", str);
 }

 @Test
 public void testReadCharacterReference() {
 Mockito.when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('x').thenReturn(';');
 tokeniser.emit(' ').emit(' ').emit('&').emit('#').emit('x').emit(';');
 Character ch = tokeniser.consumeCharacterReference(null, false);
 assertEquals((char) 0x0068, ch); // h
 }

 @Test
 public void testReadCharacterReferenceWithExtraCharacter() {
 Mockito.when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('x').thenReturn('A').thenReturn(';');
 tokeniser.emit(' ').emit(' ').emit('&').emit('#').emit('x').emit('A').emit(';');
 Character ch = tokeniser.consumeCharacterReference(null, false);
 assertNull(ch);
 }

 @Test
 public void testReadCharacterReferenceWithHexadecimal() {
 Mockito.when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('x').thenReturn('X').thenReturn('3').thenReturn('A').thenReturn(';');
 tokeniser.emit(' ').emit(' ').emit('&').emit('#').emit('x').emit('X').emit('3').emit('A').emit(';');
 Character ch = tokeniser.consumeCharacterReference(null, false);
 assertEquals((char) 0x0033, ch); // 3
 }

 @Test
 public void testReadCharacterReferenceWithNamedEntity() {
 Mockito.when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn(';');
 tokeniser.emit(' ').emit(' ').emit('&').emit('#').emit('n').emit('a').emit('m').emit(';');
 Character ch = tokeniser.consumeCharacterReference(null, false);
 assertEquals(Character.MIN_VALUE + 0x006E, ch); // n
 }

 @Test
 public void testReadCharacterReferenceWithNamedEntityAndAttribute() {
 Mockito.when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn(' ').thenReturn('a').thenReturn('t').thenReturn('t').thenReturn('r').thenReturn('i').thenReturn('b').thenReturn('u').thenReturn('t').thenReturn('e').thenReturn(';');
 tokeniser.emit(' ').emit(' ').emit('&').emit('#').emit('n').emit('a').emit('m').emit(' ').emit('a').emit('t').emit('t').emit('r').emit('i').emit('b').emit('u').emit('t').emit('e').emit(';');
 Character ch = tokeniser.consumeCharacterReference(null, true);
 assertNull(ch);
 }

 @Test
 public void testReadComment() {
 Mockito.when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('!').thenReturn('-').thenReturn('c').thenReturn('o').thenReturn('m').thenReturn('m').thenReturn('e').thenReturn('n').thenReturn('t').thenReturn('-').thenReturn('>').thenReturn('\n');
 tokeniser.emit(' ').emit(' ').emit('<').emit('/').emit('!').emit('-').emit('c').emit('o').emit('m').emit('m').emit('e').emit('n').emit('t').emit('-').emit('>').emit('\n');
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("<!--", token.data);
 }

 @Test
 public void testReadDoctype() {
 Mockito.when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('d').thenReturn('o').thenReturn('c').thenReturn('t').thenReturn('y').thenReturn('p').thenReturn('e').thenReturn(' ').thenReturn('<').thenReturn('!').thenReturn('d').thenReturn('o').thenReturn('c').thenReturn('t').thenReturn('y').thenReturn('p').thenReturn('e').thenReturn(' ').thenReturn('>').thenReturn('\n');
 tokeniser.createTempBuffer();
 tokeniser.emit(' ').emit(' ').emit('<').emit('/').emit('d').emit('o').emit('c').emit('t').emit('y').emit('p').emit('e').emit('<').emit('!').emit('d').emit('o').emit('c').emit('t').emit('y').emit('p').emit('e').emit(' ').emit('>').emit('\n');
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testReadEOF() throws IOException {
 Mockito.when(reader.isEOF()).thenReturn(true);
 tokeniser.read();
 tokeniser.eofError(TokeniserState.Data);
 List<ParseError> errors = this.errors.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
 }
}