import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 private Token doctype;
 private Token startTag;
 private Token endTag;
 private Token comment;
 private Token character;
 private Token cdata;
 private Token eof;

 @Before
 public void setUp() {
 doctype = new Token.Doctype();
 startTag = new Token.StartTag();
 endTag = new Token.EndTag();
 comment = new Token.Comment();
 character = new Token.Character();
 cdata = new Token.CData("test");
 eof = new Token.EOF();
 }

 @Test
 public void testDoctype() {
 doctype.getName();
 assertEquals("Doctype", doctype.tokenType());
 }

 @Test
 public void testDoctypeReset() {
 doctype.reset();
 assertEquals("", doctype.getName());
 assertNull(doctype.getPubSysKey());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testStartTag() {
 startTag.name("tagName").name("tagName");
 assertEquals("tagName", startTag.name());
 assertEquals("tagName", startTag.normalName());
 assertFalse(startTag.isSelfClosing());
 assertNull(startTag.getAttributes());
 }

 @Test
 public void testStartTagReset() {
 startTag.reset();
 assertNull(startTag.tagName);
 assertNull(startTag.normalName);
 assertFalse(startTag.selfClosing);
 assertNull(startTag.attributes);
 }

 @Test
 public void testStartTagNewAttribute() {
 startTag.name("tagName").attribute("attributeName", "attributeValue");
 assertEquals("tagName", startTag.name());
 assertEquals("tagName", startTag.normalName());
 assertFalse(startTag.selfClosing);
 assertEquals("attributeValue", startTag.getAttributes().get("attributeName"));
 }

 @Test
 public void testStartTagFinaliseTag() {
 startTag.name("tagName").attribute("attributeName", "attributeValue").finaliseTag();
 assertEquals("tagName", startTag.name());
 assertEquals("tagName", startTag.normalName());
 assertFalse(startTag.selfClosing);
 assertNull(startTag.getAttributes());
 }

 @Test
 public void testEndTag() {
 endTag.name("tagName");
 assertEquals("tagName", endTag.name());
 assertEquals("tagName", endTag.normalName());
 assertTrue(endTag.isSelfClosing());
 assertNull(endTag.getAttributes());
 }

 @Test
 public void testEndTagReset() {
 endTag.reset();
 assertNull(endTag.tagName);
 assertNull(endTag.normalName);
 assertFalse(endTag.selfClosing);
 assertNull(endTag.getAttributes());
 }

 @Test
 public void testComment() {
 comment.data("testComment");
 assertEquals("testComment", comment.getData());
 assertEquals("comment", comment.tokenType());
 }

 @Test
 public void testCommentReset() {
 comment.reset();
 assertEquals("", comment.getData());
 }

 @Test
 public void testCharacter() {
 character.data("testCharacter");
 assertEquals("testCharacter", character.getData());
 }

 @Test
 public void testCharacterReset() {
 character.reset();
 assertNull(character.getData());
 }

 @Test
 public void testCData() {
 cdata.getData();
 assertEquals("test", cdata.getData());
 assertEquals("cdata", cdata.tokenType().name());
 }

 @Test
 public void testCDataReset() {
 cdata.reset();
 assertNull(cdata.getData());
 }

 @Test
 public void testEOF() {
 assertEquals("EOF", eof.tokenType().name());
 }

 @Test
 public void testEOFReset() {
 eof.reset();
 }
}