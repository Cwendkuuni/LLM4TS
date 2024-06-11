import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

 // Test for Doctype token
 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.pubSysKey = "DTD XHTML 1.0";
 doctype.publicIdentifier.append("Public ID: //www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
 doctype.systemIdentifier.append("system \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
 doctype.forceQuirks = true;
 Token expectedDoctype = new Doctype();
 expectedDoctype.name.append("html");
 expectedDoctype.pubSysKey = "DTD XHTML 1.0";
 expectedDoctype.publicIdentifier.append("Public ID: //www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
 expectedDoctype.systemIdentifier.append("system \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
 expectedDoctype.forceQuirks = true;
 assertEquals(expectedDoctype, doctype);
 }

 // Test for reset method of Doctype token
 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.pubSysKey = "DTD XHTML 1.0";
 doctype.publicIdentifier.append("Public ID: //www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
 doctype.systemIdentifier.append("system \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
 doctype.forceQuirks = true;
 doctype.reset();
 assertTrue(doctype.pubSysKey == null);
 assertTrue(doctype.systemIdentifier.length() == 0);
 assertTrue(doctype.forceQuirks == false);
 }

 // Test for StartTag token with attributes
 @Test
 public void testStartTagTokenWithAttributes() {
 StartTag startTag = new StartTag();
 startTag.nameAttr("html", new Attributes().put("class", "body").put("id", "main"));
 assertEquals("html", startTag.name());
 assertTrue(startTag.isSelfClosing());
 assertEquals("class=body id=main", startTag.toString());
 }

 // Test for StartTag token without attributes
 @Test
 public void testStartTagTokenWithoutAttributes() {
 StartTag startTag = new StartTag();
 startTag.nameAttr("p");
 assertEquals("p", startTag.name());
 assertFalse(startTag.isSelfClosing());
 }

 // Test for EndTag token
 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.nameAttr("p");
 assertEquals("p", endTag.name());
 }

 // Test for Comment token
 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data.append("This is a comment.");
 assertEquals("This is a comment.", comment.getData());
 }

 // Test for reset method of Comment token
 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.data.append("This is a comment.");
 comment.reset();
 assertTrue(comment.getData().length() == 0);
 }

 // Test for Character token
 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("Hello, World!");
 assertEquals("Hello, World!", character.getData());
 }

 // Test for EOF token
 @Test
 public void testEOFToken() {
 EOF eof = new EOF();
 assertTrue(eof.isEOF());
 }

 // Test for reset method of EOF token
 @Test
 public void testResetEOFToken() {
 EOF eof = new EOF();
 eof.reset();
 assertFalse(eof.isEOF());
 }
}