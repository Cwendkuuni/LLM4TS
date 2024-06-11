import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag pTagLowercase = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknownTag");

 assertEquals("p", pTag.getName());
 assertEquals("p", pTagLowercase.getName());
 assertEquals("div", divTag.getName());
 assertNotEquals("p", divTag.getName());
 assertEquals("unknownTag", unknownTag.getName());
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(pTag.isBlock());
 assertTrue(divTag.isBlock());
 assertFalse(inputTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(pTag.formatAsBlock());
 assertTrue(divTag.formatAsBlock());
 assertFalse(inputTag.formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag inputTag = Tag.valueOf("input");

 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());
 assertFalse(inputTag.canContainBlock());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isEmpty());
 assertFalse(brTag.isEmpty());
 assertTrue(inputTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 Tag brTag = Tag.valueOf("br");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(imgTag.isSelfClosing());
 assertFalse(brTag.isSelfClosing());
 assertTrue(inputTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("unknownTag");

 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(preTag.preserveWhitespace());
 assertTrue(textareaTag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 Tag inputTag = Tag.valueOf("input");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(inputTag.isFormListed());
 assertTrue(textareaTag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 Tag inputTag = Tag.valueOf("input");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(inputTag.isFormSubmittable());
 assertTrue(textareaTag.isFormSubmittable());
 }
}