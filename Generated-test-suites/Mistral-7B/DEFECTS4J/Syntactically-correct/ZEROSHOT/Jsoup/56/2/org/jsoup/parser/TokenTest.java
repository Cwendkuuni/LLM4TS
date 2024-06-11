import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 assertTrue(doctypeToken.isDoctype());
 assertEquals(TokenType.Doctype, doctypeToken.type);

 doctypeToken.reset();
 assertFalse(doctypeToken.isDoctype());
 }

 @Test
 public void testDoctypeGetters() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.forceQuirks(true);

 assertEquals("html", doctypeToken.getName());
 assertEquals("html", doctypeToken.name().toLowerCase());
 assertTrue(doctypeToken.isForceQuirks());
 }

 @Test
 public void testResetDoctype() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.forceQuirks(true);
 doctypeToken.reset();

 assertNull(doctypeToken.getName());
 assertFalse(doctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 assertTrue(startTagToken.isStartTag());
 assertEquals(TokenType.StartTag, startTagToken.type);

 startTagToken.name("div");

 assertEquals("div", startTagToken.name());
 assertEquals("div", startTagToken.normalName());
 }

 @Test
 public void testStartTagGetAttributes() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("div").attributes(new Attributes().put("id", "container"));

 assertEquals("div", startTagToken.name());
 assertEquals("div", startTagToken.normalName());
 assertEquals("container", startTagToken.getAttributes().get("id"));
 }

 @Test
 public void testResetStartTag() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("div").attributes(new Attributes().put("id", "container"));
 startTagToken.reset();

 assertNull(startTagToken.name());
 assertNull(startTagToken.normalName());
 assertNull(startTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 assertTrue(endTagToken.isEndTag());
 assertEquals(TokenType.EndTag, endTagToken.type);

 endTagToken.name("div");

 assertEquals("div", endTagToken.name());
 assertEquals("div", endTagToken.normalName());
 }

 @Test
 public void testResetEndTag() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("div");
 endTagToken.reset();

 assertNull(endTagToken.name());
 assertNull(endTagToken.normalName());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 assertTrue(commentToken.isComment());
 assertEquals(TokenType.Comment, commentToken.type);

 commentToken.data("This is a comment");

 assertEquals("This is a comment", commentToken.getData());
 assertEquals("<!--This is a comment-->", commentToken.toString());
 }

 @Test
 public void testResetComment() {
 Comment commentToken = new Comment();
 commentToken.data("This is a comment");
 commentToken.reset();

 assertNull(commentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("A");
 assertTrue(characterToken.isCharacter());
 assertEquals(TokenType.Character, characterToken.type);

 assertEquals("A", characterToken.getData());
 assertEquals("A", characterToken.toString());
 }

 @Test
 public void testResetCharacter() {
 Character characterToken = new Character();
 characterToken.data("A");
 characterToken.reset();

 assertNull(characterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();
 assertTrue(eofToken.isEOF());
 assertEquals(TokenType.EOF, eofToken.type);
 }

 @Test
 public void testResetEOF() {
 EOF eofToken = new EOF();
 eofToken.reset();

 assertTrue(eofToken.isEOF());
 }
}