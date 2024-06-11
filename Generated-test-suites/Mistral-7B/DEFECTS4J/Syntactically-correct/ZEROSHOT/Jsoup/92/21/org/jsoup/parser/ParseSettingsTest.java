package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 @Test
 public void testParseSettingsHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertEquals(false, htmlDefault.preserveTagCase());
 assertEquals(false, htmlDefault.preserveAttributeCase());
 }

 @Test
 public void testParseSettingsPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertEquals(true, preserveCase.preserveTagCase());
 assertEquals(true, preserveCase.preserveAttributeCase());
 }

 @Test
 public void testParseSettingsNewInstanceTagFalseAttributeFalse() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 assertEquals(false, parseSettings.preserveTagCase());
 assertEquals(false, parseSettings.preserveAttributeCase());
 }

 @Test
 public void testParseSettingsNewInstanceTagTrueAttributeTrue() {
 ParseSettings parseSettings = new ParseSettings(true, true);
 assertEquals(true, parseSettings.preserveTagCase());
 assertEquals(true, parseSettings.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTag() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 String tagName = "TAG";
 String normalizedTagName = parseSettings.normalizeTag(tagName);
 assertEquals(tagName.toUpperCase(), normalizedTagName);

 parseSettings = new ParseSettings(false, false);
 normalizedTagName = parseSettings.normalizeTag(tagName);
 assertEquals("tag", normalizedTagName);
 }

 @Test
 public void testNormalizeAttribute() {
 ParseSettings parseSettings = new ParseSettings(true, true);
 String attributeName = "ATTRIBUTE";
 String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
 assertEquals(attributeName.toUpperCase(), normalizedAttributeName);

 parseSettings = new ParseSettings(false, false);
 normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
 assertEquals("attribute", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("Attr2", "value2");

 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals("attr1", normalizedAttributes.get("attr1").getName());
 assertEquals("attr2", normalizedAttributes.get("attr2").getName());
 }

 @Test
 public void testNormalizeAttributesPreserveAttributeCase() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("Attr2", "value2");

 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals("Attr1", normalizedAttributes.get("attr1").getName());
 assertEquals("Attr2", normalizedAttributes.get("Attr2").getName());
 }
}