import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = mock(CharacterReader.class);
 tokeniser = new Tokeniser(reader);
 }

 @Test
 public void testReadCharacter() {
 when(reader.current()).thenReturn('a');
 tokeniser.read();
 assertEquals("a", charBuffer.toString());
 }

 @Test
 public void testReadSelfClosingTag() {
 when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('i').thenReturn('>');
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertEquals("i", lastStartTag.tagName);
 }

 @Test
 public void testReadComment() {
 when(reader.current()).thenReturn('<!--').thenReturn(true).thenReturn(false).thenReturn('-->');
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertTrue(commentPending instanceof Token.Comment);
 tokeniser.emitCommentPending();
 }

 @Test
 public void testReadDoctype() {
 when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('d').thenReturn('o').thenReturn('c').thenReturn('t').thenReturn('y').thenReturn('p').thenReturn('>');
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertEquals("DOCTYPE", doctypePending.name);
 tokeniser.emitDoctypePending();
 }

 @Test
 public void testReadTag() {
 when(reader.current()).thenReturn('<').thenReturn('t').thenReturn('r').thenReturn('a').thenReturn('>').thenReturn('h').thenReturn('t').thenReturn('m').thenReturn('l').thenReturn('>');
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertEquals("html", lastStartTag.tagName);
 }

 @Test
 public void testReadCharacterReference() {
 when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';');
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference(null, false);
 assertEquals('a', (char) tokeniser.read().typeData);
 }

 @Test
 public void testReadCharacterReferenceWithAttribute() {
 when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';').thenReturn(' ').thenReturn('a').thenReturn('=');
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference(null, true);
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadCharacterReferenceInvalid() {
 when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('n').thenReturn('u').thenReturn(';');
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(replacementChar, (char) tokeniser.read().typeData);
 }

 @Test
 public void testReadCharacterReferenceInvalidHex() {
 when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('x').thenReturn('0').thenReturn('x').thenReturn('F').thenReturn('F').thenReturn('F').thenReturn(';');
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(replacementChar, (char) tokeniser.read().typeData);
 }

 @Test
 public void testReadCharacterReferenceInvalidNamed() {
 when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';').thenReturn(' ').thenReturn('&').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn('e').thenReturn(';');
 tokeniser.consumeCharacterReference(null, false);
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadCharacterReferenceInvalidNamedInvalid() {
 when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';').thenReturn(' ').thenReturn('&').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn('e').thenReturn('/').thenReturn('>');
 tokeniser.consumeCharacterReference(null, false);
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testErrorHandling() {
 when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('x').thenReturn('0').thenReturn('x').thenReturn('F').thenReturn('F').thenReturn('F').thenReturn(';').thenReturn(' ').thenReturn('/').thenReturn('>');
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.error(TokeniserState.CharacterReference);
 assertEquals(1, errors.size());
 assertEquals("Invalid character reference", errors.get(0).getMessage());
 }

 @Test
 public void testEOFErrorHandling() {
 when(reader.isEmpty()).thenReturn(true);
 tokeniser.read();
 tokeniser.error(TokeniserState.EOF);
 assertEquals(1, errors.size());
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
 }
}