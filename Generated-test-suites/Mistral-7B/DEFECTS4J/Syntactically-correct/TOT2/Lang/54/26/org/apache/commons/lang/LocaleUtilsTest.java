import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB_London"));
        
        // Edge cases
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB_"));
        
        // Exception handling
        try {
            LocaleUtils.toLocale("en_GB_XXX"); // Invalid variant
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            LocaleUtils.toLocale("en_XX"); // Invalid country
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            LocaleUtils.toLocale("es_ES_12345"); // Invalid separator
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_aBc"); // Invalid language
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.FRENCH); // fr_CA_xxx
        expectedList.add(Locale.FRENCH); // fr_CA
        expectedList.add(Locale.FRENCH); // fr
        expectedList.add(Locale.ENGLISH); // en

        List<Locale> actualList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        
        assertEquals(expectedList.size(), actualList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertSame(expectedList.get(i), actualList.get(i));
        }

        // Edge cases
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> defaultOnlyList = Collections.singletonList(Locale.ENGLISH);
        List<Locale> localeOnlyList = Collections.singletonList(Locale.FRENCH);

        List<Locale> emptyLocaleList = LocaleUtils.localeLookupList(null);
        assertEquals(emptyList, emptyLocaleList);

        List<Locale> defaultLocaleList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertSame(defaultOnlyList, defaultLocaleList);

        List<Locale> localeLocaleList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.FRENCH);
        assertSame(localeOnlyList, localeLocaleList);

        // Exception handling
        List<Locale> localeWithVariantLocaleList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH, new Locale("fr", "CA", "xxx"));
        assertEquals(expectedList, localeWithVariantLocaleList);

        try {
            LocaleUtils.localeLookupList(null, Locale.ENGLISH);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(Locale.ENGLISH, null);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            LocaleUtils.localeLookupList(null, null);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedList = new ArrayList<>();
        expectedList.addAll(availableLocaleList);

        // Edge cases
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> expectedEmptyList = new ArrayList<>();

        List<Locale> filteredList = new ArrayList<>();
        LocaleUtils.localeLookupList(Locale.ENGLISH, filteredList);
        List<Locale> expectedFilteredList = new ArrayList<>();
        expectedFilteredList.addAll(filteredList);

        // Comparison
        assertEquals(expectedList.size(), availableLocaleList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertSame(expectedList.get(i), availableLocaleList.get(i));
        }

        assertEquals(expectedEmptyList.size(), Collections.emptyList().size());
        assertEquals(expectedEmptyList, Collections.emptyList());

        assertEquals(expectedFilteredList.size(), filteredList.size());
        for (int i = 0; i < expectedFilteredList.size(); i++) {
            assertSame(expectedFilteredList.get(i), filteredList.get(i));
        }
        
        // Exception handling
        List<Locale> localeWithVariantLocaleList = new ArrayList<>();
        localeWithVariantLocaleList.add(new Locale("fr", "CA", "xxx"));
        try {
            LocaleUtils.availableLocaleList(localeWithVariantLocaleList);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();

        // Testing true cases
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));

        // Testing false cases
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "ES", "Madrid")));

        // Testing edge cases
        List<Locale> localeList = new ArrayList<>();
        localeList.add(Locale.ENGLISH);
        localeList.addAll(availableLocaleList);

        for (Locale locale : localeList) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.FRENCH);
        expectedList.add(Locale.ENGLISH);

        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        
        assertEquals(expectedList.size(), actualList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertSame(expectedList.get(i), actualList.get(i));
        }

        // Edge cases
        List<Locale> emptyList = Collections.emptyList();

        // Comparison
        assertEquals(expectedList.size(), LocaleUtils.languagesByCountry("CA").size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertSame(expectedList.get(i), LocaleUtils.languagesByCountry("CA").get(i));
        }

        assertEquals(emptyList.size(), LocaleUtils.languagesByCountry(null).size());
        assertEquals(emptyList, LocaleUtils.languagesByCountry(null));

        // Exception handling
        try {
            LocaleUtils.languagesByCountry("ES");
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.UK);
        expectedList.add(Locale.US);

        List<Locale> actualList = LocaleUtils.countriesByLanguage("EN");
        
        assertEquals(expectedList.size(), actualList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertSame(expectedList.get(i), actualList.get(i));
        }

        // Edge cases
        List<Locale> emptyList = Collections.emptyList();

        // Comparison
        assertEquals(expectedList.size(), LocaleUtils.countriesByLanguage("EN").size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertSame(expectedList.get(i), LocaleUtils.countriesByLanguage("EN").get(i));
        }

        assertEquals(emptyList.size(), LocaleUtils.countriesByLanguage(null).size());
        assertEquals(emptyList, LocaleUtils.countriesByLanguage(null));

        // Exception handling
        try {
            LocaleUtils.countriesByLanguage("ES");
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}