import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

 // Test for Doctype
 @Test
 public void testDoctype() {
 Doctype token = new Doctype();
 token.getName().equals("doctype");
 }

 // Test for reset method of Doctype
 @Test
 public void testDoctypeReset() {
 Doctype token = new Doctype();
 token.newName("html");
 token.reset();
 assertEquals(null, token.getName());
 }

 // Test for getName method of Doctype
 @Test
 public void testDoctypegetName() {
 Doctype token = new Doctype();
 token.newName("html");
 assertEquals("html", token.getName());
 }

 // Test for isForceQuirks method of Doctype
 @Test
 public void testDoctypeisForceQuirks() {
 Doctype token = new Doctype();
 token.isForceQuirks();
 assertFalse(token.isForceQuirks());
 }

 // Test for reset method of abstract Tag class
 @Test
 public void testTagReset() {
 StartTag token = new StartTag();
 token.name("div");
 token.reset();
 assertEquals(null, token.tagName);
 }

 // Test for name method of abstract Tag class
 @Test
 public void testTagname() {
 StartTag token = new StartTag();
 token.name("div");
 assertEquals("div", token.name());
 }

 // Test for name method with case insensitive check
 @Test
 public void testTagnameCaseInsensitive() {
 StartTag token = new StartTag();
 token.name("DIV");
 assertEquals("div", token.name());
 }

 // Test for newAttribute method of abstract Tag class
 @Test
 public void testTagnewAttribute() {
 StartTag token = new StartTag();
 token.newAttribute();
 assertNotNull(token.attributes);
 }

 // Test for newAttribute method with existing attributes
 @Test(expected = IllegalStateException.class)
 public void testTagnewAttributeWithExistingAttributes() {
 StartTag token = new StartTag();
 token.newAttribute();
 token.newAttribute();
 }

 // Test for nameAttr method of StartTag
 @Test
 public void testStartTagnameAttr() {
 StartTag token = new StartTag();
 token.nameAttr("class", null);
 assertEquals("class", token.tagName);
 assertEquals(null, token.getAttributes().get("class"));
 }

 // Test for nameAttr method with existing attribute
 @Test
 public void testStartTagnameAttrWithExistingAttribute() {
 StartTag token = new StartTag();
 token.nameAttr("class", null);
 token.newAttribute();
 token.newAttribute("id", "test");
 assertEquals("class", token.tagName);
 assertEquals("test", token.getAttributes().get("id").getValue());
 }

 // Test for toString method of StartTag
 @Test
 public void testStartTagtoString() {
 StartTag token = new StartTag();
 token.nameAttr("class", "test");
 token.newAttribute("id", "test");
 assertEquals("<div class=\"test\" id=\"test\">", token.toString());
 }

 // Test for reset method of EndTag
 @Test
 public void testEndTagReset() {
 EndTag token = new EndTag();
 token.name("div");
 token.reset();
 assertEquals(null, token.tagName);
 }

 // Test for toString method of EndTag
 @Test
 public void testEndTagtoString() {
 EndTag token = new EndTag();
 token.name("div");
 assertEquals("</div>", token.toString());
 }

 // Test for isSelfClosing method of abstract Tag class
 @Test
 public void testTagisSelfClosing() {
 StartTag token = new StartTag();
 token.isSelfClosing(false);
 assertFalse(token.isSelfClosing());
 }

 // Test for isSelfClosing method with self-closing tag
 @Test
 public void testTagisSelfClosingSelfClosing() {
 StartTag token = new StartTag();
 token.isSelfClosing(true);
 assertTrue(token.isSelfClosing());
 }

 // Test for toString method with self-closing tag
 @Test
 public void testTagtoStringSelfClosing() { // Test for toString method with self-closing tag
 StartTag token = new StartTag();
 token.name("img");
 token.isSelfClosing(true);
 assertEquals("<img />", token.toString());
 }

 // Test for reset method of Comment class
 @Test
 public void testCommentReset() {
 Comment token = new Comment();
 token.data("This is a comment");
 token.reset();
 assertEquals(null, token.getData());
 }

 // Test for getData method of Comment class
 @Test
 public void testCommentgetData() {
 Comment token = new Comment();
 token.data("This is a comment");
 assertEquals("This is a comment", token.getData());
 }

 // Test for toString method of Comment class
 @Test
 public void testCommenttoString() {
 Comment token = new Comment();
 token.data("This is a comment");
 assertEquals("<!--This is a comment-->", token.toString());
 }

 // Test for bogus method of Comment class
 @Test
 public void testCommentbogus() {
 Comment token = new Comment();
 token.bogus(true);
 assertTrue(token.isBogus());
 }

 // Test for reset method of Character class
 @Test
 public void testCharacterReset() {
 Character token = new Character();
 token.data("A");
 token.reset();
 assertEquals(null, token.getData());
 }

 // Test for getData method of Character class
 @Test
 public void testCharactergetData() {
 Character token = new Character();
 token.data("A");
 assertEquals("A", token.getData());
 }

 // Test for toString method of Character class
 @Test
 public void testCharactertoString() {
 Character token = new Character();
 token.data("A");
 assertEquals("A", token.toString());
 }

 // Test for isDoctype method of Token class
 @Test
 public void testTokenisDoctype() {
 Doctype token = new Doctype();
 assertTrue(token.isDoctype());
 }

 // Test for isStartTag method of Token class
 @Test
 public void testTokenisStartTag() {
 StartTag token = new StartTag();
 assertTrue(token.isStartTag());
 }

 // Test for isEndTag method of Token class
 @Test
 public void testTokenisEndTag() {
 EndTag token = new EndTag();
 assertTrue(token.isEndTag());
 }

 // Test for isComment method of Token class
 @Test
 public void testTokenisComment() {
 Comment token = new Comment();
 assertTrue(token.isComment());
 }

 // Test for isCharacter method of Token class
 @Test
 public void testTokenisCharacter() {
 Character token = new Character();
 assertTrue(token.isCharacter());
 }

 // Test for isEOF method of Token class
 @Test
 public void testTokenisEOF() {
 EOF token = new EOF();
 assertTrue(token.isEOF());
 }

}