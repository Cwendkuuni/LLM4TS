import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList errorList;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 characterReader = new CharacterReader("test.html");
 errorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, errorList);
 }

 @Test
 public void testRead() {
 Token token;

 // Test reading characters
 for (int i = 0; i < 10; i++) {
 token = tokeniser.read();
 assertEquals("Character", token.getType(), Token.TokenType.Character);
 assertEquals(Character.isWhitespace((char) token.data.charAt(0)), i < 5, "Unexpected character read: " + token.data);
 }

 // Test reading start tag
 token = tokeniser.read();
 assertEquals("StartTag", token.getType(), Token.TokenType.StartTag);
 assertEquals("<html", token.data, "Unexpected start tag: " + token.data);

 // Test reading end tag
 token = tokeniser.read();
 assertEquals("EndTag", token.getType(), Token.TokenType.EndTag);
 assertEquals("html", token.data, "Unexpected end tag: " + token.data);

 // Test reading comment
 tokeniser.emit(new StartTag("!--", null, null));
 token = tokeniser.read();
 assertEquals("Comment", token.getType(), Token.TokenType.Comment);
 assertEquals("<!--", token.data, "Unexpected comment: " + token.data);
 tokeniser.emit(new EndTag("--"));

 // Test reading doctype
 tokeniser.emit(new StartTag("!DOCTYPE", null, null));
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.consumeCharacterReference('P', true);
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.consumeCharacterReference('I', true);
 tokeniser.consumeCharacterReference('G', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('P', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('E', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('H', true);
 tokeniser.consumeCharacterReference('T', true);
 tokeniser.consumeCharacterReference('M', true);
 tokeniser.consumeCharacterReference('L', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('5', true);
 tokeniser.consumeCharacterReference('.', true);
 tokeniser.consumeCharacterReference('_', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('I', true);
 tokeniser.consumeCharacterReference('C', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('D', true);
 tokeniser.consumeCharacterReference('E', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('S', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('S', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference(' ', true);
 tokeniser.consumeCharacterReference('>', true);
 token = tokeniser.read();
 assertEquals("Doctype", token.getType(), Token.TokenType.Doctype);
 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" ", token.data, "Unexpected doctype: " + token.data);

 // Test reading character reference
 tokeniser.transition(TokeniserState.CharacterReference);
 token = tokeniser.read();
 assertEquals("CharacterReference", token.getType(), Token.TokenType.CharacterReference);
 assertEquals("&amp;", token.data, "Unexpected character reference: " + token.data);
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading end tag with attributes
 tokeniser.emit(newStartTag("div", null, null));
 tokeniser.emitAttribute("id", "test");
 tokeniser.emitAttribute("class", "test-class");
 tokeniser.emit(new EndTag("div"));
 token = tokeniser.read();
 assertEquals("EndTag", token.getType(), Token.TokenType.EndTag);
 assertEquals("div", token.data, "Unexpected end tag: " + token.data);
 List<Attribute> attributes = token.attributes;
 assertEquals(2, attributes.size());
 assertEquals("id", attributes.get(0).getName());
 assertEquals("test", attributes.get(0).getValue());
 assertEquals("class", attributes.get(1).getName());
 assertEquals("test-class", attributes.get(1).getValue());
 }

 @Test
 public void testErrorHandling() {
 tokeniser = new Tokeniser(new CharacterReader(""), errorList);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.error(TokeniserState.CharacterReference);
 assertTrue(errorList.size() > 0);
 }

 @Test
 public void testEOFHandling() {
 characterReader = new CharacterReader("");
 tokeniser = new Tokeniser(characterReader, errorList);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);
 assertTrue(errorList.size() > 0);
 }
}