import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getPublicIdentifier().equals("");
 doctype.getSystemIdentifier().equals("");
 doctype.isForceQuirks() == false;
 }

 @Test
 public void testDoctypeTokenWithPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getPublicIdentifier().equals("_xml");
 doctype.getSystemIdentifier().equals("/myapp/_definst.dtd");
 doctype.isForceQuirks() == false;
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeTokenWithForceQuirksTrue() {
 Doctype doctype = new Doctype();
 doctype.isForceQuirks() = true; // assignment should not be possible
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag("html");
 startTag.name().equals("html");
 assertNull(startTag.getAttributes());
 }

 @Test
 public void testStartTagTokenWithAttributes() {
 StartTag startTag = new StartTag("html").name("html").appendAttributeName("lang").appendAttributeValue("en");
 startTag.name().equals("html");
 assertEquals("lang", startTag.getAttributes().keySet().iterator().next().toString());
 assertEquals("en", startTag.getAttributes().get("lang").getValue());
 }

 @Test(expected = AssertionError.class)
 public void testStartTagTokenWithNullName() {
 new StartTag(null); // name cannot be null
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag("html");
 endTag.name().equals("html");
 }

 @Test(expected = AssertionError.class)
 public void testEndTagTokenWithNullName() {
 new EndTag(null); // name cannot be null
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.getData().equals("");
 }

 @Test
 public void testCommentTokenWithData() {
 Comment comment = new Comment();
 comment.getData().append("This is a comment");
 comment.getData().equals("This is a comment");
 }

 @Test(expected = AssertionError.class)
 public void testCommentTokenWithNullData() {
 new Comment(null); // data cannot be null
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character("A");
 character.getData().equals("A");
 }

 @Test(expected = AssertionError.class)
 public void testCharacterTokenWithNullData() {
 new Character(null); // data cannot be null
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();
 assertTrue(eof.isEOF());
 }
}