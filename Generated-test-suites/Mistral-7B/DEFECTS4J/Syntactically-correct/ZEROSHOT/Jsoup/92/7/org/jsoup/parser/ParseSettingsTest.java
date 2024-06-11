package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = settings.normalizeTag("HTML");
        String attributeName = settings.normalizeAttribute("class");
        Assert.assertEquals("html", tagName);
        Assert.assertEquals("class", attributeName);
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = settings.normalizeTag("DIV");
        Assert.assertEquals("DIV", tagName);
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attributeName = settings.normalizeAttribute("ID");
        Assert.assertEquals("ID", attributeName);
    }

    @Test
    public void testPreserveCaseBoth() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = settings.normalizeTag("SCRIPT");
        String attributeName = settings.normalizeAttribute("src");
        Assert.assertEquals("SCRIPT", tagName);
        Assert.assertEquals("src", attributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("id", "bar");
        ParseSettings settings = new ParseSettings(false, false);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String foo = normalizedAttributes.get("class");
        String bar = normalizedAttributes.get("id");
        Assert.assertEquals("class", foo);
        Assert.assertEquals("id", bar);
        Assert.assertNull(normalizedAttributes.get("CLASS"));
        Assert.assertNull(normalizedAttributes.get("ID"));

        settings = new ParseSettings(false, true);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        foo = normalizedAttributes.get("class");
        bar = normalizedAttributes.get("id");
        Assert.assertEquals("class", foo);
        Assert.assertEquals("id", bar);
        Assert.assertNull(normalizedAttributes.get("Class"));
        Assert.assertNull(normalizedAttributes.get("ID"));

        settings = new ParseSettings(true, false);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        foo = normalizedAttributes.get("class");
        bar = normalizedAttributes.get("id");
        Assert.assertEquals("CLASS", foo);
        Assert.assertEquals("ID", bar);
        Assert.assertNull(normalizedAttributes.get("class"));
        Assert.assertNull(normalizedAttributes.get("id"));

        settings = new ParseSettings(true, true);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        foo = normalizedAttributes.get("class");
        bar = normalizedAttributes.get("id");
        Assert.assertEquals("CLASS", foo);
        Assert.assertEquals("ID", bar);
        Assert.assertEquals("CLASS", normalizedAttributes.get("Class"));
        Assert.assertEquals("ID", normalizedAttributes.get("ID"));
    }
}