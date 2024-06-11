import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Test with empty input
        assertNull(LocaleUtils.toLocale(null));

        // Test with invalid input
        try {
            LocaleUtils.toLocale("zx_XY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with short input
        try {
            LocaleUtils.toLocale("zh");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = LocaleUtils.localeLookupList(locale);
        List<Locale> actual = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expected, actual);

        // Test with null input
        assertEmpty(LocaleUtils.localeLookupList(null));

        // Test with defaultLocale
        List<Locale> actualDefault = LocaleUtils.localeLookupList(Locale.ENGLISH, new Locale("fr", "CA"));
        List<Locale> expectedDefault = Arrays.asList(Locale.ENGLISH, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expectedDefault, actualDefault);

        // Test with empty list
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale(""));
        List<Locale> expectedEmpty = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH, Locale.SWEDISH, Locale.TRADITIONAL_TAIWAN, Locale.THAI, Locale.VIETNAMESE);
        assertEquals(expected, actual);

        // Test with null input
        assertNotNull(LocaleUtils.availableLocaleList());

        // Test with empty list
        List<Locale> empty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.availableLocaleList();
        assertEquals(empty, actualEmpty);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test with null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test with invalid input
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_JP));

        // Test with empty list
        List<Locale> empty = Collections.emptyList();
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
        List<Locale> locales = new ArrayList<>();
        locales.addAll(empty);
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", ""), locales.toArray(new Locale[0])));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        List<Locale> expected = LocaleUtils.languagesByCountry("CA");
        List<Locale> actual = Arrays.asList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(expected, actual);

        // Test with null input
        assertEmpty(LocaleUtils.languagesByCountry(null));

        // Test with empty list
        List<Locale> empty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.languagesByCountry("");
        assertEquals(empty, actualEmpty);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        List<Locale> expected = LocaleUtils.countriesByLanguage("en");
        List<Locale> actual = Arrays.asList(Locale.US, Locale.GB, Locale.CA);
        assertEquals(expected, actual);

        // Test with null input
        assertEmpty(LocaleUtils.countriesByLanguage(null));

        // Test with empty list
        List<Locale> empty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.countriesByLanguage("");
        assertEquals(empty, actualEmpty);
    }

    private void assertEmpty(List list) {
        assertTrue(list.isEmpty());
    }

}