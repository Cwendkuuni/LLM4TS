import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenTest {

 @Test
 void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.reset();
 }

 @Test
 void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.reset();
 doctype.getName().isEmpty();
 }

 @Test
 void testDoctypeSetName() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 doctype.getName().equals("html");
 }

 @Test
 void testDoctypeGetPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getPublicIdentifier().equals("");
 doctype.publicIdentifier.append("HTML");
 doctype.getPublicIdentifier().equals("HTML");
 doctype.reset();
 }

 @Test
 void testDoctypeGetSystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getSystemIdentifier().equals("");
 doctype.systemIdentifier.append("/index.html");
 doctype.getSystemIdentifier().equals("/index.html");
 doctype.reset();
 }

 @Test
 void testDoctypeForceQuirks() {
 Doctype doctype = new Doctype();
 Assertions.assertFalse(doctype.isForceQuirks());
 doctype.forceQuirks = true;
 Assertions.assertTrue(doctype.isForceQuirks());
 doctype.reset();
 }

 @Test
 void testTagToken() {
 Tag tag = new StartTag();
 tag.name("p").name().equals("p");
 tag.name("p").name().equals("p"); // case insensitive
 tag.reset();
 }

 @Test
 void testTagReset() {
 Tag tag = new StartTag();
 tag.name("p").name().equals("p");
 tag.reset();
 tag.name().isEmpty();
 }

 @Test
 void testTagNameAttribute() {
 StartTag startTag = new StartTag();
 startTag.name("p").name("p");
 startTag.nameAttr("class", null);
 startTag.getAttributes().get("class").isEmpty();
 startTag.nameAttr("class", "class1 class2");
 startTag.getAttributes().get("class").equals("class1 class2");
 startTag.reset();
 }

 @Test
 void testTagNewAttribute() {
 StartTag startTag = new StartTag();
 startTag.name("p").name("p");
 startTag.newAttribute();
 startTag.getAttributes().get("class").isEmpty();
 startTag.appendAttributeName("class").appendAttributeValue("class1");
 startTag.newAttribute();
 startTag.getAttributes().get("class").equals("class1");
 startTag.reset();
 }

 @Test
 void testTagFinaliseTag() {
 StartTag startTag = new StartTag();
 startTag.name("p").name("p");
 startTag.newAttribute();
 startTag.appendAttributeName("class").appendAttributeValue("class1");
 startTag.newAttribute();
 startTag.finaliseTag();
 startTag.getAttributes().get("class").equals("class1");
 startTag.reset();
 }

 @Test
 void testTagIsSelfClosing() {
 StartTag startTag = new StartTag();
 startTag.isSelfClosing = true;
 Assertions.assertTrue(startTag.isSelfClosing());
 startTag.isSelfClosing = false;
 Assertions.assertFalse(startTag.isSelfClosing());
 startTag.reset();
 }

 @Test
 void testCommentToken() {
 Comment comment = new Comment();
 comment.getData().equals("");
 comment.getData().append("--comment");
 comment.getData().equals("--comment");
 comment.reset();
 }

 @Test
 void testCommentReset() {
 Comment comment = new Comment();
 comment.getData().equals("--comment");
 comment.reset();
 comment.getData().isEmpty();
 }

 @Test
 void testCharacterToken() {
 Character character = new Character();
 character.reset();
 character.data("A");
 character.getData().equals("A");
 character.reset();
 }

 @Test
 void testCharacterData() {
 Character character = new Character();
 character.data("A");
 character.data("B");
 character.getData().equals("AB");
 character.reset();
 }

 @Test
 void testCDataToken() {
 CData cData = new CData("CDATA[ABC]");
 cData.getData().equals("ABC");
 cData.toString().equals("<![CDATA[ABC]]>");
 cData.reset();
 }

 @Test
 void testEOFToken() {
 EOF eof = new EOF();
 Assertions.assertTrue(eof.isEOF());
 }
}