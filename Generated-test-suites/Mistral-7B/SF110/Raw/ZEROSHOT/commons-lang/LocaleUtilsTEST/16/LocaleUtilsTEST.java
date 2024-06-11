package org.apache.commons.lang3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 private ConcurrentHashMap<String, List<Locale>> cLanguagesByCountrySpy;
 private ConcurrentHashMap<String, List<Locale>> cCountriesByLanguageSpy;

 @Test
 public void testToLocale() {
// Test valid locales
 Locale expectedLocale1 = new Locale("en", "US");
 String localeString1 = "en_US";
 Locale actualLocale1 = LocaleUtils.toLocale(localeString1);
 assertEquals(expectedLocale1, actualLocale1);

 Locale expectedLocale2 = new Locale("fr_FR");
 String localeString2 = "fr_FR";
 Locale actualLocale2 = LocaleUtils.toLocale(localeString2);
 assertEquals(expectedLocale2, actualLocale2);

 Locale expectedLocale3 = new Locale("en");
 String localeString3 = "en";
 Locale actualLocale3 = LocaleUtils.toLocale(localeString3);
 assertEquals(expectedLocale3, actualLocale3);

// Test invalid locales
 String invalidLocaleString1 = "en_";
 assertNull(LocaleUtils.toLocale(invalidLocaleString1));

 String invalidLocaleString2 = "en_US#";
 assertNull(LocaleUtils.toLocale(invalidLocaleString2));

 String invalidLocaleString3 = "en_US__";
 assertNull(LocaleUtils.toLocale(invalidLocaleString3));

 String invalidLocaleString4 = "";
 assertNull(LocaleUtils.toLocale(invalidLocaleString4));
 }

 @Test
 public void testLocaleLookupList() {
// Test with null locale
 List<Locale> expectedLocaleList1 = Collections.emptyList();
 Locale localeNull = null;
 List<Locale> actualLocaleList1 = LocaleUtils.localeLookupList(localeNull);
 assertEquals(expectedLocaleList1, actualLocaleList1);

// Test with null defaultLocale
 List<Locale> expectedLocaleList2 = Arrays.asList(Locale.US, Locale.ENGLISH);
 Locale localeDefaultNull = null;
 List<Locale> actualLocaleList2 = LocaleUtils.localeLookupList(Locale.GERMANY, localeDefaultNull);
 assertEquals(expectedLocaleList2, actualLocaleList2);

// Test with valid locale
 List<Locale> expectedLocaleList3 = Arrays.asList(Locale.GERMANY, new Locale("de_DE"));
 Locale localeValid = Locale.GERMANY;
 List<Locale> actualLocaleList3 = LocaleUtils.localeLookupList(localeValid);
 assertEquals(expectedLocaleList3, actualLocaleList3);
 }

 @Test
 public void testAvailableLocaleList() {
// Test with empty concurrent maps
 ConcurrentHashMap<String, List<Locale>> spies = new ConcurrentHashMap<>();
 spies.put(LocaleUtils.class.getName() + "_cLanguagesByCountry", cLanguagesByCountrySpy = new ConcurrentHashMap<>());
 spies.put(LocaleUtils.class.getName() + "_cCountriesByLanguage", cCountriesByLanguageSpy = new ConcurrentHashMap<>());

 List<Locale> expectedLocaleList = Collections.emptyList();
 List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
 assertEquals(expectedLocaleList, actualLocaleList);

// Test with populated concurrent maps
 List<Locale> localeList = Arrays.asList(Locale.ENGLISH, Locale.GERMANY, Locale.FRENCH);
 cLanguagesByCountrySpy.put("US", localeList);
 cCountriesByLanguageSpy.put("en", localeList);

 expectedLocaleList = localeList;
 actualLocaleList = LocaleUtils.availableLocaleList();
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 @Test
 public void testAvailableLocaleSet() {
// Test with empty concurrent maps
 ConcurrentHashMap<String, List<Locale>> spies = new ConcurrentHashMap<>();
 spies.put(LocaleUtils.class.getName() + "_cLanguagesByCountry", cLanguagesByCountrySpy = new ConcurrentHashMap<>());
 spies.put(LocaleUtils.class.getName() + "_cCountriesByLanguage", cCountriesByLanguageSpy = new ConcurrentHashMap<>());

 Set<Locale> expectedLocaleSet = Collections.emptySet();
 Set<Locale> actualLocaleSet = LocaleUtils.availableLocaleSet();
 assertEquals(expectedLocaleSet, actualLocaleSet);

// Test with populated concurrent maps
 List<Locale> localeList = Arrays.asList(Locale.ENGLISH, Locale.GERMANY, Locale.FRENCH);
 cLanguagesByCountrySpy.put("US", localeList);
 cCountriesByLanguageSpy.put("en", localeList);

 expectedLocaleSet = Collections.unmodifiableSet(new HashSet<>(localeList));
 actualLocaleSet = LocaleUtils.availableLocaleSet();
 assertEquals(expectedLocaleSet, actualLocaleSet);
 }

 @Test
 public void testIsAvailableLocale() {
// Test with empty concurrent maps
 ConcurrentHashMap<String, List<Locale>> spies = new ConcurrentHashMap<>();
 spies.put(LocaleUtils.class.getName() + "_cLanguagesByCountry", cLanguagesByCountrySpy = new ConcurrentHashMap<>());
 spies.put(LocaleUtils.class.getName() + "_cCountriesByLanguage", cCountriesByLanguageSpy = new ConcurrentHashMap<>());

 Locale localeEmpty = new Locale("", "");
 boolean expectedResult = false;
 boolean actualResult = LocaleUtils.isAvailableLocale(localeEmpty);
 assertEquals(expectedResult, actualResult);

// Test with populated concurrent maps and existing locale
 Locale localeExisting = new Locale("en", "US");
 cLanguagesByCountrySpy.put("US", Arrays.asList(localeExisting));
 cCountriesByLanguageSpy.put("en", Arrays.asList(localeExisting));

 expectedResult = true;
 actualResult = LocaleUtils.isAvailableLocale(localeExisting);
 assertEquals(expectedResult, actualResult);

// Test with populated concurrent maps and non-existing locale
 Locale localeNonExisting = new Locale("es", "ES");
 expectedResult = false;
 actualResult = LocaleUtils.isAvailableLocale(localeNonExisting);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testLanguagesByCountry() {
// Test with empty concurrent map
 ConcurrentHashMap<String, List<Locale>> spies = new ConcurrentHashMap<>();
 spies.put(LocaleUtils.class.getName() + "_cLanguagesByCountry", cLanguagesByCountrySpy = new ConcurrentHashMap<>());
 spies.put(LocaleUtils.class.getName() + "_cCountriesByLanguage", cCountriesByLanguageSpy = new ConcurrentHashMap<>());

 String countryCodeEmpty = "";
 List<Locale> expectedLocaleList1 = Collections.emptyList();
 List<Locale> actualLocaleList1 = LocaleUtils.languagesByCountry(countryCodeEmpty);
 assertEquals(expectedLocaleList1, actualLocaleList1);

// Test with populated concurrent map and existing country code
 String countryCodeExisting = "US";
 List<Locale> localeList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
 cLanguagesByCountrySpy.put(countryCodeExisting, localeList);

 expectedLocaleList1 = localeList;
 actualLocaleList1 = LocaleUtils.languagesByCountry(countryCodeExisting);
 assertEquals(expectedLocaleList1, actualLocaleList1);
 }

 @Test
 public void testCountriesByLanguage() {
// Test with empty concurrent map
 ConcurrentHashMap<String, List<Locale>> spies = new ConcurrentHashMap<>();
 spies.put(LocaleUtils.class.getName() + "_cLanguagesByCountry", cLanguagesByCountrySpy = new ConcurrentHashMap<>());
 spies.put(LocaleUtils.class.getName() + "_cCountriesByLanguage", cCountriesByLanguageSpy = new ConcurrentHashMap<>());

 String languageCodeEmpty = "";
 List<Locale> expectedLocaleList1 = Collections.emptyList();
 List<Locale> actualLocaleList1 = LocaleUtils.countriesByLanguage(languageCodeEmpty);
 assertEquals(expectedLocaleList1, actualLocaleList1);

// Test with populated concurrent map and existing language code
 String languageCodeExisting = "en";
 List<Locale> localeList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
 cCountriesByLanguageSpy.put(languageCodeExisting, localeList);

 expectedLocaleList1 = localeList;
 actualLocaleList1 = LocaleUtils.countriesByLanguage(languageCodeExisting);
 assertEquals(expectedLocaleList1, actualLocaleList1);
 }
}