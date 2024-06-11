import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleUtilsTest {
    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNotNull(LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String invalidLocale = "_ invalid";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        String invalidLocale = "language_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountry() {
        String invalidLocale = "language__invalid";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguage() {
        String invalidLocale = "_Invalid";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_EmptyLanguage() {
        String invalidLocale = "__";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_EmptyCountry() {
        String invalidLocale = "language";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat_TwoCharacterLanguage() {
        Locale expectedLocale = new Locale("fr", "");
        Locale actualLocale = LocaleUtils.toLocale("fr");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_ValidFormat_TwoCharacterCountry() {
        Locale expectedLocale = new Locale("", "US");
        Locale actualLocale = LocaleUtils.toLocale("", "US");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_ValidFormat_ThreeCharacterLanguageAndCountry() {
        Locale expectedLocale = new Locale("fr_CA");
        Locale actualLocale = LocaleUtils.toLocale("fr_CA");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale locale = new Locale("en", "GB");
        List<Locale> result = LocaleUtils.localeLookupList(locale, null);
        List<Locale> expectedResult = Arrays.asList(locale, new Locale("en", "GB"), new Locale("en"), Locale.ROOT);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale("fr", "FR"));
        List<Locale> expectedResult = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), new Locale("fr", "FR"));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_NotEmpty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expectedResult = Arrays.asList(Locale.ROOT);
        assertTrue(result.containsAll(expectedResult));
        assertEquals(expectedResult.size(), result.size());
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("fr_XX", "FR");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedResult = Arrays.asList(new Locale("en", "US"), new Locale("en"));
        assertTrue(result.containsAll(expectedResult));
        assertEquals(expectedResult.size(), result.size());
    }
}