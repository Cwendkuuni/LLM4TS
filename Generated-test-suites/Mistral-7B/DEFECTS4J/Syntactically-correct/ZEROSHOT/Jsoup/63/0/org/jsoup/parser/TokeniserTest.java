package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Tokeniser.TokeniserState;

import org.junit.Test;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Test
 public void testTokeniserInitialisation() {
 reader = new CharacterReader("Test string.");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadAndEmitNormalToken() {
 tokeniser.read();
 assertNull(tokeniser.getState());
 tokeniser.emit("Hello World");
 Token token = tokeniser.read();
 assertEquals("Hello World", token.data());
 assertEquals(Token.TokenType.Text, token.type());
 }

 @Test
 public void testReadAndEmitStartTag() {
 tokeniser.transition(TokeniserState.InTagName);
 tokeniser.read();
 tokeniser.emit("<p>");
 tokeniser.emitTagPending();
 token = tokeniser.read();
 assertEquals("p", token.tagName());
 assertEquals(Token.TokenType.StartTag, token.type());
 }

 @Test
 public void testReadAndEmitEndTag() {
 tokeniser.transition(TokeniserState.InTagName);
 tokeniser.read();
 tokeniser.emit("<p>");
 tokeniser.transition(TokeniserState.AfterTagName);
 tokeniser.read();
 tokeniser.emit("</p>");
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 assertEquals("p", token.tagName());
 assertEquals(Token.TokenType.EndTag, token.type());
 }

 @Test
 public void testReadAndEmitCharacter() {
 tokeniser.transition(TokeniserState.InCharacter);
 tokeniser.emit('A');
 token = tokeniser.read();
 assertEquals('A', token.data()[0]);
 assertEquals(Token.TokenType.Character, token.type());
 }

 @Test
 public void testReadAndEmitComment() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read();
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type());
 }

 @Test
 public void testReadAndEmitDoctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.read();
 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type());
 }

 @Test
 public void testReadAndEmitSelfClosingTag() {
 tokeniser.transition(TokeniserState.InTagName);
 tokeniser.read();
 tokeniser.emit("<img src=\"image.jpg\" />");
 tokeniser.emitTagPending();
 token = tokeniser.read();
 assertEquals("img", token.tagName());
 assertTrue(token.isSelfClosing());
 }

 @Test
 public void testConsumeCharacterReference() {
 tokeniser = new Tokeniser(new CharacterReader("&#x27E8;"), errors);
 tokeniser.transition(TokeniserState.InCharacter);
 int[] ref = tokeniser.consumeCharacterReference(null, false);
 assertEquals(0x27E8, ref[0]);
 }

 @Test
 public void testConsumeCharacterReferenceInAttribute() {
 tokeniser = new Tokeniser(new CharacterReader("&nbsp;"), errors);
 tokeniser.transition(TokeniserState.InAttributeValue);
 int[] ref = tokeniser.consumeCharacterReference('s', true);
 assertNull(ref);
 }

 @Test
 public void testConsumeCharacterReferenceInvalid() {
 tokeniser = new Tokeniser(new CharacterReader("&x"), errors);
 tokeniser.transition(TokeniserState.InCharacter);
 int[] ref = tokeniser.consumeCharacterReference(null, false);
 assertNull(ref);
 errors.assertErrorCount(1);
assertEquals("Invalid character reference: invalid named referenece 'x'", errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testConsumeCharacterReferenceNamed() {
 tokeniser = new Tokeniser(new CharacterReader("&copy;"), errors);
 tokeniser.transition(TokeniserState.InCharacter);
 int[] ref = tokeniser.consumeCharacterReference(null, false);
 assertEquals('©', ref[0]);
 }

 @Test
 public void testErrorHandling() {
 tokeniser = new Tokeniser(new CharacterReader(""), errors);
 reader.advance();
 tokeniser.error(TokeniserState.Data);
 errors.assertErrorCount(1);
 assertEquals("Unexpected character ' ' in input state [Data]", errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testEofErrorHandling() {
 tokeniser = new Tokeniser(new CharacterReader(""), errors);
 reader.advance();
 tokeniser.eofError(TokeniserState.Data);
 errors.assertErrorCount(1);
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser = new Tokeniser(new CharacterReader("&invalid"), errors);
 tokeniser.consumeCharacterReference(null, false);
 errors.assertErrorCount(1);
 assertEquals("Invalid character reference: invalid named referenece 'invalid'", errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testErrorHandlingWhenInTag() {
 tokeniser = new Tokeniser(new CharacterReader("<script>Test</script>"), errors);
 tokeniser.transition(TokeniserState.InTagName);
 reader.advance();
 tokeniser.error(TokeniserState.AfterTagName);
 errors.assertErrorCount(1);
 assertEquals("Unexpected character 'T' in input state [AfterTagName]", errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testErrorHandlingWhenInCharacter() {
 tokeniser = new Tokeniser(new CharacterReader("<script>Test</script>"), errors);
 tokeniser.transition(TokeniserState.InCharacter);
 reader.advance();
 tokeniser.error(TokeniserState.Data);
 errors.assertErrorCount(1);
 assertEquals("Unexpected character 'T' in input state [Data]", errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testErrorHandlingWhenInAttributeValue() {
 tokeniser = new Tokeniser(new CharacterReader("<p data-test=\"Test\"></p>"), errors);
 tokeniser.transition(TokeniserState.InAttributeValue);
 reader.advance();
 tokeniser.error(TokeniserState.Data);
 errors.assertErrorCount(1);
 assertEquals("Unexpected character ' ' in input state [Data]", errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testDoctypeHandling() {
 tokeniser = new Tokeniser(new CharacterReader("<!DOCTYPE html>"), errors);
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type());
 }

 @Test
 public void testCommentHandling() {
 tokeniser = new Tokeniser(new CharacterReader("<!-- Test -->"), errors);
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type());
 }

 @Test
 public void testTagHandling() {
 tokeniser = new Tokeniser(new CharacterReader("<p>Test</p>"), errors);
 tokeniser.transition(TokeniserState.InTagName);
 tokeniser.read();
 tokeniser.emit("<p>");
 tokeniser.transition(TokeniserState.AfterTagName);
 tokeniser.read();
 tokeniser.emit("</p>");
 tokeniser.emitTagPending();
 token = tokeniser.read();
 assertEquals("p", token.tagName());
 assertEquals(Token.TokenType.EndTag, token.type());
 }

 @Test
 public void testTagHandlingWhenSelfClosingTag() {
 tokeniser = new Tokeniser(new CharacterReader("<img src=\"image.jpg\" />"), errors);
 tokeniser.transition(TokeniserState.InTagName);
 tokeniser.read();
 tokeniser.emit("<img");
 tokeniser.transition(TokeniserState.InAttributeName);
 tokeniser.read();
 tokeniser.emit(" src=\"image.jpg\"");
 tokeniser.transition(TokeniserState.AfterAttributeName);
 tokeniser.read();
 tokeniser.emit(" />");
 tokeniser.emitTagPending();
 token = tokeniser.read();
 assertEquals("img", token.tagName());
 assertTrue(token.isSelfClosing());
 }

 @Test
 public void testUnescapeEntities() {
 tokeniser = new Tokeniser(new CharacterReader("&amp; &lt; &gt; &quot; &apos; &nbsp;"), errors);
 String unescaped = tokeniser.unescapeEntities(false);
 assertEquals("&amp; &lt; &gt; &quot; &apos; &nbsp;", unescaped);
 }

 @Test
 public void testUnescapeEntitiesInAttribute() {
 tokeniser = new Tokeniser(new CharacterReader("<p data-test=\"&amp; &lt; &gt; &quot; &apos; &nbsp;\">"), errors);
 String unescaped = tokeniser.unescapeEntities(true);
 assertEquals("&amp; &lt; &gt; &quot; &apos; &nbsp;", unescaped);
 }
}