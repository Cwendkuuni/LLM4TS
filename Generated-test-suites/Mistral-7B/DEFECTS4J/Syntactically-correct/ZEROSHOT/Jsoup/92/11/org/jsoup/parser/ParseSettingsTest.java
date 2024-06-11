package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultParseSettings() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertEquals(false, htmlDefault.preserveTagCase());
 assertEquals(false, htmlDefault.preserveAttributeCase());
 }

 @Test
 public void testPreserveCaseParseSettings() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertEquals(true, preserveCase.preserveTagCase());
 assertEquals(true, preserveCase.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTag_Lowercase() {
 ParseSettings settings = new ParseSettings(false, false);
 String tag = "tagName";
 String expected = "tagname";
 String normalizedTag = settings.normalizeTag(tag);
 assertEquals(expected, normalizedTag);
 }

 @Test
 public void testNormalizeTag_PreserveCase() {
 ParseSettings settings = new ParseSettings(true, false);
 String tag = "TagName";
 String expected = "TagName";
 String normalizedTag = settings.normalizeTag(tag);
 assertEquals(expected, normalizedTag);
 }

 @Test
 public void testNormalizeTag_EmptyString() {
 ParseSettings settings = new ParseSettings(false, false);
 String tag = "";
 String normalizedTag = settings.normalizeTag(tag);
 assertEquals("", normalizedTag);
 }

 @Test
 public void testNormalizeTag_Null() {
 ParseSettings settings = new ParseSettings(false, false);
 String tag = null;
 try {
 String normalizedTag = settings.normalizeTag(tag);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNormalizeAttribute_Lowercase() {
 ParseSettings settings = new ParseSettings(false, false);
 String attribute = "attributeName";
 String expected = "attributename";
 String normalizedAttribute = settings.normalizeAttribute(attribute);
 assertEquals(expected, normalizedAttribute);
 }

 @Test
 public void testNormalizeAttribute_PreserveCase() {
 ParseSettings settings = new ParseSettings(false, true);
 String attribute = "AttributeName";
 String expected = "AttributeName";
 String normalizedAttribute = settings.normalizeAttribute(attribute);
 assertEquals(expected, normalizedAttribute);
 }

 @Test
 public void testNormalizeAttribute_EmptyString() {
 ParseSettings settings = new ParseSettings(false, false);
 String attribute = "";
 String normalizedAttribute = settings.normalizeAttribute(attribute);
 assertEquals("", normalizedAttribute);
 }

 @Test
 public void testNormalizeAttribute_Null() {
 ParseSettings settings = new ParseSettings(false, false);
 String attribute = null;
 try {
 String normalizedAttribute = settings.normalizeAttribute(attribute);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("attribute1", "value1");
 attributes.put("attribute2", "value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());
 String attribute1Normalized = normalizedAttributes.get("attribute1");
 String attribute2Normalized = normalizedAttributes.get("attribute2");
 assertEquals("attribute1", attribute1Normalized);
 assertEquals("value1", normalizedAttributes.get("attribute1").getValue());
 assertEquals("attribute2", attribute2Normalized);
 assertEquals("value2", normalizedAttributes.get("attribute2").getValue());
 }

 @Test
 public void testNormalizeAttributes_PreserveCase() {
 ParseSettings settings = new ParseSettings(false, true);
 Attributes attributes = new Attributes();
 attributes.put("Attribute1", "Value1");
 attributes.put("Attribute2", "Value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());
 String attribute1Normalized = normalizedAttributes.get("Attribute1").getValue();
 String attribute2Normalized = normalizedAttributes.get("Attribute2").getValue();
 assertEquals("Attribute1", normalizedAttributes.get("Attribute1").getKey());
 assertEquals("Value1", attribute1Normalized);
 assertEquals("Attribute2", normalizedAttributes.get("Attribute2").getKey());
 assertEquals("Value2", normalizedAttributes.get("Attribute2").getValue());
 }
}