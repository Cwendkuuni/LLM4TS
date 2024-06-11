import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("testInput.html");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadStartTag() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 Token startTag = tokeniser.read();
 Token expectedStartTag = new Token.StartTag("html", null, null, false);
 assertEquals(expectedStartTag, startTag);
 }

 @Test
 public void testReadEndTag() {
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read();
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.read();
 Token endTag = tokeniser.read();
 Token expectedEndTag = new Token.EndTag("html");
 assertEquals(expectedEndTag, endTag);
 }

 @Test
 public void testReadSimpleText() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('H');
 tokeniser.emit('e');
 tokeniser.emit('l');
 tokeniser.emit('l');
 tokeniser.emit('o');
 tokeniser.emit(' ');
 tokeniser.emit('W');
 tokeniser.emit('o');
 tokeniser.emit('r');
 tokeniser.emit('l');
 tokeniser.emit('d');
 Token expectedToken = new Token.Character("Hello World");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testReadCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('#');
 tokeniser.consumeCharacterReference(null, false);
 Token expectedToken = new Token.Character("&hellip;");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testReadInvalidCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('#');
 tokeniser.consumeCharacterReference('x', false);
 assertEquals(null, tokeniser.read());
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testReadEOF() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.emit('\n');
 tokeniser.eofError(TokeniserState.Data);
 assertEquals(null, tokeniser.read());
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testTransitionToEndTagState() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.transition(TokeniserState.EndTag);
 assertEquals(TokeniserState.EndTag, tokeniser.getState());
 }

 @Test
 public void testTransitionToDataState() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.transition(TokeniserState.Data);
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransitionToStartTagState() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('m');
 tokeniser.emit('l');
 tokeniser.emit(' ');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.transition(TokeniserState.StartTag);
 assertEquals(TokeniserState.StartTag, tokeniser.getState());
 }
}