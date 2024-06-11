import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader reader;
 private ParseErrorList errors;
 private InputStream inputStream;

 @Before
 public void setUp() {
 reader = Mockito.mock(CharacterReader.class);
 errors = new ParseErrorList();
 inputStream = new ByteArrayInputStream("<html><head><title>Test</title><script type='text/javascript'>//<![CDATA[ var x = 'hello world'; //]]></script></head><body><p>Hello World!</p></body>".getBytes());
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() throws IOException {
 char expectedChar = 'H';
 char actualChar = tokeniser.read().asCharacter();
 assertEquals(expectedChar, actualChar);
 }

 @Test
 public void testReadTag() throws IOException {
 String expectedTagName = "html";
 Token expectedTag = new Token.StartTag("html");
 Token actualTag = tokeniser.read();
 assertEquals(expectedTagName, expectedTag.tagName);
 assertEquals(expectedTag, actualTag);
 }

 @Test(expected = IllegalStateException.class)
 public void testReadSelfClosingTag() throws IOException {
 String expectedTagName = "img";
 Token expectedTag = new Token.StartTag("img");
 expectedTag.setSelfClosing(true);
 Token actualTag = tokeniser.read();
 assertEquals(expectedTagName, expectedTag.tagName);
 assertEquals(expectedTag, actualTag);
 tokeniser.emit(expectedTag); // acknowledge self-closing flag
 }

 @Test
 public void testReadEndTag() throws IOException {
 String expectedTagName = "title";
 Token expectedTag = new Token.EndTag("title");
 Token actualTag = tokeniser.read();
 assertEquals(expectedTagName, expectedTag.tagName);
 assertEquals(expectedTag, actualTag);
 }

 @Test
 public void testReadString() throws IOException {
 String expectedString = "Test";
 String actualString = "";
 StringBuilder charBuffer = tokeniser.charBuffer;
 tokeniser.emit("Test");
 tokeniser.read();
 while (tokeniser.charBuffer.length() > 0) {
 actualString += tokeniser.read().asCharacter();
 }
 assertEquals(expectedString, actualString);
 charBuffer.setLength(0);
 }

 @Test
 public void testErrorHandling() throws IOException {
 InputStream illegalInput = new ByteArrayInputStream("<html><script type='text/javascript'>//<![CDATA[ var x = 'hello world'; //<!invalid-><script>");
 tokeniser = new Tokeniser(new InputStreamReader(illegalInput), errors);
 tokeniser.read(); // read the invalid character
 assertFalse(tokeniser.getState() == TokeniserState.Data); // check that the invalid character caused a state change
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpected character '!' in input state [Data]", error.getMessage());
 }

 @Test
 public void testEOF() throws IOException {
 InputStream eofInput = new ByteArrayInputStream("".getBytes());
 tokeniser = new Tokeniser(new InputStreamReader(eofInput), errors);
 assertTrue(tokeniser.read() == null);
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
 }
}