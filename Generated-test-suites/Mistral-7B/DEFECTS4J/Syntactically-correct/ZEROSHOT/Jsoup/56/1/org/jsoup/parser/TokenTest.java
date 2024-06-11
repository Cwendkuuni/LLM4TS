import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name("HTML");
 doctype.publicIdentifier("publicID");
 doctype.systemIdentifier("systemID");
 doctype.forceQuirks(true);

 assertEquals("Doctype", doctype.tokenType());
 assertEquals("HTML", doctype.getName());
 assertEquals("publicID", doctype.getPublicIdentifier());
 assertEquals("systemID", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());

 Doctype resetDoctype = doctype.reset();
 assertSame(doctype, resetDoctype);
 assertNull(doctype.getName());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 Tag tag = new StartTag();
 tag.name("div");
 tag.selfClosing(true);
 tag.attributes(new Attributes().put("id", "test"));

 assertEquals("StartTag", tag.tokenType().name());
 assertEquals("div", tag.name());
 assertEquals("div", tag.normalName());
 assertTrue(tag.isSelfClosing());
 assertEquals(1, tag.getAttributes().size());
 assertEquals("test", tag.getAttributes().get("id"));

 tag = tag.reset();
 assertSame(tag, tag.reset());
 assertNull(tag.name());
 assertNull(tag.normalName());
 assertFalse(tag.isSelfClosing());
 assertNull(tag.getAttributes());
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data("This is a comment");

 assertEquals("Comment", comment.tokenType().name());
 assertEquals("", comment.name());
 assertEquals("This is a comment", comment.getData());

 Comment resetComment = comment.reset();
 assertSame(comment, resetComment);
 assertNull(comment.getData());
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("A");

 assertEquals("Character", character.tokenType().name());
 assertEquals("A", character.getData());

 Character resetCharacter = character.reset();
 assertSame(character, resetCharacter);
 assertNull(character.getData());
 }

 @Test(expected = IllegalStateException.class)
 public void testMixedTokenTypes() {
 Token token = new Doctype();
 token.appendTagName("tagName");
 }

 @Test(expected = IllegalStateException.class)
 public void testSettingSelfClosingAttribute() {
 StartTag tag = new StartTag();
 tag.selfClosing(true).nameAttr("img", new Attributes().put("src", "test.jpg"));
 }

 @Test
 public void testTokenTypeEnum() {
 TokenType expectedType = TokenType.Comment;
 Token token = new Comment();
 assertEquals(expectedType, token.type);
 }
}