import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public id");
 doctype.systemIdentifier.append("/myapp.html");
 doctype.forceQuirks = true;

 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertEquals("public id", doctype.getPublicIdentifier());
 assertEquals("/myapp.html", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());

 Doctype resetDoctype = doctype.reset();
 assertEquals("Doctype", resetDoctype.tokenType());
 assertNull(resetDoctype.getName());
 assertNull(resetDoctype.getPublicIdentifier());
 assertNull(resetDoctype.getSystemIdentifier());
 assertFalse(resetDoctype.isForceQuirks());
 }

 @Test
 public void testResetFuncForStringBuilder() {
 StringBuilder sb = new StringBuilder("Hello World");
 Token.reset(sb);
 assertEquals("", sb.toString());
 }

 @Test
 public void testTagToken() {
 Tag tag = new Tag();
 tag.tagName = "div";
 tag.name("p");
 tag.newAttribute().name("class").value("test");
 tag.newAttribute().name("id").value("id123");
 tag.newAttribute().name("style").value("color: red;");
 tag.attributes.put("title", new Attribute("title", "Test Title"));
 tag.isSelfClosing = true;

 assertEquals("Tag", tag.tokenType());
 assertEquals("div", tag.name());
 assertEquals("p", tag.normalName());
 assertEquals(4, tag.getAttributes().size());
 assertTrue(tag.isSelfClosing());

 Tag resetTag = tag.reset();
 assertEquals("Tag", resetTag.tokenType());
 assertNull(resetTag.tagName);
 assertNull(resetTag.normalName);
 assertEquals(0, resetTag.getAttributes().size());
 assertFalse(resetTag.isSelfClosing());
 }

 @Test
 public void testTagTokenWithEmptyAttributeValue() {
 Tag tag = new Tag();
 tag.tagName = "img";
 tag.newAttribute().name("src").setEmptyAttributeValue();
 tag.newAttribute().name("title").value("Image Title");
 tag.attributes.put("alt", new Attribute("alt", ""));

 assertEquals("Tag", tag.tokenType());
 assertEquals("img", tag.name());
 assertEquals("img", tag.normalName());
 assertEquals(3, tag.getAttributes().size());
 assertFalse(tag.hasPendingAttributeValue);
 assertTrue(tag.hasEmptyAttributeValue);
 assertTrue(tag.getAttributes().get("src").getValue().isEmpty());
 assertNull(tag.getAttributes().get("src").getName());
 }

 @Test
 public void testTagTokenWithPendingAttributeValue() {
 Tag tag = new Tag();
 tag.tagName = "input";
 tag.newAttribute().name("type").value("text");
 tag.newAttribute().name("name").value("username");
 tag.newAttribute().name("id").value("userNameInput");
 tag.pendingAttributeValueS = "data-test";
 tag.newAttribute();
 tag.name("label");
 tag.finaliseTag();

 assertEquals("Tag", tag.tokenType());
 assertEquals("input", tag.name());
 assertEquals("input", tag.normalName());
 assertEquals(6, tag.getAttributes().size());
 assertEquals("data-test", tag.getAttributes().get(5).getName());
 assertNull(tag.getAttributes().get(5).getValue());
 }

 @Test(expected = AssertionError.class)
 public void testTagTokenWithInvalidAttributeName() {
 Tag tag = new Tag();
 tag.tagName = "input";
 tag.newAttribute().name("$").value("text");
 tag.newAttribute().name("name").value("username");
 tag.newAttribute().name("id").value("userNameInput");
 tag.pendingAttributeValueS = "data-test";
 tag.newAttribute();
 tag.finaliseTag();
 }

 @Test
 public void testTagTokenWithInvalidAttributeValue() {
 Tag tag = new Tag();
 tag.tagName = "input";
 tag.newAttribute().name("type").value("text");
 tag.newAttribute().name("name").value("username");
 tag.newAttribute().name("id").value("userNameInput");
 tag.pendingAttributeValueS = "&";
 tag.newAttribute();
 tag.finaliseTag();

 assertEquals("Tag", tag.tokenType());
 assertEquals("input", tag.name());
 assertEquals("input", tag.normalName());
 assertEquals(5, tag.getAttributes().size());
 assertEquals("", tag.getAttributes().get(4).getValue());
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");

 assertEquals("Comment", comment.tokenType());
 assertEquals("", comment.tokenType());
 assertEquals("This is a comment", comment.getData());
 assertFalse(comment.bogus);

 Comment resetComment = comment.reset();
 assertEquals("Comment", resetComment.tokenType());
 assertNull(resetComment.getData());
 assertFalse(resetComment.bogus);
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("Hello, World!");

 assertEquals("Character", character.tokenType());
 assertEquals("Hello, World!", character.getData());

 Character resetCharacter = character.reset();
 assertEquals("Character", resetCharacter.tokenType());
 assertNull(resetCharacter.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 assertEquals("EOF", eof.tokenType());
 }
}