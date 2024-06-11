import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype token = new Doctype();
 assertEquals("Doctype", token.tokenType());

 token.getName() = "html";
 assertEquals("html", token.getName());

 token.getPublicIdentifier() = "public id";
 assertEquals("public id", token.getPublicIdentifier());

 token.getSystemIdentifier() = "system id";
 assertEquals("system id", token.getSystemIdentifier());

 token.isForceQuirks() = true;
 assertTrue(token.isForceQuirks());
 }

 @Test
 public void testDoctypeTokenReset() {
 Doctype token = new Doctype();
 token.getName() = "html";
 token.getPublicIdentifier() = "public id";
 token.getSystemIdentifier() = "system id";
 token.isForceQuirks() = true;

 token.reset();
 assertEquals("Doctype", token.tokenType());
 assertNull(token.getName());
 assertNull(token.getPublicIdentifier());
 assertNull(token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 Tag token = new StartTag();
 assertEquals("StartTag", token.tokenType());

 token.name("html");
 assertEquals("html", token.name());
 token.name("html").normalName("html");
 assertEquals("html", token.name());
 assertEquals("html", token.normalName());

 token.selfClosing = true;
 assertTrue(token.isSelfClosing());

 token.attributes = new Attributes().put("class", "someClass");
 assertEquals("someClass", token.attributes().get("class").getValue());
 }

 @Test
 public void testTagTokenReset() {
 Tag token = new StartTag();
 token.name("html");
 token.name("html").normalName("html");
 token.selfClosing = true;
 token.attributes = new Attributes().put("class", "someClass");

 token.reset();
 assertEquals("StartTag", token.tokenType());
 assertNull(token.tagName);
 assertNull(token.normalName);
 assertFalse(token.isSelfClosing());
 assertNull(token.attributes);
 }

 @Test
 public void testTagTokenNewAttribute() {
 Tag token = new StartTag();
 token.newAttribute();
 token.appendAttributeName("class");
 token.appendAttributeValue("someClass");
 assertEquals("someClass", token.attributes().get("class").getValue());
 }

 @Test
 public void testTagTokenFinaliseTag() {
 Tag token = new StartTag();
 token.name("html");
 token.newAttribute();
 token.appendAttributeName("class");
 token.appendAttributeValue("someClass");
 token.finaliseTag();
 assertEquals("html", token.name());
 assertEquals("html", token.normalName());
 assertFalse(token.isSelfClosing());
 assertNotNull(token.attributes());
 }

 @Test
 public void testCommentToken() {
 Comment token = new Comment();
 assertEquals("Comment", token.tokenType());

 token.getData() = "This is a comment";
 assertEquals("This is a comment", token.getData());
 }

 @Test
 public void testCommentTokenReset() {
 Comment token = new Comment();
 token.getData() = "This is a comment";

 token.reset();
 assertEquals("Comment", token.tokenType());
 assertNull(token.getData());
 }

 @Test
 public void testCharacterToken() {
 Character token = new Character();
 assertEquals("Character", token.tokenType());

 token.data("abc");
 assertEquals("abc", token.getData());
 }

 @Test
 public void testCharacterTokenReset() {
 Character token = new Character();
 token.data("abc");

 token.reset();
 assertEquals("Character", token.tokenType());
 assertNull(token.getData());
 }

 @Test
 public void testEOFToken() {
 EOF token = new EOF();
 assertEquals("EOF", token.tokenType());
 }

 @Test
 public void testEOFTokenReset() {
 EOF token = new EOF();

 token.reset();
 assertEquals("EOF", token.tokenType());
 }
}