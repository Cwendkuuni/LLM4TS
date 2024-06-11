import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private List<Token> expectedTokens;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream("some html text".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 expectedTokens = new ArrayList<>();
 }

 @Test
 public void testReadCharacter() {
 char expected = 'a';
 tokeniser.emit('a');
 tokeniser.read();
 assertEquals(new Token.Character(Character.toString(expected)), tokeniser.read());
 }

 @Test
 public void testReadStartTag() {
 String tagName = "div";
 tokeniser.createTagPending(true);
 tokeniser.read(); // '<'
 tokeniser.read(); // 'd'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'v'
 tokeniser.read(); // '>'
 Token startTag = tokeniser.emitPending();
 assertEquals(tagName, startTag.tagName);
 assertEquals(Token.TagType.StartTag, startTag.type);
 }

 @Test
 public void testReadEndTag() {
 String tagName = "div";
 tokeniser.createTagPending(false);
 tokeniser.read(); // '<'
 tokeniser.read(); // '/'
 tokeniser.read(); // 'd'
 tokeniser.read(); // '>'
 Token endTag = tokeniser.emitPending();
 assertEquals(tagName, endTag.tagName);
 assertEquals(Token.TagType.EndTag, endTag.type);
 }

 @Test
 public void testReadString() {
 String expected = "some text";
 tokeniser.emit('s');
 tokeniser.emit('o');
 tokeniser.emit('m');
 tokeniser.emit('e');
 tokeniser.emit(' ');
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('x');
 tokeniser.emit('t');
 String actual = tokeniser.readString();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeCharacterReference() {
 Character expected = '&';
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(expected, tokeniser.read().tokenValue);
 }

 @Test
 public void testConsumeCharacterReferenceNamed() {
 String expected = "amp";
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.read(); // '&'
 tokeniser.read(); // 'a'
 tokeniser.read(); // 'm'
 tokeniser.read(); // 'p'
 assertEquals(expected, tokeniser.readString());
 }

 @Test(expected = ParseError.class)
 public void testInvalidCharacterReference() {
 tokeniser.consumeCharacterReference('x', false);
 }

 @Test(expected = ParseError.class)
 public void testInvalidNamedCharacterReference() {
 tokeniser.consumeCharacterReference("invalid", false);
 }

 @Test
 public void testErrorHandling() {
 tokeniser.error("Unexpected character '%s' in input state [DATA]", '>');
 assertEquals(1, errors.size());
 }

 @Test
 public void testReadEOF() {
 tokeniser.eofError("Unexpectedly reached end of file (EOF) in input state [DATA]");
 assertEquals(1, errors.size());
 }

 @Test
 public void testReadAllTokens() throws IOException {
 String html = "<div><p>Hello <world</p></div>";
 expectedTokens.add(new Token.StartTag("div"));
 expectedTokens.add(new Token.Character("<"));
 expectedTokens.add(new Token.Character("d"));
 expectedTokens.add(new Token.Character("i"));
 expectedTokens.add(new Token.Character("v"));
 expectedTokens.add(new Token.StartTag("p"));
 expectedTokens.add(new Token.Character("H"));
 expectedTokens.add(new Token.Character("e"));
 expectedTokens.add(new Token.Character("l"));
 expectedTokens.add(new Token.Character("l"));
 expectedTokens.add(new Token.Character("o"));
 expectedTokens.add(new Token.Character(" "));
 expectedTokens.add(new Token.Character("<"));
 expectedTokens.add(new Token.Character("w"));
 expectedTokens.add(new Token.Character("o"));
 expectedTokens.add(new Token.Character("r"));
 expectedTokens.add(new Token.Character("l"));
 expectedTokens.add(new Token.Character("d"));
 expectedTokens.add(new Token.EndTag("p"));
 expectedTokens.add(new Token.EndTag("div"));
 expectedTokens.add(new Token.Character(">"));
 expectedTokens.add(null); // EOF

 List<Token> actualTokens = new ArrayList<>();
 while (tokeniser.read() != null) {
 actualTokens.add(tokeniser.read());
 }
 assertEquals(expectedTokens.size(), actualTokens.size());
 for (int i = 0; i < expectedTokens.size(); i++) {
 Token expected = expectedTokens.get(i);
 Token actual = actualTokens.get(i);
 assertEquals(expected.tagName, actual.tagName);
 assertEquals(expected.type, actual.type);
 assertEquals(expected.tokenValue, actual.tokenValue);
 }
 }

 @Test
 public void testSelfClosingFlag() {
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.createTagPending(true);
 tokeniser.read(); // '<'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'm'
 tokeniser.read(); // 'g'
 tokeniser.read(); // '/'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'm'
 tokeniser.read(); // 'g'
 tokeniser.read(); // '>'
 Token tag = tokeniser.emitPending();
 assertTrue(tag.selfClosing);
 }

 @Test
 public void testAppropriateEndTag() {
 String tagName = "div";
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.createTagPending(true);
 tokeniser.read(); // '<'
 tokeniser.read(); // 'd'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'v'
 tokeniser.read(); // '>'
 tokeniser.createTagPending(true);
 tokeniser.read(); // '<'
 tokeniser.read(); // '/'
 tokeniser.read(); // 'd'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'v'
 tokeniser.read(); // '>'
 tokeniser.read(); // '<'
 tokeniser.read(); // '/'
 tokeniser.read(); // 'p'
 tokeniser.read(); // '>'
 assertTrue(tokeniser.isAppropriateEndTagToken());
 assertEquals(tagName, tokeniser.appropriateEndTagName());
 }

 @Test
 public void testInappropriateEndTag() {
 String tagName1 = "div";
 String tagName2 = "p";
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.createTagPending(true);
 tokeniser.read(); // '<'
 tokeniser.read(); // 'd'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'v'
 tokeniser.read(); // '>'
 tokeniser.createTagPending(true);
 tokeniser.read(); // '<'
 tokeniser.read(); // '/'
 tokeniser.read(); // 'p'
 tokeniser.read(); // '>'
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }
}