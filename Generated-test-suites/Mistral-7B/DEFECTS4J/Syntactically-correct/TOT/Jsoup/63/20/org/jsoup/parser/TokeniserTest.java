package org.jsoup.parser;

import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token;
import org.jsoup.parser.ParseError;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 tokeniser = new Tokeniser(new InputStreamReader(new ByteArrayInputStream(new byte[]{}), "UTF-8"), new ParseErrorList());
 errors = tokeniser.getErrors();
 }

 @Test
 public void testEmptyInput() {
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Eof, token.type);
 }

 @Test
 public void testSingleCharacterInput() {
 tokeniser.emit("a");
 tokeniser.emit('a');
 tokeniser.transition(TokeniserState.Char);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals('a', token.charPositionInLine());
 }

 @Test
 public void testMultipleCharacterInput() {
 tokeniser.emit("abcd");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.transition(TokeniserState.Data);
 Token token = tokeniser.read();
 String expected = "abcd";
 Assert.assertEquals(Token.TokenType.Data, token.type);
 Assert.assertEquals(expected, token.data());
 }

 @Test
 public void testSelfClosingTagInput() {
 tokeniser.emit("<img/>");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.transition(TokeniserState.SelfClosing);
 Token tagStart = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tagStart.type);
 Assert.assertEquals("img", tagStart.tagName());

 tokeniser.emitEndTag();
 Token tagEnd = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tagEnd.type);
 Assert.assertEquals("img", tagEnd.tagName());
 }

 @Test
 public void testNonSelfClosingTagInput() {
 tokeniser.emit("<p>Hello world!</p>");
 tokeniser.transition(TokeniserState.StartTag);
 Token tagStart = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tagStart.type);
 Assert.assertEquals("p", tagStart.tagName());

 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.Data);
 Token tagEnd2 = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tagEnd2.type);
 Assert.assertEquals("p", tagEnd2.tagName());
 }

 @Test
 public void testTagNameInput() {
 tokeniser.emit("<tagName attr=\"value\">Content</tagName>");
 tokeniser.transition(TokeniserState.StartTag);
 Token tagStart = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tagStart.type);
 Assert.assertEquals("tagName", tagStart.tagName());

 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.Data);
 Token tagEnd = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tagEnd.type);
 Assert.assertEquals("tagName", tagEnd.tagName());
 }

 @Test
 public void testTagWithAttributesInput() {
 tokeniser.emit("<tag attr=\"value\" class=\"classname\" id=\"idvalue\">Content</tag>");
 tokeniser.transition(TokeniserState.StartTag);
 Token tagStart = tokeniser.read();
 String expectedTagName = "tag";
 Assert.assertEquals(Token.TokenType.StartTag, tagStart.type);
 Assert.assertEquals(expectedTagName, tagStart.tagName());

 Token attributes = tagStart.attributes();
 Assert.assertEquals(3, attributes.size());
 Assert.assertEquals("value", attributes.keyAt(0));
 Assert.assertEquals("value", attributes.valueAt(0));
 Assert.assertEquals("classname", attributes.keyAt(1));
 Assert.assertEquals("classname", attributes.valueAt(1));
 Assert.assertEquals("idvalue", attributes.keyAt(2));
 Assert.assertEquals("idvalue", attributes.valueAt(2));

 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.Data);
 Token tagEnd = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tagEnd.type);
 Assert.assertEquals(expectedTagName, tagEnd.tagName());
 }

 @Test
 public void testTagWithEmptyAttributesInput() {
 tokeniser.emit("<tag attr=\" value=\" class=\" classname\" id=\" idvalue \"/>");
 tokeniser.transition(TokeniserState.StartTag);
 Token tagStart = tokeniser.read();
 String expectedTagName = "tag";
 Assert.assertEquals(Token.TokenType.StartTag, tagStart.type);
 Assert.assertEquals(expectedTagName, tagStart.tagName());

 Token attributes = tagStart.attributes();
 Assert.assertEquals(3, attributes.size());
 Assert.assertEquals("value", attributes.keyAt(0));
 Assert.assertNull(attributes.valueAt(0));
 Assert.assertEquals("classname", attributes.keyAt(1));
 Assert.assertNull(attributes.valueAt(1));
 Assert.assertEquals("idvalue", attributes.keyAt(2));
 Assert.assertNull(attributes.valueAt(2));

 tokeniser.emitEndTag();
 tokeniser.transition(TokeniserState.Data);
 Token tagEnd = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tagEnd.type);
 Assert.assertEquals("tag", tagEnd.tagName());
 }

 @Test
 public void testErrorHandlingInput() {
 tokeniser.emit("<script>console.log('Hello world');</script>");
 tokeniser.transition(TokeniserState.Script);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // consume <script>

 tokeniser.emit("console.log('Hello world');</script>"); // missing >
 tokeniser.transition(TokeniserState.Data);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Error, token.type);
 Assert.assertTrue(errors.size() > 0);
 Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Script]", errors.get(0).getMessage());
 }

 @Test
 public void testErrorHandlingInput2() {
 tokeniser.emit("<script>console.log('Hello world');</script>");
 tokeniser.transition(TokeniserState.Script);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // consume <script>

 tokeniser.emit("console.log('Hello world');<script>"); // missing >
 tokeniser.transition(TokeniserState.Script);
 tokeniser.transition(TokeniserState.Data);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Error, token.type);
 Assert.assertTrue(errors.size() > 0);
 Assert.assertEquals("Invalid character reference: unexpected '\\<'", errors.get(0).getMessage());
 }

 @Test
 public void testErrorHandlingInput3() {
 tokeniser.emit("<script>console.log('Hello world');</script>");
 tokeniser.transition(TokeniserState.Script);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // consume <script>

 tokeniser.emit("console.log(&apos;Hello world&apos;);</script>"); // invalid character reference
 tokeniser.transition(TokeniserState.Data);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Error, token.type);
 Assert.assertTrue(errors.size() > 0);
 Assert.assertEquals("Invalid character reference: unexpected '&apos'", errors.get(0).getMessage());
 }
}