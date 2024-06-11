package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.TokeniserState;
import org.junit.Assert.*;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("test.html");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() {
 Token token;
 do {
 token = tokeniser.read();
 System.out.println("Read token: " + token);
 } while (token.type != Token.TokenType.EOF);
 }

 @Test
 public void testEmit() {
 tokeniser.emit(new StartTag("html"));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new Character('t'));
 tokeniser.emit(new Character('e'));
 tokeniser.emit(new Character('st'));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new StartTag("body"));
 tokeniser.emit(new Character('\n'));
 tokeniser.emit(new Comment("<!-- This is a comment -->"));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new Character('--'));
 tokeniser.emit(new Character('>'));
 tokeniser.emit(new EndTag("body"));
 tokeniser.emit(new Character('\n'));
 tokeniser.emit(new EndTag("html"));
 tokeniser.emit(new Character('\0')); // should be ignored

 Token expectedToken = new StartTag("html");
 assertEquals(expectedToken, tokeniser.read());
 tokeniser.emitPending();
 expectedToken = new Character(' ');
 assertEquals(expectedToken, tokeniser.read());
 // ... continue testing with other expected tokens
 }

 @Test
 public void testErrorHandling() {
 tokeniser = new Tokeniser(new CharacterReader("test_error.html"), errors);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read(); // should throw ParseError
 Assert.assertNotNull(tokeniser.errors.getError());
 }

 @Test
 public void testConsumeCharacterReference() {
 int[] result;
 // test valid character references
 result = tokeniser.consumeCharacterReference(null, false);
 assertEquals(Arrays.asList(0xA9), Arrays.asList(result)); // ©
 result = tokeniser.consumeCharacterReference('x', false);
 assertEquals(Character.toCodePoint('x'), result[0]);
 result = tokeniser.consumeCharacterReference("ampersand", false);
 assertEquals(Character.toCodePoint('&'), result[0]);
 result = tokeniser.consumeCharacterReference("apos", false);
 assertEquals(Character.QUOTE, result[0]);
 result = tokeniser.consumeCharacterReference("apos", true); // in attribute
 assertEquals(Character.QUOTE, result[0]);
 result = tokeniser.consumeCharacterReference("quot", false);
 assertEquals(Character.QUOTE, result[0]);
 result = tokeniser.consumeCharacterReference("quot", true); // in attribute
 assertEquals(Character.QUOTE, result[0]);
 result = tokeniser.consumeCharacterReference("apos;", false); // semicolon doesn't matter outside an attribute
 assertEquals(Character.QUOTE, result[0]);
 result = tokeniser.consumeCharacterReference("#123;", false); // numbered reference
 assertEquals(Character.toCodePoint(0x007B), result[0]); // {
 result = tokeniser.consumeCharacterReference("#x41;", false); // hexadecimal reference
 assertEquals(Character.toCodePoint(0x0041), result[0]); // A
 result = tokeniser.consumeCharacterReference("#x41#x42;", false); // multiple codepoints
 assertEquals(Arrays.asList(0x0041, 0x0042), Arrays.asList(result));
 result = tokeniser.consumeCharacterReference("&nbsp;", false); // named reference
 assertEquals(Character.WHITESPACE, result[0]);
 result = tokeniser.consumeCharacterReference("&nbsp;", true); // in attribute
 assertEquals(Character.WHITESPACE, result[0]);
 result = tokeniser.consumeCharacterReference("&#x00A9;", false); // unicode reference
 assertEquals(Character.toCodePoint(0x00A9), result[0]); // ©

 // test invalid character references
 result = tokeniser.consumeCharacterReference(null, false); // empty reference
 assertNull(result);
 result = tokeniser.consumeCharacterReference(null, true); // empty reference in attribute
 assertNull(result);
 result = tokeniser.consumeCharacterReference("invalid", false); // invalid name
 assertNull(result);
 result = tokeniser.consumeCharacterReference("&", false); // reference to '&' itself
 assertNull(result);
 result = tokeniser.consumeCharacterReference("&foo", false); // unknown named reference
 assertNull(result);
 result = tokeniser.consumeCharacterReference("&foo;", false); // unknown named reference with semicolon
 assertNull(result);
 result = tokeniser.consumeCharacterReference("#foo", false); // unknown numbered reference
 assertNull(result);
 result = tokeniser.consumeCharacterReference("&foo;bar", false); // multiple invalid references
 assertNull(result);
 }
}