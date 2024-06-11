package org.apache.commons.lang3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<>();
 private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<>();

 static {
 cLanguagesByCountry.putAll(LocaleUtils.cLanguagesByCountry);
 cCountriesByLanguage.putAll(LocaleUtils.cCountriesByLanguage);
 }

 @Test
 public void testToLocale() {
 // Test empty string
 assertNull(LocaleUtils.toLocale(""));

 // Test null input
 assertNull(LocaleUtils.toLocale(null));

 // Test valid locale with language and country
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));
 List<Locale> actualLocales = Arrays.asList(LocaleUtils.toLocale("en_US"), LocaleUtils.toLocale("en_GB"), LocaleUtils.toLocale("en_CA"));
 assertEquals(expectedLocales, actualLocales);

 // Test valid locale with language, country, and variant
 List<Locale> expectedLocalesWithVariant = Arrays.asList(new Locale("en", "US", ""), new Locale("en", "GB", ""), new Locale("en", "CA", ""));
 List<Locale> actualLocalesWithVariant = Arrays.asList(LocaleUtils.toLocale("en_US_"), LocaleUtils.toLocale("en_GB_"), LocaleUtils.toLocale("en_CA_"));
 assertEquals(expectedLocalesWithVariant, actualLocalesWithVariant);

 // Test invalid locale formats
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(""));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(null));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("invalid_locale"));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_invalid_locale"));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("invalid_locale_with_variant"));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_invalid_locale_with_variant"));
 }

 @Test
 public void testLocaleLookupList() {
 // Test null input
 assertEmpty(LocaleUtils.localeLookupList(null));

 // Test empty locale
 assertEmpty(LocaleUtils.localeLookupList(new Locale("")));

 // Test valid locale
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", ""), new Locale("defaultLocale"));
 List<Locale> actualLocales = LocaleUtils.localeLookupList(new Locale("en_US"));
 assertEquals(expectedLocales, actualLocales);

 // Test locale with variant
 List<Locale> expectedLocalesWithVariant = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", ""), new Locale("defaultLocale"), new Locale("en", "US"));
 List<Locale> actualLocalesWithVariant = LocaleUtils.localeLookupList(new Locale("en_US_"));
 assertEquals(expectedLocalesWithVariant, actualLocalesWithVariant);
 }

 @Test
 public void testAvailableLocaleList() {
 // Test empty list
 assertEmpty(LocaleUtils.availableLocaleList());

 // Test available locales
 List<Locale> expectedLocales = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH);
 List<Locale> actualLocales = (List<Locale>) LocaleUtils.availableLocaleList();
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testAvailableLocaleSet() {
 // Test empty set
 assertEmpty(LocaleUtils.availableLocaleSet());

 // Test available locales
 Set<Locale> expectedLocales = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH).stream().collect(Collectors.toSet());
 Set<Locale> actualLocales = LocaleUtils.availableLocaleSet();
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testIsAvailableLocale() {
 // Test null input
 assertFalse(LocaleUtils.isAvailableLocale(null));

 // Test invalid locale
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid_locale")));

 // Test available locale
 assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
 }

 @Test
 public void testLanguagesByCountry() {
 // Test null input
 assertEmpty(LocaleUtils.languagesByCountry(null));

 // Test empty country
 assertEmpty(LocaleUtils.languagesByCountry(""));

 // Test valid country
 List<Locale> expectedLocales = Arrays.asList(Locale.ENGLISH, Locale.GERMAN, Locale.FRENCH, Locale.ITALIAN);
 List<Locale> actualLocales = LocaleUtils.languagesByCountry("DE");
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testCountriesByLanguage() {
 // Test null input
 assertEmpty(LocaleUtils.countriesByLanguage(null));

 // Test empty language
 assertEmpty(LocaleUtils.countriesByLanguage(""));

 // Test valid language
 List<Locale> expectedLocales = Arrays.asList(Locale.GERMAN, Locale.GERMANY, Locale.AUSTRIA, Locale.SWITZERLAND, Locale.LIECHTENSTEIN);
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage("de");
 assertEquals(expectedLocales, actualLocales);
 }
}