import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.publicIdentifier.append("Public ID");
 doctypeToken.systemIdentifier.append("System ID");
 doctypeToken.isForceQuirks(true);

 Token expectedToken = new Doctype();
 expectedToken.name.append("html");
 expectedToken.publicIdentifier.append("Public ID");
 expectedToken.systemIdentifier.append("System ID");
 expectedToken.isForceQuirks(true);

 Assert.assertEquals(expectedToken.tokenType(), doctypeToken.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), doctypeToken.tokenType());
 Assert.assertEquals(expectedToken.toString(), doctypeToken.toString());
 }

 @Test
 public void testStartTagTokenWithEmptyName() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html");

 Token expectedToken = new StartTag();
 expectedToken.name("html");

 Assert.assertEquals(expectedToken.tokenType(), startTagToken.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), startTagToken.tokenType());
 Assert.assertEquals(expectedToken.toString(), startTagToken.toString());
 }

 @Test
 public void testStartTagTokenWithAttributes() {
 StartTag startTagToken = new StartTag("html");
 startTagToken.newAttribute();
 startTagToken.appendAttributeName("class");
 startTagToken.appendAttributeValue("test");
 startTagToken.newAttribute();
 startTagToken.appendAttributeName("id");
 startTagToken.appendAttributeValue("testID");

 Token expectedToken = new StartTag("html");
 expectedToken.newAttribute();
 expectedToken.attributes.put(new Attribute("class", "test"));
 expectedToken.newAttribute();
 expectedToken.attributes.put(new Attribute("id", "testID"));

 Assert.assertEquals(expectedToken.tokenType(), startTagToken.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), startTagToken.tokenType());
 Assert.assertEquals(expectedToken.toString(), startTagToken.toString());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag("html");

 Token expectedToken = new EndTag("html");

 Assert.assertEquals(expectedToken.tokenType(), endTagToken.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), endTagToken.tokenType());
 Assert.assertEquals(expectedToken.toString(), endTagToken.toString());
 }

 @Test(expected = AssertionError.class)
 public void testStartTagTokenWithNullName() {
 StartTag startTagToken = new StartTag();
 startTagToken.tagName = null;
 startTagToken.newAttribute();
 startTagToken.appendAttributeName("class");
 startTagToken.appendAttributeValue("test");

 Assert.assertNull(startTagToken.tagName);
 Assert.assertNotNull(startTagToken.attributes);
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character("abc");

 Token expectedToken = new Character("abc");

 Assert.assertEquals(expectedToken.tokenType(), characterToken.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), characterToken.tokenType());
 Assert.assertEquals(expectedToken.toString(), characterToken.toString());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 Token expectedToken = new EOF();

 Assert.assertEquals(expectedToken.tokenType(), eofToken.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), eofToken.tokenType());
 Assert.assertEquals(expectedToken.toString(), eofToken.toString());
 }
}