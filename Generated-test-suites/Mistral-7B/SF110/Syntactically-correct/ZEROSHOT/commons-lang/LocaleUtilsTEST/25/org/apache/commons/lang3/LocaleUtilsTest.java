package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
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
 // Test invalid locale format with no language or country
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
 // Test valid locale format with two characters
 assertNotNull(LocaleUtils.toLocale("en"));
 assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
 assertEquals("", LocaleUtils.toLocale("en").getCountry());
 // Test valid locale format with three characters
 assertNotNull(LocaleUtils.toLocale("en_US"));
 assertEquals("en", LocaleUtils.toLocale("en_US").getLanguage());
 assertEquals("US", LocaleUtils.toLocale("en_US").getCountry());
 // Test valid locale format with language, country and variant
 assertNotNull(LocaleUtils.toLocale("en_US_POSIX"));
 assertEquals("en", LocaleUtils.toLocale("en_US_POSIX").getLanguage());
 assertEquals("US", LocaleUtils.toLocale("en_US_POSIX").getCountry());
 assertEquals("POSIX", LocaleUtils.toLocale("en_US_POSIX").getVariant());
 // Test invalid locale format with '#' character
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en#"));
 // Test invalid locale format with language and empty country
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
 // Test invalid locale format with country and empty language
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_FR"));
 // Test invalid locale format with language, country and empty variant
 assertNotNull(LocaleUtils.toLocale("en_FR"));
 assertEquals("en", LocaleUtils.toLocale("en_FR").getLanguage());
 assertEquals("FR", LocaleUtils.toLocale("en_FR").getCountry());
 assertEquals("", LocaleUtils.toLocale("en_FR").getVariant());
 // Test invalid locale format with language, country and variant with length < 3
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_FR_a"));
 // Test invalid locale format with country, language and variant with length < 3
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("FR_en_a"));
 // Test invalid locale format with incorrect order of language and country
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("FR_en"));
 // Test invalid locale format with incorrect order of language, country and variant
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("FR_en_POSIX"));
 // Test invalid locale format with language and country containing '#' character
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_FR#"));
 // Test invalid locale format with language, country and variant containing '#' character
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_FR_POSIX#"));
 // Test available locales
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 assertEquals(221, availableLocaleList.size());
 for (Locale locale : availableLocaleList) {
 LocaleUtils.toLocale(locale.getDisplayName());
 }
 }

 @Test
 public void testLocaleLookupList() {
 // Test null input
 assertNotNull(LocaleUtils.localeLookupList(null));
 assertEquals(0, LocaleUtils.localeLookupList(null).size());
 // Test empty locale
 assertNotNull(LocaleUtils.localeLookupList(new Locale("", "")));
 assertEquals(1, LocaleUtils.localeLookupList(new Locale("", "")).size());
 // Test valid locale
 Locale validLocale = new Locale("en", "US");
 List<Locale> localeList = LocaleUtils.localeLookupList(validLocale);
 assertEquals(3, localeList.size());
 assertEquals(validLocale, localeList.get(0));
 // Test valid locale with variant
 Locale variantLocale = new Locale("en", "US", "POSIX");
 localeList = LocaleUtils.localeLookupList(variantLocale, validLocale);
 assertEquals(4, localeList.size());
 assertEquals(validLocale, localeList.get(0));
 assertEquals(new Locale("en", "US"), localeList.get(1));
 assertEquals(new Locale("en", ""), localeList.get(2));
 assertEquals(validLocale, localeList.get(3));
 }

 @Test
 public void testLanguagesByCountry() {
 // Test null input
 assertEmpty(LocaleUtils.languagesByCountry(null));
 // Test empty string
 assertEmpty(LocaleUtils.languagesByCountry(""));
 // Test invalid country code
 assertEmpty(LocaleUtils.languagesByCountry("XX"));
 // Test valid country code
 List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
 assertEquals(12, englishLocales.size());
 for (Locale locale : englishLocales) {
 assertFalse(locale.getCountry().equals("US"));
 }
 // Test available locales
 List<Locale> allLocales = LocaleUtils.availableLocaleList();
 List<Locale> allLanguages = new ArrayList<>();
 for (Locale locale : allLocales) {
 allLanguages.add(locale.getLanguage());
 }
 List<Locale> allLanguageLocales = Collections.nCopies(allLanguages.size(), new Locale("", ""));
 for (int i = 0; i < allLanguages.size(); i++) {
 List<Locale> languageLocales = LocaleUtils.languagesByCountry(allLanguageLocales.get(i).getDisplayName());
 assertEquals(allLanguages.get(i).length(), languageLocales.size());
 for (Locale locale : languageLocales) {
 assertEquals(allLanguages.get(i), locale.getLanguage());
 }
 }
 }

 @Test
 public void testCountriesByLanguage() {
 // Test null input
 assertEmpty(LocaleUtils.countriesByLanguage(null));
 // Test empty string
 assertEmpty(LocaleUtils.countriesByLanguage(""));
 // Test invalid language code
 assertEmpty(LocaleUtils.countriesByLanguage("XX"));
 // Test valid language code
 List<Locale> frenchLocales = LocaleUtils.countriesByLanguage("fr");
 assertEquals(26, frenchLocales.size());
 for (Locale locale : frenchLocales) {
 assertFalse(locale.getLanguage().equals("fr"));
 }
 // Test available locales
 List<Locale> allLocales = LocaleUtils.availableLocaleList();
 List<Locale> allCountries = new ArrayList<>();
 for (Locale locale : allLocales) {
 allCountries.add(locale.getCountry());
 }
 List<Locale> allCountryLocales = Collections.nCopies(allCountries.size(), new Locale("", ""));
 for (int i = 0; i < allCountries.size(); i++) {
 List<Locale> countryLocales = LocaleUtils.countriesByLanguage(allCountryLocales.get(i).getDisplayName());
 assertEquals(allCountries.get(i).length(), countryLocales.size());
 for (Locale locale : countryLocales) {
 assertEquals(allCountries.get(i), locale.getCountry());
 }
 }
 }

 @Test
 public void testIsAvailableLocale() {
 // Test null input
 assertFalse(LocaleUtils.isAvailableLocale(null));
 // Test empty string
 assertFalse(LocaleUtils.isAvailableLocale(""));
 // Test invalid locale format
 assertFalse(LocaleUtils.isAvailableLocale("en_US#"));
 // Test valid locale format
 assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
 assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US", "POSIX")));
 }

 @Test(expected = AssertionError.class)
 public void testStaticInitializationOrderFailure() {
 // This test should fail as the static initialization order is not correct
 }

 static {
 testCLanguagesByCountry.putAll(LocaleUtils.cLanguagesByCountry);
 testCCountriesByLanguage.putAll(LocaleUtils.cCountriesByLanguage);
 }

}