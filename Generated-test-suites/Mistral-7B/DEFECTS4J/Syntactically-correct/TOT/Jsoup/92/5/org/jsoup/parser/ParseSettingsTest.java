import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultNormalizeTag() {
 String tagName = "HTML";
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String normalizedTag = htmlDefault.normalizeTag(tagName);
 assertEquals("html", normalizedTag);
 }

 @Test
 public void testHtmlDefaultNormalizeAttribute() {
 String attributeName = "CLASS";
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String normalizedAttribute = htmlDefault.normalizeAttribute(attributeName);
 assertEquals("class", normalizedAttribute);
 }

 @Test
 public void testPreserveCaseNormalizeTag() {
 String tagName = "Head";
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String normalizedTag = preserveCase.normalizeTag(tagName);
 assertEquals("Head", normalizedTag);
 }

 @Test
 public void testPreserveCaseNormalizeAttribute() {
 String attributeName = "ID";
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String normalizedAttribute = preserveCase.normalizeAttribute(attributeName);
 assertEquals("ID", normalizedAttribute);
 }

 @Test
 public void testPreserveCaseNormalizeAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("ID", "value1");
 attributes.put("CLASS", "value2");
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
 assertEquals("ID", normalizedAttributes.get("ID"));
 assertEquals("value1", normalizedAttributes.get("ID"));
 assertEquals("CLASS", normalizedAttributes.get("CLASS"));
 assertEquals("value2", normalizedAttributes.get("CLASS"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNormalizeTag() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 String tagName = null;
 String normalizedTag = parseSettings.normalizeTag(tagName);
 fail("Expected IllegalArgumentException but was not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNormalizeAttribute() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 String attributeName = null;
 String normalizedAttribute = parseSettings.normalizeAttribute(attributeName);
 fail("Expected IllegalArgumentException but was not thrown");
 }
}