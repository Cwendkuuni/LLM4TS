import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader reader;
 private List<ParseError> errors;

 @Before
 public void setUp() {
 errors = new ArrayList<>();
 reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Test data".getBytes())));
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadBasic() {
 // Test reading a simple text token
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("e", token.value);

 // Test reading an end tag token
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read();
 Assert.assertEquals(TokenType.EndTag, token.type);
 Assert.assertEquals("test", token.value);
 }

 @Test
 public void testReadSelfClosingTag() {
 // Test reading a self-closing tag token
 tokeniser.transition(TokeniserState.SelfClosingTag);
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals(TokenType.EndTag, token.type);
 Assert.assertEquals("img", token.value);
 Assert.assertEquals(true, token.selfClosing);
 }

 @Test
 public void testReadStartTag() {
 // Test reading a start tag token
 tokeniser.emit(new Token.EndTag("test"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 Token token = tokeniser.read();
 Assert.assertEquals(TokenType.StartTag, token.type);
 Assert.assertEquals("html", token.tagName.toLowerCase(Locale.ENGLISH));
 }

 @Test
 public void testReadCharacterReference() {
 // Test reading a character reference token
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference(null, false);
 Token token = tokeniser.read();
 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("&", token.value);
 }

 @Test
 public void testReadNamedCharacterReference() {
 // Test reading a named character reference token
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference("amp;", false);
 Token token = tokeniser.read();
 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("&amp;", token.value);
 }

 @Test
 public void testReadInvalidCharacterReference() {
 // Test reading an invalid character reference error
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference("xpto", false);
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Invalid character reference: unexpected character 'x' in input state [EndTag]", errors.get(0).getMessage());
 }

 @Test
 public void testReadSelfClosingFlagError() {
 // Test reading a self-closing flag error
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 tokeniser.emit('>');
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.error(TokeniserState.EndTag);
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Self closing flag not acknowledged", errors.get(0).getMessage());
 }

 @Test
 public void testReadInvalidEndTag() {
 // Test reading an invalid end tag error
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 tokeniser.emit('/');
 tokeniser.emit('x');
 tokeniser.emit('>');
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Attributes incorrectly present on end tag", errors.get(0).getMessage());
 }

 @Test
 public void testReadScriptData() {
 // Test reading script data and check if the appropriate end tag is emitted
 tokeniser.createTempBuffer();
 tokeniser.emit('<');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r'); tokeniser.emit('i'); tokeniser.emit('p'); tokeniser.emit('t');
 tokeniser.emit('>');
 tokeniser.emit('S'); tokeniser.emit('c'); tokeniser.emit('r'); tokeniser.emit('i'); tokeniser.emit('p'); tokeniser.emit('t');
 tokeniser.emit('>');
 String scriptData = tokeniser.dataBuffer.toString();
 Assert.assertEquals("script", tokeniser.appropriateEndTagName());
 Assert.assertEquals("<script>\nScrip\n</script>", scriptData);
 }

 @Test
 public void testReadMalformedScriptData() {
 // Test reading malformed script data and check if errors are reported correctly
 tokeniser.createTempBuffer();
 tokeniser.emit('<'); tokeniser.emit('s'); tokeniser.emit('c'); tokeniser.emit('r'); tokeniser.emit('i'); tokeniser.emit('p'); tokeniser.emit('t');
 tokeniser.emit('>');
 tokeniser.emit('S'); tokeniser.emit('c'); tokeniser.emit('r'); tokeniser.emit('i'); tokeniser.emit('p'); tokeniser.emit('/'); tokeniser.emit('s'); tokeniser.emit('c'); tokeniser.emit('r'); tokeniser.emit('i'); tokeniser.emit('p'); tokeniser.emit('t'); tokeniser.emit('/'); tokeniser.emit('>');
 Assert.assertEquals(3, errors.size());
 Assert.assertEquals("Unexpected character 's' in input state [ScriptData]", errors.get(0).getMessage());
 Assert.assertEquals("Unexpected character 's' in input state [ScriptData]", errors.get(1).getMessage());
 Assert.assertEquals("Missing semicolon", errors.get(2).getMessage());
 }

 @Test
 public void testReadEof() {
 // Test reaching the end of the input stream
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.read();
 tokeniser.eofError(TokeniserState.Data);
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
 }
}