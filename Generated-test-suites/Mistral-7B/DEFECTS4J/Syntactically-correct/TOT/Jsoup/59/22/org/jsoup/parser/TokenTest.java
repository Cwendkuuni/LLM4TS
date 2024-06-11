package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 doctype.pubSysKey("DTD XHTML 1.0 Strict");
 doctype.publicIdentifier("W3C//DTD XHTML 1.0 Strict//EN");
 doctype.systemIdentifier("/path/to/xhtml10_strict.dtd");
 doctype.forceQuirks(true);

 Doctype expectedDoctype = new Doctype();
 expectedDoctype.name("html");
 expectedDoctype.pubSysKey("DTD XHTML 1.0 Strict");
 expectedDoctype.publicIdentifier("W3C//DTD XHTML 1.0 Strict//EN");
 expectedDoctype.systemIdentifier("/path/to/xhtml10_strict.dtd");
 expectedDoctype.forceQuirks(true);

 Token actualDoctype = doctype.reset();

 Assert.assertEquals(expectedDoctype.getClass(), actualDoctype.getClass());
 Assert.assertEquals(expectedDoctype.tokenType(), actualDoctype.tokenType());
 Assert.assertEquals(expectedDoctype.getName(), actualDoctype.asDoctype().getName());
 Assert.assertEquals(expectedDoctype.getPubSysKey(), actualDoctype.asDoctype().getPubSysKey());
 Assert.assertEquals(expectedDoctype.getPublicIdentifier(), actualDoctype.asDoctype().getPublicIdentifier());
 Assert.assertEquals(expectedDoctype.getSystemIdentifier(), actualDoctype.asDoctype().getSystemIdentifier());
 Assert.assertEquals(expectedDoctype.isForceQuirks(), actualDoctype.asDoctype().isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.nameAttr("html", new Attributes().put("lang", "en"));

 StartTag expectedStartTag = new StartTag();
 expectedStartTag.nameAttr("html", new Attributes().put("lang", "en"));

 Token actualStartTag = startTag.reset();

 Assert.assertEquals(expectedStartTag.getClass(), actualStartTag.getClass());
 Assert.assertEquals(expectedStartTag.tokenType(), actualStartTag.tokenType());
 Assert.assertEquals(expectedStartTag.name(), actualStartTag.asStartTag().name());
 Assert.assertEquals(expectedStartTag.isSelfClosing(), actualStartTag.asStartTag().isSelfClosing());
 Assert.assertEquals(expectedStartTag.getAttributes(), actualStartTag.asStartTag().getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name("html");

 EndTag expectedEndTag = new EndTag();
 expectedEndTag.name("html");

 Token actualEndTag = endTag.reset();

 Assert.assertEquals(expectedEndTag.getClass(), actualEndTag.getClass());
 Assert.assertEquals(expectedEndTag.tokenType(), actualEndTag.tokenType());
 Assert.assertEquals(expectedEndTag.name(), actualEndTag.asEndTag().name());
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data("This is a comment.");

 Comment expectedComment = new Comment();
 expectedComment.data("This is a comment.");

 Token actualComment = comment.reset();

 Assert.assertEquals(expectedComment.getClass(), actualComment.getClass());
 Assert.assertEquals(expectedComment.tokenType(), actualComment.tokenType());
 Assert.assertEquals(expectedComment.getData(), actualComment.asComment().getData());
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("&eacute;");

 Character expectedCharacter = new Character();
 expectedCharacter.data("á");

 Token actualCharacter = character.reset();

 Assert.assertEquals(expectedCharacter.getClass(), actualCharacter.getClass());
 Assert.assertEquals(expectedCharacter.tokenType(), actualCharacter.tokenType());
 Assert.assertEquals(expectedCharacter.getData(), actualCharacter.asCharacter().getData());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidToken() {
 // This test should fail with an AssertionError due to unsupported token type
 InvalidToken invalidToken = new InvalidToken();
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 EOF expectedEof = new EOF();

 Token actualEof = eof.reset();

 Assert.assertEquals(expectedEof.getClass(), actualEof.getClass());
 Assert.assertEquals(expectedEof.tokenType(), actualEof.tokenType());
 }
}