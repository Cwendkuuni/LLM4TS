package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterConsumer;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadValidCharacter() {
 char expected = 't';
 Token token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals(expected, ((Character) token).data);
 }

 @Test
 public void testReadMultipleValidCharacters() {
 String expected = "est";
 StringBuilder sb = new StringBuilder();
 while (tokeniser.getState() != TokeniserState.Data && tokeniser.read() != null) {
 sb.append(((Character) tokeniser.read()).data);
 }
 assertEquals(expected, sb.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWithEmitPending() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.read();
 }

 @Test
 public void testEmitValidCharacter() {
 tokeniser.emit('t');
 tokeniser.read();
 assertTrue(tokeniser.read() instanceof Character);
 assertEquals('t', ((Character) tokeniser.read()).data);
 }

 @Test
 public void testEmitValidStartTag() {
 tokeniser.emit(new StartTag("div"));
 String expected = "<div";
 StringBuilder sb = new StringBuilder();
 while (tokeniser.getState() != TokeniserState.Data && tokeniser.read() != null) {
 sb.append(tokeniser.read().data);
 }
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testEmitValidEndTag() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.transition(TokeniserState.TagBody);
 tokeniser.emit("</div>");
 String expected = "</div>";
 StringBuilder sb = new StringBuilder();
 while (tokeniser.getState() != TokeniserState.Data && tokeniser.read() != null) {
 sb.append(tokeniser.read().data);
 }
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testEmitInvalidEndTag() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.transition(TokeniserState.TagBody);
 tokeniser.emit("</p>");
 assertTrue(errors.size() > 0);
 assertEquals("Unexpected end tag <p>", errors.get(0).getMessage());
 }

 @Test
 public void testEmitValidComment() {
 tokeniser.emit(new StartTag("script"));
 tokeniser.createCommentPending();
 tokeniser.emit("<!-- This is a comment -->");
 tokeniser.emitCommentPending();
 String expected = "-->This is a comment<--";
 StringBuilder sb = new StringBuilder();
 while (tokeniser.getState() != TokeniserState.Data && tokeniser.read() != null) {
 sb.append(tokeniser.read().data);
 }
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testConsumeCharacterReference() {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 assertNull(tokeniser.read());
 tokeniser.consumeCharacterReference('#', false);
 assertNull(tokeniser.read());
 tokeniser.consumeCharacterReference("a", false);
 assertNull(tokeniser.read());
 tokeniser.consumeCharacterReference("amp;", false);
 assertNull(tokeniser.read());
 tokeniser.consumeCharacterReference("apos;", false);
 assertNull(tokeniser.read());
 tokeniser.consumeCharacterReference("quot;", false);
 assertEquals('\"', tokeniser.read().data.charAt(0));
 tokeniser.consumeCharacterReference("x00A9", true);
 assertEquals((char) 0x00A9, tokeniser.read().data.charAt(0));
 }

 @Test
 public void testConsumeNamedCharacterReference() {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference("nbsp", false);
 assertEquals('\u00A0', tokeniser.read().data.charAt(0));
 tokeniser.consumeCharacterReference("iexcl", true);
 assertEquals((char) 0x00A1, tokeniser.read().data.charAt(0));
 }

 @Test
 public void testErrorHandling() {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.error(TokeniserState.CharacterReference);
 assertTrue(errors.size() > 0);
 assertEquals("Invalid character reference", errors.get(0).getMessage());
 }

 @Test
 public void testEOFErrorHandling() {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.eofError(TokeniserState.CharacterReference);
 assertTrue(errors.size() > 0);
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 tokeniser.emit("test");
 tokeniser.transition(TokeniserState.Text);
 String expected = "test";
 StringBuilder sb = new StringBuilder();
 while (tokeniser.getState() != TokeniserState.Data && tokeniser.read() != null) {
 sb.append(tokeniser.read().data);
 }
 assertEquals(expected, sb.toString());
 tokeniser.createTempBuffer();
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testAppropriateEndTagName() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertEquals("div", tokeniser.appropriateEndTagName());
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertNull(tokeniser.appropriateEndTagName());
 }
}