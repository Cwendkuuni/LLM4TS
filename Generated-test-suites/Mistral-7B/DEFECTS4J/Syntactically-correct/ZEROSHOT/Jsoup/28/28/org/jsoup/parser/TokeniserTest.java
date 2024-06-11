import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokeniserTest {

 @Mock
 private CharacterReader reader;

 @Mock
 private ParseErrorList errors;

 @InjectMocks
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(reader.current()).thenReturn('A');
 }

 @Test
 public void testRead_whenCharacterIsConsumed_returnCharacterToken() throws IOException {
 // Given
 tokeniser = new Tokeniser(reader, errors);

 // When
 Token token = tokeniser.read();

 // Then
 assertTrue(token instanceof Character);
 assertEquals('A', ((Character) token).characters());
 }

 @Test
 public void testRead_whenEndTagIsConsumed_returnEndTagToken() throws IOException {
 // Given
 tokeniser = new Tokeniser(reader, errors);
 when(reader.matches('/')).thenReturn(true);
 when(reader.matchConsume('/')).thenReturn(true);
 when(reader.matchConsume('>')).thenReturn(true);

 // When
 Token token = tokeniser.read();

 // Then
 assertTrue(token instanceof EndTag);
 assertEquals("/>", token.toString());
 }

 @Test
 public void testRead_whenStartTagIsConsumed_returnStartTagToken() throws IOException {
 // Given
 tokeniser = new Tokeniser(reader, errors);
 when(reader.matches('<')).thenReturn(true);
 when(reader.matchConsume('<')).thenReturn(true);
 when(reader.consumeLetterThenDigitSequence()).thenReturn("html");
 when(reader.matches('>')).thenReturn(true);
 when(reader.matchConsume('>')).thenReturn(true);

 // When
 Token token = tokeniser.read();

 // Then
 assertTrue(token instanceof StartTag);
 assertEquals("<html>", token.toString());
 }

 @Test
 public void testRead_whenStringIsConsumed_returnCharacterTokens() throws IOException {
 // Given
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.emit("Hello, ");
 when(reader.matchConsume(' ')).thenReturn(true);
 when(reader.matchConsume('W')).thenReturn(true);
 when(reader.matchConsume('o')).thenReturn(true);
 when(reader.matchConsume('r')).thenReturn(true);
 when(reader.matchConsume('l')).thenReturn(true);
 when(reader.matchConsume('d')).thenReturn(true);
 when(reader.matchConsume('!')).thenReturn(true);

 // When
 List<Token> tokens = new ArrayList<>();
 while (tokeniser.getState() != TokeniserState.Data) {
 tokens.add(tokeniser.read());
 }

 // Then
 assertEquals(7, tokens.size());
 for (int i = 0; i < 7; i++) {
 Token token = tokens.get(i);
 assertTrue(token instanceof Character);
 assertEquals(Character.isWhitespace(i) ? ' ' : (char) ('W' + i), ((Character) token).characters());
 }
 }

 @Test
 public void testRead_whenSelfClosingTagIsConsumed_returnCharacterTokensAndAcknowledgeFlag() throws IOException {
 // Given
 tokeniser = new Tokeniser(reader, errors);
 when(reader.matches('<')).thenReturn(true);
 when(reader.matchConsume('<')).thenReturn(true);
 when(reader.consumeLetterThenDigitSequence()).thenReturn("img");
 when(reader.matches(' ')).thenReturn(true);
 when(reader.matches('/')).thenReturn(true);
 when(reader.matchConsume('/')).thenReturn(true);
 when(reader.matches('>')).thenReturn(true);
 when(reader.matchConsume('>')).thenReturn(true);

 // When
 List<Token> tokens = new ArrayList<>();
 while (tokeniser.getState() != TokeniserState.Data) {
 tokens.add(tokeniser.read());
 }

 // Then
 assertEquals(3, tokens.size());
 assertTrue(tokeniser.selfClosingFlagAcknowledged());
 for (int i = 0; i < 3; i++) {
 Token token = tokens.get(i);
 assertTrue(token instanceof Character);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_whenNoTokenIsPending_throwException() throws IOException {
 // Given
 tokeniser = new Tokeniser(reader, errors);
 when(reader.isEmpty()).thenReturn(true);

 // When & Then
 tokeniser.read();
 }

 @Test
 public void testTransition_whenStateIsChanged_stateIsChanged() throws IOException {
 // Given
 tokeniser = new Tokeniser(reader, errors);
 assertEquals(TokeniserState.Data, tokeniser.getState());

 // When
 tokeniser.transition(TokeniserState.Script);

 // Then
 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag_flagIsAcknowledged() throws IOException {
 // Given
 tokeniser = new Tokeniser(reader, errors);

 // When
 tokeniser.acknowledgeSelfClosingFlag();

 // Then
 assertTrue(tokeniser.selfClosingFlagAcknowledged());
 }
}