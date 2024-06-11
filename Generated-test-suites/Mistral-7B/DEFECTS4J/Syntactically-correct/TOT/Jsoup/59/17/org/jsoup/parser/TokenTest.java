import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.pubSysKey = "PUBLIC";
 doctypeToken.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
 doctypeToken.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
 doctypeToken.forceQuirks = false;

 Token expectedToken = new Doctype();
 expectedToken.name.append("html");
 expectedToken.pubSysKey = "PUBLIC";
 expectedToken.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
 expectedToken.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
 expectedToken.forceQuirks = false;

 assertEquals("Doctype tokens should be equal when all properties are set", expectedToken, doctypeToken);

 Doctype resetDoctypeToken = doctypeToken.reset();
 assertTrue("Doctype should be reset to its initial state", resetDoctypeToken.isDoctype());
 assertEquals("Name should be reset to null", null, resetDoctypeToken.name.toString());
 assertEquals("PubSysKey should be reset to null", null, resetDoctypeToken.pubSysKey);
 assertEquals("PublicIdentifier should be reset to null", null, resetDoctypeToken.publicIdentifier.toString());
 assertEquals("SystemIdentifier should be reset to null", null, resetDoctypeToken.systemIdentifier.toString());
 assertEquals("ForceQuirks should be reset to false", false, resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");

 Token expectedToken = new Comment();
 expectedToken.data.append("This is a comment");

 assertEquals("Comment tokens should be equal when all properties are set", expectedToken, commentToken);

 Comment resetCommentToken = commentToken.reset();
 assertTrue("Comment should be reset to its initial state", resetCommentToken.isComment());
 assertEquals("Data should be reset to null", null, resetCommentToken.getData().toString());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data = "!";

 Token expectedToken = new Character();
 expectedToken.data = "!";

 assertEquals("Character tokens should be equal when all properties are set", expectedToken, characterToken);

 Character resetCharacterToken = characterToken.reset();
 assertTrue("Character should be reset to its initial state", resetCharacterToken.isCharacter());
 assertEquals("Data should be reset to null", null, resetCharacterToken.getData().toString());
 }

 @Test(expected = AssertionError.class)
 public void testStartTagTokenWithEmptyTagName() {
 StartTag startTagToken = new StartTag();
 startTagToken.tagName = "";

 startTagToken.reset(); // This is to test the exception when creating a new attribute
 startTagToken.newAttribute();
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.tagName.append("html");
 startTagToken.selfClosing = false;

 Token expectedToken = new StartTag();
 expectedToken.tagName.append("html");
 expectedToken.selfClosing = false;

 assertEquals("StartTag tokens should be equal when all properties are set", expectedToken, startTagToken);

 StartTag resetStartTagToken = startTagToken.reset();
 assertTrue("StartTag should be reset to its initial state", resetStartTagToken.isStartTag());
 assertEquals("TagName should be reset to null", null, resetStartTagToken.tagName.toString());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.tagName.append("html");

 Token expectedToken = new EndTag();
 expectedToken.tagName.append("html");

 assertEquals("EndTag tokens should be equal when all properties are set", expectedToken, endTagToken);

 EndTag resetEndTagToken = endTagToken.reset();
 assertTrue("EndTag should be reset to its initial state", resetEndTagToken.isEndTag());
 assertEquals("TagName should be reset to null", null, resetEndTagToken.tagName.toString());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 Token expectedToken = new EOF();
 assertEquals("EOF tokens should be equal when all properties are set", expectedToken, eofToken);
 }
}