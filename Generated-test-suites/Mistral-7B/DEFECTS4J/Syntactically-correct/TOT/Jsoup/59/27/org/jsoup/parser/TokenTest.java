import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name().equals("Doctype");
 doctypeToken.reset();
 Assertions.assertNull(doctypeToken.getName());
 doctypeToken.pubSysKey = "pubSysKey";
 doctypeToken.publicIdentifier.append("publicIdentifier");
 doctypeToken.systemIdentifier.append("systemIdentifier");
 doctypeToken.forceQuirks = true;
 doctypeToken.getName().equals("Doctype");
 doctypeToken.getPubSysKey().equals("pubSysKey");
 doctypeToken.getPublicIdentifier().equals("publicIdentifier");
 doctypeToken.getSystemIdentifier().equals("systemIdentifier");
 doctypeToken.isForceQuirks().equals(true);
 doctypeToken.reset();
 Assertions.assertNull(doctypeToken.getName());
 Assertions.assertNull(doctypeToken.getPubSysKey());
 Assertions.assertNull(doctypeToken.getPublicIdentifier());
 Assertions.assertNull(doctypeToken.getSystemIdentifier());
 Assertions.assertFalse(doctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html").normalName("html");
 startTagToken.isSelfClosing().equals(false);
 Attributes attributes = new Attributes();
 attributes.put("class", "classValue");
 startTagToken.nameAttr("html", attributes);
 startTagToken.name().equals("html");
 startTagToken.normalName().equals("html");
 startTagToken.isSelfClosing().equals(false);
 startTagToken.getAttributes().get("class").getValue().equals("classValue");
 startTagToken.toString().equals("<html class=\"classValue\"/>");
 startTagToken.reset();
 startTagToken.name(null).normalName(null).isSelfClosing(false).attributes(null);
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("body").normalName("body");
 endTagToken.toString().equals("</body>");
 endTagToken.reset();
 endTagToken.name(null).normalName(null);
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data("-- This is a comment").getData().equals("-- This is a comment");
 commentToken.bogus = true;
 commentToken.toString().equals("<!-- -- This is a comment -->");
 commentToken.reset();
 commentToken.data(null).bogus(false);
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("Character data").getData().equals("Character data");
 characterToken.toString().equals("Character data");
 characterToken.reset();
 characterToken.data(null);
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();
 eofToken.isEOF().equals(true);
 eofToken.toString().equals("");
 eofToken.reset();
 }

 @Test
 public void testResetAllTokens() {
 Doctype doctypeToken = new Doctype();
 StartTag startTagToken = new StartTag();
 EndTag endTagToken = new EndTag();
 Comment commentToken = new Comment();
 Character characterToken = new Character();
 EOF eofToken = new EOF();
 doctypeToken.reset();
 startTagToken.reset();
 endTagToken.reset();
 commentToken.reset();
 characterToken.reset();
 eofToken.reset();
 // empty asserts
 }
}