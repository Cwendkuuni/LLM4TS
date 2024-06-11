
package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLocale() {
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_EmptyString() {
        String localeString = "";
        try {
            LocaleUtils.toLocale(localeString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocale_NullString() {
        String localeString = null;
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertNull(actualLocale);
    }

    // Add test cases for other methods and edge cases

    // ...
}
