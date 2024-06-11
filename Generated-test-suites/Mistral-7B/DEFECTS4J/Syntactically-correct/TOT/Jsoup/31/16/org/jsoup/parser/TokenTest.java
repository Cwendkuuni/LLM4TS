import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeCreation() {
 Doctype doctype = new Doctype();
 Assert.assertEquals(TokenType.Doctype, doctype.type);
 Assert.assertNull(doctype.getName());
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeNameSetting() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 Assert.assertEquals("html", doctype.getName());
 }

 @Test
 public void testDoctypePublicIdentifierSetting() {
 Doctype doctype = new Doctype();
 doctype.publicIdentifier("_public");
 Assert.assertEquals("_public", doctype.getPublicIdentifier());
 }

 @Test
 public void testDoctypeSystemIdentifierSetting() {
 Doctype doctype = new Doctype();
 doctype.systemIdentifier("/path/to/file.html");
 Assert.assertEquals("/path/to/file.html", doctype.getSystemIdentifier());
 }

 @Test
 public void testDoctypeForceQuirksSetting() {
 Doctype doctype = new Doctype();
 doctype.forceQuirks(true);
 Assert.assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testStartTagCreation() {
 StartTag startTag = new StartTag();
 Assert.assertEquals(TokenType.StartTag, startTag.type);
 Assert.assertNull(startTag.tagName);
 Assert.assertNull(startTag.attributes);
 Assert.assertFalse(startTag.isSelfClosing());
 }

 @Test
 public void testStartTagNameSetting() {
 StartTag startTag = new StartTag();
 startTag.name("div");
 Assert.assertEquals("div", startTag.tagName);
 }

 @Test
 public void testStartTagAttributesSetting() {
 StartTag startTag = new StartTag();
 startTag.newAttribute().name("id").value("elementId");
 Assert.assertNotNull(startTag.attributes);
 Assert.assertEquals("id", startTag.attributes.getKey(0).getName());
 Assert.assertEquals("elementId", startTag.attributes.getValue(0).getValue());
 }

 @Test
 public void testStartTagSelfClosingSetting() {
 StartTag startTag = new StartTag();
 startTag.selfClosing = true;
 Assert.assertTrue(startTag.isSelfClosing());
 }

 @Test(expected = AssertionError.class)
 public void testStartTagAttributesSettingDuplicateName() {
 StartTag startTag = new StartTag();
 startTag.newAttribute().name("id").value("elementId");
 startTag.newAttribute().name("id").value("anotherElementId");
 }

 @Test
 public void testEndTagCreation() {
 EndTag endTag = new EndTag();
 Assert.assertEquals(TokenType.EndTag, endTag.type);
 Assert.assertNull(endTag.tagName);
 Assert.assertNull(endTag.attributes);
 }

 @Test
 public void testEndTagNameSetting() {
 EndTag endTag = new EndTag();
 endTag.name("div");
 Assert.assertEquals("div", endTag.tagName);
 }

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment();
 Assert.assertEquals(TokenType.Comment, comment.type);
 Assert.assertNull(comment.getData());
 }

 @Test
 public void testCommentDataSetting() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");
 Assert.assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCharacterCreation() {
 Character character = new Character("A");
 Assert.assertEquals(TokenType.Character, character.type);
 Assert.assertEquals("A", character.getData());
 }

 @Test
 public void testCharacterDataSetting() {
 Character character = new Character("A\u00E9");
 Assert.assertEquals("A\u00E9", character.getData());
 }

 @Test
 public void testEOFCreation() {
 EOF eof = new EOF();
 Assert.assertEquals(TokenType.EOF, eof.type);
 }

 @Test
 public void testTokenTypeIdentification() {
 Token token = new Doctype();
 Assert.assertTrue(token.isDoctype());
 token = new StartTag();
 Assert.assertTrue(token.isStartTag());
 token = new EndTag();
 Assert.assertTrue(token.isEndTag());
 token = new Comment();
 Assert.assertTrue(token.isComment());
 token = new Character('A');
 Assert.assertTrue(token.isCharacter());
 token = new EOF();
 Assert.assertTrue(token.isEOF());
 }
}