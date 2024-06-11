import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
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
 public void testDoctypeCreation() {
 Doctype doctype = new Doctype();
 Doctype expectedDoctype = new Doctype();
 Assertions.assertEquals(expectedDoctype, doctype);
 }

 @Test
 public void testDoctypeSettingName() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 }

 @Test
 public void testDoctypeSettingPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getPublicIdentifier().equals("");
 doctype.setPublicIdentifier("public");
 doctype.getPublicIdentifier().equals("public");
 }

 @Test
 public void testDoctypeSettingSystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getSystemIdentifier().equals("");
 doctype.setSystemIdentifier("system");
 doctype.getSystemIdentifier().equals("system");
 }

 @Test
 public void testDoctypeSettingForceQuirks() {
 Doctype doctype = new Doctype();
 assertFalse(doctype.isForceQuirks());
 doctype.setForceQuirks(true);
 assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testTagCreation() {
 Tag tag = new Tag();
 Tag expectedTag = new Tag();
 Assertions.assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagSettingName() {
 Tag tag = new Tag();
 tag.name().equals("tag");
 tag.name("newTag").name().equals("newTag");
 }

 @Test
 public void testTagSettingSelfClosing() {
 Tag tag = new Tag();
 assertFalse(tag.isSelfClosing());
 tag.setSelfClosing(true);
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testTagAttributesCreation() {
 Tag tag = new Tag();
 Attributes attributes = new Attributes();
 tag.newAttribute();
 tag.attributes = attributes;
 tag.newAttribute();
 Tag expectedTag = new Tag();
 expectedTag.newAttribute();
 expectedTag.attributes = attributes;
 Assertions.assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagSettingAttributeName() {
 Tag tag = new Tag();
 tag.newAttribute();
 tag.pendingAttributeName("attributeName");
 tag.newAttribute();
 tag.pendingAttributeName = null;
 Tag expectedTag = new Tag();
 expectedTag.newAttribute();
 expectedTag.pendingAttributeName = "attributeName";
 expectedTag.newAttribute();
 expectedTag.pendingAttributeName = null;
 Assertions.assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagSettingAttributeValue() {
 Tag tag = new Tag();
 tag.newAttribute();
 tag.pendingAttributeValue("value");
 tag.newAttribute();
 tag.pendingAttributeValue = null;
 Tag expectedTag = new Tag();
 expectedTag.newAttribute();
 expectedTag.pendingAttributeValue = "value";
 expectedTag.newAttribute();
 expectedTag.pendingAttributeValue = null;
 Assertions.assertEquals(expectedTag, tag);
 }

 @Test
 public void testStartTagCreation() {
 StartTag startTag = new StartTag();
 StartTag expectedStartTag = new StartTag();
 Assertions.assertEquals(expectedStartTag, startTag);
 }

 @Test
 public void testStartTagSettingName() {
 StartTag startTag = new StartTag();
 startTag.name().equals("startTag");
 startTag.name("newStartTag").name().equals("newStartTag");
 }

 @Test
 public void testStartTagSettingAttributes() {
 StartTag startTag = new StartTag();
 Attributes attributes = new Attributes();
 attributes.put(new Attribute("attributeName", "attributeValue"));
 tag.newAttribute();
 tag.pendingAttributeName = "attributeName";
 tag.pendingAttributeValue = "attributeValue";
 tag.newAttribute();
 tag.attributes = attributes;
 Tag expectedTag = new StartTag();
 expectedTag.attributes = attributes;
 Assertions.assertEquals(expectedTag, startTag);
 }

 @Test
 public void testEndTagCreation() {
 EndTag endTag = new EndTag();
 EndTag expectedEndTag = new EndTag();
 Assertions.assertEquals(expectedEndTag, endTag);
 }

 @Test
 public void testEndTagSettingName() {
 EndTag endTag = new EndTag();
 endTag.name().equals("endTag");
 endTag.name("newEndTag").name().equals("newEndTag");
 }

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment();
 Comment expectedComment = new Comment();
 Assertions.assertEquals(expectedComment, comment);
 }

 @Test
 public void testCommentSettingData() {
 Comment comment = new Comment();
 comment.getData().equals("");
 comment.getData().append("data");
 comment.getData().equals("data");
 }

 @Test
 public void testCharacterCreation() {
 Character character = new Character("characterData");
 Character expectedCharacter = new Character("characterData");
 Assertions.assertEquals(expectedCharacter, character);
 }

 @Test
 public void testEOFCreation() {
 EOF eof = new EOF();
 EOF expectedEOF = new EOF();
 Assertions.assertEquals(expectedEOF, eof);
 }
}