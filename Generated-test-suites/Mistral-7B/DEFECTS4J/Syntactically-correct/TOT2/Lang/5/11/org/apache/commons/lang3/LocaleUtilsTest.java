import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    

    @Test
    public void testToLocale_ValidInput() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidInput() {
        assertNull(LocaleUtils.toLocale("en_GB_xxx")); // invalid variant
        assertNull(LocaleUtils.toLocale("en_GB_en")); // invalid language
        assertNull(LocaleUtils.toLocale("en_GB_GB")); // invalid country
        assertNull(LocaleUtils.toLocale("en_GB_en_GB")); // invalid repetition of country
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupList_InvalidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        Locale invalidLocale = new Locale("en", "CA"); // invalid country for default locale
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        Locale locale = new Locale("en", "US_US"); // invalid variant
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_US_US"); // invalid repetition of country
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "CA"); // invalid country
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_CA"); // valid locale but not available
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    
}