package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertEquals(false, htmlDefault.preserveTagCase());
 assertEquals(false, htmlDefault.preserveAttributeCase());
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertEquals(true, preserveCase.preserveTagCase());
 assertEquals(true, preserveCase.preserveAttributeCase());
 }

 @Test
 public void testNewParseSettings() {
 ParseSettings customSettings = new ParseSettings(true, false);
 assertEquals(true, customSettings.preserveTagCase());
 assertEquals(false, customSettings.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTag() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "TagName";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);

 settings = ParseSettings.htmlDefault;
 normalizedTagName = settings.normalizeTag(tagName);
 assertNotSame(tagName, normalizedTagName);
 assertEquals("tagname", normalizedTagName);
 }

 @Test
 public void testNormalizeAttribute() {
 ParseSettings settings = new ParseSettings(false, true);
 String attributeName = "AttributeName";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttributeName);

 settings = ParseSettings.htmlDefault;
 normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertNotSame(attributeName, normalizedAttributeName);
 assertEquals("attributename", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("attribute-name", "value");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(1, normalizedAttributes.size());
 assertEquals("attributename", normalizedAttributes.get("attribute-name").getValue());
 }

 @Test
 public void testNormalizeAttributesPreserveCase() {
 ParseSettings settings = new ParseSettings(true, true);
 Attributes attributes = new Attributes();
 attributes.put("Attribute-Name", "value");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(1, normalizedAttributes.size());
 assertEquals("Attribute-Name", normalizedAttributes.get("Attribute-Name").getValue());
 }
}