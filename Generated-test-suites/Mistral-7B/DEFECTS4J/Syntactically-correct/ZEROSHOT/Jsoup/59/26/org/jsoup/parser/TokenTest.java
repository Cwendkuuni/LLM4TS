import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 assertEquals("Doctype", doctypeToken.tokenType());

 doctypeToken.getName() = "html";
 assertEquals("html", doctypeToken.getName());
 doctypeToken.setForceQuirks(true);
 assertEquals(true, doctypeToken.isForceQuirks());

 Doctype resetDoctypeToken = doctypeToken.reset();
 assertEquals("Doctype", resetDoctypeToken.tokenType());
 assertNull(resetDoctypeToken.getName());
 assertNull(resetDoctypeToken.getPubSysKey());
 assertNull(resetDoctypeToken.getPublicIdentifier());
 assertNull(resetDoctypeToken.getSystemIdentifier());
 assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 assertEquals("StartTag", startTagToken.tokenType().name());

 StartTag htmlTag = startTagToken.name("html");
 assertEquals("html", htmlTag.name());
 assertEquals("HTML", htmlTag.normalName());

 StartTag nameAttrTag = htmlTag.nameAttr("xmlns", new Attributes().put("xmlns:test", "testValue"));
 assertEquals("html", nameAttrTag.name());
 assertEquals("HTML", nameAttrTag.normalName());
 assertEquals("xmlns:test", nameAttrTag.getAttributes().get("xmlns").toString());

 StartTag selfClosingTag = htmlTag.name("img").selfClosing(true);
 assertEquals("img", selfClosingTag.name());
 assertEquals("IMG", selfClosingTag.normalName());
 assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 assertEquals("EndTag", endTagToken.tokenType().name());

 EndTag bodyEndTag = endTagToken.name("body");
 assertEquals("body", bodyEndTag.name());
 assertEquals("BODY", bodyEndTag.normalName());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 assertEquals("Comment", commentToken.tokenType().name());

 commentToken.getData().append("This is a comment");
 assertEquals("This is a comment", commentToken.getData().toString());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 assertEquals("Character", characterToken.tokenType().name());

 Character characterTokenWithData = characterToken.data("&amp;");
 assertEquals("&amp;", characterTokenWithData.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();
 assertEquals("EOF", eofToken.tokenType().name());
 }
}