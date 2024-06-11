import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeWithName() {
 Doctype doctype = new Doctype().name("html");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeWithNamePublicIdentifier() {
 Doctype doctype = new Doctype().name("html").publicIdentifier("_xml");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 doctype.getPublicIdentifier().equals("_xml");
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeWithNameSystemIdentifier() {
 Doctype doctype = new Doctype().name("html").systemIdentifier("/index.html");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 Assert.assertNull(doctype.getPublicIdentifier());
 doctype.getSystemIdentifier().equals("/index.html");
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeWithNamePublicIdentifierSystemIdentifier() {
 Doctype doctype = new Doctype().name("html").publicIdentifier("_xml").systemIdentifier("/index.html");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 doctype.getPublicIdentifier().equals("_xml");
 doctype.getSystemIdentifier().equals("/index.html");
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeWithForceQuirks() {
 Doctype doctype = new Doctype().name("html").forceQuirks(true);
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag().name("div");
 startTag.getName().equals("div");
 Assert.assertNull(startTag.getAttributes());
 Assert.assertFalse(startTag.isSelfClosing());
 }

 @Test
 public void testStartTagWithAttributes() {
 StartTag startTag = new StartTag().name("img").newAttribute().name("src").value("image.png");
 startTag.getName().equals("img");
 Attributes attributes = startTag.getAttributes();
 Attribute srcAttribute = attributes.get("src");
 Assert.assertNotNull(srcAttribute);
 Assert.assertEquals("image.png", srcAttribute.getValue());
 Assert.assertFalse(startTag.isSelfClosing());
 }

 @Test
 public void testStartTagWithSelfClosing() {
 StartTag startTag = new StartTag().name("img").selfClosing(true);
 startTag.getName().equals("img");
 Assert.assertNull(startTag.getAttributes());
 Assert.assertTrue(startTag.isSelfClosing());
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag().name("div");
 endTag.getName().equals("div");
 Assert.assertNull(endTag.getAttributes());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment().data("This is a comment.");
 comment.getData().equals("This is a comment.");
 }

 @Test
 public void testCharacter() {
 Character character = new Character("A");
 character.getData().equals("A");
 }

 @Test(expected = AssertionError.class)
 public void testInvalidCharacter() {
 Character character = new Character(null);
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();
 Assert.assertTrue(eof.isEOF());
 }
}