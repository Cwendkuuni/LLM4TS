import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "Div";
        String attributeName1 = "class";
        String attributeName2 = "Id";

        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tag = settings.normalizeTag(tagName);
        String attribute1 = settings.normalizeAttribute(attributeName1);
        String attribute2 = settings.normalizeAttribute(attributeName2);

        assertEquals("div", tag);
        assertEquals("class", attribute1);
        assertEquals("id", attribute2);

        Attributes attributes = new Attributes();
        attributes.put("Class", "testClass");
        attributes.put("Id", "testId");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttribute1 = normalizedAttributes.get("class");
        String normalizedAttribute2 = normalizedAttributes.get("id");

        assertEquals("class", normalizedAttribute1);
        assertEquals("id", normalizedAttribute2);
        assertEquals(2, normalizedAttributes.size());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName1 = "CLASS";
        String attributeName2 = "ID";

        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tag = settings.normalizeTag(tagName);
        String attribute1 = settings.normalizeAttribute(attributeName1);
        String attribute2 = settings.normalizeAttribute(attributeName2);

        assertEquals("DIV", tag);
        assertEquals("CLASS", attribute1);
        assertEquals("ID", attribute2);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "testClass");
        attributes.put("ID", "testId");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttribute1 = normalizedAttributes.get("class");
        String normalizedAttribute2 = normalizedAttributes.get("id");

        assertEquals("CLASS", normalizedAttribute1);
        assertEquals("ID", normalizedAttribute2);
        assertEquals(2, normalizedAttributes.size());
    }

    @Test
    public void testNullInputs() {
        ParseSettings settings = ParseSettings.htmlDefault;

        String tagName = null;
        String attributeName = null;

        try {
            String tag = settings.normalizeTag(tagName);
            String attribute = settings.normalizeAttribute(attributeName);
            Attributes attributes = new Attributes();
            settings.normalizeAttributes(attributes);
            Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        } catch (NullPointerException e) {
            // Expected
        }
    }
}