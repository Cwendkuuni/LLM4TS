package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Tokeniser class.
 */
public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(new CharacterReader(""), errors);
 }

 @Test
 public void testCreateTokeniser() {
 assertNotNull(tokeniser);
 }

 @Test
 public void testReadNullToken() {
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadEmptyCharacter() {
 tokeniser.reader.consume(' ');
 Token token = tokeniser.read();
 assertInstanceOf(Character.class, token);
 assertEquals("", token.data());
 }

 @Test
 public void testReadSingleCharacter() {
 tokeniser.reader.consume('A');
 Token token = tokeniser.read();
 assertInstanceOf(Character.class, token);
 assertEquals("A", token.data());
 }

 @Test
 public void testReadMultipleCharacters() {
 tokeniser.reader.consume('A');
 tokeniser.reader.consume('B');
 tokeniser.reader.consume('C');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 Token token = tokeniser.read();
 assertInstanceOf(StartTag.class, token);
 assertEquals("ABC", token.data());
 }

 @Test
 public void testReadEndTag() {
 tokeniser.reader.consume('/');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('/');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 Token token = tokeniser.read();
 assertInstanceOf(EndTag.class, token);
 assertEquals("img", token.tagName());
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.reader.consume('/');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.selfClosingFlagAcknowledged = false;
 tokeniser.read();
 assertNull(tokeniser.tagPending);
 }

 @Test(expected = NullPointerException.class)
 public void testReadWhenSelfClosingFlagNotAcknowledged() {
 tokeniser.selfClosingFlagAcknowledged = false;
 tokeniser.read();
 }

 @Test
 public void testReadStartTagWithAttributes() {
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('s');
 tokeniser.reader.consume('o');
 tokeniser.reader.consume('r');
 tokeniser.reader.consume('c');
 tokeniser.reader.consume('e');
 tokeniser.reader.consume('=');
 tokeniser.reader.consume('"');
 tokeniser.reader.consume('h');
 tokeniser.reader.consume('e');
 tokeniser.reader.consume('l');
 tokeniser.reader.consume('l');
 tokeniser.reader.consume('o');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 Token token = tokeniser.read();
 assertInstanceOf(StartTag.class, token);
 assertNotNull(token.tagName());
 assertEquals("img", token.tagName());
 assertNotNull(token.attributes());
 }

 @Test
 public void testReadStartTagWithNoAttributes() {
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 Token token = tokeniser.read();
 assertInstanceOf(StartTag.class, token);
 assertNotNull(token.tagName());
 assertEquals("img", token.tagName());
 assertNull(token.attributes());
 }

 @Test
 public void testReadComment() {
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('!');
 tokeniser.reader.consume('-');
 tokeniser.reader.consume('-');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 assertInstanceOf(Comment.class, token);
 assertNotNull(token.data());
 }

 @Test
 public void testReadDoctype() {
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('d');
 tokeniser.reader.consume('o');
 tokeniser.reader.consume('c');
 tokeniser.reader.consume('t');
 tokeniser.reader.consume('y');
 tokeniser.reader.consume('p');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('h');
 tokeniser.reader.consume('t');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('l');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 assertInstanceOf(Doctype.class, token);
 assertNotNull(token.data());
 }

 @Test
 public void testReadScriptTag() {
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('/');
 tokeniser.reader.consume('s');
 tokeniser.reader.consume('c');
 tokeniser.reader.consume('r');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('p');
 tokeniser.reader.consume('t');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.createTempBuffer();
 tokeniser.createEndTagPending();
 tokeniser.emitEndTagPending();
 tokeniser.emitTagPending();
 tokeniser.emitDoctypePending();
 tokeniser.emitCommentPending();
 tokeniser.emitTagPending();
 tokeniser.read(); // script tag
 tokeniser.read(); // /script tag
 tokeniser.read(); // doctype
 tokeniser.read(); // comment
 tokeniser.read(); // tag
 tokeniser.read(); // tag
 assertNull(tokeniser.tagPending);
 }

 @Test
 public void testReadScriptTagWithAttributes() {
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('/');
 tokeniser.reader.consume('s');
 tokeniser.reader.consume('c');
 tokeniser.reader.consume('r');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('p');
 tokeniser.reader.consume('t');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('s');
 tokeniser.reader.consume('o');
 tokeniser.reader.consume('r');
 tokeniser.reader.consume('c');
 tokeniser.reader.consume('e');
 tokeniser.reader.consume('=');
 tokeniser.reader.consume('"');
 tokeniser.reader.consume('h');
 tokeniser.reader.consume('t');
 tokeniser.reader.consume('t');
 tokeniser.reader.consume('p');
 tokeniser.reader.consume(':');
 tokeniser.reader.consume('7');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.createTempBuffer();
 tokeniser.createEndTagPending();
 tokeniser.emitEndTagPending();
 tokeniser.emitTagPending();
 tokeniser.emitDoctypePending();
 tokeniser.emitCommentPending();
 tokeniser.emitTagPending();
 tokeniser.read(); // script tag
 tokeniser.read(); // /script tag
 Token tag = tokeniser.tagPending;
 assertNotNull(tag);
 assertSame(StartTag.class, tag.getClass());
 assertEquals("script", tag.tagName());
 assertSame(tag, tokeniser.read().tagPending);
 }

 @Test
 public void testReadStartTagWithAttributesAndSelfClosing() {
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('/');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('s');
 tokeniser.reader.consume('o');
 tokeniser.reader.consume('r');
 tokeniser.reader.consume('c');
 tokeniser.reader.consume('e');
 tokeniser.reader.consume('=');
 tokeniser.reader.consume('"');
 tokeniser.reader.consume('h');
 tokeniser.reader.consume('e');
 tokeniser.reader.consume('l');
 tokeniser.reader.consume('l');
 tokeniser.reader.consume('o');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.selfClosingFlagAcknowledged = false;
 tokeniser.reader.consume('/');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.createTempBuffer();
 tokeniser.createEndTagPending();
 tokeniser.emitEndTagPending();
 tokeniser.emitTagPending();
 tokeniser.emitDoctypePending();
 tokeniser.emitCommentPending();
 tokeniser.emitTagPending();
 tokeniser.read(); // img tag
 tokeniser.read(); // /img tag
 assertNull(tokeniser.tagPending);
 }

 @Test
 public void testReadStartTagWithAttributesAndEmptyStringValue() {
 tokeniser.reader.consume('<');
 tokeniser.reader.consume('/');
 tokeniser.reader.consume('i');
 tokeniser.reader.consume('m');
 tokeniser.reader.consume('g');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('s');
 tokeniser.reader.consume('o');
 tokeniser.reader.consume('r');
 tokeniser.reader.consume('c');
 tokeniser.reader.consume('e');
 tokeniser.reader.consume('=');
 tokeniser.reader.consume('"');
 tokeniser.reader.consume(' ');
 tokeniser.reader.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.createTempBuffer();
 tokeniser.createEndTagPending();
 tokeniser.emitEndTagPending();
 tokeniser.emitTagPending();
 tokeniser.emitDoctypePending();
 tokeniser.emitCommentPending();
 tokeniser.emitTagPending();
 tokeniser.read(); // img tag
 tokeniser.read(); // /img tag
 Token tag = tokeniser.tagPending;
 assertNotNull(tag);
 assertSame(StartTag.class, tag.getClass());
 assertEquals("img", tag.tagName());
 assertNull(tag.attributes());
 }

 @Test
 public void testConsumeCharacterReference() {
 tokeniser.reader.consume('#');
 tokeniser.consumeCharacterReference(null, false);
 assertArrayEquals(new int[]{replacementChar}, tokeniser.codepointHolder);
 tokeniser.reader.consume('#');
 tokeniser.consumeCharacterReference(null, false);
 assertArrayEquals(new int[]{replacementChar}, tokeniser.codepointHolder);
 tokeniser.reader.consume('x');
 tokeniser.consumeCharacterReference('x', false);
 assertArrayEquals(new int[]{0x25}, tokeniser.codepointHolder);
 tokeniser.reader.consume('x');
 tokeniser.consumeCharacterReference('x', false);
 assertArrayEquals(new int[]{0x25}, tokeniser.codepointHolder);
 tokeniser.reader.consume('#');
 tokeniser.consumeCharacterReference("xx", false);
 assertArrayEquals(new int[]{0x2033, 0x2033}, tokeniser.multipointHolder);
 tokeniser.reader.consume('#');
 tokeniser.consumeCharacterReference("xx", false);
 assertArrayEquals(new int[]{0x2033, 0x2033}, tokeniser.multipointHolder);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceInAttribute() {
 tokeniser.reader.consume('#');
 tokeniser.consumeCharacterReference(null, true);
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTagPending(true);
 assertInstanceOf(StartTag.class, tokeniser.tagPending);
 tokeniser.createTagPending(false);
 assertInstanceOf(EndTag.class, tokeniser.tagPending);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 assertNull(tokeniser.tagPending);
 }

 @Test
 public void testError() {
 tokeniser.error(TokeniserState.Data);
 assertNotNull(tokeniser.errors);
 }

 @Test
 public void testEofError() {
 tokeniser.consumeTo('a');
 tokeniser.emit(' ');
 tokeniser.emit(' ');
 tokeniser.eofError(TokeniserState.Data);
 assertNotNull(tokeniser.errors);
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.reader.consume('#');
 tokeniser.characterReferenceError("numeric reference with no numerals");
 }
}