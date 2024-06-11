import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList parseErrorList;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 characterReader = Mockito.mock(CharacterReader.class);
 parseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 }

 @Test
 public void testReadAndEmitCharacter() {
 String input = "abc";
 Mockito.when(characterReader.current()).thenReturn('a');
 Mockito.when(characterReader.read()).thenReturn('b');
 Mockito.when(characterReader.read()).thenReturn('c');
 Mockito.when(characterReader.read()).thenReturn(-1);

 tokeniser.transition(TokeniserState.Data);

 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("a", token.data);

 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("b", token.data);

 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("c", token.data);

 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadIgnoreSelfClosingFlag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(new Token.StartTag(Token.TagName.SCRIPT, true));
 Mockito.when(characterReader.current()).thenReturn('a');

 tokeniser.read();
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test(expected = AssertionError.class)
 public void testSelfClosingFlagNotAcknowledgedError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(new Token.StartTag(Token.TagName.SCRIPT, true));
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testReadAndEmitTag() {
 String input = "<tag>";
 Mockito.when(characterReader.current()).thenReturn('<');
 Mockito.when(characterReader.read()).thenReturn('t');
 Mockito.when(characterReader.read()).thenReturn('a');
 Mockito.when(characterReader.read()).thenReturn('>');
 Mockito.when(characterReader.read()).thenReturn(-1);

 tokeniser.transition(TokeniserState.Data);

 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 Token.StartTag startTag = (Token.StartTag) token;
 assertEquals(Token.TagName.TAG, startTag.tagName);

 tokeniser.emit(token);
 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadAndEmitComment() {
 String input = "-- comment";
 Mockito.when(characterReader.current()).thenReturn('--');
 Mockito.when(characterReader.read()).thenReturn(' ');
 Mockito.when(characterReader.read()).thenReturn('c');
 Mockito.when(characterReader.read()).thenReturn('o');
 Mockito.when(characterReader.read()).thenReturn('m');
 Mockito.when(characterReader.read()).thenReturn('m');
 Mockito.when(characterReader.read()).thenReturn('e');
 Mockito.when(characterReader.read()).thenReturn('n');
 Mockito.when(characterReader.read()).thenReturn('t');
 Mockito.when(characterReader.read()).thenReturn('\n');
 Mockito.when(characterReader.read()).thenReturn(-1);

 tokeniser.transition(TokeniserState.Data);

 tokeniser.createCommentPending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();

 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("comment", token.data);

 tokeniser.emitCommentPending();
 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadAndEmitDoctype() {
 String input = "<!doctype html>";
 Mockito.when(characterReader.current()).thenReturn('!');
 Mockito.when(characterReader.read()).thenReturn('d');
 Mockito.when(characterReader.read()).thenReturn('o');
 Mockito.when(characterReader.read()).thenReturn('c');
 Mockito.when(characterReader.read()).thenReturn('t');
 Mockito.when(characterReader.read()).thenReturn('y');
 Mockito.when(characterReader.read()).thenReturn('p');
 Mockito.when(characterReader.read()).thenReturn('e');
 Mockito.when(characterReader.read()).thenReturn(' ');
 Mockito.when(characterReader.read()).thenReturn('h');
 Mockito.when(characterReader.read()).thenReturn('t');
 Mockito.when(characterReader.read()).thenReturn('m');
 Mockito.when(characterReader.read()).thenReturn('l');
 Mockito.when(characterReader.read()).thenReturn('>');
 Mockito.when(characterReader.read()).thenReturn(-1);

 tokeniser.transition(TokeniserState.Data);

 tokeniser.createDoctypePending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();

 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type);
 assertEquals("html", token.data);

 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadIgnoreCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 Mockito.when(characterReader.current()).thenReturn('&');
 Mockito.when(characterReader.read()).thenReturn('#');
 Mockito.when(characterReader.read()).thenReturn('x');
 Mockito.when(characterReader.read()).thenReturn(';');

 tokeniser.read();
 assertNull(tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadConsumeCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 Mockito.when(characterReader.current()).thenReturn('&');
 Mockito.when(characterReader.read()).thenReturn('#');
 Mockito.when(characterReader.read()).thenReturn('x');
 Mockito.when(characterReader.read()).thenReturn(';');

 tokeniser.read();
 assertEquals("x", tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadConsumeNamedCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 Mockito.when(characterReader.current()).thenReturn('&');
 Mockito.when(characterReader.read()).thenReturn('a');
 Mockito.when(characterReader.read()).thenReturn('m');
 Mockito.when(characterReader.read()).thenReturn('p');
 Mockito.when(characterReader.read()).thenReturn(';');

 tokeniser.read();
 assertEquals("amp", tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadConsumeInvalidNamedCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 Mockito.when(characterReader.current()).thenReturn('&');
 Mockito.when(characterReader.read()).thenReturn('x');
 Mockito.when(characterReader.read()).thenReturn('y');
 Mockito.when(characterReader.read()).thenReturn(';');

 tokeniser.read();
 assertNull(tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadConsumeNumberCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 Mockito.when(characterReader.current()).thenReturn('&');
 Mockito.when(characterReader.read()).thenReturn('#');
 Mockito.when(characterReader.read()).thenReturn('x');
 Mockito.when(characterReader.read()).thenReturn('3');
 Mockito.when(characterReader.read()).thenReturn(';');

 tokeniser.read();
 assertEquals("\u0033", tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadConsumeNumberCharacterReferenceOutOfRange() {
 tokeniser.transition(TokeniserState.Data);
 Mockito.when(characterReader.current()).thenReturn('&');
 Mockito.when(characterReader.read()).thenReturn('#');
 Mockito.when(characterReader.read()).thenReturn('x');
 Mockito.when(characterReader.read()).thenReturn('{');
 Mockito.when(characterReader.read()).thenReturn('0');
 Mockito.when(characterReader.read()).thenReturn('x');
 Mockito.when(characterReader.read()).thenReturn('F');
 Mockito.when(characterReader.read()).thenReturn('F');
 Mockito.when(characterReader.read()).thenReturn('F');
 Mockito.when(characterReader.read()).thenReturn('F');
 Mockito.when(characterReader.read()).thenReturn('}');
 Mockito.when(characterReader.read()).thenReturn(';');

 tokeniser.read();
 assertNull(tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadIgnoreEOF() {
 tokeniser.transition(TokeniserState.Data);
 Mockito.when(characterReader.read()).thenReturn(-1);

 tokeniser.read();
 assertNull(tokeniser.charBuffer.toString());
 }

 @Test(expected = AssertionError.class)
 public void testReadConsumeEOF() {
 tokeniser.transition(TokeniserState.Data);
 Mockito.when(characterReader.read()).thenReturn(-1);
 tokeniser.read();
 }

 @Test
 public void testReadAndEmitError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);

 List<ParseError> errors = tokeniser.parseErrorList.getErrors();
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals(TokeniserState.Data, error.getState());
 assertEquals("Unexpected character 'EOF' in input state [Data]", error.getMessage());
 }

 @Test
 public void testReadAndEmitErrors() {
 String input = "&x<script";
 List<ParseError> errors = new ArrayList<>();

 Mockito.when(characterReader.current()).thenReturn('&');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 assertEquals("x", tokeniser.charBuffer.toString());
 tokeniser.charBuffer.delete(0, tokeniser.charBuffer.length());

 Mockito.when(characterReader.current()).thenReturn('<');
 tokeniser.transition(TokeniserState.AfterStartTag);
 tokeniser.read();
 tokeniser.error(TokeniserState.AfterStartTag);
 errors.add(tokeniser.parseErrorList.getErrors().get(0));

 Mockito.when(characterReader.current()).thenReturn('s');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.error(TokeniserState.Data);
 errors.add(tokeniser.parseErrorList.getErrors().get(0));

 assertEquals(2, errors.size());
 ParseError error1 = errors.get(0);
 assertEquals(TokeniserState.Data, error1.getState());
 assertEquals("Unexpected character '<' in input state [Data]", error1.getMessage());
 ParseError error2 = errors.get(1);
 assertEquals(TokeniserState.AfterStartTag, error2.getState());
 assertEquals("Attributes incorrectly present on end tag", error2.getMessage());
 }
}