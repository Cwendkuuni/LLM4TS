import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 assertFalse(doctype.isForceQuirks());
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeWithInvalidName() {
 Doctype doctype = new Doctype();
 doctype.name("invalidName");
 }

 @Test
 public void testDoctypeWithForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.forceQuirks = true;
 assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testTag() {
 Tag tag = new StartTag("div");
 tag.name().equals("div");
 assertFalse(tag.isSelfClosing());
 assertNull(tag.getAttributes());
 }

 @Test
 public void testTagWithAttributes() {
 Tag tag = new StartTag("img").name("src").attr("src", "image.png");
 assertEquals("div", tag.name());
 assertNull(tag.isSelfClosing());
 Map<String, Attribute> attributes = tag.getAttributes();
 assertEquals(1, attributes.size());
 assertEquals("src", attributes.keySet().iterator().next());
 assertEquals("image.png", attributes.get("src").getValue());
 }

 @Test
 public void testTagWithSelfClosing() {
 Tag tag = new StartTag("img").name("img").selfClosing(true);
 assertEquals("img", tag.name());
 assertTrue(tag.isSelfClosing());
 assertNull(tag.getAttributes());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.getData().equals("--comment--");
 }

 @Test
 public void testCharacter() {
 Character character = new Character("a");
 character.getData().equals("a");
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();
 assertTrue(eof.isEOF());
 }

}