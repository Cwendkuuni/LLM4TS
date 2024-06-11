import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        String tagName1 = "DIV";
        String tagName2 = "pArAg";
        String attrName1 = "class";
        String attrName2 = "Class";

        // Normalize tag names with HTML default settings
        String tag1 = htmlDefault.normalizeTag(tagName1);
        String tag2 = htmlDefault.normalizeTag(tagName2);
        assertEquals("div", tag1);
        assertEquals("div", tag2); // Since lower-case by default

        // Normalize attribute names with HTML default settings
        String attr1 = htmlDefault.normalizeAttribute(attrName1);
        String attr2 = htmlDefault.normalizeAttribute(attrName2);
        assertEquals("class", attr1);
        assertEquals("class", attr2); // Since lower-case by default

        // Test normalizeAttributes method
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCase() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        String tagName1 = "DIV";
        String tagName2 = "pArAg";
        String attrName1 = "class";
        String attrName2 = "Class";

        // Normalize tag names with preserveCase settings
        String tag1 = preserveCase.normalizeTag(tagName1);
        String tag2 = preserveCase.normalizeTag(tagName2);
        assertEquals("DIV", tag1);
        assertEquals("pArAg", tag2);

        // Normalize attribute names with preserveCase settings
        String attr1 = preserveCase.normalizeAttribute(attrName1);
        String attr2 = preserveCase.normalizeAttribute(attrName2);
        assertEquals("class", attr1);
        assertEquals("Class", attr2);

        // Test normalizeAttributes method
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("class"));
    }

    @Test
    public void testNullValues() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        String tagName = null;
        String attrName = null;

        // Test normalizeTag method with null input
        String tag = preserveCase.normalizeTag(tagName);
        assertNull(tag);

        // Test normalizeAttribute method with null input
        String attr = preserveCase.normalizeAttribute(attrName);
        assertNull(attr);

        // Test normalizeAttributes method with null input
        Attributes attributes = null;
        Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
        assertNull(normalizedAttributes);
    }

    @Test(expected = NullPointerException.class)
    public void testPreserveCaseWithNullInput() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        String tagName = null;

        // Test normalizeTag method with null input and expected exception
        String tag = preserveCase.normalizeTag(tagName);
    }
}