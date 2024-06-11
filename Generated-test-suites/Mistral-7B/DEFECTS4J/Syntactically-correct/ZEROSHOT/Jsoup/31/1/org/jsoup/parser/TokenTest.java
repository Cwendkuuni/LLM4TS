import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 assertTrue(doctypeToken.isDoctype());
 assertEquals(TokenType.Doctype, doctypeToken.tokenType());
 }

 @Test
 public void testDoctypeToken_getName() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 assertEquals("html", doctypeToken.getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testDoctypeToken_getName_afterSetSystemIdentifier() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.systemIdentifier("http://example.com");
 doctypeToken.getName();
 }

 @Test
 public void testDoctypeToken_getPublicIdentifier() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.publicIdentifier("public id");
 assertEquals("public id", doctypeToken.getPublicIdentifier());
 }

 @Test
 public void testDoctypeToken_getSystemIdentifier() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.systemIdentifier("system id");
 assertEquals("system id", doctypeToken.getSystemIdentifier());
 }

 @Test
 public void testDoctypeToken_isForceQuirks() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.isForceQuirks(); // default false
 doctypeToken.isForceQuirks(true); // set to true
 assertTrue(doctypeToken.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 Tag tagToken = new StartTag();
 assertTrue(tagToken.isStartTag());
 assertEquals(TokenType.StartTag, tagToken.tokenType());
 }

 @Test
 public void testTagToken_name() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("div");
 assertEquals("div", startTagToken.name());
 }

 @Test
 public void testTagToken_selfClosing() {
 StartTag startTagToken = new StartTag();
 startTagToken.selfClosing(true);
 assertTrue(startTagToken.isSelfClosing());
 }

 @Test
 public void testTagToken_getAttributes() {
 StartTag startTagToken = new StartTag();
 Attribute attribute = new Attribute("class", "test");
 startTagToken.attributes.put(attribute);
 assertNotNull(startTagToken.getAttributes());
 }

 @Test
 public void testTagToken_appendTagName() {
 StartTag startTagToken = new StartTag();
 startTagToken.appendTagName("p");
 assertEquals("p", startTagToken.name());
 }

 @Test
 public void testTagToken_appendAttributeName() {
 StartTag startTagToken = new StartTag();
 startTagToken.appendAttributeName("id");
 assertEquals("id", startTagToken.pendingAttributeName);
 }

 @Test
 public void testTagToken_appendAttributeValue() {
 StartTag startTagToken = new StartTag();
 startTagToken.appendAttributeValue("test");
 assertEquals("test", startTagToken.pendingAttributeValue.toString());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 assertTrue(endTagToken.isEndTag());
 assertEquals(TokenType.EndTag, endTagToken.tokenType());
 }

 @Test
 public void testEndTagToken_name() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("p");
 assertEquals("p", endTagToken.name());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 assertTrue(commentToken.isComment());
 assertEquals(TokenType.Comment, commentToken.tokenType());
 }

 @Test
 public void testCommentToken_getData() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");
 assertEquals("This is a comment", commentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character('a');
 assertTrue(characterToken.isCharacter());
 assertEquals(TokenType.Character, characterToken.tokenType());
 assertEquals("a", characterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();
 assertTrue(eofToken.isEOF());
 assertEquals(TokenType.EOF, eofToken.tokenType());
 }
}