import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader mockReader;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 mockReader = new CharacterReader(); // for testing, let's assume it returns hardcoded strings
 tokeniser = new Tokeniser(mockReader);
 }

 @Test
 public void testReadCharacter() {
 mockReader.setCurrent('a');
 Token token = tokeniser.read();
 assertEquals("Character", token.type, Token.TokenType.Character);
 assertEquals("a", token.data, "Expected character 'a'");
 }

 @Test
 public void testReadStartTag() {
 mockReader.setCurrent('<', 't', 'a', 'g');
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 Token tag = tokeniser.emitPending();
 assertEquals("Tag", token.type, Token.TokenType.StartTag);
 assertEquals("tag", tag.tagName, "Expected tag name 'tag'");
 }

 @Test
 public void testReadEndTag() {
 mockReader.setCurrent('<', '/', 't', 'a', 'g');
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read();
 Token tag = tokeniser.emitPending();
 assertEquals("EndTag", token.type, Token.TokenType.EndTag);
 assertEquals("tag", tag.tagName, "Expected tag name 'tag'");
 }

 @Test
 public void testReadSelfClosingTag() {
 mockReader.setCurrent('<', 'i', 'm', 'g', '/');
 tokeniser.transition(TokeniserState.SelfClosingTag);
 tokeniser.read();
 Token tag = tokeniser.emitPending();
 assertEquals("EndTag", token.type, Token.TokenType.EndTag);
 assertEquals("img", tag.tagName, "Expected tag name 'img'");
 }

 @Test
 public void testReadComment() {
 mockReader.setCurrent('<', '/', 'c', 'o', 'm', 'm', 'e', 'n', 't');
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read();
 tokeniser.read(); // read '--'
 Token comment = tokeniser.emitPending();
 assertEquals("Comment", token.type, Token.TokenType.Comment);
 assertEquals("", comment.data, "Expected empty comment");
 }

 @Test
 public void testReadCharacterReference() {
 mockReader.setCurrent('&', '#', 'x', 'A');
 tokeniser.read();
 tokeniser.read(); // read ';'
 Character charRef = tokeniser.consumeCharacterReference(null, false);
 assertEquals(Character.toUpperCase('A'), charRef.intValue(), "Expected character 'A'");
 }

 @Test
 public void testReadCharacterReferenceWithAdditionalAllowedCharacter() {
 mockReader.setCurrent('&', '#', 'x', 'A', '&');
 tokeniser.read(); // '&'
 tokeniser.read(); // '#'
 tokeniser.read(); // 'x'
 tokeniser.consumeCharacter(); // 'A'
 tokeniser.consumeCharacter(); // '&'
 Character charRef = tokeniser.consumeCharacterReference('&', false);
 assertNull("Expected no character reference", charRef);
 }

 @Test
 public void testReadCharacterReferenceWithName() {
 mockReader.setCurrent('&', 'n', 'a', 'm', 'e');
 tokeniser.read(); // '&'
 tokeniser.read(); // 'n'
 tokeniser.read(); // 'a'
 tokeniser.read(); // 'm'
 tokeniser.read(); // 'e'
 tokeniser.read(); // ';'
 Character charRef = tokeniser.consumeCharacterReference(null, false);
 assertEquals(Character.forInteger(33), charRef.intValue(), "Expected character '!'");
 }

 @Test
 public void testReadCharacterReferenceWithNameInAttribute() {
 mockReader.setCurrent('<', 's', 'c', 'r', 'i', 'p', 't', ' ', 't', 'a', 'g', ' ', 'a', ' ', '=', ' ', '&', 'n', 'a', 'm', 'e');
 tokeniser.read(); // 's'
 tokeniser.read(); // 'c'
 tokeniser.read(); // 'r'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'p'
 tokeniser.read(); // 't'
 tokeniser.read(); // ' '
 tokeniser.read(); // 'a'
 tokeniser.read(); // ' '
 tokeniser.read(); // '='
 tokeniser.read(); // ' '
 tokeniser.read(); // '&'
 tokeniser.read(); // 'n'
 tokeniser.read(); // 'a'
 tokeniser.read(); // 'm'
 tokeniser.read(); // 'e'
 tokeniser.read(); // ';'
 Character charRef = tokeniser.consumeCharacterReference(null, true);
 assertNull("Expected no character reference in attribute", charRef);
 }

 @Test
 public void testReadInvalidCharacterReference() {
 mockReader.setCurrent('&', '#', 'x', 'F', 'F', 'F'); // Unicode point FFFE is not a valid character
 tokeniser.read(); // '&'
 tokeniser.read(); // '#'
 tokeniser.read(); // 'x'
 tokeniser.read(); // 'F'
 tokeniser.read(); // 'F'
 tokeniser.read(); // 'F'
 Character charRef = tokeniser.consumeCharacterReference(null, false);
 assertEquals(Tokeniser.replacementChar, charRef.intValue(), "Expected replacement character '�'");
 }

 @Test
 public void testReadInvalidCharacterReferenceWithName() {
 mockReader.setCurrent('&', '#', 'x', 'F', 'F', 'F', ';'); // Unicode point FFFE is not a valid character
 tokeniser.read(); // '&'
 tokeniser.read(); // '#'
 tokeniser.read(); // 'x'
 tokeniser.read(); // 'F'
 tokeniser.read(); // 'F'
 tokeniser.read(); // 'F'
 tokeniser.read(); // ';'
 Character charRef = tokeniser.consumeCharacterReference(null, false);
 assertEquals(Tokeniser.replacementChar, charRef.intValue(), "Expected replacement character '�'");
 }

 @Test
 public void testReadInvalidCharacterReferenceWithNameInAttribute() {
 mockReader.setCurrent('<', 'i', 'm', 'g', ' ', 's', 'r', 'i', 'p', 't', ' ', 't', 'a', 'g', ' ', '=', ' ', '&', 'x', 'F', 'F', 'F', ';'); // Unicode point FFFE is not a valid character
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'm'
 tokeniser.read(); // 'g'
 tokeniser.read(); // ' '
 tokeniser.read(); // 's'
 tokeniser.read(); // 'r'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'p'
 tokeniser.read(); // 't'
 tokeniser.read(); // ' '
 tokeniser.read(); // 't'
 tokeniser.read(); // 'a'
 tokeniser.read(); // 'g'
 tokeniser.read(); // ' '
 tokeniser.read(); // '='
 tokeniser.read(); // ' '
 tokeniser.read(); // '&'
 tokeniser.read(); // 'x'
 tokeniser.read(); // 'F'
 tokeniser.read(); // 'F'
 tokeniser.read(); // 'F'
 tokeniser.read(); // ';'
 Character charRef = tokeniser.consumeCharacterReference(null, true);
 assertEquals(Tokeniser.replacementChar, charRef.intValue(), "Expected replacement character '�'");
 }

 @Test
 public void testReadInvalidCharacterReferenceWithHexCode() {
 mockReader.setCurrent('&', '#', 'x', 'X', '3', 'B', '0', '7', ';'); // Unicode point F0F0 is not a valid character
 tokeniser.read(); // '&'
 tokeniser.read(); // '#'
 tokeniser.read(); // 'x'
 tokeniser.read(); // 'X'
 tokeniser.read(); // '3'
 tokeniser.read(); // 'B'
 tokeniser.read(); // '0'
 tokeniser.read(); // '7'
 tokeniser.read(); // ';'
 Character charRef = tokeniser.consumeCharacterReference(null, false);
 assertEquals(Tokeniser.replacementChar, charRef.intValue(), "Expected replacement character '�'");
 }

 @Test
 public void testReadInvalidCharacterReferenceWithHexCodeInAttribute() {
 mockReader.setCurrent('<', 'i', 'm', 'g', ' ', 's', 'r', 'i', 'p', 't', ' ', 't', 'a', 'g', ' ', '=', ' ', '&', 'x', 'X', '3', 'B', '0', '7', ';'); // Unicode point F0F0 is not a valid character
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'm'
 tokeniser.read(); // 'g'
 tokeniser.read(); // ' '
 tokeniser.read(); // 's'
 tokeniser.read(); // 'r'
 tokeniser.read(); // 'i'
 tokeniser.read(); // 'p'
 tokeniser.read(); // 't'
 tokeniser.read(); // ' '
 tokeniser.read(); // 't'
 tokeniser.read(); // 'a'
 tokeniser.read(); // 'g'
 tokeniser.read(); // ' '
 tokeniser.read(); // '='
 tokeniser.read(); // ' '
 tokeniser.read(); // '&'
 tokeniser.read(); // 'x'
 tokeniser.read(); // 'X'
 tokeniser.read(); // '3'
 tokeniser.read(); // 'B'
 tokeniser.read(); // '0'
 tokeniser.read(); // '7'
 tokeniser.read(); // ';'
 Character charRef = tokeniser.consumeCharacterReference(null, true);
 assertEquals(Tokeniser.replacementChar, charRef.intValue(), "Expected replacement character '�'");
 }

 @Test
 public void testReadEOF() {
 mockReader.setCurrent('&', '#', 'x', 'A', ';'); // Consume character reference to advance reader
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.read(); // ';'
 tokeniser.eofError(TokeniserState.Data);
 List<ParseError> errors = tokeniser.getErrors();
 assertEquals(1, errors.size(), "Expected one error");
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
 }

 @Test
 public void testReadUnexpectedCharacter() {
 mockReader.setCurrent('&');
 tokeniser.error(TokeniserState.Data);
 List<ParseError> errors = tokeniser.getErrors();
 assertEquals(1, errors.size(), "Expected one error");
 assertEquals("Unexpected character in input", errors.get(0).getMessage());
 }

 @Test
 public void testReadInvalidCharacter() {
 mockReader.setCurrent('&', '#', 'x', 'F'); // Unicode point FF is not a valid character
 tokeniser.read(); // '&'
 tokeniser.read(); // '#'
 tokeniser.read(); // 'x'
 tokeniser.read(); // 'F'
 tokeniser.error(TokeniserState.Data);
 List<ParseError> errors = tokeniser.getErrors();
 assertEquals(1, errors.size(), "Expected one error");
 assertEquals("Invalid character reference", errors.get(0).getMessage());
 }

}