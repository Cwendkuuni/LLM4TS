package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.jsoup.internal.Normalizer.lowerCase;

import java.util.Arrays;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 Doctype resetDoctypeToken = doctypeToken.reset();

 assertEquals("Doctype", doctypeToken.tokenType());
 assertEquals("Doctype", resetDoctypeToken.tokenType());

 doctypeToken.getName("html");
 assertEquals("html", doctypeToken.getName());
 assertEquals("html", resetDoctypeToken.getName());

 doctypeToken.setPubSysKey("pubsyskey");
 assertEquals("pubsyskey", doctypeToken.getPubSysKey());
 assertNull(resetDoctypeToken.getPubSysKey());

 doctypeToken.setPublicIdentifier("publicid");
 assertEquals("publicid", doctypeToken.getPublicIdentifier());
 assertNull(resetDoctypeToken.getPublicIdentifier());

 doctypeToken.setSystemIdentifier("systemid");
 assertEquals("systemid", doctypeToken.getSystemIdentifier());
 assertNull(resetDoctypeToken.getSystemIdentifier());

 doctypeToken.setForceQuirks(true);
 assertTrue(doctypeToken.isForceQuirks());
 assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 Tag tagToken = new StartTag();
 Tag resetTagToken = tagToken.reset();

 assertEquals("StartTag", tagToken.tokenType());
 assertEquals("StartTag", resetTagToken.tokenType());

 tagToken.name("div");
 assertEquals("div", tagToken.name());
 assertEquals("div", resetTagToken.name());

 tagToken.newAttribute().name("class").value("classvalue");
 assertEquals("class=classvalue", tagToken.toString());
 assertNull(resetTagToken.getAttributes());

 tagToken.finaliseTag();
 assertEquals("div class=classvalue", tagToken.toString());
 assertNull(resetTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 EndTag resetEndTagToken = endTagToken.reset();

 assertEquals("EndTag", endTagToken.tokenType());
 assertEquals("EndTag", resetEndTagToken.tokenType());

 endTagToken.name("div");
 assertEquals("div", endTagToken.name());
 assertEquals("div", resetEndTagToken.name());

 assertEquals("</div>", endTagToken.toString());
 assertNull(resetEndTagToken.getAttributes());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 Comment resetCommentToken = commentToken.reset();

 assertEquals("Comment", commentToken.tokenType());
 assertEquals("Comment", resetCommentToken.tokenType());

 commentToken.getData("This is a comment");
 assertEquals("This is a comment", commentToken.getData());
 assertNull(resetCommentToken.getData());

 assertEquals("<!--This is a comment-->", commentToken.toString());
 assertNull(resetCommentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 Character resetCharacterToken = characterToken.reset();

 assertEquals("Character", characterToken.tokenType());
 assertEquals("Character", resetCharacterToken.tokenType());

 characterToken.data("A");
 assertEquals("A", characterToken.getData());
 assertEquals("A", resetCharacterToken.getData());

 assertEquals("A", characterToken.toString());
 assertNull(resetCharacterToken.getData());
 }

 @Test
 public void testCDataToken() {
 CData cDataToken = new CData("CDATA[This is CData]");
 CData resetCDataToken = cDataToken.reset();

 assertEquals("Character", cDataToken.tokenType());
 assertEquals("Character", resetCDataToken.tokenType());

 assertEquals("CDATA[This is CData]", cDataToken.toString());
 assertNull(resetCDataToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();
 EOF resetEofToken = eofToken.reset();

 assertEquals("EOF", eofToken.tokenType());
 assertEquals("EOF", resetEofToken.tokenType());

 assertFalse(eofToken.isDoctype());
 assertFalse(eofToken.isStartTag());
 assertFalse(eofToken.isEndTag());
 assertFalse(eofToken.isComment());
 assertFalse(eofToken.isCharacter());
 assertFalse(eofToken.isCData());
 assertTrue(eofToken.isEOF());

 assertNull(eofToken.getName());
 assertNull(eofToken.getPubSysKey());
 assertNull(eofToken.getPublicIdentifier());
 assertNull(eofToken.getSystemIdentifier());
 assertNull(eofToken.getAttributes());
 assertNull(eofToken.getData());
 }

 @Test
 public void testTokenReset() {
 Doctype doctypeToken = new Doctype();
 Tag tagToken = new StartTag();
 Comment commentToken = new Comment();
 Character characterToken = new Character();
 EOF eofToken = new EOF();

 // Testing Doctype reset
 assertFalse(doctypeToken.isStartTag());
 assertFalse(doctypeToken.isEndTag());
 assertFalse(doctypeToken.isComment());
 assertFalse(doctypeToken.isCharacter());
 assertFalse(doctypeToken.isCData());
 assertFalse(doctypeToken.isEOF());
 assertNull(doctypeToken.getData());
 assertNull(doctypeToken.getAttributes());
 assertNull(doctypeToken.getName());
 assertNull(doctypeToken.getPubSysKey());
 assertNull(doctypeToken.getPublicIdentifier());
 assertNull(doctypeToken.getSystemIdentifier());
 assertNull(doctypeToken.getPublicIdentifier());
 assertNull(doctypeToken.getSystemIdentifier());

 Doctype resetDoctypeToken = doctypeToken.reset();
 assertTrue(resetDoctypeToken.isDoctype());
 assertNull(resetDoctypeToken.getName());
 assertNull(resetDoctypeToken.getPubSysKey());
 assertNull(resetDoctypeToken.getPublicIdentifier());
 assertNull(resetDoctypeToken.getSystemIdentifier());
 assertNull(resetDoctypeToken.getPublicIdentifier());
 assertNull(resetDoctypeToken.getSystemIdentifier());

 // Testing Tag reset
 assertFalse(tagToken.isDoctype());
 assertFalse(tagToken.isStartTag());
 assertFalse(tagToken.isEndTag());
 assertFalse(tagToken.isComment());
 assertFalse(tagToken.isCharacter());
 assertFalse(tagToken.isCData());
 assertFalse(tagToken.isEOF());
 assertNull(tagToken.getData());
 assertNull(tagToken.getAttributes());
 assertNull(tagToken.getName());

 Tag resetTagToken = tagToken.reset();
 assertTrue(resetTagToken.isTag());
 assertNull(resetTagToken.getName());
 assertNull(resetTagToken.getAttributes());

 // Testing Comment reset
 assertFalse(commentToken.isDoctype());
 assertFalse(commentToken.isStartTag());
 assertFalse(commentToken.isEndTag());
 assertFalse(commentToken.isComment());
 assertFalse(commentToken.isCharacter());
 assertFalse(commentToken.isCData());
 assertFalse(commentToken.isEOF());
 assertNull(commentToken.getData());
 assertNull(commentToken.getAttributes());

 Comment resetCommentToken = commentToken.reset();
 assertTrue(resetCommentToken.isComment());
 assertNull(resetCommentToken.getData());

 // Testing Character reset
 assertFalse(characterToken.isDoctype());
 assertFalse(characterToken.isStartTag());
 assertFalse(characterToken.isEndTag());
 assertFalse(characterToken.isComment());
 assertFalse(characterToken.isCharacter());
 assertFalse(characterToken.isCData());
 assertFalse(characterToken.isEOF());
 assertNull(characterToken.getData());
 assertNull(characterToken.getAttributes());
 assertNull(characterToken.getName());

 Character resetCharacterToken = characterToken.reset();
 assertTrue(resetCharacterToken.isCharacter());
 assertNull(resetCharacterToken.getData());

 // Testing EOF reset
 assertFalse(eofToken.isDoctype());
 assertFalse(eofToken.isStartTag());
 assertFalse(eofToken.isEndTag());
 assertFalse(eofToken.isComment());
 assertFalse(eofToken.isCharacter());
 assertFalse(eofToken.isCData());
 assertTrue(eofToken.isEOF());
 assertNull(eofToken.getData());
 assertNull(eofToken.getAttributes());
 assertNull(eofToken.getName());
 assertNull(eofToken.getPubSysKey());
 assertNull(eofToken.getPublicIdentifier());
 assertNull(eofToken.getSystemIdentifier());

 EOF resetEofToken = eofToken.reset();
 assertTrue(resetEofToken.isEOF());
 }

 @Test
 public void testTokenTypeToStringMapping() {
 Token[] tokens = new Token[] { new Doctype(), new StartTag(), new EndTag(), new Comment(), new Character(), new CData(), new EOF() };

 Arrays.stream(tokens).forEach(token -> {
 String tokenTypeString = token.tokenType().toString();
 String expectedTokenTypeString;

 switch (token.tokenType()) {
 case TokenType.Doctype:
 expectedTokenTypeString = "DOCTYPE";
 break;
 case TokenType.StartTag:
 expectedTokenTypeString = "START_TAG";
 break;
 case TokenType.EndTag:
 expectedTokenTypeString = "END_TAG";
 break;
 case TokenType.Comment:
 expectedTokenTypeString = "COMMENT";
 break;
 case TokenType.Character:
 expectedTokenTypeString = "CHARACTER";
 break;
 case TokenType.CData:
 expectedTokenTypeString = "CDATA";
 break;
 case TokenType.EOF:
 expectedTokenTypeString = "EOF";
 break;
 default:
 throw new IllegalStateException("Unhandled TokenType: " + token.tokenType());
 }

 assertEquals(expectedTokenTypeString, tokenTypeString);
 });
 }
}