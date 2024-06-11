import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private Stack<TokeniserState> stateStack;
 private ParseErrorList errors;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'i', 'n', 'p', ' ', 't', 'e', 's', 't', '.', ' ', 'o', 'n', ' ', 't', 'h', 'e', ' ', 'h', 'e', 'a', 'd', '>', '\n', 'h', '1', ' ', 'i', 's', ' ', 'i', 'n', 'p', 'u', 't', '>', '\n'}));
 tokeniser = new Tokeniser(reader, new ArrayList<>());
 stateStack = new Stack<>();
 errors = new ArrayList<>();
 }

 @Test
 public void testReadEmptyInput() {
 tokeniser.transition(TokeniserState.Data);
 Assert.assertNull(tokeniser.read());
 }

 @Test
 public void testDataStateReadHtmlTag() {
 stateStack.push(TokeniserState.Data);
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('h'));
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emit(new Token.Character('m'));
 tokeniser.emit(new Token.Character('l'));

 Token tag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tag.type);
 Assert.assertEquals("html", tag.tagName);

 stateStack.pop(); // should be back in Data state
 }

 @Test
 public void testDataStateReadLiteralCharacter() {
 stateStack.push(TokeniserState.Data);
 tokeniser.emit(new Token.Character('a'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('c'));

 Token characterToken = tokeniser.read();
 StringBuilder expected = new StringBuilder("abc");
 Assert.assertEquals(Token.TokenType.Character, characterToken.type);
 Assert.assertEquals(expected.toString(), characterToken.text);

 stateStack.pop(); // should be back in Data state
 }

 @Test
 public void testDataStateReadScriptTag() {
 stateStack.push(TokeniserState.Data);
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('/'));
 tokeniser.emit(new Token.Character('s'));
 tokeniser.emit(new Token.Character('c', 'r', 'i', 'p', 't', '>', '\n'));

 Token scriptTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, scriptTag.type);
 Assert.assertEquals("script", scriptTag.tagName);

 stateStack.pop(); // should be back in Data state
 }

 @Test
 public void testDataStateReadSelfClosingTag() {
 stateStack.push(TokeniserState.Data);
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('/'));
 tokeniser.emit(new Token.Character('i', 'm'));
 tokeniser.emit(new Token.Character('>', '\n'));

 Token selfClosingTag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, selfClosingTag.type);
 Assert.assertEquals("img", selfClosingTag.tagName);

 stateStack.pop(); // should be back in Data state
 }

 @Test
 public void testDataStateReadCommentTag() {
 stateStack.push(TokeniserState.Data);
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.createCommentPending();
 tokeniser.emit(new Token.Character('*'));
 tokeniser.emit(new Token.Character('*'));
 tokeniser.emit(new Token.Character(' ', 't', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'c', 'o', 'm', 'm', 'e', 'n', 't'));
 tokeniser.emit(new Token.Character(' ', '*'));
 tokeniser.emit(new Token.Character('*'));
 tokeniser.emitDoctypePending();

 Token commentToken = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, commentToken.type);
 Assert.assertEquals("this is a comment", commentToken.text);

 stateStack.pop(); // should be back in Data state
 }

 @Test
 public void testDataStateReadCharacterReference() {
 stateStack.push(TokeniserState.Data);
 tokeniser.emit(new Token.Character('&'));
 tokeniser.emit(new Token.Character('a', 'm', 'p'));
 tokeniser.emit(new Token.Character(';'));

 Token characterReferenceToken = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, characterReferenceToken.type);
 Assert.assertEquals("&amp;", characterReferenceToken.text);

 stateStack.pop(); // should be back in Data state
 }

 @Test
 public void testDataStateReadCharacterReferenceWithAttributeValue() {
 stateStack.push(TokeniserState.Data);
 tokeniser.transition(TokeniserState.InAttribute);
 tokeniser.emit(new Token.Character('&'));
 tokeniser.emit(new Token.Character('a', 'm', 'p'));
 tokeniser.emit(new Token.Character(';'));

 // Should not match this character reference as we're currently in Attribute state
 tokeniser.read();

 tokeniser.transition(TokeniserState.Data);
 tokeniser.createTagPending(false);
 tokeniser.emit(new Token.Character('<', 'i', 'm', ' '));
 tokeniser.emit(new Token.Character(' ', 's', 'o', 'r', 'c', 'e', ' ', 't', 'a', 'g', '>'));
 tokeniser.emit(new Token.Character(' ', 'c', 'l', 'a', 's', 's', '=', ' '));
 tokeniser.emit(new Token.Character('&', 'x', 'a', 'm', 'p', ';'));
 tokeniser.emit(new Token.Character(' ', 'o', 'n', ' ', 'c', 'l', 'o', 's', 'e', 's', '=', ' "', 'h', 'e', 'l', 'l', '"', '\n'));
 tokeniser.emitTagPending();

 Token characterReferenceToken = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, characterReferenceToken.type);
 Assert.assertEquals("&amp;", characterReferenceToken.text);

 stateStack.pop(); // should be back in Data state
 }

 @Test
 public void testErrorHandling() {
 stateStack.push(TokeniserState.Data);
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emit(new Token.Character('<', '/', 's', 'c', 'r', 'i', 'p', 't', '>'));

 // Should throw error for missing '>' character
 tokeniser.read();

 Assert.assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 Assert.assertEquals(TokeniserState.Script.toString(), error.inputState);
 Assert.assertEquals("Unexpected character 's' in input state [Script]", error.message);
 }

 @Test
 public void testEofError() {
 stateStack.push(TokeniserState.Data);
 tokeniser.emit(new Token.Character('&'));
 tokeniser.emit(new Token.Character('a', 'm', 'p'));
 tokeniser.emit(new Token.Character(';'));
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emit(new Token.Character('>'));
 tokeniser.consumeCharacterReference(null, false);

 // Consume 'EOF' character and expect eofError to be thrown
 tokeniser.read();
 tokeniser.read();

 Assert.assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Script]", error.message);
 }
}