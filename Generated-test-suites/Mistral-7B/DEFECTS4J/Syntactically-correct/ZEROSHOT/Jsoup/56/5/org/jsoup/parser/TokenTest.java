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
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 doctype.setForceQuirks(true);
 doctype.reset();

 assertEquals("Doctype", doctype.tokenType());
 assertNull(doctype.getName());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testStartTagReset() {
 StartTag startTag = new StartTag();
 startTag.name("html");
 startTag.reset();

 assertEquals("StartTag", startTag.tokenType());
 assertNull(startTag.tagName);
 assertNull(startTag.getAttributes());
 }

 @Test
 public void testEndTagReset() {
 EndTag endTag = new EndTag();
 endTag.name("html");
 endTag.reset();

 assertEquals("EndTag", endTag.tokenType());
 assertNull(endTag.tagName);
 }

 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.getData("This is a comment");
 comment.reset();

 assertEquals("Comment", comment.tokenType());
 assertNull(comment.getData());
 }

 @Test
 public void testCharacterReset() {
 Character character = new Character();
 character.data("A");
 character.reset();

 assertEquals("Character", character.tokenType());
 assertNull(character.getData());
 }

 @Test
 public void testTokenTypeIdentification() {
 Token token = new Doctype();
 assertEquals(TokenType.Doctype, token.tokenTypeAsEnum());

 token = new StartTag();
 assertEquals(TokenType.StartTag, token.tokenTypeAsEnum());

 token = new EndTag();
 assertEquals(TokenType.EndTag, token.tokenTypeAsEnum());

 token = new Comment();
 assertEquals(TokenType.Comment, token.tokenTypeAsEnum());

 token = new Character();
 assertEquals(TokenType.Character, token.tokenTypeAsEnum());

 token = new EOF();
 assertEquals(TokenType.EOF, token.tokenTypeAsEnum());
 }

 @Test
 public void testTokenTypeStringIdentification() {
 Token token = new Doctype();
 assertTrue(token.isDoctype());
 assertSame(Doctype.class, token.getClass());

 token = new StartTag();
 assertTrue(token.isStartTag());
 assertSame(StartTag.class, token.getClass());

 token = new EndTag();
 assertTrue(token.isEndTag());
 assertSame(EndTag.class, token.getClass());

 token = new Comment();
 assertTrue(token.isComment());
 assertSame(Comment.class, token.getClass());

 token = new Character();
 assertTrue(token.isCharacter());
 assertSame(Character.class, token.getClass());

 token = new EOF();
 assertTrue(token.isEOF());
 assertSame(EOF.class, token.getClass());
 }
}