package org.apache.commons.lang3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 private static final ConcurrentHashMap<String, List<Locale>> testCLanguagesByCountry = new ConcurrentHashMap<>();
 private static final ConcurrentHashMap<String, List<Locale>> testCCountriesByLanguage = new ConcurrentHashMap<>();

 @Test
 public void testToLocale() {
 // Test empty string
 assertNull(LocaleUtils.toLocale(""));
 // Test null input
 assertNull(LocaleUtils.toLocale(null));
 // Test invalid locale format (no language or country)
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
 // Test valid locale format (language only)
 assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
 // Test valid locale format (language and country)
 assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
 // Test valid locale format (language, country, and variant)
 assertEquals(new Locale("en", "US", ""), LocaleUtils.toLocale("en_US_"));
 // Test invalid locale format (missing country or language)
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_US"));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
 // Test invalid locale format (missing or incorrect separator)
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en#US"));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US#"));
 // Test invalid locale format (empty language or country)
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(""));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
 }

 @Test
 public void testLocaleLookupList() {
 // Test null input
 assertNotNull(LocaleUtils.localeLookupList(null));
 assertEquals(0, LocaleUtils.localeLookupList(null).size());
 // Test empty locale
 assertNotNull(LocaleUtils.localeLookupList(new Locale("")));
 assertEquals(0, LocaleUtils.localeLookupList(new Locale("")).size());
 // Test invalid locale format (missing language or country)
 assertNotNull(LocaleUtils.localeLookupList(new Locale("_")));
 assertEquals(0, LocaleUtils.localeLookupList(new Locale("_")).size());
 assertNotNull(LocaleUtils.localeLookupList(new Locale("", "_")));
 assertEquals(0, LocaleUtils.localeLookupList(new Locale("", "_")).size());
 // Test valid locale format (language and country)
 assertEquals(2, LocaleUtils.localeLookupList(new Locale("en", "US")).size());
 // Test valid locale format (language, country, and variant)
 assertEquals(3, LocaleUtils.localeLookupList(new Locale("en", "US", "")).size());
 // Test locale with default locale
 assertEquals(4, LocaleUtils.localeLookupList(new Locale("en", "US"), new Locale("fr", "CA")).size());
 }

 @Test
 public void testAvailableLocaleList() {
 // Test empty list
 assertNotNull(LocaleUtils.availableLocaleList());
 assertEquals(0, LocaleUtils.availableLocaleList().size());
 // Test filled list
 testCLanguagesByCountry.put("US", new CopyOnWriteArrayList<>(Arrays.asList(new Locale("en", "US"), new Locale("es", "US"))));
 testCCountriesByLanguage.put("en", new CopyOnWriteArrayList<>(Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"))));
 assertEquals(3, LocaleUtils.availableLocaleList().size());
 }

 @Test
 public void testIsAvailableLocale() {
 // Test null input
 assertFalse(LocaleUtils.isAvailableLocale(null));
 // Test empty locale
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("")));
 // Test invalid locale format (missing language or country)
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("_")));
 // Test valid locale format (language and country)
 assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
 // Test valid locale format (language, country, and variant)
 assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US", "")));
 // Test invalid locale format (empty language or country)
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
 }

 @Test
 public void testLanguagesByCountry() {
 // Test null input
 assertNotNull(LocaleUtils.languagesByCountry(null));
 assertEquals(0, LocaleUtils.languagesByCountry(null).size());
 // Test empty country
 assertNotNull(LocaleUtils.languagesByCountry(""));
 assertEquals(0, LocaleUtils.languagesByCountry("").size());
 // Test valid country
 assertEquals(2, LocaleUtils.languagesByCountry("US").size());
 // Test country with default locale
 assertEquals(3, LocaleUtils.languagesByCountry("US", new Locale("fr", "CA")).size());
 }

 @Test
 public void testCountriesByLanguage() {
 // Test null input
 assertNotNull(LocaleUtils.countriesByLanguage(null));
 assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
 // Test empty language
 assertNotNull(LocaleUtils.countriesByLanguage(""));
 assertEquals(0, LocaleUtils.countriesByLanguage("").size());
 // Test valid language
 assertEquals(2, LocaleUtils.countriesByLanguage("en").size());
 // Test language with default locale
 assertEquals(3, LocaleUtils.countriesByLanguage("en", new Locale("fr", "CA")).size());
 }

}