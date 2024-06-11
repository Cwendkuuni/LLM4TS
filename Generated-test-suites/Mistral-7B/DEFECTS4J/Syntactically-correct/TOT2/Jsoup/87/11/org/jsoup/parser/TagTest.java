import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

 // Basic test cases for getName() method
 @Test
 public void testTaggetName() {
 Tag htmlTag = Tag.valueOf("html");
 String htmlTagName = htmlTag.getName();
 assertEquals("html", htmlTagName);
 }

 @Test
 public void testTaggetNormalizedName() {
 Tag pTag = Tag.valueOf("P");
 String pTagNormalizedName = pTag.getNormalizedName();
 assertEquals("p", pTagNormalizedName);
 }

 // Basic test case for valueOf() method with known tag
 @Test
 public void testTagValueOfKnownTag() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isKnownTag());
 assertEquals("p", pTag.getName());
 }

 // Basic test case for valueOf() method with unknown tag
 @Test
 public void testTagValueOfUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag");
 assertFalse(unknownTag.isKnownTag());
 }

 // Test for isBlock() method with known block tag
 @Test
 public void testTagIsBlockHtml() {
 Tag htmlTag = Tag.valueOf("html");
 assertTrue(htmlTag.isBlock());
 }

 // Test for isBlock() method with known inline tag
 @Test
 public void testTagIsBlockP() {
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isBlock());
 }

 // Test for formatAsBlock() method with known block tag
 @Test
 public void testTagFormatAsBlockHtml() {
 Tag htmlTag = Tag.valueOf("html");
 assertTrue(htmlTag.formatAsBlock());
 }

 // Test for formatAsBlock() method with known inline tag
 @Test
 public void testTagFormatAsBlockP() {
 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.formatAsBlock());
 }

 // Test for canContainInline() method with known block tag
 @Test
 public void testTagCanContainInlineHtml() {
 Tag htmlTag = Tag.valueOf("html");
 assertTrue(htmlTag.canContainInline());
 }

 // Test for canContainInline() method with known inline tag
 @Test
 public void testTagCanContainInlineP() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.canContainInline());
 }

 // Test for isData() method with known data only tag
 @Test
 public void testTagIsDataScript() {
 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());
 }

 // Test for isEmpty() method with known empty tag
 @Test
 public void testTagIsEmptyImg() {
 Tag imgTag = Tag.valueOf("img");
 assertTrue(imgTag.isEmpty());
 }

 // Test for isSelfClosing() method with known self-closing tag
 @Test
 public void testTagIsSelfClosingSelfClosingTag() {
 Tag selfClosingTag = Tag.valueOf("meta");
 assertTrue(selfClosingTag.isSelfClosing());
 }

 // Test for isKnownTag() method with known tag
 @Test
 public void testTagIsKnownTagHtml() {
 assertTrue(Tag.isKnownTag("html"));
 }

 // Test for isKnownTag() method with unknown tag
 @Test(expected = AssertionError.class)
 public void testTagIsKnownTagUnknownTag() {
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 // Test for preserveWhitespace() method with known preserving whitespace tag
 @Test
 public void testTagPreserveWhitespacePre() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());
 }

 // Test for isFormListed() method with known form listed tag
 @Test
 public void testTagIsFormListedButton() {
 Tag buttonTag = Tag.valueOf("button");
 assertTrue(buttonTag.isFormListed());
 }

 // Test for isFormSubmittable() method with known form submittable tag
 @Test
 public void testTagIsFormSubmittableInput() {
 Tag inputTag = Tag.valueOf("input");
 assertTrue(inputTag.isFormSubmittable());
 }

 // Edge case test for valueOf() method with case insensitive settings
 @Test
 public void testTagValueOfCaseInsensitiveSettingSP() {
 Tag spTag = Tag.valueOf("SP", ParseSettings.ignoreCase);
 assertEquals("span", spTag.getName());
 }

 // Edge case test for valueOf() method with case insensitive settings and an unknown tag
 @Test
 public void testTagValueOfCaseInsensitiveSettingUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag", ParseSettings.ignoreCase);
 assertFalse(unknownTag.isKnownTag());
 }

 // Error scenario test for valueOf() method with null tag name
 @Test(expected = NullPointerException.class)
 public void testTagValueOfNullTagName() {
 Tag unknownTag = Tag.valueOf(null);
 }

 // Error scenario test for valueOf() method with empty tag name
 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmptyTagName() {
 Tag unknownTag = Tag.valueOf("");
 }
}