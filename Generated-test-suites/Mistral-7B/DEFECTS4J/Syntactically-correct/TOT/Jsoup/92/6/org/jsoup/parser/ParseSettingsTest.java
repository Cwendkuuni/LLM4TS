import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import static org.jsoup.parser.ParseSettings.htmlDefault;
import static org.jsoup.parser.ParseSettings.preserveCase;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultNormalizeTag() {
 String input = "HTMLTag";
 String expected = "htmltag"; // lowercase tag name as per HTML default
 ParseSettings actual = htmlDefault;
 String result = actual.normalizeTag(input);
 assertEquals(expected, result);
 }

 @Test
 public void testHtmlDefaultNormalizeAttribute() {
 String input = "class='HTMLAttribute'";
 String expected = "class='htmlattribute'"; // lowercase attribute name as per HTML default
 ParseSettings actual = htmlDefault;
 String result = actual.normalizeAttribute(input);
 assertEquals(expected, result);
 }

 @Test
 public void testPreserveCaseNormalizeTag() {
 String input = "UPPERcaseTag";
 String expected = "UPPERCASETAG";
 ParseSettings actual = preserveCase;
 String result = actual.normalizeTag(input);
 assertEquals(expected, result);
 }

 @Test
 public void testPreserveCaseNormalizeAttribute() {
 String input = "UPPERcaseAttribute='UPPERvalue'";
 String expected = "UPPERCASEATTRIBUTE='UPPERVALUE'";
 ParseSettings actual = preserveCase;
 String result = actual.normalizeAttribute(input);
 assertEquals(expected, result);
 }

 @Test
 public void testPreserveCaseNormalizeAttributes() {
 Attributes input = new Attributes()
 .put("UPPERcaseAttribute", "UPPERvalue")
 .put("lowercaseAttribute", "lowervalue");
 ParseSettings actual = preserveCase;
 Attributes result = actual.normalizeAttributes(input);
 assertEquals(input.toString(), result.toString()); // verify that attributes are not modified when preserveAttributeCase is true
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInputForNormalizeTag() {
 ParseSettings actual = preserveCase;
 String input = null;
 String expectedMessage = "Name cannot be null";
 try {
 String result = actual.normalizeTag(input);
 } catch (IllegalArgumentException e) {
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInputForNormalizeAttribute() {
 ParseSettings actual = preserveCase;
 String input = null;
 String expectedMessage = "Name cannot be null";
 try {
 String result = actual.normalizeAttribute(input);
 } catch (IllegalArgumentException e) {
 String actualMessage = e.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
 }

}