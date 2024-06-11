import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 // Test for Doctype method reset()
 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 doctype.pubSysKey("DTD XHTML 1.0");
 doctype.publicIdentifier("Public ID: //www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
 doctype.systemIdentifier("system \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
 doctype.forceQuirks(true);
 doctype.reset();
 Assert.assertEquals("", doctype.getName());
 Assert.assertNull(doctype.getPubSysKey());
 Assert.assertEquals("", doctype.getPublicIdentifier());
 Assert.assertEquals("", doctype.getSystemIdentifier());
 Assert.assertEquals(false, doctype.isForceQuirks());
 }

 // Test for Doctype getName() method
 @Test
 public void testDoctypeName() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 Assert.assertEquals("html", doctype.getName());
 }

 // Test for Tag method reset()
 @Test
 public void testTagReset() {
 Tag tag = new StartTag();
 tag.name("div");
 tag.newAttribute().name("class").value("class1");
 tag.newAttribute().name("id").value("id1");
 tag.finaliseTag();
 tag.reset();
 Assert.assertNull(tag.tagName);
 Assert.assertNull(tag.normalName);
 Assert.assertNull(tag.pendingAttributeName);
 Assert.assertEquals(false, tag.hasEmptyAttributeValue);
 Assert.assertEquals(false, tag.hasPendingAttributeValue);
 Assert.assertNull(tag.pendingAttributeValue);
 Assert.assertNull(tag.pendingAttributeValueS);
 Assert.assertEquals(false, tag.selfClosing);
 Assert.assertNull(tag.attributes);
 }

 // Test for Tag method name()
 @Test
 public void testTagName() {
 StartTag tag = new StartTag();
 tag.name("div");
 Assert.assertEquals("div", tag.name());
 }

 // Test for Tag method newAttribute()
 @Test
 public void testTagNewAttribute() {
 StartTag tag = new StartTag();
 tag.name("div");
 tag.newAttribute().name("class").value("class1");
 tag.newAttribute().name("id").value("id1");
 tag.finaliseTag();
 Assert.assertEquals("class", tag.getAttributes().keySet().iterator().next());
 Assert.assertEquals("class1", tag.getAttributes().getValue("class"));
 Assert.assertEquals("id", tag.getAttributes().keySet().iterator().next());
 Assert.assertEquals("id1", tag.getAttributes().getValue("id"));
 }

 // Test for Tag method toString()
 @Test
 public void testTagToString() {
 StartTag tag = new StartTag().name("div");
 tag.newAttribute().name("class").value("class1");
 tag.newAttribute().name("id").value("id1");
 tag.finaliseTag();
 Assert.assertEquals("<div class=\"class1\" id=\"id1\"></div>", tag.toString());
 }

 // Test for Comment method reset()
 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.data("This is a comment");
 comment.bogus(true);
 comment.reset();
 Assert.assertEquals("", comment.getData());
 Assert.assertEquals(false, comment.isBogus());
 }

 // Test for Comment method getData()
 @Test
 public void testCommentData() {
 Comment comment = new Comment();
 comment.data("This is a comment");
 Assert.assertEquals("This is a comment", comment.getData());
 }

 // Test for Comment method toString()
 @Test
 public void testCommentToString() {
 Comment comment = new Comment();
 comment.data("This is a comment");
 Assert.assertEquals("<!--This is a comment-->", comment.toString());
 }

 // Test for Character method reset()
 @Test
 public void testCharacterReset() {
 Character character = new Character();
 character.data("A");
 character.reset();
 Assert.assertNull(character.getData());
 }

 // Test for Character method data()
 @Test
 public void testCharacterData() {
 Character character = new Character();character.data("A");
 Assert.assertEquals("A", character.getData());
 }

 // Test for Character method toString()
 @Test
 public void testCharacterToString() {
 Character character = new Character();character.data("A");
 Assert.assertEquals("A", character.toString());
 }

 // Test for EOF method reset()
 @Test
 public void testEOFReset() {
 EOF eof = new EOF();
 eof.reset();
 // EOF doesn't have any methods to test
 }

 // Test for EOF method isEOF()
 @Test
 public void testEOFIsEOF() {
 EOF eof = new EOF();
 Assert.assertEquals(true, eof.isEOF());
 }
}