package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setUp() throws IOException {
 byte[] html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>".getBytes();
 reader = new CharArrayReader(new String(html).toCharArray());
 tokeniser = new Tokeniser(reader);
 errors = tokeniser.getErrors();
 }

 @After
 public void tearDown() {
 reader.close();
 }

 @Test
 public void testRead_NormalFlow() throws IOException {
 Token token;
 for (int i = 0; i < 10; i++) {
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 assertTrue(token.getData().matches("[\\x00-\\x7F]"));
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_PendingEmit() throws IOException {
 tokeniser.emit(new Token.Character('A'));
 tokeniser.read();
 }

 @Test
 public void testRead_SelfClosingFlagAcknowledged() throws IOException {
 tokeniser.emit(new Token.StartTag("selfClosingTag", null, null, null, true));
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.read();
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_SelfClosingFlagNotAcknowledged() throws IOException {
 tokeniser.emit(new Token.StartTag("selfClosingTag", null, null, null, true));
 tokeniser.read();
 }

 @Test
 public void testRead_EndTag() throws IOException {
 tokeniser.emit(new Token.StartTag("p", null, null, null, false));
 tokeniser.emit(new Token.Character('H'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('o'));
 tokeniser.emit(new Token.Character(' '));
 tokeniser.emit(new Token.Character('.'));
 tokeniser.emit(new Token.Character('\n'));
 tokeniser.emit(new Token.Character('\r'));
 tokeniser.emit(new Token.EndTag("p"));
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 }

 @Test
 public void testEmit_Character() throws IOException {
 tokeniser.emit(new Token.Character('A'));
 tokeniser.read();
 tokeniser.emit(new Token.Character('B'));
 tokeniser.read();
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals('B', tokeniser.read().getData().charAt(0));
 }

 @Test
 public void testEmit_StartTag() throws IOException {
 tokeniser.read();
 tokeniser.emit(new Token.StartTag("p", null, null, null, false));
 tokeniser.emit(new Token.Character('H'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('o'));
 tokeniser.read();
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("p", tokeniser.read().getData().tagName);
 }

 @Test
 public void testEmit_EndTag() throws IOException {
 tokeniser.emit(new Token.StartTag("p", null, null, null, false));
 tokeniser.emit(new Token.Character('H'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('o'));
 tokeniser.emit(new Token.Character(' '));
 tokeniser.emit(new Token.Character('.'));
 tokeniser.emit(new Token.Character('\n'));
 tokeniser.emit(new Token.Character('\r'));
 tokeniser.emit(new Token.EndTag("p"));
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 }

 @Test
 public void testEmit_Comment() throws IOException {
 tokeniser.read();
 tokeniser.emit(new Token.Comment("Test Comment"));
 tokeniser.read();
 assertEquals(Token.TokenType.Comment, tokeniser.read().getType());
 assertEquals("Test Comment", tokeniser.read().getData().getData());
 }

 @Test
 public void testEmit_Doctype() throws IOException {
 tokeniser.read();
 tokeniser.emit(new Token.Doctype("HTML", null, null));
 tokeniser.read();
 assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 assertEquals("HTML", tokeniser.read().getData().doctype);
 }

 @Test
 public void testErrors() throws IOException {
 tokeniser.transition(TokeniserState.Error);
 tokeniser.error("Unexpected character in input: &");
 tokeniser.read();
 tokeniser.error("Unexpected end of file");
 tokeniser.read();
 assertEquals(2, errors.size());
 assertEquals("Unexpected character in input: &", errors.get(0).getMessage());
 assertEquals("Unexpectedly reached end of file (EOF)", errors.get(1).getMessage());
 }

 @Test
 public void testGetState() throws IOException {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition() throws IOException {
 tokeniser.transition(TokeniserState.Error);
 assertEquals(TokeniserState.Error, tokeniser.getState());
 tokeniser.transition(TokeniserState.Data);
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition() throws IOException {
 tokeniser.advance();
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testIsAppropriateEndTagToken() throws IOException {
 tokeniser.emit(new Token.StartTag("p", null, null, null, false));
 tokeniser.emit(new Token.Character('H'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('o'));
 tokeniser.read();
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new Token.StartTag("p", null, null, null, false));
 assertEquals(true, tokeniser.isAppropriateEndTagToken());
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new Token.StartTag("div", null, null, null, false));
 assertEquals(false, tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors() throws IOException {
 tokeniser.setTrackErrors(false);
 assertFalse(tokeniser.isTrackErrors());
 tokeniser.setTrackErrors(true);
 assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testCreateTagPending() throws IOException {
 tokeniser.createTagPending(true);
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 tokeniser.createTagPending(false);
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 }

 @Test
 public void testEmitTagPending() throws IOException {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.read();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 tokeniser.read();
 }

 @Test
 public void testCreateDoctypePending() throws IOException {
 tokeniser.createDoctypePending();
 assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 tokeniser.createTempBuffer();
 }

 @Test
 public void testIsTrackErrorsWithErrors() throws IOException {
 tokeniser.transition(TokeniserState.Error);
 tokeniser.error("Unexpected character in input: &");
 tokeniser.read();
 tokeniser.error("Unexpected end of file");
 tokeniser.read();
 tokeniser.setTrackErrors(false);
 assertEquals(0, errors.size());
 tokeniser.setTrackErrors(true);
 assertEquals(2, errors.size());
 }

}