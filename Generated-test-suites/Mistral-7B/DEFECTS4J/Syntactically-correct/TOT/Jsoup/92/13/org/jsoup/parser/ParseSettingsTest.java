import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

 @Test
 public void testNormalizeTag_HtmlDefault() {
 String tagName = "HTML";
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String normalizedTagName = htmlDefault.normalizeTag(tagName);
 assertEquals("html", normalizedTagName);
 }

 @Test
 public void testNormalizeTag_PreserveCase() {
 String tagName = "DIV";
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String normalizedTagName = preserveCase.normalizeTag(tagName);
 assertEquals("DIV", normalizedTagName);
 }

 @Test
 public void testNormalizeAttribute_HtmlDefault() {
 String attrName = "class";
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String normalizedAttrName = htmlDefault.normalizeAttribute(attrName);
 assertEquals("class", normalizedAttrName);
 }

 @Test
 public void testNormalizeAttribute_PreserveCase() {
 String attrName = "ID";
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String normalizedAttrName = preserveCase.normalizeAttribute(attrName);
 assertEquals("ID", normalizedAttrName);
 }

 @Test
 public void testNormalizeAttributes() {
 String[] attributes = new String[]{"ID", "CLASS", "STYLE"};
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes originAttributes = new Attributes();
 for (String attr : attributes) {
 originAttributes.put(attr, "value");
 }
 Attributes normalizedAttributes = preserveCase.normalizeAttributes(originAttributes);
 String[] normalizedAttributesNames = normalizedAttributes.keySet().toArray(new String[0]);
 for (int i = 0; i < normalizedAttributesNames.length; i++) {
 String expectedName = attributes[i];
 String normalizedName = normalizedAttributesNames[i];
 assertEquals(expectedName, normalizedName);
 }
 }

 @Test(expected = AssertionError.class)
 public void testNormalizeTag_Null() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = null;
 String normalizedTagName = preserveCase.normalizeTag(tagName);
 }

 @Test(expected = AssertionError.class)
 public void testNormalizeAttribute_Null() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String attrName = null;
 String normalizedAttrName = preserveCase.normalizeAttribute(attrName);
 }

 @Test
 public void testNormalizeAttributes_HtmlDefault() {
 String[] attributes = new String[]{"Id", "ClAss", "StYle"};
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 Attributes originAttributes = new Attributes();
 for (String attr : attributes) {
 originAttributes.put(attr, "value");
 }
 Attributes normalizedAttributes = htmlDefault.normalizeAttributes(originAttributes);
 String[] normalizedAttributesNames = normalizedAttributes.keySet().toArray(new String[0]);
 for (int i = 0; i < normalizedAttributesNames.length; i++) {
 String expectedName = lowerCase(attributes[i]);
 String normalizedName = normalizedAttributesNames[i];
 assertEquals(expectedName, normalizedName);
 }
 }
}