import org.junit.Test;
import org.junit.Assert.*;
import static org.apache.commons.lang3.LocaleUtils.*;
import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        String validLocale = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = toLocale(validLocale);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocaleInvalidFormatEmpty() {
        String invalidLocale = "";
        assertNull(toLocale(invalidLocale));
    }

    @Test
    public void testToLocaleInvalidFormatNull() {
        String invalidLocale = null;
        assertNull(toLocale(invalidLocale));
    }

    @Test
    public void testToLocaleInvalidFormatContainsHash() {
        String invalidLocale = "en_US#";
        assertThrows(IllegalArgumentException.class, () -> toLocale(invalidLocale));
    }

    // Add test cases for other methods and their edge cases

    @Test
    public void testLocaleLookupList() {
        // Add test cases for localeLookupList method
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        // Add test cases for localeLookupList method with a defaultLocale
    }

    // Add test cases for availableLocaleList, availableLocaleSet, isAvailableLocale, languagesByCountry, and countriesByLanguage

    @Test
    public void testIsAvailableLocaleTrue() {
        String validLocale = "en_US";
        assertTrue(isAvailableLocale(new Locale("en", "US")));
    }

    @Test
    public void testIsAvailableLocaleFalse() {
        String invalidLocale = "InvalidLocale";
        assertFalse(isAvailableLocale(new Locale("InvalidLocale", "")));
    }

    // Add test cases for other methods and their edge cases

}