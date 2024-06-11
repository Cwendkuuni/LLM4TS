import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid cases
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMANY, LocaleUtils.toLocale("de_DE"));

        // Invalid cases
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Valid cases
        List<Locale> list = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(Locale.FRENCH, list.get(0));
        assertEquals(Locale.FRANCE, list.get(1));
        assertEquals(Locale.FRENCH, list.get(2));
        assertEquals(Locale.FRENCH, list.get(3));
        assertEquals(Locale.ENGLISH, list.get(4));

        list = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.ENGLISH, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.FRENCH, list.get(3));
        assertEquals(Locale.FRENCH, list.get(4));

        // Invalid cases
        list = LocaleUtils.localeLookupList(null);
        assertTrue(list.isEmpty());

        list = LocaleUtils.localeLookupList("xx");
        assertTrue(list.isEmpty());

        list = LocaleUtils.localeLookupList(Locale.FRENCH, null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Valid cases
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertFalse(list.isEmpty());

        // Invalid cases
        try {
            LocaleUtils.availableLocaleList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Valid cases
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));

        // Invalid cases
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale("xx"));
    }

    @Test
    public void testLanguagesByCountry() {
        // Valid cases
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(Locale.ENGLISH, list.get(0));

        list = LocaleUtils.languagesByCountry("FR");
        assertEquals(Locale.FRENCH, list.get(0));
        assertEquals(Locale.FRENCH, list.get(1));

        // Invalid cases
        list = LocaleUtils.languagesByCountry(null);
        assertTrue(list.isEmpty());

        list = LocaleUtils.languagesByCountry("xx");
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Valid cases
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(Locale.US, list.get(0));
        assertEquals(Locale.GB, list.get(1));

        list = LocaleUtils.countriesByLanguage("fr");
        assertEquals(Locale.FRANCE, list.get(0));
        assertEquals(Locale.CANADA, list.get(1));

        // Invalid cases
        list = LocaleUtils.countriesByLanguage(null);
        assertTrue(list.isEmpty());

        list = LocaleUtils.countriesByLanguage("xx");
        assertTrue(list.isEmpty());
    }
}