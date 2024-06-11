import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;
import static org.junit.Assert.*;

public class TokenTest {

 private Token token;

 @Before
 public void setup() {
 token = new Doctype();
 }

 @Test
 public void testDoctype() {
 Doctype doctype = (Doctype) token;
 doctype.getName().equals("doctype");
 assertTrue(doctype.isDoctype());
 }

 @Test
 public void testDoctypeReset() {
 Doctype doctype = (Doctype) token;
 doctype.reset();
 assertNull(doctype.getName());
 assertFalse(doctype.isDoctype());
 }

 @Test
 public void testStartTag() {
 token = new StartTag();
 StartTag startTag = (StartTag) token;
 startTag.name("html").attributes(new Attributes().put("lang", "en"));
 assertEquals("html", startTag.name());
 assertEquals("html", startTag.normalName());
 assertEquals("html", startTag.toString());
 assertTrue(startTag.isStartTag());
 }

 @Test
 public void testStartTagReset() {
 StartTag startTag = (StartTag) token;
 startTag.reset();
 assertNull(startTag.tagName);
 assertNull(startTag.normalName);
 assertNull(startTag.pendingAttributeName);
 assertNull(startTag.pendingAttributeValue);
 assertNull(startTag.pendingAttributeValueS);
 assertFalse(startTag.hasEmptyAttributeValue);
 assertFalse(startTag.hasPendingAttributeValue);
 assertFalse(startTag.selfClosing);
 assertNull(startTag.attributes);
 }

 @Test
 public void testEndTag() {
 token = new EndTag();
 EndTag endTag = (EndTag) token;
 endTag.name("html");
 assertEquals("html", endTag.name());
 assertEquals("html", endTag.normalName());
 assertTrue(endTag.isEndTag());
 }

 @Test
 public void testComment() {
 token = new Comment();
 Comment comment = (Comment) token;
 comment.data("This is a comment");
 assertEquals("This is a comment", comment.getData());
 assertTrue(comment.isComment());
 }

 @Test
 public void testCommentReset() {
 Comment comment = (Comment) token;
 comment.reset();
 assertNull(comment.getData());
 assertFalse(comment.isComment());
 }

 @Test
 public void testCharacter() {
 token = new Character();
 Character character = (Character) token;
 character.data("A");
 assertEquals("A", character.getData());
 assertTrue(character.isCharacter());
 }

 @Test
 public void testCharacterReset() {
 Character character = (Character) token;
 character.reset();
 assertNull(character.getData());
 assertFalse(character.isCharacter());
 }

 @Test
 public void testEOF() {
 token = new EOF();
 EOF eof = (EOF) token;
 assertTrue(eof.isEOF());
 }

 @Test
 public void testResetStringBuilder() {
 StringBuilder sb = new StringBuilder();
 Token.reset(sb);
 assertTrue(sb.length() == 0);
 }
}