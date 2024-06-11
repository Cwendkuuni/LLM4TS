import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("Doctype");
 assertEquals(doctype.getClass(), Token.Doctype.class);
 doctype.isDoctype();
 assertFalse(doctype.isStartTag());
 assertFalse(doctype.isEndTag());
 assertFalse(doctype.isComment());
 assertFalse(doctype.isCharacter());
 assertFalse(doctype.isEOF());
 }

 @Test
 public void testDoctype_getName() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("");
 doctype.newName("test");
 doctype.getName().equals("test");
 }

 @Test
 public void testDoctype_publicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.newPublicIdentifier("public");
 assertEquals("public", doctype.getPublicIdentifier());
 doctype.newPublicIdentifier("public", "test");
 assertEquals("public", doctype.getPublicIdentifier());
 assertEquals("test", doctype.getSystemIdentifier());
 }

 @Test
 public void testDoctype_systemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.newSystemIdentifier("system");
 assertEquals("system", doctype.getSystemIdentifier());
 doctype.newSystemIdentifier("system", "test");
 assertEquals("system", doctype.getSystemIdentifier());
 assertEquals("test", doctype.getSystemIdentifier());
 }

 @Test
 public void testDoctype_isForceQuirks() {
 Doctype doctype = new Doctype();
 assertFalse(doctype.isForceQuirks());
 doctype.newForceQuirks(true);
 assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testTag() {
 Tag tag = new Tag();
 tag.getClass().equals(Token.Tag.class);
 tag.isDoctype();
 assertFalse(tag.isStartTag());
 assertFalse(tag.isEndTag());
 assertFalse(tag.isComment());
 assertFalse(tag.isCharacter());
 assertFalse(tag.isEOF());
 }

 @Test
 public void testTag_name() {
 Tag tag = new Tag();
 tag.newName("tagName");
 assertEquals("tagName", tag.name());
 }

 @Test
 public void testTag_selfClosing() {
 Tag tag = new Tag();
 tag.newSelfClosing(true);
 assertTrue(tag.isSelfClosing());
 tag.newSelfClosing(false);
 assertFalse(tag.isSelfClosing());
 }

 @Test
 public void testTag_attributes() {
 Tag tag = new StartTag();
 tag.newAttribute();
 tag.getAttributes().size();
 assertEquals(1, tag.getAttributes().size());
 tag.newAttribute("test", "testValue");
 tag.getAttributes().get("test").getValue().equals("testValue");
 tag.newAttribute("test2");
 tag.getAttributes().get("test2").getValue().equals("");
 tag.getAttributes().size();
 assertEquals(2, tag.getAttributes().size());
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag();
 startTag.getClass().equals(Token.StartTag.class);
 startTag.isDoctype();
 assertFalse(startTag.isDoctype());
 assertFalse(startTag.isEndTag());
 assertFalse(startTag.isComment());
 assertFalse(startTag.isCharacter());
 assertFalse(startTag.isEOF());
 startTag.newName("test");
 assertEquals("test", startTag.name());
 }

 @Test
 public void testStartTag_attributes() {
 StartTag startTag = new StartTag();
 startTag.newName("test");
 startTag.newAttribute("test", "testValue");
 assertEquals("test", startTag.name());
 assertEquals("testValue", startTag.getAttributes().get("test").getValue());
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag();
 endTag.getClass().equals(Token.EndTag.class);
 endTag.isDoctype();
 assertFalse(endTag.isDoctype());
 assertFalse(endTag.isStartTag());
 assertFalse(endTag.isEndTag());
 assertFalse(endTag.isComment());
 assertFalse(endTag.isCharacter());
 assertFalse(endTag.isEOF());
 endTag.newName("test");
 assertEquals("test", endTag.name());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.getClass().equals(Token.Comment.class);
 comment.isDoctype();
 assertFalse(comment.isDoctype());
 assertFalse(comment.isStartTag());
 assertFalse(comment.isEndTag());
 assertFalse(comment.isComment());
 assertFalse(comment.isCharacter());
 assertFalse(comment.isEOF());
 comment.newData("test");
 assertEquals("test", comment.getData());
 }

 @Test
 public void testCharacter() {
 Character character = new Character("test");
 character.getClass().equals(Token.Character.class);
 character.isDoctype();
 assertFalse(character.isDoctype());
 assertFalse(character.isStartTag());
 assertFalse(character.isEndTag());
 assertFalse(character.isComment());
 assertFalse(character.isCharacter());
 assertFalse(character.isEOF());
 assertEquals("test", character.getData());
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();
 eof.getClass().equals(Token.EOF.class);
 eof.isDoctype();
 assertFalse(eof.isDoctype());
 assertFalse(eof.isStartTag());
 assertFalse(eof.isEndTag());
 assertFalse(eof.isComment());
 assertFalse(eof.isCharacter());
 assertFalse(eof.isEOF());
 }
}