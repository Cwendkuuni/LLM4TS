import org.jsoup.parser.*;
import org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 doctype.setPubSysKey("PSK-123");
 doctype.getPublicIdentifier().append("PUB ID:");
 doctype.getSystemIdentifier().append("SYS ID:");
 doctype.isForceQuirks(true);

 Token expectedDoctype = new Doctype();
 expectedDoctype.getName().equals("html");
 expectedDoctype.getPubSysKey().equals("PSK-123");
 expectedDoctype.getPublicIdentifier().equals("PUB ID:");
 expectedDoctype.getSystemIdentifier().equals("SYS ID:");
 expectedDoctype.isForceQuirks(true);

 assertEquals(expectedDoctype, doctype);
 }

 @Test
 public void testDoctypeTokenReset() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 doctype.setPubSysKey("PSK-123");
 doctype.getPublicIdentifier().append("PUB ID:");
 doctype.getSystemIdentifier().append("SYS ID:");
 doctype.isForceQuirks(true);
 doctype.reset();

 String expectedName = null;
 String expectedPubSysKey = null;
 StringBuilder expectedPublicIdentifier = new StringBuilder();
 StringBuilder expectedSystemIdentifier = new StringBuilder();
 boolean expectedForceQuirks = false;

 assertEquals(expectedName, doctype.getName());
 assertEquals(expectedPubSysKey, doctype.getPubSysKey());
 assertEquals(expectedPublicIdentifier.toString(), doctype.getPublicIdentifier().toString());
 assertEquals(expectedSystemIdentifier.toString(), doctype.getSystemIdentifier().toString());
 assertEquals(expectedForceQuirks, doctype.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 Tag tag = new StartTag();
 tag.name("html");
 tag.newAttribute().name("class").value("class1 class2");
 tag.newAttribute().name("id").value("id1");
 tag.selfClosing(true);

 Token expectedTag = new StartTag();
 expectedTag.name("html").name("html");
 expectedTag.newAttribute().name("class").value("class1 class2");
 expectedTag.newAttribute().name("id").value("id1");
 expectedTag.selfClosing(true);

 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagTokenReset() {
 Tag tag = new StartTag();
 tag.name("html");
 tag.newAttribute().name("class").value("class1 class2");
 tag.newAttribute().name("id").value("id1");
 tag.selfClosing(true);
 tag.reset();

 assertNull(tag.tagName);
 assertNull(tag.normalName);
 assertNull(tag.pendingAttributeName);
 assertEquals(0, tag.pendingAttributeValue.length());
 assertEquals(null, tag.pendingAttributeValueS);
 assertFalse(tag.hasEmptyAttributeValue);
 assertFalse(tag.hasPendingAttributeValue);
 assertFalse(tag.selfClosing);
 assertNull(tag.attributes);
 }

 @Test
 public void testTagTokenNewAttribute() {
 Tag tag = new StartTag();
 tag.name("html");
 tag.newAttribute().name("class").value("class1 class2");
 tag.newAttribute().name("id").value("id1");
 tag.newAttribute();
 tag.newAttribute().name("style").value("style1");

 Token expectedTag = new StartTag();
 expectedTag.name("html").name("html");
 expectedTag.newAttribute().name("class").value("class1 class2");
 expectedTag.newAttribute().name("id").value("id1");
 expectedTag.newAttribute().name("style").value("style1");

 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagTokenFinaliseTag() {
 Tag tag = new StartTag();
 tag.name("html");
 tag.newAttribute().name("class").value("class1 class2");
 tag.newAttribute().name("id").value("id1");
 tag.finaliseTag();

 Token expectedTag = new StartTag();
 expectedTag.name("html").name("html");
 expectedTag.newAttribute().name("class").value("class1 class2");
 expectedTag.newAttribute().name("id").value("id1");
 expectedTag.attributes = new Attributes();

 assertEquals(expectedTag, tag);
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.getData().append("<!-- test comment -->");

 Token expectedComment = new Comment();
 expectedComment.getData().equals("test comment");

 assertEquals(expectedComment, comment);
 }

 @Test
 public void testCommentTokenReset() {
 Comment comment = new Comment();
 comment.getData().append("<!-- test comment -->");
 comment.reset();

 assertEquals(0, comment.data.length());
 }

 @Test (expected = AssertionError.class)
 public void testCharacterTokenInvalidCharacter() {
 Character character = new Character();
 character.data("\u0000");
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("test character");

 Token expectedCharacter = new Character();
 expectedCharacter.data("test character");

 assertEquals(expectedCharacter, character);
 }

 @Test
 public void testCharacterTokenReset() {
 Character character = new Character();
 character.data("test character");
 character.reset();

 assertEquals(null, character.data);
 }

 @Test
 public void testCDataToken() {
 CData cdata = new CData("<![CDATA[test CData]]>");

 Token expectedCData = new CData();
 expectedCData.getData().equals("<![CDATA[test CData]]>");

 assertEquals(expectedCData, cdata);
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 assertEquals(TokenType.EOF, eof.type);
 }

 @Test
 public void testEOFTokenReset() {
 EOF eof = new EOF();
 eof.reset();

 assertEquals(null, eof);
 }
}