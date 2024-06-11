import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private CharacterReader reader;

 @Before
 public void setup() {
 errors = new ParseErrorList();
 reader = Mockito.spy(new CharacterReader() {
 @Override
 public char current() {
 return (char) 0;
 }

 @Override
 public int pos() {
 return 0;
 }

 @Override
 public boolean matches(char expected) {
 return current() == expected;
 }

 @Override
 public void matchConsume(char expected) {
 current--;
 }

 @Override
 public void matchConsumeLetter() {
 matchConsume(Character.isLetter(current()) ? current() : '\u0000');
 }

 @Override
 public void matchConsumeDigit() {
 matchConsume(Character.isDigit(current()) ? current() : '\u0000');
 }

 @Override
 public void matchConsumeAny(char[] chars) {
 matchConsume(chars[0]);
 }

 @Override
 public void matchConsumeAnySorted(char[] chars) {
 Arrays.sort(chars);
 while (current() != chars[0]) {
 matchConsume(1);
 }
 matchConsume(1);
 }

 @Override
 public boolean matchesAny(char[] chars) {
 for (char c : chars) {
 if (matches(c)) {
 return true;
 }
 }
 return false;
 }

 @Override
 public void matchConsumeIgnoreCase(char expected) {
 if (Character.toLowerCase(current()) == expected) {
 matchConsume(1);
 } else {
 fail("Unexpected character: " + current());
 }
 }

 @Override
 public InputStream inputStream() {
 return new ByteArrayInputStream(getInput().getBytes());
 }

 @Override
 public void mark(int readAheadLimit) {
 // No implementation needed
 }

 @Override
 public void reset() {
 // No implementation needed
 }

 @Override
 public void advance() throws IOException {
 current--;
 }

 @Override
 public boolean isEmpty() {
 return (current() == '\u0000');
 }
 });
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadEmptyStream() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadWhiteSpace() {
 reader.mark();
 reader.matchConsume('\t');
 reader.matchConsume('\n');
 reader.matchConsume('\r');
 reader.matchConsume('\f');
 reader.matchConsume(' ');
 tokeniser.read();
 Token charToken = tokeniser.charPending.data("");
 assertEquals("", charToken.data());
 }

 @Test
 public void testReadSelfClosingTag() {
 reader.mark();
 reader.matchConsume('<');
 reader.matchConsume('i');
 reader.matchConsume('m');
 reader.matchConsume('g');
 reader.matchConsume('>');
 reader.matchConsume('/');
 reader.matchConsume('i');
 reader.matchConsume('m');
 reader.matchConsume('g');
 reader.matchConsume('>');
 tokeniser.read();
 Token tagToken = tokeniser.emitPending;
 assertEquals("img", tagToken.tagName);
 assertEquals(Token.TokenType.StartTag, tagToken.type);
 assertSame(tagToken, tokeniser.startPending);
 }

 @Test
 public void testReadEmptyTag() {
 reader.mark();
 reader.matchConsume('<');
 reader.matchConsume('/');
 reader.matchConsume('/');
 reader.matchConsume('>');
 tokeniser.read();
 Token tagToken = tokeniser.emitPending;
 assertEquals("", tagToken.tagName);
 assertEquals(Token.TokenType.EndTag, tagToken.type);
 }

 @Test
 public void testReadString() {
 reader.mark();
 reader.consume("abcdef");
 tokeniser.read();
 Token charToken = tokeniser.charPending.data("abcdef");
 assertEquals("abcdef", charToken.data());
 }

 @Test
 public void testReadCharacterReference() {
 reader.mark();
 reader.matchConsume('#');
 reader.matchConsume('x');
 reader.matchConsume('F');
 reader.matchConsume('F');
 reader.matchConsume('F');
 reader.matchConsume(';');
 tokeniser.read();
 int[] codePoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(0x00FF, codePoints[0]);
 }

 @Test
 public void testReadNamedCharacterReference() {
 reader.mark();
 reader.consumeLetterThenDigitSequence("amp");
 reader.matchConsume(';');
 tokeniser.read();
 int[] codePoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(0x0026, codePoints[0]);
 assertEquals(0x002A, codePoints[1]);
 }

 @Test
 public void testReadCharacterReferenceWithInvalidSyntax() {
 reader.mark();
 reader.matchConsume('#');
 reader.matchConsume('x');
 reader.matchConsume('F');
 reader.matchConsume('F');
 reader.matchConsume(';');
 tokeniser.read();
 assertNull(tokeniser.charPending.data());
 assertEquals(1, errors.size());
 assertEquals("Invalid character reference: unexpected semicolon", errors.get(0).getMessage());
 }

 @Test
 public void testReadCharacterReferenceWithInvalidCharacter() {
 reader.mark();
 reader.matchConsume('#');
 reader.matchConsume('x');
 reader.matchConsume('A');
 reader.matchConsume('0'); // not a hex digit
 reader.matchConsume(';');
 tokeniser.read();
 assertNull(tokeniser.charPending.data());
 assertEquals(1, errors.size());
 assertEquals("Invalid character reference: unexpected character 'A'", errors.get(0).getMessage());
 }

 @Test
 public void testReadCharacterReferenceWithOutOfRangeCharacter() {
 reader.mark();
 reader.matchConsume('#');
 reader.matchConsume('x');
 reader.matchConsume('110000');
 reader.matchConsume(';');
 tokeniser.read();
 assertNotNull(tokeniser.charPending.data());
 assertEquals(Character.toString(Tokeniser.replacementChar), tokeniser.charPending.data());
 }

 @Test
 public void testReadCharacterReferenceWithExtraCharacters() {
 reader.mark();
 reader.matchConsume('#');
 reader.matchConsume('x');
 reader.matchConsume('F');
 reader.matchConsume('F');
 reader.matchConsume(';');
 reader.matchConsumeLetter(); // should not be consumed
 tokeniser.read();
 assertNull(tokeniser.charPending.data());
 assertEquals(1, errors.size());
 assertEquals("Unexpected character: '.'", errors.get(0).getMessage());
 }

 @Test
 public void testReadTagWithExtraCharacter() {
 reader.mark();
 reader.matchConsume('<');
 reader.matchConsume('i');
 reader.matchConsume('m');
 reader.matchConsume('g');
 reader.matchConsume('>');
 reader.matchConsume(' '); // should not be consumed
 tokeniser.read();
 Token tagToken = tokeniser.emitPending;
 assertSame(tagToken, tokeniser.startPending);
 assertEquals(1, errors.size());
 assertEquals("Unexpected character: '.'", errors.get(0).getMessage());
 }

 @Test
 public void testReadTagWithInvalidTagName() {
 reader.mark();
 reader.matchConsume('<');
 reader.matchConsume('/');
 reader.matchConsume('/');
 reader.matchConsume('x');
 reader.matchConsume('>');
 tokeniser.read();
 assertNull(tokeniser.emitPending);
 assertEquals(1, errors.size());
 assertEquals("Invalid tag name: 'x'", errors.get(0).getMessage());
 }

 @Test
 public void testReadTagWithMissingEndTag() {
 reader.mark();
 reader.matchConsume('<');
 reader.matchConsume('i');
 reader.matchConsume('m');
 reader.matchConsume('g');
 reader.matchConsume('>');
 reader.matchConsume('/');
 reader.matchConsume('/');
 tokeniser.read();
 tokeniser.read();
 assertNull(tokeniser.emitPending);
 assertEquals(1, errors.size());
 assertEquals("No end tag for start tag: 'img'", errors.get(0).getMessage());
 }

 @Test
 public void testReadTagWithExtraTag() {
 reader.mark();
 reader.matchConsume('<');
 reader.matchConsume('i');
 reader.matchConsume('m');
 reader.matchConsume('g');
 reader.matchConsume('>');
 reader.matchConsume('<');
 reader.matchConsume('/');
 reader.matchConsume('/');
 reader.matchConsume('i');
 reader.matchConsume('m');
 reader.matchConsume('g');
 reader.matchConsume('>');
 reader.matchConsume('>');
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertNull(tokeniser.emitPending);
 assertEquals(1, errors.size());
 assertEquals("Unexpected character: '.'", errors.get(0).getMessage());
 }

 @Test
 public void testReadComment() {
 reader.mark();
 reader.consumeLetterThenDigitSequence("--");
 reader.matchConsume('>');
 tokeniser.read();
 Token commentToken = tokeniser.emitCommentPending;
 assertEquals("", commentToken.data());
 }

 @Test
 public void testReadScriptTag() {
 reader.mark();
 reader.matchConsume('<');
 reader.matchConsume('/');
 reader.matchConsume('s');
 reader.matchConsume('c');
 reader.matchConsume('r');
 reader.matchConsume('i');
 reader.matchConsume('p');
 reader.matchConsume('>');
 tokeniser.read();
 tokeniser.emitDoctypePending();
 assertEquals(Token.TokenType.Script, tokeniser.getState().getStateType());
 }

 @Test
 public void testReadDoctypeDeclaration() {
 reader.mark();
 reader.consumeLetterThenDigitSequence("DOCTYPE");
 reader.matchConsumeSpace();
 reader.consumeLetterThenDigitSequence("html");
 reader.matchConsumeSpace();
 reader.consumeLetterThenDigitSequence("PUBLIC");
 reader.matchConsumeSpace();
 reader.consumeLetterThenDigitSequence("\"-//W3C//DTD HTML 4.01//EN\"");
 reader.matchConsumeSpace();
 reader.matchConsume('<');
 reader.matchConsume('/');
 reader.matchConsume('!');
 reader.matchConsume('>');
 tokeniser.read();
 tokeniser.emitDoctypePending();
 assertEquals(Token.TokenType.DocType, tokeniser.getState().getStateType());
 }

 @Test
 public void testReadErrorHandling() {
 reader.mark();
 reader.matchConsume('x');
 tokeniser.read();
 Token token = tokeniser.read();
 assertNull(token);
 assertEquals(1, errors.size());
 assertEquals("Unexpected character: 'x'", errors.get(0).getMessage());
 }

 @Test
 public void testReadEndOfStream() {
 reader.mark();
 reader.consumeTo('>');
 tokeniser.read();
 assertEquals(Token.TokenType.EndOfStream, tokeniser.getState().getStateType());
 }
}