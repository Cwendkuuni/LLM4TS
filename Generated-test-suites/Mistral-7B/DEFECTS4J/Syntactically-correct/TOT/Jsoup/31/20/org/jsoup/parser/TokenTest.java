import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.publicIdentifier.append("W3C");
 doctypeToken.systemIdentifier.append("/TR/REC-html401-20001219/DTD/html.dtd");
 doctypeToken.forceQuirks = true;

 Token expectedToken = new Doctype();
 expectedToken.name.append("html");
 expectedToken.publicIdentifier.append("W3C");
 expectedToken.systemIdentifier.append("/TR/REC-html401-20001219/DTD/html.dtd");
 expectedToken.forceQuirks = true;

 Assert.assertEquals(expectedToken, doctypeToken);
 }

 @Test
 public void testNameTag() {
 StartTag nameTag = new StartTag("html");
 String expectedTagName = "html";

 Assert.assertEquals(expectedTagName, nameTag.name());
 }

 @Test
 public void testSelfClosingTag() {
 StartTag imgTag = new StartTag("img").name("img").isSelfClosing(true);
 boolean expectedSelfClosing = true;

 Assert.assertEquals(expectedSelfClosing, imgTag.isSelfClosing());
 }

 @Test
 public void testAttributesTag() {
 StartTag pTag = new StartTag("p");
 pTag.appendAttributeName("class").appendAttributeValue("test");
 pTag.newAttribute();
 pTag.appendAttributeName("id").appendAttributeValue("testId");
 pTag.newAttribute();

 Attributes expectedAttributes = new Attributes();
 expectedAttributes.put("class", "test");
 expectedAttributes.put("id", "testId");

 Assert.assertEquals(expectedAttributes, pTag.getAttributes());
 }

 @Test
 public void testEmptyTag() {
 StartTag imgTag = new StartTag("img");
 imgTag.isSelfClosing(true);

 Assert.assertEquals("<img>", imgTag.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateAttributeName() {
 StartTag pTag = new StartTag("p");
 pTag.appendAttributeName("class").appendAttributeValue("test");
 pTag.newAttribute(); // New attribute with the same name
 pTag.appendAttributeName("class").appendAttributeValue("test2");
 }

 @Test
 public void testEndTag() {
 EndTag pTag = new EndTag("p");
 String expectedTagName = "p";

 Assert.assertEquals(expectedTagName, pTag.name());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");

 Token expectedToken = new Comment();
 expectedToken.data.append("This is a comment");

 Assert.assertEquals(expectedToken, commentToken);
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character('A');
 String expectedCharacter = "A";

 Assert.assertEquals(expectedCharacter, characterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 Assert.assertEquals(TokenType.EOF, eofToken.type);
 }
}