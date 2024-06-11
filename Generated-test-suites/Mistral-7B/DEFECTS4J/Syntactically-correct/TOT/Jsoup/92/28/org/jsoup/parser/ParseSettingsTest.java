import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultSettings() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 // Test normalizeTag method with lower-cased tag name
 String tagName = "div";
 String normalizedTag = htmlDefault.normalizeTag(tagName);
 assertEquals("div", normalizedTag);
 // Test normalizeAttribute method with lower-cased attribute name
 String attributeName = "class";
 String normalizedAttribute = htmlDefault.normalizeAttribute(attributeName);
 assertEquals("class", normalizedAttribute);
 // Test normalizeAttributes method with lower-cased attribute names
 Attributes attributes = new Attributes();
 attributes.put("class", "class1");
 attributes.put("id", "id1");
 ParseSettings.htmlDefault.normalizeAttributes(attributes);
 assertEquals("class1", attributes.get("class"));
 assertEquals("id1", attributes.get("id"));
 }

 @Test
 public void testPreserveCaseSettings() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 // Test normalizeTag method with upper-cased tag name
 String tagName = "DIV";
 String normalizedTag = preserveCase.normalizeTag(tagName);
 assertEquals("DIV", normalizedTag);
 // Test normalizeAttribute method with upper-cased attribute name
 String attributeName = "CLASS";
 String normalizedAttribute = preserveCase.normalizeAttribute(attributeName);
 assertEquals("CLASS", normalizedAttribute);
 // Test normalizeAttributes method with upper-cased attribute names
 Attributes attributes = new Attributes();
 attributes.put("CLASS", "class1");
 attributes.put("ID", "id1");
 ParseSettings.preserveCase.normalizeAttributes(attributes);
 assertEquals("CLASS", attributes.get("CLASS"));
 assertEquals("ID", attributes.get("ID"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributes() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 Attributes attributes = null;
 htmlDefault.normalizeAttributes(attributes);
 }

}