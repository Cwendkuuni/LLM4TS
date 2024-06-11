import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;
import static org.jsoup.parser.Tag.*;

public class TagTest {

 // Test for getName method
 @Test
 public void testTaggetName() {
 Tag tag = new Tag("p");
 String name = tag.getName();
 assertEquals("p", name);
 }

 // Test for valueOf method with known tag
 @Test
 public void testTagValueOfKnownTag() {
 Tag tag = valueOf("p");
 String name = tag.getName();
 assertEquals("p", name);
 }

 // Test for valueOf method with unknown tag
 @Test
 public void testTagValueOfUnknownTag() {
 Tag tag = valueOf("unknownTag");
 String name = tag.getName();
 assertTrue(name.equals("unknownTag"));
 }

 // Test for isBlock method
 @Test
 public void testTagIsBlock() {
 Tag tag = new Tag("p");
 boolean isBlock = tag.isBlock();
 assertTrue(isBlock);

 Tag tagInline = new Tag("span");
 boolean isBlockInline = tagInline.isBlock();
 assertFalse(isBlockInline);
 }

 // Test for formatAsBlock method
 @Test
 public void testTagFormatAsBlock() {
 Tag tag = new Tag("p");
 boolean formatAsBlock = tag.formatAsBlock();
 assertTrue(formatAsBlock);

 Tag tagInline = new Tag("span");
 boolean formatAsBlockInline = tagInline.formatAsBlock();
 assertFalse(formatAsBlockInline);
 }

 // Test for canContainInline method
 @Test
 public void testTagCanContainInline() {
 Tag tag = new Tag("p");
 boolean canContainInline = tag.canContainInline();
 assertTrue(canContainInline);

 Tag tagData = new Tag("script");
 boolean canContainInlineData = tagData.canContainInline();
 assertFalse(canContainInlineData);
 }

 // Test for isEmpty method
 @Test
 public void testTagIsEmpty() {
 Tag tag = new Tag("img");
 boolean isEmpty = tag.isEmpty();
 assertFalse(isEmpty);

 Tag tagEmpty = new Tag("img").setSelfClosing();
 boolean isEmptyEmpty = tagEmpty.isEmpty();
 assertTrue(isEmptyEmpty);
 }

 // Test for isSelfClosing method
 @Test
 public void testTagIsSelfClosing() {
 Tag tag = new Tag("img");
 boolean isSelfClosing = tag.isSelfClosing();
 assertFalse(isSelfClosing);

 Tag tagSelfClosing = new Tag("img").setSelfClosing();
 boolean isSelfClosingSelfClosing = tagSelfClosing.isSelfClosing();
 assertTrue(isSelfClosingSelfClosing);
 }

 // Test for isKnownTag method
 @Test
 public void testTagIsKnownTag() {
 Tag tagP = new Tag("p");
 boolean isKnownTag = Tag.isKnownTag("p");
 assertTrue(isKnownTag);

 Tag tagUnknown = new Tag("unknownTag");
 boolean isKnownTagUnknown = Tag.isKnownTag("unknownTag");
 assertFalse(isKnownTagUnknown);
 }

 // Test for preserveWhitespace method
 @Test
 public void testTagPreserveWhitespace() {
 Tag tagPre = new Tag("pre");
 boolean preserveWhitespace = tagPre.preserveWhitespace();
 assertTrue(preserveWhitespace);

 Tag tagTextarea = new Tag("textarea");
 boolean preserveWhitespaceTextarea = tagTextarea.preserveWhitespace();
 assertTrue(preserveWhitespaceTextarea);
 }

 // Test for isFormListed method
 @Test
 public void testTagIsFormListed() {
 Tag tagInput = new Tag("input");
 boolean isFormListed = tagInput.isFormListed();
 assertTrue(isFormListed);

 Tag tagDiv = new Tag("div");
 boolean isFormListedDiv = tagDiv.isFormListed();
 assertFalse(isFormListedDiv);
 }

 // Test for isFormSubmittable method
 @Test
 public void testTagIsFormSubmittable() {
 Tag tagInput = new Tag("input");
 boolean isFormSubmittable = tagInput.isFormSubmittable();
 assertTrue(isFormSubmittable);

 Tag tagDiv = new Tag("div");
 boolean isFormSubmittableDiv = tagDiv.isFormSubmittable();
 assertFalse(isFormSubmittableDiv);
 }
}