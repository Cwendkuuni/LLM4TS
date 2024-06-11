import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "SCRIPT";
        String attributeName1 = "class";
        String attributeName2 = "type";

        // Test tag name normalization
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("script", normalizedTag);

        // Test attribute name normalization
        String normalizedAttribute1 = settings.normalizeAttribute(attributeName1);
        assertEquals("class", normalizedAttribute1);

        String normalizedAttribute2 = settings.normalizeAttribute(attributeName2);
        assertEquals("type", normalizedAttribute2);

        // Test attributes normalization
        Attributes attributes = new Attributes();
        attributes.put("class", "scriptClass");
        attributes.put("type", "text/javascript");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("scriptclass", normalizedAttributes.get("class"));
        assertEquals("text/javascript", normalizedAttributes.get("type"));
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName1 = "CLASS";
        String attributeName2 = "ID";

        // Test tag name normalization
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTag);

        // Test attribute name normalization
        String normalizedAttribute1 = settings.normalizeAttribute(attributeName1);
        assertEquals("class", normalizedAttribute1);

        String normalizedAttribute2 = settings.normalizeAttribute(attributeName2);
        assertEquals("id", normalizedAttribute2);

        // Test attributes normalization
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "scriptClass");
        attributes.put("ID", "scriptId");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", normalizedAttributes.get("class"));
        assertEquals("ID", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveTagCaseWithLowercaseInput() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "div";

        // Test tag name normalization
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTag);
    }

    @Test
    public void testPreserveAttributeCaseWithLowercaseInput() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attributeName = "class";

        // Test attribute name normalization
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttribute);
    }

    @Test (expected = NullPointerException.class)
    public void testNullInputForTagNormalization() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = null;

        // Test tag name normalization with null input
        String normalizedTag = settings.normalizeTag(tagName);
        fail("Expected a NullPointerException, but got: " + normalizedTag);
    }

    @Test (expected = NullPointerException.class)
    public void testNullInputForAttributeNormalization() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attributeName = null;

        // Test attribute name normalization with null input
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        fail("Expected a NullPointerException, but got: " + normalizedAttribute);
    }
}