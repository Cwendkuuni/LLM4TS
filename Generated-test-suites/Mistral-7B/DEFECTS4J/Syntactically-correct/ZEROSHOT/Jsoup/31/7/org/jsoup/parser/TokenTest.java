import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 @org.junit.Test
 public void testDoctype() {
 Doctype token = new Doctype();
 assertEquals("Doctype", token.tokenType());
 assertNull(token.getName());
 assertNull(token.getPublicIdentifier());
 assertNull(token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @org.junit.Test
 public void testDoctypeWithName() {
 Doctype token = new Doctype();
 token.getName().append("html");
 assertEquals("Doctype", token.tokenType());
 assertEquals("html", token.getName());
 assertNull(token.getPublicIdentifier());
 assertNull(token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @org.junit.Test
 public void testDoctypeWithNamePublicIdentifier() {
 Doctype token = new Doctype();
 token.getName().append("html");
 token.getPublicIdentifier().append("_public");
 assertEquals("Doctype", token.tokenType());
 assertEquals("html", token.getName());
 assertEquals("_public", token.getPublicIdentifier());
 assertNull(token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @org.junit.Test
 public void testDoctypeWithNameSystemIdentifier() {
 Doctype token = new Doctype();
 token.getName().append("html");
 token.getSystemIdentifier().append("/path/to/file.html");
 assertEquals("Doctype", token.tokenType());
 assertEquals("html", token.getName());
 assertNull(token.getPublicIdentifier());
 assertEquals("/path/to/file.html", token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @org.junit.Test
 public void testDoctypeWithNamePublicIdentifierSystemIdentifier() {
 Doctype token = new Doctype();
 token.getName().append("html");
 token.getPublicIdentifier().append("_public");
 token.getSystemIdentifier().append("/path/to/file.html");
 assertEquals("Doctype", token.tokenType());
 assertEquals("html", token.getName());
 assertEquals("_public", token.getPublicIdentifier());
 assertEquals("/path/to/file.html", token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @org.junit.Test
 public void testDoctypeWithForceQuirks() {
 Doctype token = new Doctype();
 token.isForceQuirks() = true;
 assertEquals("Doctype", token.tokenType());
 assertFalse(token.isForceQuirks()); // check initial value
 assertTrue(token.isForceQuirks()); // check set value
 }

 @org.junit.Test
 public void testStartTag() {
 StartTag token = new StartTag();
 assertEquals("StartTag", token.tokenType());
 assertNull(token.tagName);
 assertFalse(token.selfClosing);
 assertNull(token.getAttributes());
 }

 @org.junit.Test
 public void testStartTagWithName() {
 StartTag token = new StartTag();
 token.tagName = "div";
 assertEquals("StartTag", token.tokenType());
 assertEquals("div", token.tagName);
 assertFalse(token.selfClosing);
 assertNull(token.getAttributes());
 }

 @org.junit.Test
 public void testStartTagWithNameAttributes() {
 StartTag token = new StartTag();
 token.tagName = "div";
 token.newAttribute();
 token.appendAttributeName("class");
 token.appendAttributeValue("container");
 token.newAttribute();
 assertEquals("StartTag", token.tokenType());
 assertEquals("div", token.tagName);
 assertFalse(token.selfClosing);
 assertEquals("class", token.pendingAttributeName);
 assertEquals("container", token.pendingAttributeValue.toString());
 assertNotNull(token.getAttributes());
 }

 @org.junit.Test
 public void testStartTagWithNameAttributesFinalise() {
 StartTag token = new StartTag();
 token.tagName = "div";
 token.newAttribute();
 token.appendAttributeName("class");
 token.appendAttributeValue("container");
 token.finaliseTag();
 assertEquals("StartTag", token.tokenType());
 assertEquals("div", token.tagName);
 assertFalse(token.selfClosing);
 assertNull(token.pendingAttributeName);
 assertNull(token.pendingAttributeValue);
 assertEquals("class", token.getAttributes().keySet().iterator().next());
 assertEquals("container", token.getAttributes().valueIterator().next().getValue());
 }

 @org.junit.Test
 public void testStartTagWithNameAttributesMultiple() {
 StartTag token = new StartTag();
 token.tagName = "div";
 token.newAttribute();
 token.appendAttributeName("class");
 token.appendAttributeValue("container");
 token.newAttribute();
 token.appendAttributeName("id");
 token.appendAttributeValue("main");
 token.finaliseTag();
 assertEquals("StartTag", token.tokenType());
 assertEquals("div", token.tagName);
 assertFalse(token.selfClosing);
 assertNull(token.pendingAttributeName);
 assertNull(token.pendingAttributeValue);
 assertEquals(2, token.getAttributes().size());
 assertEquals("class", token.getAttributes().keySet().iterator().next());
 assertEquals("container", token.getAttributes().valueIterator().next().getValue());
 assertEquals("id", token.getAttributes().keySet().iterator().next());
 assertEquals("main", token.getAttributes().valueIterator().next().getValue());
 }

 @org.junit.Test
 public void testStartTagWithNameAttributesSelfClosing() {
 StartTag token = new StartTag();
 token.tagName = "img";
 token.selfClosing = true;
 token.newAttribute();
 token.appendAttributeName("src");
 token.appendAttributeValue("image.png");
 token.finaliseTag();
 assertEquals("StartTag", token.tokenType());
 assertEquals("img", token.tagName);
 assertTrue(token.selfClosing);
 assertNull(token.pendingAttributeName);
 assertNull(token.pendingAttributeValue);
 assertEquals("src", token.getAttributes().keySet().iterator().next());
 assertEquals("image.png", token.getAttributes().valueIterator().next().getValue());
 }

 @org.junit.Test
 public void testEndTag() {
 EndTag token = new EndTag();
 assertEquals("EndTag", token.tokenType());
 assertNull(token.tagName);
 }

 @org.junit.Test
 public void testEndTagWithName() {
 EndTag token = new EndTag();
 token.tagName = "div";
 assertEquals("EndTag", token.tokenType());
 assertEquals("div", token.tagName);
 }

 @org.junit.Test
 public void testComment() {
 Comment token = new Comment();
 assertEquals("Comment", token.tokenType());
 assertNull(token.getData());
 }

 @org.junit.Test
 public void testCommentWithData() {
 Comment token = new Comment();
 token.getData().append("This is a comment.");
 assertEquals("Comment", token.tokenType());
 assertEquals("This is a comment.", token.getData().toString());
 }

 @org.junit.Test
 public void testCharacter() {
 Character token = new Character("a");
 assertEquals("Character", token.tokenType());
 assertEquals("a", token.getData());
 }

 @org.junit.Test
 public void testEOF() {
 EOF token = new EOF();
 assertEquals("EOF", token.tokenType());
 }

 @org.junit.Test
 public void testIsDoctype() {
 Token token = new Doctype();
 assertTrue(token.isDoctype());
 }

 @org.junit.Test
 public void testIsStartTag() {
 Token token = new StartTag();
 assertTrue(token.isStartTag());
 }

 @org.junit.Test
 public void testIsEndTag() {
 Token token = new EndTag();
 assertTrue(token.isEndTag());
 }

 @org.junit.Test
 public void testIsComment() {
 Token token = new Comment();
 assertTrue(token.isComment());
 }

 @org.junit.Test
 public void testIsCharacter() {
 Character token = new Character("a");
 assertTrue(token.isCharacter());
 }

 @org.junit.Test
 public void testIsEOF() {
 EOF token = new EOF();
 assertTrue(token.isEOF());
 }

}