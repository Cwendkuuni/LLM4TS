package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertTrue(settings.preserveTagCase() == false);
        assertTrue(settings.preserveAttributeCase() == false);
    }

    @Test
    public void testPreserveCaseSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertTrue(settings.preserveTagCase() == true);
        assertTrue(settings.preserveAttributeCase() == true);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TagName";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizedTagName);

        tagName = "tagName";
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizedTagName);

        settings = new ParseSettings(false, false);
        tagName = "TagName";
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("tagname", normalizedTagName);

        tagName = "tagName";
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("tagname", normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        String attributeName = "attributeName";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals(attributeName, normalizedAttributeName);

        attributeName = "ATTRIBUTE_NAME";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals(attributeName, normalizedAttributeName);

        settings = new ParseSettings(false, false);
        attributeName = "attributeName";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("attributename", normalizedAttributeName);

        attributeName = "ATTRIBUTE_NAME";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("attributename", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("attributeName1", "value1");
        attributes.put("ATTRIBUTE_NAME2", "value2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attributeName1", normalizedAttributes.get("attributeName1").toString());
        assertEquals("ATTRIBUTE_NAME2", normalizedAttributes.get("ATTRIBUTE_NAME2").toString());

        settings = new ParseSettings(false, false);
        attributes = new Attributes();
        attributes.put("attributeName1", "value1");
        attributes.put("ATTRIBUTE_NAME2", "value2");

        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attributename1", normalizedAttributes.get("attributeName1").toString());
        assertEquals("attributename2", normalizedAttributes.get("ATTRIBUTE_NAME2").toString());
    }
}