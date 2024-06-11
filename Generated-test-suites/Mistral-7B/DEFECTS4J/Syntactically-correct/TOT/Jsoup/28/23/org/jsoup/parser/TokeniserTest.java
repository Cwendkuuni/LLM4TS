import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadStartTag() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();

 Token expectedToken = new Token.StartTag("test");
 Token actualToken = tokeniser.read();

 assertEquals("Unexpected token type", expectedToken.type, actualToken.type);
 assertEquals("Unexpected tag name", expectedToken.tagName, actualToken.tagName);
 }

 @Test
 public void testReadEndTag() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.createEndTag();
 tokeniser.emitCommentPending();
 tokeniser.emitEndTagPending();

 Token expectedEndTag = new Token.EndTag("test");
 Token actualEndTag = tokeniser.read();

 assertEquals("Unexpected end tag name", expectedEndTag.tagName, actualEndTag.tagName);
 }

 @Test
 public void testReadCharacter() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("t");
 tokeniser.emit("e");
 tokeniser.emit("st");

 String expectedCharacter = "test";
 String actualCharacter = tokeniser.read().getText();

 assertEquals("Unexpected character", expectedCharacter, actualCharacter);
 }

 @Test
 public void testReadEmpty() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(" ");
 tokeniser.emit("\n");
 tokeniser.emit("\r");
 tokeniser.emit("\f");
 tokeniser.emit("\t");

 List<Token> expectedTokens = new ArrayList<>();
 expectedTokens.add(new Token.Character(" "));
 expectedTokens.add(new Token.Character("\n"));
 expectedTokens.add(new Token.Character("\r"));
 expectedTokens.add(new Token.Character("\f"));
 expectedTokens.add(new Token.Character("\t"));

 List<Token> actualTokens = new ArrayList<>();
 while (tokeniser.read() != null) {
 actualTokens.add(tokeniser.getPreviousToken());
 }

 assertEquals("Unexpected number of tokens", expectedTokens.size(), actualTokens.size());
 for (int i = 0; i < expectedTokens.size(); i++) {
 Token expectedToken = expectedTokens.get(i);
 Token actualToken = actualTokens.get(i);
 assertEquals("Unexpected character", expectedToken.getText(), actualToken.getText());
 assertEquals("Unexpected token type", expectedToken.type, actualToken.type);
 }
 }

 @Test
 public void testReadError() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.createEndTag();
 tokeniser.emitCommentPending();
 tokeniser.emitEndTagPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);

 assertEquals("Unexpected number of errors", 1, errors.size());
 assertEquals("Unexpected error message", "Unexpected character 'E' in input state [DATA]", errors.get(0).getMessage());
 }

 @Test
 public void testReadEOF() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.createEndTag();
 tokeniser.emitCommentPending();
 tokeniser.emitEndTagPending();
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.createEndTag();
 tokeniser.emitCommentPending();
 tokeniser.emitEndTagPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.EOF);
 tokeniser.error(TokeniserState.EOF);
 tokeniser.acknowledgeSelfClosingFlag();

 assertEquals("Unexpected number of errors", 2, errors.size());
 assertSame("Unexpected error type", errors.get(0).getErrorType(), ParseError.Type.UNEXPECTED_CHARACTER);
 assertSame("Unexpected error type", errors.get(1).getErrorType(), ParseError.Type.UNEXPECTEDLY_REACHED_EOF);
 }
}