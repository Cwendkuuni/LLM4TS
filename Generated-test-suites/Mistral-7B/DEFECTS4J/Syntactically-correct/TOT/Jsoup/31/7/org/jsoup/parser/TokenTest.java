import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("PublicIdentifier");
 doctype.systemIdentifier.append("SystemIdentifier");
 doctype.forceQuirks = true;

 Token expectedDoctype = new Doctype();
 expectedDoctype.name.append("html");
 expectedDoctype.publicIdentifier.append("PublicIdentifier");
 expectedDoctype.systemIdentifier.append("SystemIdentifier");
 expectedDoctype.forceQuirks = true;

 Assert.assertEquals(expectedDoctype.tokenType(), doctype.tokenType());
 Assert.assertEquals(expectedDoctype.getClass().getSimpleName(), doctype.tokenType());
 Assert.assertEquals(expectedDoctype.toString(), doctype.toString());
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag("html");
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 startTag.appendAttributeValue("test");
 startTag.newAttribute();
 startTag.appendAttributeName("id");
 startTag.appendAttributeValue("testId");

 Token expectedStartTag = new StartTag("html");
 expectedStartTag.tagName = "html";
 expectedStartTag.attributes.put(new Attribute("class", "test"));
 expectedStartTag.attributes.put(new Attribute("id", "testId"));

 Assert.assertEquals(expectedStartTag.tokenType(), startTag.tokenType());
 Assert.assertEquals(expectedStartTag.getClass().getSimpleName(), startTag.tokenType());
 Assert.assertEquals(expectedStartTag.toString(), startTag.toString());
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag("html");

 Token expectedEndTag = new EndTag("html");
 expectedEndTag.tagName = "html";

 Assert.assertEquals(expectedEndTag.tokenType(), endTag.tokenType());
 Assert.assertEquals(expectedEndTag.getClass().getSimpleName(), endTag.tokenType());
 Assert.assertEquals(expectedEndTag.toString(), endTag.toString());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");

 Token expectedComment = new Comment();
 expectedComment.data.append("This is a comment");

 Assert.assertEquals(expectedComment.tokenType(), comment.tokenType());
 Assert.assertEquals(expectedComment.getClass().getSimpleName(), comment.tokenType());
 Assert.assertEquals(expectedComment.toString(), comment.toString());
 }

 @Test
 public void testCharacter() {
 Character character = new Character("&amp;");

 Token expectedCharacter = new Character("&amp;");
 expectedCharacter.data = "&amp;";

 Assert.assertEquals(expectedCharacter.tokenType(), character.tokenType());
 Assert.assertEquals(expectedCharacter.getClass().getSimpleName(), character.tokenType());
 Assert.assertEquals(expectedCharacter.toString(), character.toString());
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();

 Token expectedEOF = new EOF();

 Assert.assertEquals(expectedEOF.tokenType(), eof.tokenType());
 Assert.assertEquals(expectedEOF.getClass().getSimpleName(), eof.tokenType());
 Assert.assertEquals(expectedEOF.toString(), eof.toString());
 }

 @Test
 public void testDoctypeWithInvalidData() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("PublicIdentifier");
 doctype.systemIdentifier.append("SystemIdentifier");
 doctype.forceQuirks = true;
 doctype.systemIdentifier.append("invalid"); // invalid character

 try {
 Token expectedDoctype = new Doctype();
 expectedDoctype.name.append("html");
 expectedDoctype.publicIdentifier.append("PublicIdentifier");
 expectedDoctype.systemIdentifier.append("SystemIdentifier");
 expectedDoctype.forceQuirks = true;

 Assert.assertThrows(IllegalArgumentException.class, () -> {
 Assert.assertEquals(expectedDoctype.tokenType(), doctype.tokenType());
 });
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testStartTagWithInvalidName() {
 StartTag startTag = new StartTag("");
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 startTag.appendAttributeValue("test");
 startTag.newAttribute();
 startTag.appendAttributeName(""); // empty name
 startTag.appendAttributeValue("testId");

 try {
 Token expectedStartTag = new StartTag("html");
 expectedStartTag.tagName = "html";
 expectedStartTag.attributes.put(new Attribute("class", "test"));
 expectedStartTag.attributes.put(new Attribute("", "testId")); // empty name

 Assert.assertThrows(IllegalArgumentException.class, () -> {
 Assert.assertEquals(expectedStartTag.tokenType(), startTag.tokenType());
 });
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEndTagWithInvalidName() {
 EndTag endTag = new EndTag("");
 endTag.tagName = ""; // empty name

 try {
 Token expectedEndTag = new EndTag("html");
 expectedEndTag.tagName = "html";

 Assert.assertThrows(IllegalArgumentException.class, () -> {
 Assert.assertEquals(expectedEndTag.tokenType(), endTag.tokenType());
 });
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCommentWithEmptyData() {
 Comment comment = new Comment();
 comment.data.delete(0, comment.data.length()); // empty data

 Token expectedComment = new Comment();
 expectedComment.data.delete(0, expectedComment.data.length()); // empty data

 Assert.assertEquals(expectedComment.tokenType(), comment.tokenType());
 Assert.assertEquals(expectedComment.getClass().getSimpleName(), comment.tokenType());
 Assert.assertEquals(expectedComment.toString(), comment.toString());
 }

 @Test
 public void testCharacterWithInvalidData() {
 Character character = new Character("&invalid;"); // invalid character

 try {
 Token expectedCharacter = new Character("&amp;");
 expectedCharacter.data = "&amp;";

 Assert.assertThrows(IllegalArgumentException.class, () -> {
 Assert.assertEquals(expectedCharacter.tokenType(), character.tokenType());
 });
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}