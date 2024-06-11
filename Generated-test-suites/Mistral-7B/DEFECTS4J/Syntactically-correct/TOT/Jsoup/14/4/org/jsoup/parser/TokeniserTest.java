import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setUp() {
 reader = mock(CharacterReader.class);
 tokeniser = new Tokeniser(reader);
 errors = new ArrayList<>();
 tokeniser.setTrackErrors(true);
 }

 @Test
 public void testReadCharacter() {
 // Test reading a single character
 tokeniser.transition(TokeniserState.Character);
 reader.expects("read").andReturn((char) 'a').atLeastOnce();
 tokeniser.emit('a');
 assertNull(tokeniser.read());
 assertEquals("a", tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadStartTag() {
 // Test reading a start tag
 tokeniser.transition(TokeniserState.StartTag);
 reader.expects("read").andReturn('<').atLeastOnce();
 reader.expects("read").andReturn('/').atLeastOnce();
 reader.expects("read").andReturns((char) 'p').atLeastOnce();
 reader.expects("read").andReturns('<').atLeastOnce();
 reader.expects("read").andReturns('>').atLeastOnce();

 ArgumentCaptor<Token.StartTag> startTagCaptor = ArgumentCaptor.forClass(Token.StartTag.class);
 tokeniser.emit(startTagCaptor.capture());
 assertEquals("p", startTagCaptor.getValue().tagName);
 }

 @Test
 public void testReadEndTag() {
 // Test reading an end tag
 tokeniser.transition(TokeniserState.EndTag);
 reader.expects("read").andReturn('/').atLeastOnce();
 reader.expects("read").andReturns('/').atLeastOnce();
 reader.expects("read").andReturns('p').atLeastOnce();
 reader.expects("read").andReturns('>').atLeastOnce();

 tokeniser.emit(new Token.EndTag("p"));
 }

 @Test
 public void testReadComment() {
 // Test reading a comment
 tokeniser.transition(TokeniserState.Comment);
 reader.expects("read").andReturns('*').atLeastOnce();
 reader.expects("read").andReturns('/').atLeastOnce();
 reader.expects("read").andReturns('>').atLeastOnce();

 tokeniser.emit(new Token.Comment());
 }

 @Test
 public void testReadDoctype() {
 // Test reading a doctype
 tokeniser.transition(TokeniserState.Doctype);
 reader.expects("read").andReturns('<').atLeastOnce();
 reader.expects("read").andReturns('!').atLeastOnce();
 reader.expects("read").andReturns('D').atLeastOnce();
 reader.expects("read").andReturns('O').atLeastOnce();
 reader.expects("read").andReturns('C').atLeastOnce();
 reader.expects("read").andReturns('T').atLeastOnce();
 reader.expects("read").andReturns('Y').atLeastOnce();
 reader.expects("read").andReturns('<').atLeastOnce();
 reader.expects("read").andReturns('!').atLeastOnce();
 reader.expects("read").andReturns('>').atLeastOnce();

 tokeniser.emit(new Token.Doctype());
 }

 @Test
 public void testReadError() {
 // Test reading an error
 tokeniser.transition(TokeniserState.Error);
 reader.expects("read").andThrows(IOException.class);
 tokeniser.error(TokeniserState.Error);
 assertTrue(errors.size() > 0);
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
 }

 @Test
 public void testReadUnexpectedCharacter() {
 // Test reading an unexpected character
 tokeniser.transition(TokeniserState.Data);
 reader.expects("read").andReturns('>').atLeastOnce(); // Should not be here
 tokeniser.error(TokeniserState.Data);
 assertTrue(errors.size() > 0);
 assertEquals("Unexpected character in input", errors.get(0).getMessage());
 }

 @Test
 public void testReadEOF() {
 // Test reading EOF
 tokeniser.transition(TokeniserState.Data);
 reader.expects("read").andThrows(IOException.class);
 tokeniser.eofError(TokeniserState.Data);
 assertTrue(errors.size() > 0);
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
 }

 @Test
 public void testReadCharacterReference() {
 // Test reading a character reference
 tokeniser.transition(TokeniserState.CharacterReference);
 reader.expects("read").andReturns('#').atLeastOnce();
 reader.expects("read").andReturns('x').atLeastOnce();
 reader.expects("read").andReturns('F').atLeastOnce();
 reader.expects("read").andReturns('F').atLeastOnce();
 reader.expects("read").andReturns('F').atLeastOnce();
 reader.expects("read").andReturns(';').atLeastOnce();

 ArgumentCaptor<Character> charCaptor = ArgumentCaptor.forClass(Character.class);
 tokeniser.emit(charCaptor.capture());
 assertEquals(Character.forInt(0x00FF), charCaptor.getValue());
 }

 @Test
 public void testReadNamedCharacterReference() {
 // Test reading a named character reference
 tokeniser.transition(TokeniserState.CharacterReference);
 reader.expects("read").andReturns('#').atLeastOnce();
 reader.expects("read").andReturns('a').atLeastOnce();
 reader.expects("read").andReturns('m').atLeastOnce();
 reader.expects("read").andReturns('p').atLeastOnce();
 reader.expects("read").andReturns(';').atLeastOnce();

 ArgumentCaptor<Character> charCaptor = ArgumentCaptor.forClass(Character.class);
 tokeniser.emit(charCaptor.capture());
 assertEquals('&amp;', charCaptor.getValue());
 }
}