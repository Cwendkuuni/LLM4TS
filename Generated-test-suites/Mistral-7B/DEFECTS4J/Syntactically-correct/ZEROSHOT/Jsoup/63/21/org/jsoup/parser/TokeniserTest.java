package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader mockCharacterReader;
 private ParseErrorList errorList;
 private Tokeniser tokeniser;

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @Before
 public void setUp() {
 mockCharacterReader = new MockCharacterReader();
 errorList = new ParseErrorList();
 tokeniser = new Tokeniser(mockCharacterReader, errorList);
 }

 @Test
 public void testReadEmptyStream() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadSingleCharacter() {
 mockCharacterReader.append('a');
 mockCharacterReader.setPosition(0);

 Token token = tokeniser.read();
 assertEquals('a', token.data());
 }

 @Test
 public void testReadMultipleCharacters() {
 mockCharacterReader.append("abc");
 mockCharacterReader.setPosition(0);

 Token token = tokeniser.read();
 assertEquals("abc", token.data());
 }

 @Test
 public void testReadSelfClosingFlag() {
 mockCharacterReader.append("/");
 mockCharacterReader.append('p');
 mockCharacterReader.append('>');
 mockCharacterReader.setPosition(0);

 tokeniser.read();
 tokeniser.read();
 tokeniser.read();

 tokeniser.acknowledgeSelfClosingFlag();

 token = tokeniser.read();
 assertFalse(token.selfClosing());
 }

 @Test(expected = IllegalStateException.class)
 public void testReadPendingToken() {
 tokeniser.emit(new Token.Character('a'));

 token = tokeniser.read();
 assertNull(token);

 token = tokeniser.read();
 assertEquals('a', token.data());
 }

 @Test
 public void testEmitCharacter() {
 tokeniser.emit(new Token.Character('a'));

 token = tokeniser.read();
 assertEquals('a', token.data());
 }

 @Test
 public void testEmitStartTag() {
 tokeniser.emitStartTag("p");

 token = tokeniser.read();
 assertEquals("p", token.tagName());
 assertSame(Token.StartTag.class, token.type());
 }

 @Test
 public void testEmitEndTag() {
 tokeniser.emitEndTag("p");

 token = tokeniser.read();
 assertEquals("p", token.tagName());
 assertSame(Token.EndTag.class, token.type());
 }

 @Test
 public void testEmitEmptyElement() {
 tokeniser.emitEmptyElement("img");

 token = tokeniser.read();
 assertEquals("img", token.tagName());
 assertSame(Token.StartTag.class, token.type());
 assertSame(Token.StartTag.EMPTY_ELEMENT_TAG, token.emptyElementTagName());
 }

 @Test
 public void testEmitDoctype() {
 mockCharacterReader.append("<!DOCTYPE html>");
 mockCharacterReader.setPosition(0);

 tokeniser.emitDoctype();

 token = tokeniser.read();
 assertEquals("html", token.tagName());
 assertSame(Token.Doctype.class, token.type());
 }

 @Test
 public void testConsumeCharacterReference() {
 mockCharacterReader.append("#x41");
 mockCharacterReader.setPosition(0);

 int[] referenceCodePoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, referenceCodePoints.length);
 assertEquals(65, referenceCodePoints[0]);

 mockCharacterReader.append("#x26");
 mockCharacterReader.setPosition(0);
 int[] multiReferenceCodePoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(2, multiReferenceCodePoints.length);
 assertEquals(38, multiReferenceCodePoints[0]);
 assertEquals(38, multiReferenceCodePoints[1]);
 }

 @Test
 public void testConsumeNamedCharacterReference() {
 mockCharacterReader.append("&nbsp;");
 mockCharacterReader.setPosition(0);

 int[] referenceCodePoints = tokeniser.consumeCharacterReference('\u00A0', false);
 assertEquals(1, referenceCodePoints.length);
 assertEquals(160, referenceCodePoints[0]);

 mockCharacterReader.append("&amp;");
 mockCharacterReader.append("&quot;");
 mockCharacterReader.setPosition(0);
 int[] multiReferenceCodePoints = tokeniser.consumeCharacterReference('\u0026', false);
 assertEquals(2, multiReferenceCodePoints.length);
 assertEquals(38, multiReferenceCodePoints[0]);
 assertEquals(34, multiReferenceCodePoints[1]);
 }

 @Test
 public void testConsumeNamedCharacterReferenceWithAttribute() {
 mockCharacterReader.append("class=\"value &amp; name\"");
 mockCharacterReader.setPosition(0);
 int[] referenceCodePoints = tokeniser.consumeCharacterReference('\u0026', true);
 assertEquals(1, referenceCodePoints.length);
 assertEquals(38, referenceCodePoints[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceWithInappropriateCharacter() {
 mockCharacterReader.append("#x0");
 mockCharacterReader.setPosition(0);
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReferenceWithInvalidCharacterSequence() {
 mockCharacterReader.append("&#");
 mockCharacterReader.setPosition(0);
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTagPending(true);
 Token tagPending = tokeniser.tagPending();
 assertTrue(tagPending instanceof Token.StartTag);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();

 Token tagEmitted = tokeniser.read();
 Token tagPending = tokeniser.tagPending();

 assertEquals("p", tagEmitted.tagName());
 assertEquals("p", tagPending.tagName());
 assertNull(tagPending.attributes());
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 Token commentPending = tokeniser.commentPending();
 assertTrue(commentPending instanceof Token.Comment);
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();

 Token commentEmitted = tokeniser.read();
 Token commentPending = tokeniser.commentPending();

 assertEquals("-->", commentEmitted.data());
 assertEquals("-->", commentPending.data());
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Token doctypePending = tokeniser.doctypePending();
 assertTrue(doctypePending instanceof Token.Doctype);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();

 Token doctypeEmitted = tokeniser.read();
 Token doctypePending = tokeniser.doctypePending();

 assertEquals("html", doctypeEmitted.tagName());
 assertEquals("html", doctypePending.tagName());
 assertNull(doctypePending.attributes());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.emitStartTag("p");
 tokeniser.emitTagPending();

 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenWithWrongTagName() {
 tokeniser.emitStartTag("p");
 tokeniser.emitEndTag("div");

 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenWithNoLastStartTag() {
 tokeniser.emitStartTag("p");
 tokeniser.emitTagPending();
 tokeniser.emitEndTag("p");

 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testErrorOnUnexpectedCharacterReferenceState() {
 tokeniser.read();
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.error("Unexpected character reference state");
 }

 @Test
 public void testErrorOnUnexpectedEndOfStreamState() {
 tokeniser.read();
 tokeniser.error(TokeniserState.EndOfStream);
 }

 @Test
 public void testErrorOnUnexpectedCommentState() {
 tokeniser.read();
 tokeniser.emitCommentPending();
 tokeniser.error(TokeniserState.Comment);
 }

 @Test
 public void testErrorOnUnexpectedDoctypeState() {
 tokeniser.read();
 tokeniser.emitDoctypePending();
 tokeniser.error(TokeniserState.Doctype);
 }

 @Test
 public void testErrorOnUnexpectedStartTagState() {
 tokeniser.read();
 tokeniser.emitStartTag("p");
 tokeniser.error(TokeniserState.StartTag);
 }

 @Test
 public void testErrorOnUnexpectedEndTagState() {
 tokeniser.read();
 tokeniser.emitEndTag("p");
 tokeniser.error(TokeniserState.EndTag);
 }

 @Test
 public void testErrorOnUnexpectedCharacterState() {
 tokeniser.read();
 tokeniser.emitCharacter('a');
 tokeniser.error(TokeniserState.Character);
 }

 @Test
 public void testErrorOnUnexpectedWhitespaceState() {
 tokeniser.read();
 tokeniser.error(TokeniserState.Whitespace);
 }

 @Test
 public void testErrorOnUnexpectedEmptyElementState() {
 tokeniser.read();
 tokeniser.emitEmptyElement("img");
 tokeniser.error(TokeniserState.EmptyElement);
 }

 @Test
 public void testErrorOnUnexpectedTagNameState() {
 tokeniser.read();
 tokeniser.emitTagPending();
 tokeniser.error(TokeniserState.TagName);
 }

 @Test
 public void testErrorOnUnexpectedAttributeState() {
 tokeniser.read();
 tokeniser.emitTagPending();
 tokeniser.error(TokeniserState.Attribute);
 }
}