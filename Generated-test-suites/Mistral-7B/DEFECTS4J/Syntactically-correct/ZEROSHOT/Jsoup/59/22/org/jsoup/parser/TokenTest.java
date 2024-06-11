package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 doctype.setForceQuirks(true);
 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertTrue(doctype.isForceQuirks());
 Doctype resetDoctype = doctype.reset();
 assertEquals(doctype, resetDoctype);
 }

 @Test
 public void testResetDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 doctype.setForceQuirks(true);
 doctype.reset();
 assertNull(doctype.getName());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 Tag tag = new StartTag();
 tag.name("div");
 assertEquals("StartTag", tag.tokenType());
 assertEquals("div", tag.name());
 tag.name("div").normalName("book");
 assertEquals("book", tag.normalName());
 tag.getAttributes().put("class", "red");
 tag.getAttributes().put("id", "box");
 tag.finaliseTag();
 assertEquals(2, tag.getAttributes().size());
 tag.reset();
 assertNull(tag.getName());
 assertNull(tag.getAttributes());
 }

 @Test
 public void testResetStartTag() {
 Tag tag = new StartTag();
 tag.name("div");
 tag.reset();
 assertNull(tag.getName());
 assertNull(tag.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name("div");
 assertEquals("EndTag", endTag.tokenType());
 assertEquals("div", endTag.name());
 endTag.reset();
 assertNull(endTag.getName());
 }

 @Test
 public void testResetEndTag() {
 EndTag endTag = new EndTag();
 endTag.name("div");
 endTag.reset();
 assertNull(endTag.getName());
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.getData("This is a comment.");
 assertEquals("Comment", comment.tokenType());
 assertEquals("This is a comment.", comment.getData());
 comment.reset();
 assertNull(comment.getData());
 }

 @Test
 public void testResetComment() {
 Comment comment = new Comment();
 comment.getData("This is a comment.");
 comment.reset();
 assertNull(comment.getData());
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("A");
 assertEquals("Character", character.tokenType());
 assertEquals("A", character.getData());
 character.reset();
 assertNull(character.getData());
 }

 @Test
 public void testResetCharacter() {
 Character character = new Character();
 character.data("A");
 character.reset();
 assertNull(character.getData());
 }

 @Test
 public void testEofToken() {
 EOF eof = new EOF();
 assertEquals("EOF", eof.tokenType());
 eof.reset();
 assertTrue(eof.isEOF());
 }

 @Test
 public void testResetEof() {
 EOF eof = new EOF();
 eof.reset();
 assertTrue(eof.isEOF());
 }
}