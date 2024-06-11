import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getCountry());
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String invalidFormat = "_country";
        try {
            LocaleUtils.toLocale(invalidFormat);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidFormat, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountry() {
        String invalidFormat = "_invalidCountry";
        try {
            LocaleUtils.toLocale(invalidFormat);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidFormat, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguage() {
        String invalidFormat = "InvalidLanguage_country";
        try {
            LocaleUtils.toLocale(invalidFormat);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidFormat, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_MissingLanguageVersion() {
        String invalidFormat = "Language_";
        try {
            LocaleUtils.toLocale(invalidFormat);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidFormat, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_MissingCountryVersion() {
        String invalidFormat = "Language_country_";
        try {
            LocaleUtils.toLocale(invalidFormat);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidFormat, e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat() {
        String validFormat = "en_US";
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(validFormat));
        assertEquals("US", LocaleUtils.toLocale(validFormat).getCountry());
        assertEquals("en", LocaleUtils.toLocale(validFormat).getLanguage());
    }

    @Test
    public void testToLocale_ValidFormat_ThreeLetterCountryCode() {
        String validFormat = "fr_CA";
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(validFormat));
        assertEquals("CA", LocaleUtils.toLocale(validFormat).getCountry());
        assertEquals("fr", LocaleUtils.toLocale(validFormat).getLanguage());
    }

    @Test
    public void testToLocale_ValidFormat_ThreeLetterLanguageCode() {
        String validFormat = "es_ES";
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(validFormat));
        assertEquals("ES", LocaleUtils.toLocale(validFormat).getCountry());
        assertEquals("es", LocaleUtils.toLocale(validFormat).getLanguage());
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        assertEmpty(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        assertEmpty(LocaleUtils.localeLookupList(new Locale("", "")));
    }

    @Test
    public void testLocaleLookupList_SingleLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> locals = LocaleUtils.localeLookupList(locale);
        assertEquals(1, locals.size());
        assertEquals(locale, locals.get(0));
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        Locale locale = new Locale("de", "DE", "DE_CH");
        List<Locale> locals = LocaleUtils.localeLookupList(locale);
        assertEquals(3, locals.size());
        assertEquals(locale, locals.get(0));
        assertEquals(new Locale("de", "DE"), locals.get(1));
        assertEquals(new Locale("de", ""), locals.get(2));
    }

    @Test
    public void testLocaleLookupList_EmptyDefaultLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> locals = LocaleUtils.localeLookupList(locale, new Locale(""));
        assertEquals(4, locals.size());
        assertTrue(locals.contains(locale));
        assertTrue(locals.contains(new Locale("de", "DE")));
        assertTrue(locals.contains(new Locale("de", "")));
        assertTrue(locals.contains(new Locale("")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_EmptyLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("Invalid", "Invalid")));
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        assertEmpty(LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_EmptyList() {
        assertEmpty(LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertFalse(result.isEmpty());
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("en", "CA")));
        // Add more test cases as needed
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        assertEmpty(LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguage_EmptyList() {
        assertEmpty(LocaleUtils.countriesByLanguage(""));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertFalse(result.isEmpty());
        assertTrue(result.contains(new Locale("US", "en")));
        assertTrue(result.contains(new Locale("CA", "en")));
        // Add more test cases as needed
    }
}