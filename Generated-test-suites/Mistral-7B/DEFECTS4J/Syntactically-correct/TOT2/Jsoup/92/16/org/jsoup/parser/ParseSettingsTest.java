import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings htmlDefault = new ParseSettings(false, false);
        Assert.assertFalse(htmlDefault.preserveTagCase());

        ParseSettings preserveCase = new ParseSettings(true, true);
        Assert.assertTrue(preserveCase.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings preserveCase = new ParseSettings(true, false);
        String tagName = "TagName";
        String expected = "TagName";
        String result = preserveCase.normalizeTag(tagName);
        Assert.assertEquals(expected, result);

        ParseSettings htmlDefault = new ParseSettings(false, false);
        expected = "tagname";
        result = htmlDefault.normalizeTag(tagName);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings htmlDefault = new ParseSettings(false, false);
        Assert.assertFalse(htmlDefault.preserveAttributeCase());

        ParseSettings preserveCase = new ParseSettings(true, true);
        Assert.assertTrue(preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings preserveCase = new ParseSettings(true, true);
        String attributeName = "AttributeName";
        String expected = "AttributeName";
        String result = preserveCase.normalizeAttribute(attributeName);
        Assert.assertEquals(expected, result);

        ParseSettings htmlDefault = new ParseSettings(false, false);
        expected = "attributename";
        result = htmlDefault.normalizeAttribute(attributeName);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("attribute1", "Attribute1Value");
        originalAttributes.put("attribute2", "Attribute2Value");

        ParseSettings preserveCase = new ParseSettings(true, true);
        Attributes normalizedAttributes = preserveCase.normalizeAttributes(originalAttributes);

        String[] originalAttributeNames = originalAttributes.keySet().toArray(new String[originalAttributes.size()]);
        String[] expectedNormalizedAttributeNames = new String[originalAttributeNames.length];
        for (int i = 0; i < originalAttributeNames.length; i++) {
            expectedNormalizedAttributeNames[i] = originalAttributeNames[i];
        }

        String[] resultNormalizedAttributeNames = normalizedAttributes.keySet().toArray(new String[normalizedAttributes.size()]);
        assertArrayEquals(expectedNormalizedAttributeNames, resultNormalizedAttributeNames);

        ParseSettings htmlDefault = new ParseSettings(false, false);
        normalizedAttributes = htmlDefault.normalizeAttributes(originalAttributes);

        String[] normalizedAttributeNamesHtmlDefault = normalizedAttributes.keySet().toArray(new String[normalizedAttributes.size()]);
        assertNotEquals(expectedNormalizedAttributeNames, normalizedAttributeNamesHtmlDefault);
    }
}