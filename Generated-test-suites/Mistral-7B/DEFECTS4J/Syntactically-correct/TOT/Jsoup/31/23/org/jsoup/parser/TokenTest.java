import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.Attributes;
import org.jsoup.helper.Validate;

public class TokenTest {

 @Test
 public void testDoctypeCreation() {
 Doctype doctype = new Doctype();
 Doctype expectedDoctype = new Doctype();

 Assertions.assertEquals(expectedDoctype.getClass(), doctype.getClass());
 Assertions.assertEquals(TokenType.Doctype, doctype.type);
 }

 @Test
 public void testDoctypeNameSetterAndGetter() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 String expectedName = "html";

 Assertions.assertEquals(expectedName, doctype.getName());
 }

 @Test
 public void testDoctypePublicIdentifierSetterAndGetter() {
 Doctype doctype = new Doctype();
 doctype.publicIdentifier.append("public");
 String expectedPublicIdentifier = "public";

 Assertions.assertEquals(expectedPublicIdentifier, doctype.getPublicIdentifier());
 }

 @Test
 public void testDoctypeSystemIdentifierSetterAndGetter() {
 Doctype doctype = new Doctype();
 doctype.systemIdentifier.append("system.xml");
 String expectedSystemIdentifier = "system.xml";

 Assertions.assertEquals(expectedSystemIdentifier, doctype.getSystemIdentifier());
 }

 @Test
 public void testDoctypeForceQuirksSetterAndGetter() {
 Doctype doctype = new Doctype();
 doctype.forceQuirks = true;
 boolean expectedForceQuirks = true;

 Assertions.assertEquals(expectedForceQuirks, doctype.isForceQuirks());
 }

 @Test
 public void testStartTagCreation() {
 StartTag startTag = new StartTag("div");
 StartTag expectedStartTag = new StartTag("div");

 Assertions.assertEquals(expectedStartTag.getClass(), startTag.getClass());
 Assertions.assertEquals(TokenType.StartTag, startTag.type);
 }

 @Test
 public void testStartTagNameSetterAndGetter() {
 StartTag startTag = new StartTag();
 startTag.name("p");
 String expectedName = "p";

 Assertions.assertEquals(expectedName, startTag.name());
 }

 @Test
 public void testStartTagNameAndAttributesSetterAndGetter() {
 StartTag startTag = new StartTag();
 startTag.name("p").attributes(new Attributes().put("class", "class1").put("id", "id1"));
 String expectedName = "p";
 Attributes expectedAttributes = new Attributes().put("class", "class1").put("id", "id1");

 Assertions.assertEquals(expectedName, startTag.name());
 Assertions.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testEndTagCreation() {
 EndTag endTag = new EndTag("div");
 EndTag expectedEndTag = new EndTag("div");

 Assertions.assertEquals(expectedEndTag.getClass(), endTag.getClass());
 Assertions.assertEquals(TokenType.EndTag, endTag.type);
 }

 @Test
 public void testEndTagNameSetterAndGetter() {
 EndTag endTag = new EndTag();
 endTag.name("p");
 String expectedName = "p";

 Assertions.assertEquals(expectedName, endTag.name());
 }

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment();
 Comment expectedComment = new Comment();

 Assertions.assertEquals(expectedComment.getClass(), comment.getClass());
 Assertions.assertEquals(TokenType.Comment, comment.type);
 }

 @Test
 public void testCommentDataSetterAndGetter() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");
 String expectedData = "This is a comment";

 Assertions.assertEquals(expectedData, comment.getData());
 }

 @Test
 public void testCharacterCreation() {
 Character character = new Character('A');
 Character expectedCharacter = new Character('A');

 Assertions.assertEquals(expectedCharacter.getClass(), character.getClass());
 Assertions.assertEquals(TokenType.Character, character.type);
 }

 @Test
 public void testCharacterDataSetterAndGetter() {
 Character character = new Character();
 character.data.append("A");
 String expectedData = "A";

 Assertions.assertEquals(expectedData, character.getData());
 }

 @Test
 public void testEOFCreation() {
 EOF eof = new EOF();
 EOF expectedEof = new EOF();

 Assertions.assertEquals(expectedEof.getClass(), eof.getClass());
 Assertions.assertEquals(Token.TokenType.EOF, eof.type);
 }

 @Test
 public void testDoctypeWithInvalidName() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> {
 Doctype doctype = new Doctype();
 doctype.name(":");
 });
 }

 @Test
 public void testDoctypeWithNullName() {
 Assertions.assertThrows(NullPointerException.class, () -> {
 Doctype doctype = new Doctype();
 doctype.name(null);
 });
 }

 @Test
 public void testStartTagWithNullName() {
 Assertions.assertThrows(NullPointerException.class, () -> {
 StartTag startTag = new StartTag();
 startTag.name(null);
 });
 }

 @Test
 public void testStartTagWithEmptyName() {
 StartTag startTag = new StartTag();
 Assertions.assertEquals("", startTag.name());
 }

 @Test
 public void testEndTagWithNullName() {
 Assertions.assertThrows(NullPointerException.class, () -> {
 EndTag endTag = new EndTag();
 endTag.name(null);
 });
 }

 @Test
 public void testEndTagWithEmptyName() {
 EndTag endTag = new EndTag();
 Assertions.assertEquals("", endTag.name());
 }

 @Test
 public void testNewAttributeWithoutValue() {
 StartTag startTag = new StartTag();
 startTag.newAttribute();
 Attributes expectedAttributes = new Attributes();

 Assertions.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testNewAttributeWithValue() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeName("class").appendAttributeValue("class1");
 Attributes expectedAttributes = new Attributes().put("class", "class1");

 Assertions.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testNewAttributeWithSameNameTwice() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeName("class").appendAttributeValue("class1");
 startTag.appendAttributeName("class").appendAttributeValue("class2");
 Attributes expectedAttributes = new Attributes().put("class", "class1 class2");

 Assertions.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testNewAttributeWithEmptyValue() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeName("class").appendAttributeValue("");
 Attributes expectedAttributes = new Attributes().put("class", "");

 Assertions.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testNewAttributeWithNullValue() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeName("class").appendAttributeValue(null);
 Attributes expectedAttributes = new Attributes();

 Assertions.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testFinaliseTag() {
 StartTag startTag = new StartTag();
 startTag.appendTagName("div").newAttribute().appendAttributeName("class").appendAttributeValue("class1");
 startTag.finaliseTag();
 Attributes expectedAttributes = new Attributes().put("class", "class1");

 Assertions.assertNotNull(startTag.getName());
 Assertions.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testFinalisingTagTwice() {
 StartTag startTag = new StartTag();
 startTag.appendTagName("div").newAttribute().appendAttributeName("class").appendAttributeValue("class1");
 startTag.finaliseTag();
 startTag.finaliseTag();

 Assertions.assertNotNull(startTag.getName());
 Assertions.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testCommentWithNullData() {
 Assertions.assertThrows(NullPointerException.class, () -> {
 Comment comment = new Comment();
 comment.data = null;
 });
 }
}