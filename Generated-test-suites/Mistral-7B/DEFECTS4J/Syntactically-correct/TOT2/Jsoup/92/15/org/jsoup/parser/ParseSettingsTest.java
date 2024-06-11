package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TagName";
        String normalizedTagName = settings.normalizeTag(tagName);
        assert tagName.equals(normalizedTagName); // Test for preserving tag case
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "AttributeName";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assert attributeName.equals(normalizedAttributeName); // Test for preserving attribute case
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        ParseSettings preserveTagCase = new ParseSettings(true, false);

        String tagName1 = "TAG";
        String tagName2 = "tag";

        String htmlDefaultNormalizedTagName = htmlDefault.normalizeTag(tagName1);
        String preserveTagCaseNormalizedTagName = preserveTagCase.normalizeTag(tagName1);
        String preserveTagCaseNormalizedTagName2 = preserveTagCase.normalizeTag(tagName2);

        assert htmlDefaultNormalizedTagName.equalsIgnoreCase(tagName1.toLowerCase()); // Test for lower case tag
        assert preserveTagCaseNormalizedTagName.equals(tagName1); // Test for preserving tag case
        assert preserveTagCaseNormalizedTagName2.equals(tagName2); // Test for preserving tag case for different tag name
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        ParseSettings preserveAttributeCase = new ParseSettings(false, true);

        String attributeName1 = "ATTRIBUTE";
        String attributeName2 = "attribute";

        String htmlDefaultNormalizedAttributeName = htmlDefault.normalizeAttribute(attributeName1);
        String preserveAttributeCaseNormalizedAttributeName = preserveAttributeCase.normalizeAttribute(attributeName1);
        String preserveAttributeCaseNormalizedAttributeName2 = preserveAttributeCase.normalizeAttribute(attributeName2);

        assert htmlDefaultNormalizedAttributeName.equalsIgnoreCase(attributeName1.toLowerCase()); // Test for lower case attribute
        assert preserveAttributeCaseNormalizedAttributeName.equalsIgnoreCase(attributeName1); // Test for preserving attribute case
        assert preserveAttributeCaseNormalizedAttributeName2.equalsIgnoreCase(attributeName2); // Test for preserving attribute case for different attribute name
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        ParseSettings preserveAttributeCase = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("TAG", "value1");
        attributes.put("tag", "value2");

        Attributes normalizedAttributes_htmlDefault = htmlDefault.normalizeAttributes(attributes);
        Attributes normalizedAttributes_preserveAttributeCase = preserveAttributeCase.normalizeAttributes(attributes);

        assert normalizedAttributes_htmlDefault.get("TAG").getValue().equalsIgnoreCase("value1"); // Test for lower case attribute name
        assert normalizedAttributes_preserveAttributeCase.get("TAG").getValue().equals("TAG"); // Test for preserving attribute case
        assert normalizedAttributes_preserveAttributeCase.get("tag").getValue().equals("tag"); // Test for preserving attribute case for different attribute name
    }
}