import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.publicIdentifier.append("Public ID: ");
 doctypeToken.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
 doctypeToken.forceQuirks = true;

 assertEquals("Doctype", doctypeToken.tokenType());
 assertEquals("html", doctypeToken.getName());
 assertEquals("Public ID: Public ID: ", doctypeToken.getPublicIdentifier());
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctypeToken.getSystemIdentifier());
 assertTrue(doctypeToken.isForceQuirks());

 Doctype resetDoctypeToken = doctypeToken.reset();
 assertNotSame(doctypeToken, resetDoctypeToken);
 assertEquals("Doctype", resetDoctypeToken.tokenType());
 assertNull(resetDoctypeToken.getName());
 assertNull(resetDoctypeToken.getPublicIdentifier());
 assertNull(resetDoctypeToken.getSystemIdentifier());
 assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html").attributes(new Attributes().put("lang", "en"));

 assertEquals("StartTag", startTagToken.tokenType());
 assertEquals("html", startTagToken.name());
 assertEquals("html", startTagToken.normalName());
 assertTrue(startTagToken.isSelfClosing());
 assertNull(startTagToken.getAttributes());

 StartTag resetStartTagToken = startTagToken.reset();
 assertNotSame(startTagToken, resetStartTagToken);
 assertEquals("StartTag", resetStartTagToken.tokenType());
 assertNull(resetStartTagToken.name());
 assertNull(resetStartTagToken.normalName());
 assertFalse(resetStartTagToken.isSelfClosing());
 assertNull(resetStartTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("html");

 assertEquals("EndTag", endTagToken.tokenType());
 assertEquals("html", endTagToken.name());
 assertEquals("html", endTagToken.normalName());

 EndTag resetEndTagToken = endTagToken.reset();
 assertNotSame(endTagToken, resetEndTagToken);
 assertEquals("EndTag", resetEndTagToken.tokenType());
 assertNull(resetEndTagToken.name());
 assertNull(resetEndTagToken.normalName());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("--This is a comment--");

 assertEquals("Comment", commentToken.tokenType());
 assertEquals("--This is a comment--", commentToken.getData());

 Comment resetCommentToken = commentToken.reset();
 assertNotSame(commentToken, resetCommentToken);
 assertEquals("Comment", resetCommentToken.tokenType());
 assertNull(resetCommentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&hellip;");

 assertEquals("Character", characterToken.tokenType());
 assertEquals("&hellip;", characterToken.getData());

 Character resetCharacterToken = characterToken.reset();
 assertNotSame(characterToken, resetCharacterToken);
 assertEquals("Character", resetCharacterToken.tokenType());
 assertNull(resetCharacterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 assertEquals("EOF", eofToken.tokenType());

 EOF resetEOFToken = eofToken.reset();
 assertNotSame(eofToken, resetEOFToken);
 assertEquals("EOF", resetEOFToken.tokenType());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidToken() {
 Token invalidToken = new Token();
 // This would throw a RuntimeException if the constructor was called
 assertTrue(false);
 }
}