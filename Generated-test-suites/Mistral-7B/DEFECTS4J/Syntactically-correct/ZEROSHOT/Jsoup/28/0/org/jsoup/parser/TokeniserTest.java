import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
 public void testInitialState() {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadCharacter() {
 String input = "A";
 Mockito.when(reader.current()).thenReturn('A');
 Mockito.when(reader.consume()).thenReturn(true);

 Token token = tokeniser.read();
 assertEquals(new Character("A"), token);
 }

 @Test
 public void testReadCharacterReference() {
 String input = "&#x1F600;";
 Mockito.when(reader.current()).thenReturn('#');
 Mockito.when(reader.matchConsumeIgnoreCase("X")).thenReturn(true);
 Mockito.when(reader.consumeHexSequence()).thenReturn("1F600");
 Mockito.when(reader.matchConsume(";")).thenReturn(true);

 Character charRef = tokeniser.consumeCharacterReference(null, false);
 assertEquals('\uD835\uDD10', charRef.getChars()[0]);

 Mockito.verify(reader, Mockito.times(4)).consume();
 }

 @Test
 public void testReadNamedCharacterReference() {
 String input = "&#x2603;";
 Mockito.when(reader.current()).thenReturn('#');
 Mockito.when(reader.matchConsumeIgnoreCase("X")).thenReturn(false);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("amp");
 Mockito.when(reader.matchConsume(";")).thenReturn(true);

 Character charRef = tokeniser.consumeCharacterReference('m', false);
 assertEquals('&', charRef.getChars()[0]);

 Mockito.verify(reader, Mockito.times(3)).consume();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadCharacterReferenceWithInvalidAdditionalAllowedCharacter() {
 String input = "&#x2603;x";
 Mockito.when(reader.current()).thenReturn('#');
 Mockito.when(reader.matchConsumeIgnoreCase("X")).thenReturn(false);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("amp");
 Mockito.when(reader.matchConsume(";")).thenReturn(true);

 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testReadCharacterReferenceInAttribute() {
 String input = "class=\"red #FF0000\"";
 Mockito.when(reader.current()).thenReturn('#');
 Mockito.when(reader.matchConsumeIgnoreCase("X")).thenReturn(false);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("FF0000");
 Mockito.when(reader.matchConsume(";")).thenReturn(false);

 Character charRef = tokeniser.consumeCharacterReference(null, true);
 assertNull(charRef);

 Mockito.verify(reader, Mockito.times(3)).consume();
 }

 @Test
 public void testCreateTagPending() {
 Token tagPending = tokeniser.createTagPending(true);
 assertEquals(StartTag.class, tagPending.getClass());
 }

 @Test
 public void testReadStartTag() {
 String input = "<p>";
 Mockito.when(reader.current()).thenReturn('<');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("p");
 Mockito.when(reader.matchConsume('/')).thenReturn(false);
Mockito.when(reader.matchConsumeIgnoreCase(">")) .thenReturn(true);

 StartTag startTag = tokeniser.read();
 assertEquals("<p>", startTag.toString());
 assertEquals("p", startTag.tagName);

 Mockito.verify(reader, Mockito.times(5)).consume();
 }

 @Test
 public void testReadEndTag() {
 String input = "</p>";
 Mockito.when(reader.current()).thenReturn('/');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("p");
 Mockito.when(reader.matchConsume('/')).thenReturn(true);
Mockito.when(reader.matchConsumeIgnoreCase(">")) .thenReturn(true);

 EndTag endTag = tokeniser.read();
 assertEquals("</p>", endTag.toString());
 assertEquals("p", endTag.tagName);

 Mockito.verify(reader, Mockito.times(4)).consume();
 }

 @Test
 public void testReadScriptTagData() {
 String input = "<script type=\"text/javascript\">";
 Mockito.when(reader.current()).thenReturn('<');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("script");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("type");
 Mockito.when(reader.consume()).thenReturn('=').thenReturn('>');
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("text");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("/");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("javascript");

 tokeniser.transition(TokeniserState.ScriptData);
 StringBuilder dataBuffer = tokeniser.dataBuffer;
 ArgumentCaptor<StringBuilder> dataBufferCaptor = ArgumentCaptor.forClass(StringBuilder.class);

 assertTrue(tokeniser.isAppropriateEndTagToken());
 assertEquals("script", tokeniser.appropriateEndTagName());
 assertEquals(dataBuffer, dataBufferCaptor.capture());
 assertEquals(0, dataBuffer.length());
 }

 @Test
 public void testReadScriptTagDataWithScriptTagEnd() {
 String input = "<script type=\"text/javascript\">console.log('Hello World');</script>";
 Mockito.when(reader.current()).thenReturn('<');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("script");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("type");
 Mockito.when(reader.consume()).thenReturn('=').thenReturn('>');
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("text");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("/");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("javascript");

 tokeniser.transition(TokeniserState.ScriptData);
 StringBuilder dataBuffer = tokeniser.dataBuffer;
 ArgumentCaptor<StringBuilder> dataBufferCaptor = ArgumentCaptor.forClass(StringBuilder.class);

 assertTrue(tokeniser.isAppropriateEndTagToken());
 assertEquals("script", tokeniser.appropriateEndTagName());
 assertEquals(dataBuffer, dataBufferCaptor.capture());
 assertEquals("console.log('Hello World');", dataBuffer.toString());
 }

 @Test
 public void testReadScriptTagDataWithScriptTagEndAndError() {
 String input = "<script type=\"text/javascript\">console.log('Hello World');<script>";
 Mockito.when(reader.current()).thenReturn('<');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("script");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("type");
 Mockito.when(reader.consume()).thenReturn('=').thenReturn('>');
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("text");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("/");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("javascript");

 tokeniser.transition(TokeniserState.ScriptData);
 StringBuilder dataBuffer = tokeniser.dataBuffer;
 ArgumentCaptor<StringBuilder> dataBufferCaptor = ArgumentCaptor.forClass(StringBuilder.class);

 assertFalse(tokeniser.isAppropriateEndTagToken());
 assertNull(tokeniser.appropriateEndTagName());
 assertSame(dataBuffer, dataBufferCaptor.capture());
 assertEquals(0, dataBuffer.length());
 tokeniser.eofError(TokeniserState.ScriptData);
 }

 @Test
 public void testReadComment() {
 String input = "-- This is a comment";
 Mockito.when(reader.current()).thenReturn('--');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeAny('-')).thenReturn(true, false);

 Comment comment = tokeniser.read();
 assertEquals("-- This is a comment", comment.toString());

 Mockito.verify(reader, Mockito.times(3)).consume();
 }

 @Test
 public void testReadCommentWithInvalidCommentStart() {
 String input = "--<p";
 Mockito.when(reader.current()).thenReturn('--');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeAny('-')).thenReturn(false);

 assertNull(tokeniser.read());

 Mockito.verify(reader, Mockito.times(2)).consume();
 }

 @Test
 public void testReadCommentWithCommentEnd() {
 String input = "-- This is a comment -->";
 Mockito.when(reader.current()).thenReturn('--');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeAny('-')).thenReturn(true, true);

 assertNull(tokeniser.read());

 Mockito.verify(reader, Mockito.times(3)).consume();
 }

 @Test
 public void testReadDoctypePending() {
 String input = "<!doctype html>";
 Mockito.when(reader.current()).thenReturn('!');
 Mockito.when(reader.consume()).thenReturn(true);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("doctype");
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn("html");

 Doctype doctype = tokeniser.read();
 assertEquals("<!doctype html>", doctype.toString());

 Mockito.verify(reader, Mockito.times(3)).consume();
 }
}