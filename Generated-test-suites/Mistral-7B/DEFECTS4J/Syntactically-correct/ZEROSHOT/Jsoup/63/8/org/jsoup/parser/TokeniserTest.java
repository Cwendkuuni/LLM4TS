package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("input.html");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadEmptyInput() {
 Token token = tokeniser.read();
 assertTrue(token == null);
 }

 @Test
 public void testReadValidInput() {
 reader.mark("abc<p>def<script src='script.js'></script>ghi");
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals("abc", token.data());
 token = tokeniser.read();
 assertEquals("<p>", token.toString());
 token = tokeniser.read();
 assertEquals("def", token.data());
 token = tokeniser.read();
 assertEquals("", token.data()); // empty character buffer
 token = tokeniser.read();
 assertEquals("<script src='script.js'>", token.toString());
 token = tokeniser.read();
 assertEquals("", token.data()); // empty character buffer
 token = tokeniser.read();
 assertEquals("</script>", token.toString());
 token = tokeniser.read();
 assertEquals("ghi", token.data());
 token = tokeniser.read();
 assertEquals(null, token);
 }

 @Test
 public void testReadSelfClosingTag() {
 reader.mark("<img src='image.jpg' />");
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals("<img src='image.jpg'", token.toString());
 assertTrue(tokeniser.selfClosingFlagAcknowledged());
 }

 @Test
 public void testReadInvalidInputWithErrorHandling() {
 reader.mark("abc<p>def<script src='script.js'></script>ghi<");
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals("abc", token.data());
 tokeniser.error("Unexpected character '<' in state [Data]");
 token = tokeniser.read();
 assertEquals("<p>", token.toString());
 tokeniser.error("Unexpected character 's' in state [Data]");
 tokeniser.error("Unexpected character 'c' in state [Data]");
 tokeniser.error("Unexpected character 'r' in state [Data]");
 tokeniser.error("Unexpected character 'ipt' in state [Data]");
 tokeniser.error("Unexpected character '>' in state [Data]");
 tokeniser.error("Unexpected character '<' in state [Data]");
 tokeniser.read();
 assertEquals("def", token.data());
 tokeniser.error("Unexpected character 's' in state [Data]");
 tokeniser.error("Unexpected character 'c' in state [Data]");
 tokeniser.error("Unexpected character 'r' in state [Data]");
 tokeniser.error("Unexpected character 'i' in state [Data]");
 tokeniser.error("Unexpected character 'p' in state [Data]");
 tokeniser.error("Unexpected character '>' in state [Data]");
 tokeniser.read();
 assertEquals("", token.data()); // empty character buffer
 tokeniser.error("Unexpected character '<' in state [Data]");
 tokeniser.read();
 assertEquals("</script>", token.toString());
 tokeniser.read();
 assertEquals("", token.data()); // empty character buffer
 tokeniser.read();
 assertEquals("</", token.toString());
 tokeniser.error("Unexpected character '<' in state [Data]");
 tokeniser.read();
 assertEquals(">", token.toString());
 tokeniser.read();
 assertEquals("ghi", token.data());
 tokeniser.read();
 assertEquals(null, token);
 }

 @Test
 public void testReadInvalidInputWithoutErrorHandling() {
 reader.mark("abc<p>def<script src='script.js'></script>ghi<");
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals("abc", token.data());
 tokeniser.read(); // should error on "<p"
 tokeniser.read(); // should error on "<script"
 tokeniser.read(); // should error on "src='"
 tokeniser.read(); // should error on "'"
 tokeniser.read(); // should error on ">"
 tokeniser.read(); // should error on "<"
 tokeniser.read(); // should error on "<"
 tokeniser.read(); // should error on ">"
 tokeniser.read(); // should error on "ghi<"
 assertEquals(5, errors.size());
 }

 @Test
 public void testEmit() {
 tokeniser.read();
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('b'));
 tokeniser.emit(new Character('c'));
 tokeniser.emit(new StartTag("p"));
 tokeniser.emit(new Character('d'));
 tokeniser.emit(new EndTag("p"));
 tokeniser.read(); // should be empty character buffer
 assertEquals(6, tokeniser.getState().getTotalTokensEmitted());
 }

 @Test
 public void testEmitWithErrors() {
 tokeniser.read();
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('b'));
 tokeniser.emit(new StartTag("p"));
 tokeniser.error("Unexpected character 'p' in state [Data]");
 tokeniser.read(); // should be empty character buffer
 assertEquals(3, tokeniser.getState().getTotalTokensEmitted());
 }

 @Test
 public void testEmitTags() {
 tokeniser.read();
 tokeniser.emit(new StartTag("p"));
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('b'));
 tokeniser.emit(new EndTag("p"));
 tokeniser.read(); // should be empty character buffer
 assertEquals(3, tokeniser.getState().getTotalTokensEmitted());
 }

 @Test
 public void testEmitComment() {
 tokeniser.read();
 tokeniser.emit(new Comment("<!-- this is a comment -->"));
 tokeniser.read(); // should be empty character buffer
 assertEquals(1, tokeniser.getState().getTotalTokensEmitted());
 }

 @Test
 public void testEmitDoctype() {
 tokeniser.read();
 tokeniser.emit(new Doctype("html", Arrays.asList("HTML", "html")));
 tokeniser.read(); // should be empty character buffer
 assertEquals(1, tokeniser.getState().getTotalTokensEmitted());
 }

 @Test
 public void testState() {
 TokeniserState state = TokeniserState.Data;
 tokeniser.transition(state);
 assertEquals(state, tokeniser.getState());
 tokeniser.advanceTransition(TokeniserState.StartTag);
 assertEquals(TokeniserState.StartTag, tokeniser.getState());
 tokeniser.advanceTransition(TokeniserState.EndTag);
 assertEquals(TokeniserState.EndTag, tokeniser.getState());
 }

 @Test
 public void testCharacterReference() {
 tokeniser.read();
 tokeniser.emit(new Character('&'));
 int[] codePoints = tokeniser.consumeCharacterReference(Character.MIN_VALUE, false);
 assertEquals(Arrays.asList(0x00A9), Arrays.asList(codePoints));
 tokeniser.read(); // should be empty character buffer
 }

 @Test
 public void testCharacterReferenceWithAdditionalAllowedCharacter() {
 tokeniser.read();
 tokeniser.emit(new Character('&'));
 tokeniser.emit(new Character('a'));
 int[] codePoints = tokeniser.consumeCharacterReference('a', false);
 assertEquals(Arrays.asList(0x0061), Arrays.asList(codePoints));
 tokeniser.read(); // should be empty character buffer
 }

 @Test(expected = AssertionError.class)
 public void testCharacterReferenceUnexpectedCharacter() {
 tokeniser.read();
 tokeniser.emit(new Character('&'));
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testCharacterReferenceWithAttribute() {
 tokeniser.read();
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new StartTag("a"));
 tokeniser.emit(new Attribute("class", "foo bar"));
 tokeniser.emit(new Character('&'));
 tokeniser.emit(new Character('x'));
 tokeniser.emit(new Character('r'));
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('m'));
 tokeniser.emit(new Character(';'));
 tokeniser.emit(new Character('/'));
 tokeniser.emit(new Character('>'));
 int[] codePoints = tokeniser.consumeCharacterReference(Character.MIN_VALUE, true);
 assertEquals(Arrays.asList(0x002F), Arrays.asList(codePoints));
 }

 @Test(expected = AssertionError.class)
 public void testCharacterReferenceUnexpectedAttribute() {
 tokeniser.read();
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new StartTag("a"));
 tokeniser.emit(new Attribute("class", "foo bar"));
 tokeniser.emit(new Character('&'));
 tokeniser.emit(new Character('x'));
 tokeniser.emit(new Character('r'));
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('m'));
 tokeniser.emit(new Character(';'));
 tokeniser.consumeCharacterReference(Character.MIN_VALUE, true);
 }

 @Test
 public void testTagPending() {
 tokeniser.read();
 tokeniser.emit(new StartTag("p"));
 tokeniser.emitPendingTag();
 assertEquals("<p>", tokeniser.lastEmittedToken().toString());
 }

 @Test
 public void testCommentPending() {
 tokeniser.read();
 tokeniser.emitPendingComment();
 assertEquals("<!--", tokeniser.lastEmittedToken().toString());
 }

 @Test
 public void testDoctypePending() {
 tokeniser.read();
 tokeniser.emitPendingDoctype();
 assertEquals("<!DOCTYPE html SYSTEM>", tokeniser.lastEmittedToken().toString());
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('b'));
 tokeniser.createTempBuffer();
 tokeniser.emit(new Character('c'));
 tokeniser.emit(new Character('d'));
 assertEquals("abc", tokeniser.tempBuffer().data());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.read();
 tokeniser.emit(new StartTag("p"));
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('b'));
 tokeniser.emit(new EndTag("p"));
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_false() {
 tokeniser.read();
 tokeniser.emit(new StartTag("p"));
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('b'));
 tokeniser.emit(new StartTag("div"));
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "ab&#x1F4;cd &#xA9ef&#x20AC;ghi";
 String expectedOutput = "ab�cd ©efᵐghi";
 String output = tokeniser.unescapeEntities(false);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescapeEntitiesInAttribute() {
 String input = "ab&lt;p&gt;cd &#xA9ef&apos;ghi";
 String expectedOutput = "ab<p>cd ©ef'ghi";
 String output = tokeniser.unescapeEntities(true);
 assertEquals(expectedOutput, output);
 }
}