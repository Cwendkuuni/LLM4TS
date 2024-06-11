package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParseSettingsTest {

 @Test
 public void testParseSettingsDefaultConstruction() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertFalse(htmlDefault.preserveTagCase());
 assertFalse(htmlDefault.preserveAttributeCase());
 }

 @Test
 public void testParseSettingsPreserveCaseConstruction() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertTrue(preserveCase.preserveTagCase());
 assertTrue(preserveCase.preserveAttributeCase());
 }

 @Test
 public void testParseSettingsConstructionWithArgs() {
 ParseSettings settings = new ParseSettings(true, true);
 assertTrue(settings.preserveTagCase());
 assertTrue(settings.preserveAttributeCase());
 }

 @Test
 public void testParseSettingsConstructionWithOnlyTagPreservation() {
 ParseSettings settings = new ParseSettings(true, false);
 assertTrue(settings.preserveTagCase());
 assertFalse(settings.preserveAttributeCase());
 }

 @Test
 public void testParseSettingsConstructionWithOnlyAttributePreservation() {
 ParseSettings settings = new ParseSettings(false, true);
 assertFalse(settings.preserveTagCase());
 assertTrue(settings.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTagHyphen() {
 String tagName = "my-tag";
 ParseSettings settings = new ParseSettings(true, false);
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("my-tag", normalizedTagName);
 }

 @Test
 public void testNormalizeTagUnderscore() {
 String tagName = "my_tag";
 ParseSettings settings = new ParseSettings(true, false);
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("my_tag", normalizedTagName);
 }

 @Test
 public void testNormalizeTagNoChange() {
 String tagName = "div";
 ParseSettings settings = new ParseSettings(true, false);
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("div", normalizedTagName);
 }

 @Test
 public void testNormalizeTagLowercase() {
 ParseSettings settings = new ParseSettings(false, false);
 String tagName = "DIV";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("div", normalizedTagName);
 }

 @Test
 public void testNormalizeTagMixedCase() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "DIv";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("DIv", normalizedTagName);
 }

 @Test
 public void testNormalizeTagWithPreservedName() {
 ParseSettings settings = new ParseSettings(true, true);
 String tagName = "DIv";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("DIv", normalizedTagName);
 }

 @Test
 public void testNormalizeAttributeLowercase() {
 ParseSettings settings = new ParseSettings(false, false);
 String attributeName = "myAttribute";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("myattribute", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributeMixedCase() {
 ParseSettings settings = new ParseSettings(false, false);
 String attributeName = "MyAttribute";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("myattribute", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributeWithPreservedName() {
 ParseSettings settings = new ParseSettings(false, true);
 String attributeName = "MyAttribute";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("Myattribute", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributesWithPreservedNames() {
 ParseSettings settings = new ParseSettings(false, true);
 Attributes attributes = new Attributes();
 attributes.put("MyAttribute", "value");
 attributes.put("AnotherAttribute", "value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals("Myattribute", normalizedAttributes.get("MyAttribute"));
 assertEquals("Anotherattribute", normalizedAttributes.get("AnotherAttribute"));
 }

 @Test
 public void testNormalizeAttributesWithLowercasedNames() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("MyAttribute", "value");
 attributes.put("AnotherAttribute", "value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals("myattribute", normalizedAttributes.get("Myattribute"));
 assertEquals("anotherattribute", normalizedAttributes.get("Anotherattribute"));
 }
}