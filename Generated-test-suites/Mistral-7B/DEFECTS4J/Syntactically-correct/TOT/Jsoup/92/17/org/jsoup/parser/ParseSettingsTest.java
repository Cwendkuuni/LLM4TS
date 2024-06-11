import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        String tagName = "HTML";
        String attributeName = "class";
        String expectedLowerTagName = "html";
        String expectedLowerAttributeName = "class";

        ParseSettings parseSettings = ParseSettings.htmlDefault;

        String normalizedTagName = parseSettings.normalizeTag(tagName);
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);

        assertEquals(expectedLowerTagName, normalizedTagName);
        assertEquals(expectedLowerAttributeName, normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
    }

    @Test
    public void testPreserveCase() {
        String tagName = "DIV";
        String attributeName = "id";
        String expectedTagName = "DIV";
        String expectedAttributeName = "id";

        ParseSettings parseSettings = ParseSettings.preserveCase;

        String normalizedTagName = parseSettings.normalizeTag(tagName);
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);

        assertEquals(expectedTagName, normalizedTagName);
        assertEquals(expectedAttributeName, normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("ID", "test");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("ID", normalizedAttributes.get("id").getValue());
    }
}