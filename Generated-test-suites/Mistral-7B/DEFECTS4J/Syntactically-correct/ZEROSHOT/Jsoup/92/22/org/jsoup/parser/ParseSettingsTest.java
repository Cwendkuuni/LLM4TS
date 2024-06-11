package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagNameLowerCased = settings.normalizeTag("HTML");
        String tagNamePreserved = settings.normalizeTag("HTML");
        assertTrue(tagNameLowerCased.equals(tagNamePreserved.toLowerCase()));

        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String attributeNameLowerCased = normalizedAttributes.get("class");
        String attributeNamePreserved = normalizedAttributes.get("class");
        assertTrue(attributeNameLowerCased.equals(attributeNamePreserved));
        assertTrue(attributeNameLowerCased.equalsIgnoreCase("testClass"));
    }

    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagNameLowerCased = settings.normalizeTag("HTML");
        String tagNamePreserved = settings.normalizeTag("HTML");
        assertFalse(tagNameLowerCased.equals(tagNamePreserved));
        assertTrue(tagNamePreserved.equals("HTML"));

        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String attributeNameLowerCased = normalizedAttributes.get("class");
        String attributeNamePreserved = normalizedAttributes.get("class");
        assertFalse(attributeNameLowerCased.equals(attributeNamePreserved));
        assertTrue(attributeNamePreserved.equals("class"));
    }

    @Test
    public void testParseSettingsNewInstance() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagNameLowerCased = settings.normalizeTag("HTML");
        String tagNamePreserved = settings.normalizeTag("HTML");
        assertFalse(tagNameLowerCased.equals(tagNamePreserved));
        assertTrue(tagNamePreserved.equals("HTML"));

        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String attributeNameLowerCased = normalizedAttributes.get("class");
        String attributeNamePreserved = normalizedAttributes.get("class");
        assertFalse(attributeNameLowerCased.equals(attributeNamePreserved));
        assertTrue(attributeNamePreserved.equals("class"));
    }
}