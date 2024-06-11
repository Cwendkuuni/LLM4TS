package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

 @Test
 public void testParseSettingsInstanceCreation() {
 // Test HTML default instance
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertNotNull(htmlDefault);
 assertEquals(false, htmlDefault.preserveTagCase());
 assertEquals(false, htmlDefault.preserveAttributeCase());

 // Test preserveCase instance
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertNotNull(preserveCase);
 assertEquals(true, preserveCase.preserveTagCase());
 assertEquals(true, preserveCase.preserveAttributeCase());

 // Test custom instance
 ParseSettings customSettings = new ParseSettings(true, false);
 assertNotNull(customSettings);
 assertEquals(true, customSettings.preserveTagCase());
 assertEquals(false, customSettings.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTag() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName1 = "TAG";
 String tagName2 = tagName1.toLowerCase();
 String normalizedTagName1 = settings.normalizeTag(tagName1);
 String normalizedTagName2 = settings.normalizeTag(tagName2);
 assertEquals(tagName1, normalizedTagName1);
 assertEquals(tagName2, normalizedTagName2);
 }

 @Test
 public void testNormalizeTagPreserved() {
 ParseSettings settings = ParseSettings.preserveCase;
 String tagName = "TAG";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);
 }

 @Test
 public void testNormalizeAttribute() {
 ParseSettings settings = new ParseSettings(false, true);
 String attrName1 = "attr";
 String attrName2 = attrName1.toLowerCase();
 String normalizedAttrName1 = settings.normalizeAttribute(attrName1);
 String normalizedAttrName2 = settings.normalizeAttribute(attrName2);
 assertEquals(attrName1, normalizedAttrName1);
 assertEquals(attrName2, normalizedAttrName2);
 }

 @Test
 public void testNormalizeAttributePreserved() {
 ParseSettings settings = ParseSettings.preserveCase;
 String attrName = "attr";
 String normalizedAttrName = settings.normalizeAttribute(attrName);
 assertEquals(attrName, normalizedAttrName);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("attr2", "value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals("value1", normalizedAttributes.get("attr1"));
 assertEquals("value2", normalizedAttributes.get("attr2"));
 }

 @Test
 public void testNormalizeAttributesPreserved() {
 ParseSettings settings = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("attr2", "value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals("attr1", normalizedAttributes.get("attr1").getName());
 assertEquals("attr2", normalizedAttributes.get("attr2").getName());
 }
}