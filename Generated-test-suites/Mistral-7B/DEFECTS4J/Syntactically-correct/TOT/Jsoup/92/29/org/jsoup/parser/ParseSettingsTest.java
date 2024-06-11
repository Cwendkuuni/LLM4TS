import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String tagName = "HTML";
 String attributeName1 = "class";
 String attributeName2 = "ID";
 String attributeValue = "test-class";

 String tag = settings.normalizeTag(tagName);
 String attribute1 = settings.normalizeAttribute(attributeName1);
 String attribute2 = settings.normalizeAttribute(attributeName2);

 assertEquals(tagName, tag); // HTML should be lower-cased
 assertEquals(attributeName1, attribute1); // class should be lower-cased
 assertEquals(attributeName2, attribute2); // ID should be lower-cased

 Attributes attributes = new Attributes();
 attributes.add("class", attributeValue);
 attributes.add("ID", "test-id");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(attributeValue, normalizedAttributes.get("class")); // class value should be the same
 assertEquals("test-id", normalizedAttributes.get("ID")); // ID value should be the same
 }

 @Test
 public void testPreserveCase() {
 ParseSettings settings = ParseSettings.preserveCase;
 String tagName = "DIV";
 String attributeName1 = "CLASS";
 String attributeName2 = "ID";
 String attributeValue1 = "test-class";
 String attributeValue2 = "test-id";

 String tag = settings.normalizeTag(tagName);
 String attribute1 = settings.normalizeAttribute(attributeName1);
 String attribute2 = settings.normalizeAttribute(attributeName2);

 assertEquals(tagName, tag); // DIV should not be lower-cased
 assertEquals(attributeName1, attribute1); // CLASS should not be lower-cased
 assertEquals(attributeName2, attribute2); // ID should not be lower-cased

 Attributes attributes = new Attributes();
 attributes.add(attributeName1, attributeValue1);
 attributes.add(attributeName2, attributeValue2);

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(attributeValue1, normalizedAttributes.get(attributeName1)); // class value should be the same
 assertEquals(attributeValue2, normalizedAttributes.get(attributeName2)); // ID value should be the same
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 ParseSettings settings = ParseSettings.preserveCase;
 String tagName = null;
 String attributeName = "class";
 String attributeValue = "test-class";

 String tag = settings.normalizeTag(tagName);
 String attribute = settings.normalizeAttribute(attributeName);

 assertNull(tag); // null input for tag name should result in null output
 assertNotNull(attribute); // null input for attribute name should not affect attribute normalization

 Attributes attributes = new Attributes();
 attributes.add(attributeName, attributeValue);

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes); // null input for attributes should not affect normalization
 }
}