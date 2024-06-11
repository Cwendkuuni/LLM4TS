import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Tokeniser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = Mockito.mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() throws IOException {
 String input = "hello world";
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
 reader.setInput(inputStream);
 reader.reset();

 for (int i = 0; i < input.length(); i++) {
 Token token = tokeniser.read();
 Character expected = new Character(Character.toString(input.charAt(i)));
 assertEquals(expected, token);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testReadSelfClosingFlagNotAcknowledged() throws IOException {
 reader.reset();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 }

 @Test
 public void testReadEndTag() throws IOException {
 String input = "<html></html>";
 reader.setInput(new ByteArrayInputStream(input.getBytes()));
 reader.reset();
 tokeniser.read();
 tokeniser.read(); // <html>
 tokeniser.read(); // <html>
 tokeniser.read(); // </html>
 Token endTag = tokeniser.read();
 assertEquals(new EndTag("html"), endTag);
 }

 @Test
 public void testReadStartTag() throws IOException {
 String input = "<html><head></head><body></body></html>";
 reader.setInput(new ByteArrayInputStream(input.getBytes()));
 reader.reset();

 List<Token> tokens = new ArrayList<>();
 while (tokeniser.getState() != TokeniserState.EndElement) {
 tokens.add(tokeniser.read());
 }

 Token startTag = tokens.get(1);
 Token endTag = tokens.get(tokens.size() - 1);
 assertEquals(new StartTag("html"), startTag);
 assertEquals(new EndTag("html"), endTag);
 }

 @Test
 public void testReadString() throws IOException {
 String input = "hello <world>";
 reader.setInput(new ByteArrayInputStream(input.getBytes()));
 reader.reset();

 for (int i = 0; i < input.length(); i++) {
 Token token = tokeniser.read();
 if (token instanceof Character) {
 continue;
 }
 String str = "";
 while (!(token instanceof Character)) {
 str += token.getText();
 token = tokeniser.read();
 }
 assertEquals(Character.toString(input.charAt(i)), ((Character) token).getText());
 assertEquals(str, input.substring(i, i + 1));
 }
 }

 @Test
 public void testReadCharacterReference() throws IOException {
 String input = "&aacute;";
 reader.setInput(new ByteArrayInputStream(input.getBytes()));
 reader.reset();
 Character expected = '\u00E1';
 Character reference = tokeniser.advanceTransition(TokeniserState.CharacterReference).consumeCharacterReference(null, false);
 assertEquals(expected, reference);
 }

 @Test
 public void testReadNamedCharacterReference() throws IOException {
 String input = "&aelig;";
 reader.setInput(new ByteArrayInputStream(input.getBytes()));
 reader.reset();
 Character expected = 'æ';
 Character reference = tokeniser.advanceTransition(TokeniserState.NamedCharacterReference).consumeCharacterReference(null, false);
 assertEquals(expected, reference);
 }

 @Test
 public void testReadInvalidNamedCharacterReference() throws IOException {
 String input = "&foo;";
 reader.setInput(new ByteArrayInputStream(input.getBytes()));
 reader.reset();
 assertNull(tokeniser.advanceTransition(TokeniserState.NamedCharacterReference).consumeCharacterReference("foo", false));
 }

 @Test
 public void testReadInvalidCharacterReference() throws IOException {
 String input = "&";
 reader.setInput(new ByteArrayInputStream(input.getBytes()));
 reader.reset();
 assertNull(tokeniser.consumeCharacterReference(null, false));
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTempBuffer();
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.createTagPending(true);
 assertEquals(new StartTag(), tokeniser.tagPending);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTempBuffer();
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 assertNull(tokeniser.tagPending);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createTempBuffer();
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.createCommentPending();
 assertEquals(new Comment(), tokeniser.commentPending);
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createTempBuffer();
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 assertNull(tokeniser.commentPending);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createTempBuffer();
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.createDoctypePending();
 assertEquals(new Doctype(), tokeniser.doctypePending);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createTempBuffer();
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 assertNull(tokeniser.doctypePending);
 }

 @Test
 public void testError() {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.error(TokeniserState.StartTagBegin);
 assertEquals(new ParseError(0, "Unexpected character '%c' in input state [StartTagBegin]", (char) 0), errors.getErrors().get(0));
 }
}