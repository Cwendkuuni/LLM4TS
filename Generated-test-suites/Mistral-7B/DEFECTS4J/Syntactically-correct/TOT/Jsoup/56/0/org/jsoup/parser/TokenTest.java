import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public id");
 doctype.systemIdentifier.append("system id");
 doctype.forceQuirks = true;

 Token resetDoctype = doctype.reset();
 assertEquals(TokenType.Doctype, resetDoctype.tokenType());
 assertEquals("Doctype", resetDoctype.getClass().getSimpleName());
 assertEquals("html", resetDoctype.asDoctype().getName());
 assertEquals("public id", resetDoctype.asDoctype().getPublicIdentifier());
 assertEquals("system id", resetDoctype.asDoctype().getSystemIdentifier());
 assertEquals(true, resetDoctype.asDoctype().isForceQuirks());
 }

 @Test
 public void testResetDoctype() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public id");
 doctype.systemIdentifier.append("system id");
 doctype.forceQuirks = true;

 Token doctypeToken = doctype;
 doctype = doctype.reset();

 assertFalse(doctypeToken.isForceQuirks());
 assertNull(doctypeToken.getName());
 assertNull(doctypeToken.getPublicIdentifier());
 assertNull(doctypeToken.getSystemIdentifier());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.name("html");
 startTag.attributes(new Attributes().put("class", "testClass"));

 Token resetStartTag = startTag.reset();
 assertEquals(TokenType.StartTag, resetStartTag.tokenType());
 assertEquals("StartTag", resetStartTag.getClass().getSimpleName());
 assertEquals("html", resetStartTag.asStartTag().name());
 assertEquals("html", resetStartTag.asStartTag().normalName());
 assertSame(startTag.attributes, resetStartTag.asStartTag().getAttributes());
 }

 @Test
 public void testResetStartTag() {
 StartTag startTag = new StartTag();
 startTag.name("html");
 startTag.attributes(new Attributes().put("class", "testClass"));

 Token startTagToken = startTag;
 startTag = startTag.reset();

 assertNull(startTagToken.attributes);
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name("html");

 Token resetEndTag = endTag.reset();
 assertEquals(TokenType.EndTag, resetEndTag.tokenType());
 assertEquals("EndTag", resetEndTag.getClass().getSimpleName());
 assertEquals("html", resetEndTag.asEndTag().name());
 }

 @Test
 public void testResetEndTag() {
 EndTag endTag = new EndTag();
 endTag.name("html");

 Token endTagToken = endTag;
 endTag = endTag.reset();

 assertNull(endTagToken.name());
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data("This is a comment");

 Token resetComment = comment.reset();
 assertEquals(TokenType.Comment, resetComment.tokenType());
 assertEquals("Comment", resetComment.getClass().getSimpleName());
 assertEquals("This is a comment", resetComment.asComment().getData());
 }

 @Test
 public void testResetComment() {
 Comment comment = new Comment();
 comment.data("This is a comment");

 Token commentToken = comment;
 comment = comment.reset();

 assertNull(commentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("&amp;");

 Token resetCharacter = character.reset();
 assertEquals(TokenType.Character, resetCharacter.tokenType());
 assertEquals("Character", resetCharacter.getClass().getSimpleName());
 assertEquals("&amp;", resetCharacter.asCharacter().getData());
 }

 @Test
 public void testResetCharacter() {
 Character character = new Character();
 character.data("&amp;");

 Token characterToken = character;
 character = character.reset();

 assertNull(characterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 Token resetEOF = eof.reset();
 assertEquals(TokenType.EOF, resetEOF.tokenType());
 assertEquals("EOF", resetEOF.getClass().getSimpleName());
 }

 @Test
 public void testResetEOF() {
 EOF eof = new EOF();

 Token eofToken = eof;
 eof = eof.reset();

 assertNull(eofToken);
 }
}