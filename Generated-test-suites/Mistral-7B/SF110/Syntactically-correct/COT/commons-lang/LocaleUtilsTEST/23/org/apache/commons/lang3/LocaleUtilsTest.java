import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang3.LocaleUtils;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidFormat() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_NullInput() {
        LocaleUtils.toLocale(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        String invalidLocale = "en#US";
        assertFalse(LocaleUtils.isAvailableLocale(new Locale(invalidLocale)));
        try {
            LocaleUtils.toLocale(invalidLocale);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_SinglePart() {
        Locale expected = new Locale("fr");
        Locale actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_TwoParts() {
        Locale expected = new Locale("fr_CA");
        Locale actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_ThreeParts() {
        Locale expected = new Locale("fr_CA_QC");
        Locale actual = LocaleUtils.toLocale("fr_CA_QC");
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(locale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyCountry() {
        Locale locale = new Locale("en", "");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), new Locale("en", "ZA"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en", "ZA"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyLanguage() {
        Locale locale = new Locale("", "US");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("fr", "CA"), new Locale("en", "ZA"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("fr", "CA"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<Locale>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("", "");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidCountry() {
        String countryCode = "US";
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountry() {
        String countryCode = "";
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguage() {
        String languageCode = "en";
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "CA"));
        expected.add(new Locale("en", "GB"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguage() {
        String languageCode = "";
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(actual.isEmpty());
    }
}