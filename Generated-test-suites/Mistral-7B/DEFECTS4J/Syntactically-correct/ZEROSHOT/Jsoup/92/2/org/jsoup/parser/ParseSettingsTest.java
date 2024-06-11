package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testParseSettingsHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertTrue(htmlDefault.preserveTagCase() == false);
 assertTrue(htmlDefault.preserveAttributeCase() == false);
 }

 @Test
 public void testParseSettingsPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertTrue(preserveCase.preserveTagCase() == true);
 assertTrue(preserveCase.preserveAttributeCase() == true);
 }

 @Test
 public void testNormalizeTagTrue() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "TAG_NAME";
 String normalizedTag = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTag);
 }

 @Test
 public void testNormalizeTagFalse() {
 ParseSettings settings = new ParseSettings(false, false);
 String tagName = "TAG_NAME";
 String normalizedTag = settings.normalizeTag(tagName);
 assertEquals(lowerCase(tagName), normalizedTag);
 }

 @Test
 public void testNormalizeAttributeTrue() {
 ParseSettings settings = new ParseSettings(false, true);
 String attributeName = "ATTRIBUTE_NAME";
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttribute);
 }

 @Test
 public void testNormalizeAttributeFalse() {
 ParseSettings settings = new ParseSettings(false, false);
 String attributeName = "ATTRIBUTE_NAME";
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertEquals(lowerCase(attributeName), normalizedAttribute);
 }

 @Test
 public void testNormalizeAttributesTrue() {
 ParseSettings settings = new ParseSettings(false, true);
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("Attr2", "value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals(attributes.size(), normalizedAttributes.size());
 for (String name : normalizedAttributes.names()) {
 String value = normalizedAttributes.get(name);
 String originalName = attributes.name(attributes.index(name));
 if (originalName != null) {
 assertEquals(originalName, name);
 }
 }
 }

 @Test
 public void testNormalizeAttributesFalse() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("Attr2", "value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 Attributes originalAttributes = new Attributes(attributes);
 for (String name : normalizedAttributes.names()) {
 String value = normalizedAttributes.get(name);
 String originalName = originalAttributes.name(originalAttributes.index(name.equalsIgnoreCase(name)));
 assertEquals(originalName, name);
 }
 }
}