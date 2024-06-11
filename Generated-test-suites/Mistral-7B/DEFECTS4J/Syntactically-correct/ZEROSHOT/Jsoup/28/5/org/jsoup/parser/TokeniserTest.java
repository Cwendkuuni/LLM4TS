import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
 public void testRead_WhenReaderIsEmpty_ReturnsNull() throws IOException {
 Mockito.when(reader.isEmpty()).thenReturn(true);
 assertNull(tokeniser.read());
 }

 @Test
 public void testRead_WhenCharacterIsConsumed_ReturnsCharacterToken() throws IOException {
 Mockito.when(reader.current()).thenReturn('a');
 Mockito.when(reader.consume()).thenReturn('a');
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("a", token.data);
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsConsumed_EmitsCharacter() throws IOException {
 String expectedData = "abcd";
 Mockito.when(reader.current()).thenReturn('a');
 Mockito.when(reader.consume()).thenCallRealMethod();
 tokeniser.emit('a');
 Mockito.when(reader.current()).thenReturn('b');
 tokeniser.emit('b');
 Mockito.when(reader.consume()).thenCallRealMethod();
 tokeniser.emit('c');
 Mockito.when(reader.consume()).thenReturn('d');
 tokeniser.emit('d');
 Mockito.when(reader.consume()).thenReturn(Tokeniser.replacementChar);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(expectedData, token.data);
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsNotConsumed_ReturnsCharacterReference() throws IOException {
 Mockito.when(reader.current()).thenReturn('&');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Mockito.when(reader.matchConsume("#")).thenReturn(true);
 Mockito.when(reader.consume()).thenReturn('x');
 Mockito.when(reader.consume()).thenReturn('r');
 Mockito.when(reader.consume()).thenReturn('a');
 Mockito.when(reader.consume()).thenReturn(';');
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertEquals(Tokeniser.replacementChar, character);
 tokeniser.acknowledgeSelfClosingFlag();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.CharacterReference, token.type);
 assertEquals("#xra;", token.data);
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsNotConsumed_ReturnsNamedCharacterReference() throws IOException {
 Mockito.when(reader.current()).thenReturn('&');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Mockito.when(reader.matchConsume("#")).thenReturn(false);
 Mockito.when(reader.matchConsume("x")).thenReturn(true);
 Mockito.when(reader.consume()).thenReturn('A');
 Mockito.when(reader.consume()).thenReturn('m');
 Mockito.when(reader.consume()).thenReturn('p');
 Mockito.when(reader.consume()).thenReturn(';');
 Character character = tokeniser.consumeCharacterReference('m', false);
 assertEquals('&amp;', character.toString());
 tokeniser.acknowledgeSelfClosingFlag();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.NamedCharacterReference, token.type);
 assertEquals("amp", token.data);
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsNotConsumed_ReturnsInvalidCharacterReference() throws IOException {
 Mockito.when(reader.current()).thenReturn('&');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Mockito.when(reader.matchConsume("#")).thenReturn(true);
 Mockito.when(reader.consume()).thenReturn('x');
 Mockito.when(reader.consume()).thenReturn('r');
 Mockito.when(reader.consume()).thenReturn('a');
 Mockito.when(reader.consume()).thenReturn(';');
 Mockito.when(reader.consume()).thenReturn('.');
 Token token = tokeniser.read();
 assertNull(token);
 assertEquals(1, errors.size());
 assertEquals("Invalid character reference: unexpected character '.'", errors.get(0).getMessage());
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsNotConsumed_ReturnsComment() throws IOException {
 Mockito.when(reader.current()).thenReturn('/');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Mockito.when(reader.consume()).thenReturn('*');
 tokeniser.emit('*');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Mockito.when(reader.consume()).thenReturn('*');
 tokeniser.emit('*');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("", token.data);
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsNotConsumed_ReturnsStartTag() throws IOException {
 Mockito.when(reader.current()).thenReturn('<');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Mockito.when(reader.matchConsume('t')).thenReturn(true);
 Mockito.when(reader.matchConsume('h')).thenReturn(true);
 Mockito.when(reader.matchConsume('e')).thenReturn(true);
 Mockito.when(reader.matchConsume('d')).thenReturn(true);
 Mockito.when(reader.matchConsume('>')).thenReturn(true);
 Token startTag = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, startTag.type);
 assertEquals("th", startTag.tagName);
 tokeniser.acknowledgeSelfClosingFlag();
 Token endTag = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, endTag.type);
 assertEquals("th", endTag.tagName);
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsNotConsumed_ReturnsDoctype() throws IOException {
 Mockito.when(reader.current()).thenReturn('<');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Mockito.when(reader.matchConsume('d')).thenReturn(true);
 Mockito.when(reader.matchConsume('o')).thenReturn(true);
 Mockito.when(reader.matchConsume('c')).thenReturn(true);
 Mockito.when(reader.matchConsume('t')).thenReturn(true);
 Mockito.when(reader.matchConsume('y')).thenReturn(true);
 Mockito.when(reader.matchConsume('p')).thenReturn(true);
 Mockito.when(reader.matchConsume('>')).thenReturn(true);
 Token docType = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, docType.type);
 assertEquals("", docType.data);
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsNotConsumed_ReturnsEndOfFile() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("abcdefg".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.emit('a');
 tokeniser.emit('b');
 tokeniser.emit('c');
 tokeniser.emit('d');
 tokeniser.emit('e');
 tokeniser.emit('f');
 tokeniser.emit('g');
 Mockito.when(reader.isEmpty()).thenReturn(true);
 Token token = tokeniser.read();
 assertNull(token);
 assertEquals(1, errors.size());
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
 }

 @Test
 public void testRead_WhenStateIsDataAndCharacterIsNotConsumed_ReturnsError() throws IOException {
 Mockito.when(reader.current()).thenReturn('&');
 Mockito.when(reader.consume()).thenCallRealMethod();
 Mockito.when(reader.matchConsume("#")).thenReturn(true);
 Mockito.when(reader.consume()).thenReturn('x');
 Mockito.when(reader.consume()).thenReturn('r');
 Mockito.when(reader.consume()).thenReturn('d');
 Mockito.when(reader.consume()).thenReturn(';');
 Mockito.when(reader.consume()).thenReturn('$');
 Token token = tokeniser.read();
 assertNull(token);
 assertEquals(1, errors.size());
 assertEquals("Invalid character reference: unexpected character '$'", errors.get(0).getMessage());
 }
}