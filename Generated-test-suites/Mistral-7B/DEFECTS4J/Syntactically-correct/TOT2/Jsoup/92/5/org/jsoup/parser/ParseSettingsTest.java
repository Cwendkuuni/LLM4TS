// Add the necessary imports at the beginning of the file
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

// Test cases for ParseSettings class
public class ParseSettingsTest {

    // Test for preserveTagCase() method
    @Test
    public void testPreserveTagCase() {
        ParseSettings settingsHtmlDefault = new ParseSettings(false, false);
        boolean expectedHtmlDefault = false;
        boolean actualHtmlDefault = settingsHtmlDefault.preserveTagCase();
        assertEquals(expectedHtmlDefault, actualHtmlDefault);

        ParseSettings settingsPreserveCase = new ParseSettings(true, true);
        boolean expectedPreserveCase = true;
        boolean actualPreserveCase = settingsPreserveCase.preserveTagCase();
        assertEquals(expectedPreserveCase, actualPreserveCase);
    }

    // Test for normalizeTag(String name) method
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);

        String tagNameUpperCase = "TagName";
        String normalizeTagExpected = "tagname";
        String normalizeTagActual = settings.normalizeTag(tagNameUpperCase);
        assertEquals(normalizeTagExpected, normalizeTagActual);

        String tagNameLowerCase = "tagname";
        String normalizeTagExpectedLowerCase = "tagname";
        String normalizeTagActualLowerCase = settings.normalizeTag(tagNameLowerCase);
        assertEquals(normalizeTagExpectedLowerCase, normalizeTagActualLowerCase);
    }

    // Test for edge cases: empty string and null string
    @Test
    public void testNormalizeTagEdgeCases() {
        ParseSettings settings = new ParseSettings(true, false);

        String tagNameEmpty = "";
        String normalizeTagExpectedEmpty = "";
        String normalizeTagActualEmpty = settings.normalizeTag(tagNameEmpty);
        assertEquals(normalizeTagExpectedEmpty, normalizeTagActualEmpty);

        String tagNameNull = null;
        String normalizeTagExpectedNull = null;
        String normalizeTagActualNull = settings.normalizeTag(tagNameNull);
        assertNull(normalizeTagActualNull);
    }
}