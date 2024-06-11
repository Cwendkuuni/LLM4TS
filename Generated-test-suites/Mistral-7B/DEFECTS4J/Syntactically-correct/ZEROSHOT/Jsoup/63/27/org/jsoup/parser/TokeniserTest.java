import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
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
 public void testRead_SelfClosingFlagNotAcknowledged() {
 tokeniser.transition(TokeniserState.SelfClosingFlag);
 tokeniser.read();
 Mockito.verify(reader).error("Self closing flag not acknowledged");
 }

 @Test
 public void testRead_EmptyInput() {
 Mockito.when(reader.isEmpty()).thenReturn(true);
 assertEquals(Token.TokenType.Eof, tokeniser.read().type);
 }

 @Test
 public void testRead_Whitespace() {
 Mockito.when(reader.current()).thenReturn(' ');
 Mockito.when(reader.matchConsume(' ')).thenReturn(true);
 assertEquals(Token.TokenType.Whitespace, tokeniser.read().type);
 }

 @Test
 public void testRead_LessThan() {
 Mockito.when(reader.current()).thenReturn('<');
 Mockito.when(reader.matchConsume('<')).thenReturn(true);
 assertEquals(Token.TokenType.StartTagBegin, tokeniser.read().type);
 }

 @Test
 public void testRead_LessThanWithName() {
 String tagName = "tag";
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn(tagName);
 Mockito.when(reader.matches('>')).thenReturn(true);
 tokeniser.createTagPending(true);
 tokeniser.read();
 tokeniser.emitTagPending();
 assertEquals(tagName.toLowerCase(), tokeniser.appropriateEndTagName());
 }

 @Test
 public void testRead_LessThanWithSelfClosingFlag() {
 String tagName = "tag";
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn(tagName);
 Mockito.when(reader.matches('/')).thenReturn(true);
 Mockito.when(reader.matches('>')).thenReturn(true);
 tokeniser.transition(TokeniserState.SelfClosingFlag);
 tokeniser.read();
 tokeniser.emitTagPending();
 assertEquals(tagName.toLowerCase(), tokeniser.appropriateEndTagName());
 }

 @Test
 public void testRead_LessThanWithAttributes() {
 String tagName = "tag";
 String attributeName = "attr";
 String attributeValue = "value";
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn(tagName);
 Mockito.when(reader.matches(' ')).thenReturn(true);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn(attributeName);
 Mockito.when(reader.consume('=')).thenReturn(true);
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn(attributeValue);
 Mockito.when(reader.matches('>')).thenReturn(true);
 tokeniser.createTagPending(true);
 tokeniser.read();
 tokeniser.emitTagPending();
 Token tagToken = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, tagToken.type);
 Token attrToken = ((Token.StartTag) tagToken).attributes().get(0);
 assertEquals(attributeName, attrToken.name());
 assertEquals(attributeValue, attrToken.data());
 }

 @Test
 public void testRead_EndTag() {
 String tagName = "tag";
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn(tagName);
 Mockito.when(reader.matches('/')).thenReturn(true);
 Mockito.when(reader.matches('>')).thenReturn(true);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 assertNull(tokeniser.appropriateEndTagName());
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.read();
 assertEquals(tagName.toLowerCase(), tokeniser.appropriateEndTagName());
 }

 @Test
 public void testRead_Comment() {
 String commentText = "--comment";
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn(commentText);
 Mockito.when(reader.matches('-->')).thenReturn(true);
 tokeniser.createCommentPending();
 tokeniser.read();
 tokeniser.emitCommentPending();
 assertEquals(commentText, ((Token.Comment) tokeniser.read().data()).data());
 }

 @Test
 public void testRead_ScriptData() {
 String scriptData = "<script>data</script>";
 InputStream inputStream = new ByteArrayInputStream(scriptData.getBytes());
 Mockito.when(reader.inputStream()).thenReturn(inputStream);
 tokeniser.read();
 tokeniser.emit(tokeniser.read()); // consume script start tag
 assertEquals(Token.TokenType.Text, tokeniser.read().type);
 assertEquals("data", tokeniser.read().data());
 assertEquals(Token.TokenType.EndTag, tokeniser.read().type);
 assertEquals("script", tokeniser.appropriateEndTagName());
 }

 @Test
 public void testRead_Text() {
 String text = "text";
 Mockito.when(reader.consumeLetterThenDigitSequence()).thenReturn(text);
 Mockito.when(reader.isEmpty()).thenReturn(false);
 for (int i = 0; i < text.length(); i++) {
 tokeniser.emit(' ');
 }
 assertEquals(Token.TokenType.Text, tokeniser.read().type);
 assertEquals(text, tokeniser.read().data());
 }

 @Test
 public void testRead_Entities() {
 String text = "&amp;&lt;&gt;&#x27;";
 tokeniser.transition(TokeniserState.Data);
 for (int i = 0; i < text.length(); i++) {
 tokeniser.emit(text.charAt(i));
 }
 assertEquals(Token.TokenType.Text, tokeniser.read().type);
 assertEquals("&amp;&lt;&gt;&#x27;", tokeniser.read().data());
 }

 @Test
 public void testRead_CharacterReference() {
 String text = "&A1;&#x00A9;&#x00E5;";
 tokeniser.transition(TokeniserState.Data);
 for (int i = 0; i < text.length(); i++) {
 tokeniser.emit(text.charAt(i));
 }
 int[] codePoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(3, codePoints.length);
 assertEquals(961, codePoints[0]);
 assertEquals(169, codePoints[1]);
 assertEquals(229, codePoints[2]);
 }

 @Test
 public void testRead_CharacterReferenceInAttribute() {
 String text = "<tag attr='&foo;'/>";
 tokeniser.transition(TokeniserState.Data);
 for (int i = 0; i < text.length(); i++) {
 tokeniser.emit(text.charAt(i));
 }
 assertNull(tokeniser.read().attributes());
 tokeniser.transition(TokeniserState.AttributeName);
 for (int i = 0; i < text.length(); i++) {
 tokeniser.emit(text.charAt(i));
 }
 tokeniser.emit('=');
 for (int i = 0; i < text.length(); i++) {
 tokeniser.emit(text.charAt(i));
 }
 tokeniser.emit('>');
 tokeniser.transition(TokeniserState.Data);
 assertEquals(1, tokeniser.read().attributes().size());
 assertEquals("foo", tokeniser.read().attributes().get(0).name());
 assertNull(tokeniser.read().attributes().get(0).data());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_UnexpectedEndOfStream() {
 Mockito.when(reader.isEmpty()).thenReturn(true);
 tokeniser.read();
 tokeniser.read();
 }

 @Test
 public void testRead_Error() {
 String errorMessage = "Error message";
 Mockito.when(reader.pos()).thenReturn(1);
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, errors.size());
 assertEquals(errorMessage, errors.get(0).message());
 }

}