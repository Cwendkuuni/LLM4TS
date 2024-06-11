package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TAG";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertNotNull(normalizedTagName);
        assertEquals(tagName, normalizedTagName);
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "ATTRIBUTE";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertNotNull(normalizedAttributeName);
        assertEquals(attributeName, normalizedAttributeName);
    }

    @Test
    public void testEmptyTagName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("", normalizedTagName);
    }

    @Test
    public void testEmptyAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("", normalizedAttributeName);
    }

    @Test
    public void testBothPreserved() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = "TagName";
        String attributeName = "AttributeName";
        String normalizedTagName = settings.normalizeTag(tagName);
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals(tagName, normalizedTagName);
        assertEquals(attributeName, normalizedAttributeName);
    }

    @Test
    public void testBothNotPreserved() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "TAG";
        String attributeName = "ATTRIBUTE";
        String normalizedTagName = settings.normalizeTag(tagName);
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertFalse(tagName.equals(normalizedTagName));
        assertFalse(attributeName.equals(normalizedAttributeName));
    }

    @Test
    public void testTagAttributeDifferent() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TagName";
        String attributeName = "ATTRIBUTE";
        String normalizedTagName = settings.normalizeTag(tagName);
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("tagname", normalizedTagName);
        assertEquals("attribute", normalizedAttributeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTagName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = null;
        settings.normalizeTag(tagName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = null;
        settings.normalizeAttribute(attributeName);
    }
}

//