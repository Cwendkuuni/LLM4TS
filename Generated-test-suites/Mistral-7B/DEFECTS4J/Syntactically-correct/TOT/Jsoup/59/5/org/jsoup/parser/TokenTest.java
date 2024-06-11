import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
 doctypeToken.name("html");
 doctypeToken.pubSysKey("DTD HTML 5");
 doctypeToken.systemIdentifier("/html5.dtd");
 doctypeToken.forceQuirks(true);

 Assertions.assertEquals("Doctype", doctypeToken.tokenType());
 Assertions.assertEquals("html", doctypeToken.getName());
 Assertions.assertEquals("DTD HTML 5", doctypeToken.getPubSysKey());
 Assertions.assertEquals("/html5.dtd", doctypeToken.getSystemIdentifier());
 Assertions.assertTrue(doctypeToken.isForceQuirks());

 Doctype resetDoctypeToken = doctypeToken.reset();
 Assertions.assertEquals("Doctype", resetDoctypeToken.tokenType());
 Assertions.assertNull(resetDoctypeToken.getName());
 Assertions.assertNull(resetDoctypeToken.getPubSysKey());
 Assertions.assertNull(resetDoctypeToken.getSystemIdentifier());
 Assertions.assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("div");
 startTagToken.attributes(new Attributes().put("class", "container"));

 Assertions.assertEquals("StartTag", startTagToken.tokenType());
 Assertions.assertEquals("div", startTagToken.name());
 Assertions.assertEquals("div", startTagToken.normalName());
 Assertions.assertFalse(startTagToken.isSelfClosing());

 StartTag resetStartTagToken = startTagToken.reset();
 Assertions.assertEquals("StartTag", resetStartTagToken.tokenType());
 Assertions.assertNull(resetStartTagToken.name());
 Assertions.assertNull(resetStartTagToken.normalName());
 Assertions.assertNull(resetStartTagToken.getAttributes());
 Assertions.assertFalse(resetStartTagToken.isSelfClosing());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("div");

 Assertions.assertEquals("EndTag", endTagToken.tokenType());
 Assertions.assertEquals("div", endTagToken.name());
 Assertions.assertEquals("div", endTagToken.normalName());
 Assertions.assertFalse(endTagToken.isSelfClosing());

 EndTag resetEndTagToken = endTagToken.reset();
 Assertions.assertEquals("EndTag", resetEndTagToken.tokenType());
 Assertions.assertNull(resetEndTagToken.name());
 Assertions.assertNull(resetEndTagToken.normalName());
 Assertions.assertNull(resetEndTagToken.getAttributes());
 Assertions.assertFalse(resetEndTagToken.isSelfClosing());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data("This is a test comment.");

 Assertions.assertEquals("Comment", commentToken.tokenType());
 Assertions.assertEquals("", commentToken.name());
 Assertions.assertEquals("This is a test comment.", commentToken.getData());

 Comment resetCommentToken = commentToken.reset();
 Assertions.assertEquals("Comment", resetCommentToken.tokenType());
 Assertions.assertNull(resetCommentToken.name());
 Assertions.assertNull(resetCommentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&amp;");

 Assertions.assertEquals("Character", characterToken.tokenType());
 Assertions.assertEquals("&amp;", characterToken.getData());

 Character resetCharacterToken = characterToken.reset();
 Assertions.assertEquals("Character", resetCharacterToken.tokenType());
 Assertions.assertNull(resetCharacterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 Assertions.assertEquals("EOF", eofToken.tokenType());

 EOF resetEOFToken = eofToken.reset();
 Assertions.assertEquals("EOF", resetEOFToken.tokenType());
 }
}