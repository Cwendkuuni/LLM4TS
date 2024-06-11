import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 // Test for Doctype token
 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public id");
 doctype.systemIdentifier.append("system id");
 doctype.forceQuirks = true;

 Token expectedDoctype = new Doctype();
 expectedDoctype.name.append("html");
 expectedDoctype.publicIdentifier.append("public id");
 expectedDoctype.systemIdentifier.append("system id");
 expectedDoctype.forceQuirks = true;

 assertEquals(expectedDoctype, doctype);
 }

 // Test for reset method of Doctype token
 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public id");
 doctype.systemIdentifier.append("system id");
 doctype.forceQuirks = true;
 doctype.reset();

 assertTrue(doctype.name.length() == 0);
 assertTrue(doctype.publicIdentifier.length() == 0);
 assertTrue(doctype.systemIdentifier.length() == 0);
 assertFalse(doctype.forceQuirks);
 }

 // Test for Getter methods of Doctype token
 @Test
 public void testDoctypeGetters() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public id");
 doctype.systemIdentifier.append("system id");
 doctype.forceQuirks = true;

 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertEquals("public id", doctype.getPublicIdentifier());
 assertEquals("system id", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());
 }

 // Test for StartTag token
 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.nameAttr("html", new Attributes());

 Token expectedStartTag = new StartTag();
 expectedStartTag.nameAttr("html", new Attributes());

 assertEquals(expectedStartTag, startTag);
 }

 // Test for reset method of StartTag token
 @Test
 public void testStartTagReset() {
 StartTag startTag = new StartTag();
 startTag.tagName = "html";
 startTag.attributes = new Attributes();
 startTag.reset();

 assertTrue(startTag.tagName == null);
 assertTrue(startTag.attributes == null);
 }

 // Test for Getter methods of StartTag token
 @Test
 public void testStartTagGetters() {
 StartTag startTag = new StartTag();
 startTag.tagName = "html";
 startTag.attributes = new Attributes();

 assertEquals("StartTag", startTag.tokenType());
 assertEquals("html", startTag.name());
 assertEquals("html", startTag.normalName());
 assertFalse(startTag.isSelfClosing());
 }

 // Test for EOF token
 @Test
 public void testEOFToken() {
 EOF eof = new EOF();
 EOF expectedEOF = new EOF();

 assertEquals(expectedEOF, eof);
 }

 // Test for reset method of EOF token
 @Test
 public void testEOFReset() {
 EOF eof = new EOF();
 eof.reset();
 }

 // Test for Getter methods of EOF token
 @Test
 public void testEOFGetters() {
 EOF eof = new EOF();

 assertEquals("EOF", eof.tokenType().name());
 }
}