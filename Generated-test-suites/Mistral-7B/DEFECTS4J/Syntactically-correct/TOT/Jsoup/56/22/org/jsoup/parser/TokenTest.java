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
 doctype.name.append("html");
 doctype.publicIdentifier.append("public ident");
 doctype.systemIdentifier.append("system ident");
 doctype.forceQuirks = true;

 Token expected = new Doctype();
 expected.name.append("html");
 expected.publicIdentifier.append("public ident");
 expected.systemIdentifier.append("system ident");
 expected.forceQuirks = true;

 Assert.assertEquals(expected.tokenType(), doctype.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), doctype.tokenType());
 }

 @Test
 public void testResetDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public ident");
 doctype.systemIdentifier.append("system ident");
 doctype.forceQuirks = true;

 doctype.reset();

 Assert.assertFalse(doctype.isForceQuirks());
 Assert.assertEquals(0, doctype.name.length());
 Assert.assertEquals(0, doctype.publicIdentifier.length());
 Assert.assertEquals(0, doctype.systemIdentifier.length());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.name("div");

 Token expected = new StartTag();
 expected.tagName = "div";
 expected.normalName = "div";

 Assert.assertEquals(expected.tokenType(), startTag.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), startTag.tokenType());
 }

 @Test
 public void testResetStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.tagName = "div";
 startTag.normalName = "div";

 startTag.reset();

 Assert.assertNull(startTag.tagName);
 Assert.assertNull(startTag.normalName);
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name("div");

 Token expected = new EndTag();
 expected.tagName = "div";
 expected.normalName = "div";

 Assert.assertEquals(expected.tokenType(), endTag.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), endTag.tokenType());
 }

 @Test
 public void testResetEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.tagName = "div";
 endTag.normalName = "div";

 endTag.reset();

 Assert.assertNull(endTag.tagName);
 Assert.assertNull(endTag.normalName);
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");

 Token expected = new Comment();
 expected.data.append("This is a comment");

 Assert.assertEquals(expected.tokenType(), comment.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), comment.tokenType());
 }

 @Test
 public void testResetCommentToken() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");

 comment.reset();

 Assert.assertEquals(0, comment.data.length());
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("Hello World!");

 Token expected = new Character();
 expected.data = "Hello World!";

 Assert.assertEquals(expected.tokenType(), character.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), character.tokenType());
 }

 @Test
 public void testResetCharacterToken() {
 Character character = new Character();
 character.data = "Hello World!";

 character.reset();

 Assert.assertNull(character.data);
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 Token expected = new EOF();

 Assert.assertEquals(expected.tokenType(), eof.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), eof.tokenType());
 }

 @Test
 public void testResetEOFToken() {
 EOF eof = new EOF();

 eof.reset();

 Assert.assertEquals(TokenType.EOF, eof.tokenType());
 }

 @Test (expected = AssertionError.class)
 public void testInvalidTokenType() {
 Token unknownToken = new Object();

 Assert.assertNotEquals(TokenType.Doctype, unknownToken.tokenType());
 }

 @Test
 public void testTokenResetAllTokens() {
 Doctype doctype = new Doctype();
 StartTag startTag = new StartTag();
 EndTag endTag = new EndTag();
 Comment comment = new Comment();
 Character character = new Character();
 EOF eof = new EOF();

 // Reset all tokens
 doctype.reset();
 startTag.reset();
 endTag.reset();
 comment.reset();
 character.reset();
 eof.reset();

 // Test all tokens are reset correctly
 Assert.assertEquals(0, doctype.name.length());
 Assert.assertEquals(0, doctype.publicIdentifier.length());
 Assert.assertEquals(0, doctype.systemIdentifier.length());
 Assert.assertFalse(doctype.isForceQuirks());

 Assert.assertNull(startTag.tagName);
 Assert.assertNull(startTag.normalName);

 Assert.assertEquals(0, endTag.tagName.length());
 Assert.assertNull(endTag.normalName);

 Assert.assertEquals(0, comment.data.length());

 Assert.assertNull(character.data);

 Assert.assertEquals(TokenType.EOF, eof.tokenType());
 }
 }