package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 private static final ConcurrentMap<String, List<Locale>> testLocaleUtilsMap = new ConcurrentHashMap<>();

 static {
 testLocaleUtilsMap.putIfAbsent("en", Collections.singletonList(Locale.ENGLISH));
 testLocaleUtilsMap.putIfAbsent("fr", Collections.singletonList(Locale.FRENCH));
 testLocaleUtilsMap.putIfAbsent("es", Collections.singletonList(Locale.SPANISH));
 testLocaleUtilsMap.putIfAbsent("de", Collections.singletonList(Locale.GERMAN));
 testLocaleUtilsMap.putIfAbsent("it", Collections.singletonList(Locale.ITALIAN));
 }

 @Test
 public void testToLocale() {
 // Test valid locales
 List<Locale> expectedValidLocales = testLocaleUtilsMap.get("en");
 List<Locale> actualValidLocales = LocaleUtils.availableLocaleList();
 assertEquals(expectedValidLocales, actualValidLocales);

 expectedValidLocales = testLocaleUtilsMap.get("fr");
 actualValidLocales = LocaleUtils.availableLocaleList();
 assertEquals(expectedValidLocales, actualValidLocales);

 expectedValidLocales = testLocaleUtilsMap.get("es");
 actualValidLocales = LocaleUtils.availableLocaleList();
 assertEquals(expectedValidLocales, actualValidLocales);

 expectedValidLocales = testLocaleUtilsMap.get("de");
 actualValidLocales = LocaleUtils.availableLocaleList();
 assertEquals(expectedValidLocales, actualValidLocales);

 expectedValidLocales = testLocaleUtilsMap.get("it");
 actualValidLocales = LocaleUtils.availableLocaleList();
 assertEquals(expectedValidLocales, actualValidLocales);

 // Test invalid locale with empty string
 assertNull(LocaleUtils.toLocale(""));

 // Test invalid locale with no language or country
 assertNull(LocaleUtils.toLocale("_"));

 // Test invalid locale with two characters for language and country
 assertNull(LocaleUtils.toLocale("en"));

 // Test invalid locale with three characters for language and empty country
 assertNull(LocaleUtils.toLocale("en_"));

 // Test invalid locale with three characters for language, country, and variant
 assertNull(LocaleUtils.toLocale("en_US#"));

 // Test valid locale with two characters for language and country
 Locale expectedLocale1 = new Locale("fr", "CA");
 Locale actualLocale1 = LocaleUtils.toLocale("fr_CA");
 assertEquals(expectedLocale1, actualLocale1);

 // Test valid locale with three characters for language, country, and empty variant
 Locale expectedLocale2 = new Locale("es", "ES");
 Locale actualLocale2 = LocaleUtils.toLocale("es_ES");
 assertEquals(expectedLocale2, actualLocale2);

 // Test valid locale with two characters for language, empty country, and variant
 Locale expectedLocale3 = new Locale("es", "es_ES");
 Locale actualLocale3 = LocaleUtils.toLocale("es_es_ES");
 assertEquals(expectedLocale3, actualLocale3);

 // Test valid locale with three characters for language, country, and variant
 Locale expectedLocale4 = new Locale("es", "ES", "Castilian");
 Locale actualLocale4 = LocaleUtils.toLocale("es_ES_Castilian");
 assertEquals(expectedLocale4, actualLocale4);
 }

 @Test
 public void testLocaleLookupList() {
 // Test null locale
 assertEmpty(LocaleUtils.localeLookupList(null));

 // Test valid locale with no variant
 Locale locale = new Locale("fr", "CA");
 List<Locale> expectedLocales = new ArrayList<>();
 expectedLocales.add(locale);
 expectedLocales.add(new Locale("fr", ""));
 List<Locale> actualLocales = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedLocales, actualLocales);

 // Test valid locale with variant
 locale = new Locale("fr", "CA", "Quebec");
 expectedLocales.clear();
 expectedLocales.add(locale);
 expectedLocales.add(new Locale("fr", "CA"));
 expectedLocales.add(new Locale("fr", ""));
 actualLocales = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedLocales, actualLocales);

 // Test null defaultLocale
 assertEmpty(LocaleUtils.localeLookupList(locale, null));

 // Test valid locale with no variant and valid defaultLocale
 locale = new Locale("en", "US");
 expectedLocales.clear();
 expectedLocales.add(locale);
 expectedLocales.add(new Locale("en", "US"));
 expectedLocales.add(new Locale("en", ""));
 expectedLocales.add(locale);
 actualLocales = LocaleUtils.localeLookupList(locale, new Locale("fr", "CA"));
 assertEquals(expectedLocales, actualLocales);

 // Test valid locale with variant and valid defaultLocale
 locale = new Locale("fr", "CA", "Quebec");
 expectedLocales.clear();
 expectedLocales.add(locale);
 expectedLocales.add(new Locale("fr", "CA"));
 expectedLocales.add(new Locale("fr", "CA", "Quebec"));
 expectedLocales.add(new Locale("fr", ""));
 expectedLocales.add(new Locale("en", "US"));
 actualLocales = LocaleUtils.localeLookupList(locale, new Locale("en", "US"));
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testAvailableLocaleList() {
 // Test empty list
 assertEmpty(LocaleUtils.availableLocaleList());

 // Test available locales
 List<Locale> expectedLocales = testLocaleUtilsMap.get("en");
 List<Locale> actualLocales = LocaleUtils.availableLocaleList();
 assertEquals(expectedLocales, actualLocales);

 // Test available locales after adding test locale utils map
 testLocaleUtilsMap.putIfAbsent("ru", Collections.singletonList(new Locale("ru", "RU")));
 expectedLocales = testLocaleUtilsMap.get("ru");
 actualLocales = LocaleUtils.availableLocaleList();
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testAvailableLocaleSet() {
 // Test empty set
 assertTrue(LocaleUtils.availableLocaleSet().isEmpty());

 // Test available locales
 List<Locale> expectedLocales = testLocaleUtilsMap.get("en");
 Set<Locale> actualLocales = LocaleUtils.availableLocaleSet();
 assertEquals(expectedLocales, new HashSet<>(expectedLocales));

 // Test available locales after adding test locale utils map
 testLocaleUtilsMap.putIfAbsent("ru", Collections.singletonList(new Locale("ru", "RU")));
 expectedLocales = testLocaleUtilsMap.get("ru");
 actualLocales = LocaleUtils.availableLocaleSet();
 assertEquals(expectedLocales, new HashSet<>(expectedLocales));
 }

 @Test
 public void testIsAvailableLocale() {
 // Test null locale
 assertFalse(LocaleUtils.isAvailableLocale(null));

 // Test available locales
 List<Locale> expectedLocales = testLocaleUtilsMap.get("en");
 for (Locale locale : expectedLocales) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 // Test unavailable locale
 Locale unavailableLocale = new Locale("xx", "XX");
 assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
 }

 @Test
 public void testLanguagesByCountry() {
 // Test null countryCode
 assertEmpty(LocaleUtils.languagesByCountry(null));

 // Test empty map
 testLocaleUtilsMap.clear();
 List<Locale> expectedLocales = Collections.emptyList();
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(null);
 assertEquals(expectedLocales, actualLocales);

 // Test available locales
 testLocaleUtilsMap.putIfAbsent("en", Collections.singletonList(Locale.ENGLISH));
 testLocaleUtilsMap.putIfAbsent("fr", Collections.singletonList(Locale.FRENCH));
 testLocaleUtilsMap.putIfAbsent("es", Collections.singletonList(Locale.SPANISH));
 testLocaleUtilsMap.putIfAbsent("de", Collections.singletonList(Locale.GERMAN));
 testLocaleUtilsMap.putIfAbsent("it", Collections.singletonList(Locale.ITALIAN));
 testLocaleUtilsMap.putIfAbsent("ru", Collections.singletonList(new Locale("ru", "RU")));
 List<Locale> englishLocales = testLocaleUtilsMap.get("en");
 expectedLocales = new ArrayList<>();
 expectedLocales.addAll(englishLocales);
 expectedLocales.addAll(LocaleUtils.languagesByCountry("US"));
 List<Locale> actualLocales = LocaleUtils.languagesByCountry("US");
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testCountriesByLanguage() {
 // Test null languageCode
 assertEmpty(LocaleUtils.countriesByLanguage(null));

 // Test empty map
 testLocaleUtilsMap.clear();
 List<Locale> expectedLocales = Collections.emptyList();
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(null);
 assertEquals(expectedLocales, actualLocales);

 // Test available locales
 testLocaleUtilsMap.putIfAbsent("en", Collections.singletonList(Locale.ENGLISH));
 testLocaleUtilsMap.putIfAbsent("fr", Collections.singletonList(Locale.FRENCH));
 testLocaleUtilsMap.putIfAbsent("es", Collections.singletonList(Locale.SPANISH));
 testLocaleUtilsMap.putIfAbsent("de", Collections.singletonList(Locale.GERMAN));
 testLocaleUtilsMap.putIfAbsent("it", Collections.singletonList(Locale.ITALIAN));
 testLocaleUtilsMap.putIfAbsent("ru", Collections.singletonList(new Locale("ru", "RU")));
 List<Locale> englishLocale = testLocaleUtilsMap.get("en");
 List<Locale> frenchLocales = testLocaleUtilsMap.get("fr");
 expectedLocales = new ArrayList<>();
 expectedLocales.addAll(LocaleUtils.countriesByLanguage("en"));
 expectedLocales.add(englishLocale.get(0));
 expectedLocales.addAll(LocaleUtils.countriesByLanguage("fr"));
 expectedLocales.add(LocaleUtils.countriesByLanguage("FR"));
 expectedLocales.addAll(frenchLocales);
 List<Locale> actualLocales = new ArrayList<>();
 actualLocales.addAll(LocaleUtils.countriesByLanguage("en"));
 actualLocales.addAll(LocaleUtils.countriesByLanguage("fr"));
 actualLocales.addAll(LocaleUtils.countriesByLanguage("FR"));
 actualLocales.addAll(LocaleUtils.countriesByLanguage("ES"));
 assertEquals(expectedLocales, actualLocales);
 }
}