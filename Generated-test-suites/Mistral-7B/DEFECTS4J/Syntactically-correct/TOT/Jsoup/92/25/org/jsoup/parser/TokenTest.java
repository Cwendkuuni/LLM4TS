import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.CData;

public class TokenTest {

 @Test
 void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("DTD HTML 4.01 Transitional");
 doctypeToken.systemIdentifier("/myapp/html4.dtd");
 doctypeToken.forceQuirks(true);

 Doctype expectedDoctype = new Doctype();
 expectedDoctype.name("html");
 expectedDoctype.pubSysKey("DTD HTML 4.01 Transitional");
 expectedDoctype.systemIdentifier("/myapp/html4.dtd");
 expectedDoctype.forceQuirks(true);

 Assertions.assertEquals(expectedDoctype, doctypeToken);
 }

 @Test
 void testResetDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("DTD HTML 4.01 Transitional");
 doctypeToken.systemIdentifier("/myapp/html4.dtd");
 doctypeToken.forceQuirks(true);
 Doctype resetDoctypeToken = doctypeToken.reset();

 Assertions.assertNotEquals(doctypeToken, resetDoctypeToken);
 Assertions.assertEquals(TokenType.Doctype, resetDoctypeToken.tokenType());
 Assertions.assertNull(resetDoctypeToken.getName());
 Assertions.assertNull(resetDoctypeToken.getPubSysKey());
 Assertions.assertNull(resetDoctypeToken.getPublicIdentifier());
 Assertions.assertNull(resetDoctypeToken.getSystemIdentifier());
 Assertions.assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html");
 startTagToken.attributes(new Attributes().put("class", "myclass"));

 StartTag expectedStartTagToken = new StartTag();
 expectedStartTagToken.name("html");
 expectedStartTagToken.attributes(new Attributes().put("class", "myclass"));

 Assertions.assertEquals(expectedStartTagToken, startTagToken);
 }

 @Test
 void testResetStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html");
 startTagToken.attributes(new Attributes().put("class", "myclass"));
 StartTag resetStartTagToken = startTagToken.reset();

 Assertions.assertNotEquals(startTagToken, resetStartTagToken);
 Assertions.assertEquals(TokenType.StartTag, resetStartTagToken.tokenType());
 Assertions.assertNull(resetStartTagToken.tagName);
 Assertions.assertNull(resetStartTagToken.normalName);
 Assertions.assertNull(resetStartTagToken.pendingAttributeName);
 Assertions.assertNull(resetStartTagToken.pendingAttributeValue);
 Assertions.assertNull(resetStartTagToken.pendingAttributeValueS);
 Assertions.assertFalse(resetStartTagToken.hasEmptyAttributeValue);
 Assertions.assertFalse(resetStartTagToken.hasPendingAttributeValue);
 Assertions.assertFalse(resetStartTagToken.selfClosing);
 Assertions.assertNull(resetStartTagToken.attributes);
 }

 @Test
 void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data("-- This is a comment");

 Comment expectedCommentToken = new Comment();
 expectedCommentToken.data("-- This is a comment");

 Assertions.assertEquals(expectedCommentToken, commentToken);
 }

 @Test
 void testResetCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data("-- This is a comment");
 Comment resetCommentToken = commentToken.reset();

 Assertions.assertNotEquals(commentToken, resetCommentToken);
 Assertions.assertEquals(TokenType.Comment, resetCommentToken.tokenType());
 Assertions.assertNull(resetCommentToken.getData());
 }

 @Test
 void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&");

 Character expectedCharacterToken = new Character();
 expectedCharacterToken.data("&");

 Assertions.assertEquals(expectedCharacterToken, characterToken);
 }

 @Test
 void testResetCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&");
 Character resetCharacterToken = characterToken.reset();

 Assertions.assertNotEquals(characterToken, resetCharacterToken);
 Assertions.assertEquals(TokenType.Character, resetCharacterToken.tokenType());
 Assertions.assertNull(resetCharacterToken.getData());
 }

 @Test
 void testCDataToken() {
 CData cDataToken = new CData("<![CDATA[Hello, World!]]>");

 CData expectedCDataToken = new CData("<![CDATA[Hello, World!]]>");

 Assertions.assertEquals(expectedCDataToken, cDataToken);
 }

 @Test
 void testResetCDataToken() {
 CData cDataToken = new CData("<![CDATA[Hello, World!]]>");
 CData resetCDataToken = cDataToken.reset();

 Assertions.assertNotEquals(cDataToken, resetCDataToken);
 Assertions.assertEquals(TokenType.Character, resetCDataToken.tokenType());
 Assertions.assertNull(resetCDataToken.getData());
 }

 @Test
 void testEOFToken() {
 EOF eofToken = new EOF();

 EOF expectedEOFToken = new EOF();

 Assertions.assertEquals(expectedEOFToken, eofToken);
 }

 @Test
 void testResetEOFToken() {
 EOF eofToken = new EOF();
 EOF resetEOFToken = eofToken.reset();

 Assertions.assertNotEquals(eofToken, resetEOFToken);
 Assertions.assertEquals(TokenType.EOF, resetEOFToken.tokenType());
 }
}