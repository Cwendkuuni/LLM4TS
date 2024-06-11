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
 // Test null input
 assertNull(LocaleUtils.toLocale(null));

 // Test empty string
 assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));

 // Test invalid locale format (no '_')
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));

 // Test valid locates with 2 and 3 parts
 List<Locale> expectedLocales1 = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));
 assertEquals(expectedLocales1, LocaleUtils.localeLookupList(new Locale("en_US"), null));
 List<Locale> expectedLocales2 = Arrays.asList(new Locale("de", "DE"), new Locale("de", "CH"), new Locale("de", "AT"));
 assertEquals(expectedLocales2, LocaleUtils.localeLookupList(new Locale("de_CH"), null));

 // Test invalid locale format (wrong number of '_')
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_"));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US__"));
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_US"));

 // Test availableLocaleList and availableLocaleSet
 List<Locale> availableLocales = LocaleUtils.availableLocaleList();
 Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
 assertEquals(157, availableLocales.size());
 assertEquals(157, availableLocaleSet.size());

 // Test languagesByCountry and countriesByLanguage
 testCLanguagesByCountry.put("US", Arrays.asList(new Locale("en", "US"), new Locale("es", "US")));
 testCCountriesByLanguage.put("en", Arrays.asList(new Locale("en", "CA"), new Locale("en", "GB")));
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
 assertEquals(2, languagesByCountry.size());
 assertEquals(2, countriesByLanguage.size());
 assertEquals(new Locale("en", "US"), languagesByCountry.get(0));
 assertEquals(new Locale("en", "CA"), countriesByLanguage.get(0));
 assertEquals(new Locale("es", "US"), languagesByCountry.get(1));
 assertEquals(new Locale("en", "GB"), countriesByLanguage.get(1));
 }

}