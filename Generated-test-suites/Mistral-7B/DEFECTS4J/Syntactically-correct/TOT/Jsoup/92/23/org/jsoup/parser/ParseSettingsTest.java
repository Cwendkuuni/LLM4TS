package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * ParseSettings test case.
 */
public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultParseSettings() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String attributeName = "class";

        // Test tag name normalization
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("html", normalizedTagName);

        // Test attribute name normalization
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttributeName);

        // Test attributes normalization
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        attributes.put("id", "testId");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("class", normalizedAttributes.get("class"));
        assertEquals("testclass", normalizedAttributes.get("class").toLowerCase()); // lowerCase is used in the normalize() method
        assertEquals("id", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveCaseParseSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName = "CLASS";

        // Test tag name normalization
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        // Test attribute name normalization
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("CLASS", normalizedAttributeName);

        // Test attributes normalization
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "testClass");
        attributes.put("ID", "testId");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("CLASS", normalizedAttributes.get("CLASS"));
        assertEquals("testClass", normalizedAttributes.get("CLASS")); // no normalization happens
        assertEquals("ID", normalizedAttributes.get("ID"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArguments() {
        new ParseSettings(true, "invalidBoolean"); // invalid boolean argument
        new ParseSettings(-1, false); // invalid integer argument
    }

    @Test
    public void testNormalizeAttributesWithEmptyAttributes() {
        Attributes attributes = new Attributes();
        ParseSettings settings = new ParseSettings(false, false);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNull(normalizedAttributes);
    }

    @Test
    public void testNormalizeAttributesWithNullAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes normalizedAttributes = settings.normalizeAttributes(null);
        assertNull(normalizedAttributes);
    }
}