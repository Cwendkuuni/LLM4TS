import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 errors = new ParseErrorList();
 reader = Mockito.mock(CharacterReader.class);
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadStartTag() throws IOException {
 reader = new CharacterReader(new ByteArrayInputStream("<html>".getBytes()));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 tokeniser.read();
 Token startTag = tokeniser.emitPending();
 Assert.assertEquals(Token.TagType.StartTag, startTag.type);
 Assert.assertEquals("html", startTag.tagName);
 }

 @Test
 public void testReadEndTag() throws IOException {
 reader = new CharacterReader(new ByteArrayInputStream("<html><body></body></html>".getBytes()));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 tokeniser.read(); // <html>
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.consume('h');
 tokeniser.consume('1');
 tokeniser.consume('>'); // <h1>
 tokeniser.transition(TokeniserState.Content);
 tokeniser.read(); // <body>
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.advanceTransition(TokeniserState.EndTagName);
 tokeniser.consume('b');
 tokeniser.consume('o');
 tokeniser.consume('d');
 tokeniser.consume('y');
 tokeniser.consume('>'); // </body>
 Token endTag = tokeniser.emitPending();
 Assert.assertEquals(Token.TagType.EndTag, endTag.type);
 Assert.assertEquals("body", endTag.tagName);
 }

 @Test
 public void testReadCharacter() throws IOException {
 reader = new CharacterReader(new ByteArrayInputStream("abcdef".getBytes()));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('a');
 tokeniser.emit('b');
 tokeniser.emit('c');
 tokeniser.emit('d');
 tokeniser.emit('e');
 tokeniser.emit('f');
 Token charToken = tokeniser.emitPending();
 List<Character> expectedCharList = new ArrayList<>();
 expectedCharList.add('a');
 expectedCharList.add('b');
 expectedCharList.add('c');
 expectedCharList.add('d');
 expectedCharList.add('e');
 expectedCharList.add('f');
 Assert.assertEquals(Token.CharacterType.CHARACTERS, charToken.type);
 Assert.assertEquals(expectedCharList.toString(), charToken.content.toString());
 }

 @Test
 public void testErrorHandling() throws IOException {
 reader = new CharacterReader(new ByteArrayInputStream("</body>".getBytes()));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.EndTag);
 tokeniser.advanceTransition(TokeniserState.EndTagName);
 tokeniser.consume('b');
 tokeniser.consume('o');
 tokeniser.consume('d');
 tokeniser.consume('y');
 // error should have been thrown before this point
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unexpected character 'y' in input state [END_TAG]", errors.get(0).getMessage());
 }

 @Test
 public void testCharacterReference() throws IOException {
 reader = new CharacterReader(new ByteArrayInputStream("&aacute;".getBytes()));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.consume('&');
 tokeniser.consume('a');
 tokeniser.consume('a');
 tokeniser.consume('c');tokeniser.consume('u'); tokeniser.consume('t'); tokeniser.consume('e'); tokeniser.consume(';');
 Token charRefToken = tokeniser.emitPending();
 Assert.assertEquals(Token.CharacterType.CHAR_REF, charRefToken.type);
 Assert.assertEquals('\u00E1', charRefToken.content.charAt(0));
 }

 @Test
 public void testCharacterReferenceInvalid() throws IOException {
 reader = new CharacterReader(new ByteArrayInputStream("&foo;".getBytes()));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.consume('&');
 tokeniser.consume('f'); tokeniser.consume('o'); tokeniser.consume('o'); tokeniser.consume(';');
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Invalid character reference: invalid named referenece 'foo'", errors.get(0).getMessage());
 }

 @Test
 public void testSelfClosingFlag() throws IOException {
 reader = new CharacterReader(new ByteArrayInputStream("<img src=\"image.png\" />".getBytes()));
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 tokeniser.read(); // <img
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.consume('i'); tokeniser.consume('m'); tokeniser.consume('g');
 tokeniser.consumeSpace();
 tokeniser.transition(TokeniserState.Attributes);
 tokeniser.consume('s'); tokeniser.consume('r'); tokeniser.consume('c'); tokeniser.consume('='); tokeniser.consume('\"'); tokeniser.consume('image.png'); tokeniser.consume('\"');
 tokeniser.consumeSpace(); tokeniser.consume('/'); tokeniser.consume('>'); // <img src="image.png" />
 Assert.assertFalse(tokeniser.selfClosingFlagAcknowledged);
 }
}