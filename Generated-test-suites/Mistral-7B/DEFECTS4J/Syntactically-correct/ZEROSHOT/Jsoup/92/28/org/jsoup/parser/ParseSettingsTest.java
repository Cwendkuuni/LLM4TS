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
 public void testNewParseSettings() {
 ParseSettings settings = new ParseSettings(true, false);
 assertEquals(true, settings.preserveTagCase());
 assertEquals(false, settings.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTag() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "TAGName";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);

 settings = new ParseSettings(false, false);
 normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("tagname", normalizedTagName);
 }

 @Test
 public void testNormalizeAttribute() {
 ParseSettings settings = new ParseSettings(true, true);
 String attributeName = "ATTRIBUTE-NAME";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttributeName);

 settings = new ParseSettings(true, false);
 normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("attribute-name", normalizedAttributeName);

 settings = new ParseSettings(false, true);
 normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("ATTRIBUTE-NAME", normalizedAttributeName);

 settings = new ParseSettings(false, false);
 normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("attribute-name", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(true, true);
 Attributes attributes = new Attributes();
 attributes.put("name1", "value1");
 attributes.put("Name2", "value2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals("name1", normalizedAttributes.get("name1"));
 assertEquals("Name2", normalizedAttributes.get("Name2"));

 settings = new ParseSettings(true, false);
 normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("name1", normalizedAttributes.get("name1"));
 assertEquals("name2", normalizedAttributes.get("Name2"));

 settings = new ParseSettings(false, true);
 normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("Name2", normalizedAttributes.get("name1"));
 assertEquals("Name2", normalizedAttributes.get("Name2"));

 settings = new ParseSettings(false, false);
 normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("name1", normalizedAttributes.get("name1"));
 assertEquals("name2", normalizedAttributes.get("Name2"));
 }
}