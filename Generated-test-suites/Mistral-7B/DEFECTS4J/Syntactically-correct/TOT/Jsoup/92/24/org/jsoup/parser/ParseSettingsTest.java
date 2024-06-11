import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

 @Test
 public void testNormalizeTagWithPreserveCaseFalse() {
 String tagName = "TAG";
 ParseSettings settings = new ParseSettings(false, false); // htmlDefault
 String normalizedTag = settings.normalizeTag(tagName);
 assertEquals("tag", normalizedTag);
 }

 @Test
 public void testNormalizeTagWithPreserveCaseTrue() {
 String tagName = "TAG";
 ParseSettings settings = new ParseSettings(true, false); // new ParseSettings(true, false)
 String normalizedTag = settings.normalizeTag(tagName);
 assertEquals("TAG", normalizedTag);
 }

 @Test
 public void testNormalizeAttributeWithPreserveCaseFalse() {
 String attributeName = "ATTR";
 ParseSettings settings = new ParseSettings(false, false); // htmlDefault
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertEquals("attr", normalizedAttribute);
 }

 @Test
 public void testNormalizeAttributeWithPreserveCaseTrue() {
 String attributeName = "ATTR";
 ParseSettings settings = new ParseSettings(true, false); // new ParseSettings(true, false)
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertEquals("ATTR", normalizedAttribute);
 }

 @Test
 public void testNormalizeAttributesWithPreserveCaseFalse() {
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("Attr2", "value2");
 ParseSettings settings = new ParseSettings(false, false); // htmlDefault
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("attr1", normalizedAttributes.get("attr1"));
 assertEquals("attr2", normalizedAttributes.get("attr2"));
 }

 @Test
 public void testNormalizeAttributesWithPreserveCaseTrue() {
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("Attr2", "value2");
 ParseSettings settings = new ParseSettings(true, false); // new ParseSettings(true, false)
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("Attr2", normalizedAttributes.get("Attr2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeTagWithEmptyString() {
 String tagName = "";
 ParseSettings settings = new ParseSettings(false, false); // htmlDefault
 String normalizedTag = settings.normalizeTag(tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeAttributeWithEmptyString() {
 String attributeName = "";
 ParseSettings settings = new ParseSettings(false, false); // htmlDefault
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 }

 @Test(expected = NullPointerException.class)
 public void testNormalizeAttributesWithNullAttributes() {
 ParseSettings settings = new ParseSettings(false, false); // htmlDefault
 Attributes attributes = null;
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 }

}