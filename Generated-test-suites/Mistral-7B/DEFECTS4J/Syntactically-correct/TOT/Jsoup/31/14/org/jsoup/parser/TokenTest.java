import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
 doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
 doctype.isForceQuirks(true);

 Token expectedDoctype = new Doctype();
 expectedDoctype.name.append("html");
 expectedDoctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
 expectedDoctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
 expectedDoctype.isForceQuirks(true);

 Assert.assertEquals("Unexpected Doctype token.", expectedDoctype, doctype);
 }

 @Test
 public void testStartTagToken() {
 String tagName = "div";
 StartTag startTag = new StartTag(tagName);
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 startTag.appendAttributeValue("container");
 startTag.newAttribute();
 startTag.appendAttributeName("id");
 startTag.appendAttributeValue("main");
 startTag.finaliseTag();

 Token expectedStartTag = new StartTag(tagName);
 expectedStartTag.tagName = tagName;
 expectedStartTag.attributes = new Attributes().put(new Attribute("class", "container"), new Attribute("id", "main"));

 Assert.assertEquals("Unexpected StartTag token.", expectedStartTag, startTag);
 }

 @Test
 public void testEndTagToken() {
 String tagName = "div";
 EndTag endTag = new EndTag(tagName);

 Token expectedEndTag = new EndTag(tagName);

 Assert.assertEquals("Unexpected EndTag token.", expectedEndTag, endTag);
 }

 @Test
 public void testCommentToken() {
 String commentData = "This is a test comment.";
 Comment comment = new Comment();
 comment.data.append(commentData);

 Token expectedComment = new Comment();
 expectedComment.data.append(commentData);

 Assert.assertEquals("Unexpected Comment token.", expectedComment, comment);
 }

 @Test
 public void testCharacterToken() {
 char character = '&';
 Character characterToken = new Character(String.valueOf(character));

 Token expectedCharacterToken = new Character(String.valueOf(character));

 Assert.assertEquals("Unexpected Character token.", expectedCharacterToken, characterToken);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAttributeName() {
 StartTag startTag = new StartTag("div");
 startTag.newAttribute();
 startTag.appendAttributeName("onclick\""); // containing quotes
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 startTag.appendAttributeValue("container");
 startTag.finaliseTag();
 }

 @Test(expected = NullPointerException.class)
 public void testNullCharacterData() {
 Character characterToken = new Character(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 StartTag startTag = new StartTag(null);
 }
}