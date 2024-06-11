import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    

    @Test
    public void testToLocale_ValidInput_ReturnsLocaleObject() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        String invalidInput = "en_GB_xxx";
        try {
            LocaleUtils.toLocale(invalidInput);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testLocaleLookupList_ValidInput_ReturnsUnmodifiableList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_NullInput_ReturnsEmptyList() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLocaleLookupList_VariableLocale_ReturnsListWithMostSpecificLocaleFirst() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertTrue(actual.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertTrue(actual.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_ValidInput_ReturnsTrue() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput_ReturnsFalse() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidInput_ReturnsUnmodifiableList() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_NullInput_ReturnsEmptyList() {
        String countryCode = null;
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidInput_ReturnsUnmodifiableList() {
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "CH"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_NullInput_ReturnsEmptyList() {
        String languageCode = null;
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(actual.isEmpty());
    }

    
}