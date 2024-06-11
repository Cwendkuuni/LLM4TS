import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.publicIdentifier.append("Public ID:");
 doctypeToken.systemIdentifier.append("System ID:");
 doctypeToken.forceQuirks = true;

 Token expectedToken = new Doctype();
 expectedToken.asDoctype().getName().equals("html");
 expectedToken.asDoctype().getPublicIdentifier().equals("Public ID:");
 expectedToken.asDoctype().getSystemIdentifier().equals("System ID:");
 expectedToken.asDoctype().isForceQuirks().equals(true);

 Assert.assertEquals(expectedToken, doctypeToken);
 }

 @Test
 public void testResetDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.publicIdentifier.append("Public ID:");
 doctypeToken.systemIdentifier.append("System ID:");
 doctypeToken.forceQuirks = true;
 doctypeToken.reset();

 Assert.assertEquals(doctypeToken.tokenType(), TokenType.Doctype.toString());
 Assert.assertNull(doctypeToken.name);
 Assert.assertNull(doctypeToken.publicIdentifier);
 Assert.assertFalse(doctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html").attributes(new Attributes().put("class", "test"));

 Token expectedToken = new StartTag();
 expectedToken.asStartTag().name().equals("html");
 expectedToken.asStartTag().getAttributes().put("class", "test");

 Assert.assertEquals(expectedToken, startTagToken);
 }

 @Test
 public void testResetStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html").attributes(new Attributes().put("class", "test"));
 startTagToken.reset();

 Assert.assertEquals(startTagToken.tokenType(), TokenType.StartTag.toString());
 Assert.assertNull(startTagToken.tagName);
 Assert.assertNull(startTagToken.normalName);
 Assert.assertNull(startTagToken.pendingAttributeName);
 Assert.assertNull(startTagToken.pendingAttributeValue);
 Assert.assertNull(startTagToken.pendingAttributeValueS);
 Assert.assertFalse(startTagToken.hasEmptyAttributeValue);
 Assert.assertFalse(startTagToken.hasPendingAttributeValue);
 Assert.assertFalse(startTagToken.selfClosing);
 Assert.assertNull(startTagToken.attributes);
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("html");

 Token expectedToken = new EndTag();
 expectedToken.asEndTag().getName().equals("html");

 Assert.assertEquals(expectedToken, endTagToken);
 }

 @Test
 public void testResetEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("html");
 endTagToken.reset();

 Assert.assertEquals(endTagToken.tokenType(), TokenType.EndTag.toString());
 Assert.assertNull(endTagToken.tagName);
 Assert.assertNull(endTagToken.normalName);
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");

 Token expectedToken = new Comment();
 expectedToken.asComment().getData().equals("This is a comment");

 Assert.assertEquals(expectedToken, commentToken);
 }

 @Test
 public void testResetCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");
 commentToken.reset();

 Assert.assertEquals(commentToken.tokenType(), TokenType.Comment.toString());
 Assert.assertNull(commentToken.data);
 Assert.assertFalse(commentToken.bogus);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 Token invalidToken = new Object();
 Assert.assertEquals(invalidToken.tokenType(), TokenType.Character.toString());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&amp;");

 Token expectedToken = new Character();
 expectedToken.asCharacter().getData().equals("&amp;");

 Assert.assertEquals(expectedToken, characterToken);
 }

 @Test
 public void testResetCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&amp;");
 characterToken.reset();

 Assert.assertEquals(characterToken.tokenType(), TokenType.Character.toString());
 Assert.assertNull(characterToken.data);
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 Token expectedToken = new EOF();
 Assert.assertEquals(expectedToken, eofToken);
 }

 @Test
 public void testResetEOFToken() {
 EOF eofToken = new EOF();
 eofToken.reset();

 Assert.assertEquals(eofToken.tokenType(), TokenType.EOF.toString());
 }
}