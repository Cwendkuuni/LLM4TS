import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("<html></html>".getBytes()));
 tokeniser = new Tokeniser(reader);
 }

 @Test
 public void testReadEmptyStream() {
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadCharacter() {
 tokeniser.emit('a');
 tokeniser.emit('b');
 assertEquals("ab", tokeniser.read().toString());
 }

 @Test
 public void testReadCharacterReference() {
 tokeniser.emit('&');
 tokeniser.emit('a');
 tokeniser.emit('m');
 tokeniser.emit('p');
 tokeniser.emit(';');
 assertEquals("&amp;", tokeniser.read().toString());
 }

 @Test
 public void testReadTagStart() {
 tokeniser.emit('<');
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('m');
 tokeniser.emit('l');
 tokeniser.emit('>');
 Token tag = tokeniser.read();
 assertEquals("<html", tag.toString());
 }

 @Test
 public void testReadTagEnd() {
 tokeniser.emit('<');
 tokeniser.emit('/');
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('m');
 tokeniser.emit('>');
 Token tag = tokeniser.read();
 assertEquals("</html", tag.toString());
 }

 @Test
 public void testReadEndTagMismatch() {
 tokeniser.emit('<');
 tokeniser.emit('/');
 tokeniser.emit('b'); // wrong tag
 tokeniser.emit('o');
 tokeniser.emit('d');
 tokeniser.emit('y');
 tokeniser.emit('>');
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.emit('<');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit('/');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit('>');
 assertTrue(tokeniser.lastStartTag.selfClosing);
 }

 @Test
 public void testReadScriptTag() {
 tokeniser.createTempBuffer();
 tokeniser.emit('<');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit('>');
 tokeniser.emit('S');
 tokeniser.emit('C');
 tokeniser.emit('R');
 tokeniser.emit('I');
 tokeniser.emit('P');
 tokeniser.emit('T');
 tokeniser.emit('<');
 tokeniser.emit('/');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit('>');
 assertEquals(tokeniser.dataBuffer.toString(), "script");
 tokeniser.emit('<');
 tokeniser.emit('/');
 tokeniser.emit('/');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit('>');
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testErrorHandling() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html><script".getBytes())));
 tokeniser.error("Unexpected character in input", reader.current(), TokeniserState.SCRIPT_PI, reader.pos());
 }

 @Test
 public void testTrackErrors() {
 List<ParseError> errors = tokeniser.getErrors();
 assertEquals(1, errors.size());
 }

 @Test
 public void testReadEof() {
 tokeniser.emit('#');
 tokeniser.error("Unexpectedly reached end of file (EOF)", TokeniserState.MISC, reader.pos());
 }
}