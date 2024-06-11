package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.getName("html");
 doctypeToken.setForceQuirks(true);

 assertEquals("Doctype", doctypeToken.tokenType());
 assertEquals("html", doctypeToken.getName());
 assertTrue(doctypeToken.isForceQuirks());

 Doctype resetDoctypeToken = doctypeToken.reset();
 assertNotSame(doctypeToken, resetDoctypeToken);
 assertEquals("Doctype", resetDoctypeToken.tokenType());
 assertNull(resetDoctypeToken.getName());
 assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 Tag tagToken = new StartTag();
 tagToken.name("html");
 tagToken.selfClosing(false);
 tagToken.attributes(new Attributes().put("class", "test"));

 assertEquals("StartTag", tagToken.tokenType().toString());
 assertEquals("html", tagToken.name());
 assertFalse(tagToken.isSelfClosing());
 assertNotNull(tagToken.getAttributes());
 assertEquals("class", tagToken.getAttributes().keySet().toArray()[0].toString());
 assertEquals("test", tagToken.getAttributes().value("class"));

 Tag resetTagToken = tagToken.reset();
 assertNotSame(tagToken, resetTagToken);
 assertEquals("StartTag", resetTagToken.tokenType().toString());
 assertNull(resetTagToken.name());
 assertNull(resetTagToken.getAttributes());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.getData("This is a comment");

 assertEquals("Comment", commentToken.tokenType().toString());
 assertEquals("This is a comment", commentToken.getData());

 Comment resetCommentToken = commentToken.reset();
 assertNotSame(commentToken, resetCommentToken);
 assertEquals("Comment", resetCommentToken.tokenType().toString());
 assertNull(resetCommentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("!");

 assertEquals("Character", characterToken.tokenType().toString());
 assertEquals("!", characterToken.getData());

 Character resetCharacterToken = characterToken.reset();
 assertNotSame(characterToken, resetCharacterToken);
 assertEquals("Character", resetCharacterToken.tokenType().toString());
 assertNull(resetCharacterToken.getData());
 }

 @Test
 public void testCDataToken() {
 CData cDataToken = new CData("<![CDATA[This is CData]]>");

 assertEquals("Character", cDataToken.tokenType().toString());
 assertEquals("<![CDATA[This is CData]]>", cDataToken.getData());

 CData resetCDataToken = cDataToken.reset();
 assertNotSame(cDataToken, resetCDataToken);
 assertEquals("Character", resetCDataToken.tokenType().toString());
 assertNull(resetCDataToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 assertEquals("EOF", eofToken.tokenType().toString());

 EOF resetEOFToken = eofToken.reset();
 assertNotSame(eofToken, resetEOFToken);
 assertEquals("EOF", resetEOFToken.tokenType().toString());
 }
}