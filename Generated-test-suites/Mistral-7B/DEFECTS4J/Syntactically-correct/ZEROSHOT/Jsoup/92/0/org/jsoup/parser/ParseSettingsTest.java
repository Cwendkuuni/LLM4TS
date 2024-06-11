package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

 @Test
 public void testParseSettingsConstructor() {
 ParseSettings htmlDefault = new ParseSettings(false, false);
 ParseSettings preserveCase = new ParseSettings(true, true);

 assertEquals("htmlDefault", htmlDefault.preserveTagCase(), false);
 assertEquals("htmlDefault", htmlDefault.preserveAttributeCase(), false);

 assertEquals("preserveCase", preserveCase.preserveTagCase(), true);
 assertEquals("preserveCase", preserveCase.preserveAttributeCase(), true);
 }

 @Test
 public void testNormalizeTagMethod() {
 ParseSettings parseSettings = new ParseSettings(true, false);

 String tagNameUpperCase = "TAG-NAME";
 String tagNameLowerCase = tagNameUpperCase.toLowerCase();
 String normalizedTagName = parseSettings.normalizeTag(tagNameUpperCase);

 assertEquals("tagNameUpperCase", tagNameUpperCase, normalizedTagName);
 assertEquals("tagNameLowerCase", tagNameLowerCase, parseSettings.normalizeTag(tagNameLowerCase));
 }

 @Test
 public void testNormalizeAttributeMethod() {
 ParseSettings parseSettings = new ParseSettings(false, true);

 String attributeNameUpperCase = "ATTRIBUTE-NAME";
 String attributeNameLowerCase = attributeNameUpperCase.toLowerCase();
 String normalizedAttributeName = parseSettings.normalizeAttribute(attributeNameUpperCase);

 assertEquals("attributeNameUpperCase", attributeNameUpperCase, normalizedAttributeName);
 assertEquals("attributeNameLowerCase", attributeNameLowerCase, parseSettings.normalizeAttribute(attributeNameLowerCase));
 }

 @Test
 public void testNormalizeAttributesMethod() {
 ParseSettings parseSettings = new ParseSettings(false, true);

 Attributes originalAttributes = new Attributes();
 originalAttributes.put("ATTRIBUTE-NAME-1", "value1");
 originalAttributes.put("ATTRIBUTE-NAME-2", "value2");
 originalAttributes.put("ATTRIBUTE-NAME-3", "value3");

 Attributes normalizedAttributes = parseSettings.normalizeAttributes(originalAttributes);

 assertEquals(3, normalizedAttributes.size());
assertNotNull("ATTRIBUTE-NAME-1", normalizedAttributes.get("ATTRIBUTE-NAME-1"));
assertNotNull("ATTRIBUTE-NAME-2", normalizedAttributes.get("ATTRIBUTE-NAME-2"));
assertNotNull("ATTRIBUTE-NAME-3", normalizedAttributes.get("ATTRIBUTE-NAME-3"));
 }
}