import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8)));
 tokeniser = new Tokeniser(reader);
 }

 @Test
 public void testDefaultState() {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadCharacter() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("t", token.data);
 }

 @Test
 public void testReadEOF() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWhenEmitPending() {
 tokeniser.emit(new Token.Character('e'));
 tokeniser.read();
 }

 @Test
 public void testEmitCharacter() {
 tokeniser.emit(new Token.Character('t'));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("t", token.data);
 }

 @Test
 public void testEmitStartTag() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.emitTagPending();
 token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("p", token.tagName);
 }

 @Test
 public void testEmitEndTag() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.transition(TokeniserState.TagEnd);
 tokeniser.emitTagPending();
 token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("p", token.tagName);
 }

 @Test
 public void testEmitComment() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emitCommentPending();
 token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 }

 @Test
 public void testEmitDoctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emitDoctypePending();
 token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testConsumeCharacterReference() {
 Character reference = tokeniser.consumeCharacterReference(null, false);
 assertNull(reference);

 reference = tokeniser.consumeCharacterReference('&', false);
 assertNull(reference);

 reference = tokeniser.consumeCharacterReference('&', true);
 assertNull(reference);

 reference = tokeniser.consumeCharacterReference("#4869", false);
 assertEquals((char) 0xF0, reference.charValue());

 reference = tokeniser.consumeCharacterReference("#x26", false);
 assertEquals('&', reference.charValue());

 reference = tokeniser.consumeCharacterReference("#x1F4", false);
 assertEquals('\uD835', reference.toString());
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag tag = tokeniser.createTagPending(true);
 assertEquals(Token.TokenType.StartTag, tag.type);
 assertFalse(tag.selfClosing);
 }

 @Test
 public void testCreateTagPendingSelfClosing() {
 Token.Tag tag = tokeniser.createTagPending(false);
 assertEquals(Token.TokenType.EndTag, tag.type);
 assertTrue(tag.selfClosing);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.emit(new Token.EndTag("p"));
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenDifferentTags() {
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.emit(new Token.StartTag("div"));
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testSetTrackErrorsOff() {
 tokeniser.setTrackErrors(false);
 tokeniser.error("test error");
 assertTrue(tokeniser.errors.isEmpty());
 }

 @Test
 public void testTransitionToState() {
 tokeniser.transition(TokeniserState.TagStart);
 assertEquals(TokeniserState.TagStart, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransitionToState() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.advanceTransition(TokeniserState.TagEnd);
 assertEquals(TokeniserState.TagEnd, tokeniser.getState());
 }

 @Test
 public void testTransitionToStateWhenEmitting() {
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.emit(new Token.Character('t'));
 tokeniser.advanceTransition(TokeniserState.TagEnd);
 assertEquals(TokeniserState.TagEnd, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testAcknowledgeSelfClosingFlagAlreadyAcknowledged() {
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }
}