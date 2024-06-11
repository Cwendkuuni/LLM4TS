import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Token doctypeToken = new Doctype();
 doctypeToken.newAttribute().getName("html");
 doctypeToken.newAttribute().getName("publicIdentifier").setValue("-//W3C//DTD HTML 4.01//EN");
 doctypeToken.newAttribute().getName("systemIdentifier").setValue("http://www.w3.org/TR/html4/strict.dtd");
 doctypeToken.newAttribute().getName("forceQuirks").setValue("true");

 Assert.assertEquals("Doctype", doctypeToken.tokenType());
 Assert.assertEquals("html", doctypeToken.asDoctype().getName());
 Assert.assertEquals("publicIdentifier", doctypeToken.asDoctype().getPublicIdentifier());
 Assert.assertEquals("systemIdentifier", doctypeToken.asDoctype().getSystemIdentifier());
 Assert.assertEquals(true, doctypeToken.asDoctype().isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 Token startTagToken = new StartTag();
 startTagToken.name("html").newAttribute().getName("xmlns:html").setValue("http://www.w3.org/1999/xhtml");

 Assert.assertEquals("StartTag", startTagToken.tokenType());
 Assert.assertEquals("html", startTagToken.name());
 Assert.assertEquals("html", startTagToken.name()); // normalName is lowercase
 Assert.assertEquals(1, startTagToken.getAttributes().size());
 Assert.assertNotNull(startTagToken.asStartTag().getAttribute("xmlns:html"));
 }

 @Test
 public void testEndTagToken() {
 Token endTagToken = new EndTag();
 endTagToken.name("html");

 Assert.assertEquals("EndTag", endTagToken.tokenType());
 Assert.assertEquals("html", endTagToken.name());
 }

 @Test
 public void testCommentToken() {
 Token commentToken = new Comment();
 commentToken.appendAttributeValue("This is a comment.");

 Assert.assertEquals("Comment", commentToken.tokenType());
 Assert.assertEquals("", commentToken.name());
 Assert.assertEquals("This is a comment.", commentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Token characterToken = new Character();
 characterToken.data("&nbsp;");

 Assert.assertEquals("Character", characterToken.tokenType());
 Assert.assertEquals("&nbsp;", characterToken.getData());
 }

 @Test
 public void testEOFToken() {
 Token eofToken = new EOF();

 Assert.assertEquals("EOF", eofToken.tokenType());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidToken() {
 Token invalidToken = new Token();
 invalidToken.name("invalidTag");

 // This test should fail as 'invalidTag' is not defined in Token class
 }
}