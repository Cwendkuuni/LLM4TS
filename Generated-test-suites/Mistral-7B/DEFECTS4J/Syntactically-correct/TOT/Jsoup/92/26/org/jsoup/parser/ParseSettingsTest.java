import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String attributeName1 = "class";
        String attributeName2 = "id";

        String tagNameLower = settings.normalizeTag(tagName);
        Assert.assertEquals("html", tagNameLower);

        Attributes attributes = new Attributes();
        attributes.put(attributeName1, "classValue");
        attributes.put(attributeName2, "idValue");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttribute1 = normalizedAttributes.get(attributeName1);
        Assert.assertEquals("classvalue", normalizedAttribute1);
        String normalizedAttribute2 = normalizedAttributes.get(attributeName2);
        Assert.assertEquals("idvalue", normalizedAttribute2);
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName1 = "class";
        String attributeName2 = "id";

        String tagNameNormalized = settings.normalizeTag(tagName);
        Assert.assertEquals("DIV", tagNameNormalized);

        Attributes attributes = new Attributes();
        attributes.put(attributeName1, "classValue");
        attributes.put(attributeName2, "idValue");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttribute1 = normalizedAttributes.get(attributeName1);
        Assert.assertEquals("classvalue", normalizedAttribute1);
        String normalizedAttribute2 = normalizedAttributes.get(attributeName2);
        Assert.assertEquals("idvalue", normalizedAttribute2);

        attributes.put(attributeName1, "CLASSVALUE");
        attributes.put(attributeName2, "IDVALUE");

        normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttribute1Upper = normalizedAttributes.get(attributeName1);
        Assert.assertEquals("CLASSVALUE", normalizedAttribute1Upper);
        String normalizedAttribute2Upper = normalizedAttributes.get(attributeName2);
        Assert.assertEquals("IDVALUE", normalizedAttribute2Upper);
    }

    @Test
    public void testPreserveTagCaseWithLowerTagName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "div";
        String attributeName = "class";

        String tagNameNormalized = settings.normalizeTag(tagName);
        Assert.assertEquals("DIV", tagNameNormalized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPreserveTagCaseWithEmptyTagName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "";

        try {
            settings.normalizeTag(tagName);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Tag name must not be empty", e.getMessage());
            throw e;
        }
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test
    public void testPreserveAttributeCaseWithLowerAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "class";
        String attributeValue = "classValue";

        Attributes attributes = new Attributes();
        attributes.put(attributeName, attributeValue);

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttribute = normalizedAttributes.get(attributeName);
        Assert.assertEquals("class", normalizedAttribute);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPreserveAttributeCaseWithEmptyAttributeName() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "";

        try {
            settings.normalizeAttributes(new Attributes());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Attribute name must not be empty", e.getMessage());
            throw e;
        }
        Assert.fail("Expected IllegalArgumentException");
    }
}