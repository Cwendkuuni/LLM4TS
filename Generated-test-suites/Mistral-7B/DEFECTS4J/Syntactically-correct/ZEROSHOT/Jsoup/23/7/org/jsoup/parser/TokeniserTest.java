package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser.TokeniserState;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testDefaultState() {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadCharacter() {
 char character = 'a';
 when(reader.current()).thenReturn(character);
 when(reader.read()).thenReturn(new Character("a"));
 tokeniser.read();
 assertEquals(character, tokeniser.read().text);
 }

 @Test
 public void testReadStartTag() {
 String tagName = "div";
 when(reader.matchConsume('<')).thenReturn(true);
 when(reader.current()).thenReturn('d');
 when(reader.consumeLetterSequence()).thenReturn(tagName);
 when(reader.matchConsume('>')).thenReturn(true);
 tokeniser.transition(TokeniserState.InTag);
 tokeniser.read();
 Token expected = new StartTag("div");
 expected.setSelfClosing(false);
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadEndTag() {
 String tagName = "div";
 when(reader.matchConsume('/')).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn(tagName);
 when(reader.matchConsume('>')).thenReturn(true);
 tokeniser.transition(TokeniserState.InTag);
 tokeniser.read();
 tokeniser.read();
 Token expected = new EndTag("div");
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadComment() {
 when(reader.matchConsume('<!--')).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn("comment");
 when(reader.matchConsume('-->')).thenReturn(true);
 tokeniser.transition(TokeniserState.InComment);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token expected = new Comment("comment");
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadDoctype() {
 when(reader.matchConsume('<')).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn("!DOCTYPE");
 when(reader.consumeLetterSequence()).thenReturn("html");
 when(reader.consumeLetterSequence()).thenReturn(">");
 tokeniser.transition(TokeniserState.InDoctype);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token expected = new Doctype("html");
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadCharacterReference() {
 When(reader.matchConsume("#")).thenReturn(true);
 When(reader.consumeHexSequence()).thenReturn("41");
 when(reader.matchConsume(";")).thenReturn(true);
 Token expected = new Character("A");
 tokeniser.transition(TokeniserState.InCharacterReference);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadInvalidCharacterReference() {
 when(reader.matchConsume("#")).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn("invalid");
 when(reader.matchConsume(";")).thenReturn(false);
 tokeniser.transition(TokeniserState.InCharacterReference);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadNamedCharacterReference() { 
 when(reader.matchConsume("#")).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn("amp");
 when(reader.matchConsume(";")).thenReturn(true);
 when(Entities.getCharacterByName("amp")).thenReturn('&');
 tokeniser.transition(TokeniserState.InCharacterReference);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token expected = new Character("&");
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadSelfClosingTag() {
 String tagName = "img";
 when(reader.matchConsume('<')).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn(tagName);
 when(reader.consumeLetterSequence()).thenReturn(" src=\"image.jpg\"");
 when(reader.matchConsume('/>')).thenReturn(true);
 tokeniser.transition(TokeniserState.InTag);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token expected = new StartTag("img").setSelfClosing(true);
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadTagWithAttributes() {
 String tagName = "input";
 when(reader.matchConsume('<')).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn(tagName);
 when(reader.consumeLetterSequence()).thenReturn(" type=\"text\" name=\"username\"");
 when(reader.matchConsume('>')).thenReturn(true);
 tokeniser.transition(TokeniserState.InTag);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token expected = new StartTag("input").setTagName("input").setTagNameLower("input").setTagNameLowerCamelCase("input")
 .setAttributes(List.of(new Token.Attribute("type", "text"), new Token.Attribute("name", "username")));
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadEmptyElement() {
 String tagName = "img";
 when(reader.matchConsume('<')).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn(tagName);
 when(reader.consumeLetterSequence()).thenReturn("/");
 when(reader.matchConsume('>')).thenReturn(true);
 tokeniser.transition(TokeniserState.InTag);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token expected = new EndTag("img");
 assertEquals(expected, tokeniser.read());
 }

 @Test
 public void testReadMixedContent() {
 when(reader.matchConsume('<')).thenReturn(true);
 when(reader.consumeLetterSequence()).thenReturn("script");
 when(reader.matchConsume(">"));
 tokeniser.transition(TokeniserState.InScript);
 tokeniser.read();
 tokeniser.emit("JavaScript code");
 tokeniser.emit(" goes here");
 tokeniser.emit(" <div id=\"test\">Hello World</div>");
 tokeniser.emit(" and here");
 tokeniser.emit(" </script>");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 List<Token> expected = new ArrayList<>();
 expected.add(new StartTag("script"));
 expected.add(new Character("JavaScript code goes here"));
 expected.add(new Comment(" and here"));
 expected.add(new EndTag("script"));
 expected.add(new StartTag("div"));
 expected.add(new Character("H"));
 expected.add(new Character("e"));
 expected.add(new Character("l"));
 expected.add(new Character("l"));
 expected.add(new Character("o"));
 expected.add(new Character(" "));
 expected.add(new Character("W"));
 expected.add(new Character("o"));
 expected.add(new Character("r"));
 expected.add(new Character("l"));
 expected.add(new Character("d"));
 expected.add(new Character(" "));
 expected.add(new Character('>'));
 expected.add(new EndTag("div"));
 assertEquals(expected, tokeniser.getTokens());
 }

 @Test(expected = AssertionError.class)
 public void testCharacterReferenceError() {
 when(reader.matchConsume("#")).thenReturn(true);
 when(reader.consumeHexSequence()).thenReturn("4100");
 when(reader.matchConsume(";")).thenReturn(false);
 tokeniser.transition(TokeniserState.InCharacterReference);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 }

 @Test
 public void testEOFError() {
 when(reader.matchConsume('>')).thenReturn(false);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.transition(TokeniserState.InTag);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.transition(TokeniserState.InScript);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read(); // this should trigger EOF error
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 assertEquals(0, tokeniser.getTokens().size());
 }
}