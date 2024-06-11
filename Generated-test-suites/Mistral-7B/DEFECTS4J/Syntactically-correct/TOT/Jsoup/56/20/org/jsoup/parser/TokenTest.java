import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;
import static org.junit.Assert.*;

public class TokenTest {

 private Token token;

 @Before
 public void setUp() {
 token = null;
 }

 @Test
 public void testDoctype() {
 token = new Doctype();
 Doctype doctype = (Doctype) token;
 doctype.name.append("html");
 doctype.publicIdentifier.append("public id");
 doctype.systemIdentifier.append("system id");
 doctype.forceQuirks = true;

 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertEquals("public id", doctype.getPublicIdentifier());
 assertEquals("system id", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());

 // Test reset
 Doctype resetDoctype = doctype.reset();
 assertEquals(doctype.getClass(), resetDoctype.getClass());
 assertFalse(resetDoctype.isForceQuirks());
 }

 @Test
 public void testStartTag() {
 token = new StartTag();
 StartTag startTag = (StartTag) token;
 startTag.name.append("html");
 startTag.attributes = new Attributes();
 startTag.attributes.put(new Attribute("id", "root"));

 assertEquals("StartTag", startTag.tokenType().toString());
 assertEquals("html", startTag.name());
 assertEquals("html", startTag.normalName());
 assertEquals(1, startTag.getAttributes().size());
 assertNotNull(startTag.getAttributes().get("id"));
 assertTrue(startTag.isSelfClosing());

 // Test reset
 StartTag resetStartTag = startTag.reset();
 assertEquals(startTag.getClass(), resetStartTag.getClass());
 assertNull(resetStartTag.tagName);
 assertNull(resetStartTag.normalName);
 assertNull(resetStartTag.pendingAttributeName);
 reset(resetStartTag.pendingAttributeValue);
 assertNull(resetStartTag.pendingAttributeValueS);
 assertFalse(resetStartTag.hasEmptyAttributeValue);
 assertFalse(resetStartTag.hasPendingAttributeValue);
 assertFalse(resetStartTag.selfClosing);
 assertNull(resetStartTag.attributes);
 }

 @Test
 public void testEndTag() {
 token = new EndTag();
 EndTag endTag = (EndTag) token;
 endTag.name.append("html");

 assertEquals("EndTag", endTag.tokenType().toString());
 assertEquals("html", endTag.name());
 assertEquals("html", endTag.normalName());

 // Test reset
 EndTag resetEndTag = endTag.reset();
 assertEquals(endTag.getClass(), resetEndTag.getClass());
 assertNull(resetEndTag.tagName);
 assertNull(resetEndTag.normalName);
 }

 @Test
 public void testComment() {
 token = new Comment();
 Comment comment = (Comment) token;
 comment.data.append("This is a comment");

 assertEquals("Comment", comment.tokenType().toString());
 assertEquals("", comment.name());
 assertEquals("This is a comment", comment.getData());

 // Test reset
 Comment resetComment = comment.reset();
 assertEquals(comment.getClass(), resetComment.getClass());
 assertNull(resetComment.tagName);
 assertNull(resetComment.normalName);
 reset(resetComment.data);
 assertNull(resetComment.data);
 }

 @Test
 public void testCharacter() {
 token = new Character();
 Character character = (Character) token;
 character.data("&lt;");

 assertEquals("Character", character.tokenType().toString());
 assertEquals("&lt;", character.getData());

 // Test reset
 Character resetCharacter = character.reset();
 assertEquals(character.getClass(), resetCharacter.getClass());
 assertNull(resetCharacter.data);
 }

 @Test(expected = ClassCastException.class)
 public void testMismatchedTokenTypes() {
 token = new Doctype();
 StartTag startTag = (StartTag) token;
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingNameForTag() {
 token = new StartTag();
 token.attributes = new Attributes();
 token.attributes.put(new Attribute("id", "root"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullDataForCharacter() {
 token = new Character();
 character.data(null);
 }

 // Test EOF
 @Test
 public void testEOF() {
 token = new EOF();
 EOF eof = (EOF) token;
 assertEquals("EOF", eof.tokenType().toString());
 }
}