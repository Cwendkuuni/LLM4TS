import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.SyncAvoid;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    @Test
    public void toLocale_NullInput_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void toLocale_EmptyStringInput_ReturnsEmptyLocale() {
        Locale expected = new Locale("");
        Locale actual = LocaleUtils.toLocale("");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidLocaleFormat_ThrowsException() {
        LocaleUtils.toLocale("invalidFormat");
    }

    @Test
    public void toLocale_ValidLocaleFormat_ReturnsNewLocaleObject() {
        Locale expected = new Locale("en_US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_InvalidCountryCodeInLocaleFormat_ThrowsException() {
        LocaleUtils.toLocale("en__US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_EmptyLanguageCodeInLocaleFormat_ThrowsException() {
        LocaleUtils.toLocale("__US");
    }

    @Test
    public void toLocale_ValidLocaleFormatWithVariants_ReturnsNewLocaleObjectWithVariant() {
        Locale expected = new Locale("en_US-en-US");
        Locale actual = LocaleUtils.toLocale("en_US-en-US");
        assertEquals(expected, actual);
    }

    @Test
    public void localeLookupList_NullLocaleInput_ReturnsEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }

    @Test
    public void localeLookupList_ValidLocaleInput_ReturnsListWithLocaleAndItsVariants() {
        Locale locale = new Locale("en", "US");
        Locale expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", "US-en"), locale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void localeLookupList_NullLocaleAndDefaultLocaleInput_ReturnsListWithDefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void availableLocaleList_ReturnsUnmodifiableListOfAvailableLocales() {
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void availableLocaleSet_ReturnsUnmodifiableSetOfAvailableLocales() {
        Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void isAvailableLocale_ValidLocaleInput_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void isAvailableLocale_InvalidLocaleInput_ReturnsFalse() {
        Locale locale = new Locale("invalidLocale");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void languagesByCountry_NullCountryCodeInput_ReturnsEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, actual);
    }

    @Test
    public void languagesByCountry_ValidCountryCodeInput_ReturnsListWithLocalesForTheGivenCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es", "ES"));
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, actual);
    }

    @Test
    public void countriesByLanguage_NullLanguageCodeInput_ReturnsEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, actual);
    }

    @Test
    public void countriesByLanguage_ValidLanguageCodeInput_ReturnsListWithCountriesForTheGivenLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }
}