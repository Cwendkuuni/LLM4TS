package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultPreserveTagCaseFalseAndAttributeCaseFalse() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "HTML";
 String attributeName = "class";

 String tagNameNormalizedExpected = "html";
 String attributeNameNormalizedExpected = "class";

 String tagNameNormalizedActual = htmlDefault.normalizeTag(tagName);
 String attributeNameNormalizedActual = htmlDefault.normalizeAttribute(attributeName);

 assertEquals(tagNameNormalizedExpected, tagNameNormalizedActual);
 assertEquals(attributeNameNormalizedExpected, attributeNameNormalizedActual);
 }

 @Test
 public void testPreserveCasePreserveTagCaseTrueAndAttributeCaseTrue() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = "HTML";
 String attributeName = "CLASS";

 String tagNameNormalizedExpected = "HTML";
 String attributeNameNormalizedExpected = "CLASS";

 String tagNameNormalizedActual = preserveCase.normalizeTag(tagName);
 String attributeNameNormalizedActual = preserveCase.normalizeAttribute(attributeName);

 assertEquals(tagNameNormalizedExpected, tagNameNormalizedActual);
 assertEquals(attributeNameNormalizedExpected, attributeNameNormalizedActual);
 }

 @Test
 public void testPreserveTagCaseFalseAndPreserveAttributeCaseTrue() {
 ParseSettings testParseSettings1 = new ParseSettings(false, true);
 String tagName = "HTML";
 String attributeName = "Class";

 String tagNameNormalizedExpected = "html";
 String attributeNameNormalizedExpected = "class";

 String tagNameNormalizedActual = testParseSettings1.normalizeTag(tagName);
 String attributeNameNormalizedActual = testParseSettings1.normalizeAttribute(attributeName);

 assertEquals(tagNameNormalizedExpected, tagNameNormalizedActual);
 assertNotEquals(attributeNameNormalizedExpected, attributeNameNormalizedActual);
 }

 @Test
 public void testPreserveTagCaseTrueAndPreserveAttributeCaseFalse() {
 ParseSettings testParseSettings2 = new ParseSettings(true, false);
 String tagName = "HTML";
 String attributeName = "class";

 String tagNameNormalizedExpected = "HTML";
 String attributeNameNormalizedExpected = "class";

 String tagNameNormalizedActual = testParseSettings2.normalizeTag(tagName);
 String attributeNameNormalizedActual = testParseSettings2.normalizeAttribute(attributeName);

 assertEquals(tagNameNormalizedExpected, tagNameNormalizedActual);
 assertEquals(attributeNameNormalizedExpected, testParseSettings2.normalizeAttribute(attributeName));
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("class", "test");
 attributes.put("data-test", "value");

 Attributes normalizedAttributesExpected = new Attributes();
 normalizedAttributesExpected.put("class", "test");
 normalizedAttributesExpected.put("data-test", "value");

 Attributes normalizedAttributesActual = parseSettings.normalizeAttributes(attributes);

 assertEquals(normalizedAttributesExpected.toString(), normalizedAttributesActual.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPreserveTagCaseAndAttributeCaseFalseWithNullTagName() {
 ParseSettings testParseSettings = new ParseSettings(false, false);
 String tagName = null;
 testParseSettings.normalizeTag(tagName);
 fail("IllegalArgumentException expected.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPreserveTagCaseAndAttributeCaseFalseWithNullAttributeName() {
 ParseSettings testParseSettings = new ParseSettings(false, false);
 String attributeName = null;
 testParseSettings.normalizeAttribute(attributeName);
 fail("IllegalArgumentException expected.");
 }
}