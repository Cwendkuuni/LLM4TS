package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;

public class LocaleUtilsTest {

 private List<Locale> createTestLocaleList(Locale... locales) {
 return new ArrayList<Locale>(Arrays.asList(locales));
 }

 @Test
 public void testToLocale() {
 // Test empty string
 assertNull(LocaleUtils.toLocale(""));

 // Test empty locale
 assertNull(LocaleUtils.toLocale(null));

 // Test invalid locale with no '#'
 assertNull(LocaleUtils.toLocale("invalidLocale"));

 // Test valid locale with 1 part
 assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));

 // Test valid locale with 2 parts
 assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

 // Test valid locale with 3 parts
 assertEquals(new Locale("es", "ES", "Castilian"), LocaleUtils.toLocale("es_ES_Castilian"));

 // Test invalid locale with more than 3 parts
 assertNull(LocaleUtils.toLocale("es_ES_Castilian_Madrid"));

 // Test invalid locale with invalid country code
 assertNull(LocaleUtils.toLocale("es_InvalidCountry"));

 // Test invalid locale with invalid language code
 assertNull(LocaleUtils.toLocale("InvalidLanguage_US"));
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en", "US");
 List<Locale> expectedLocaleList = createTestLocaleList(new Locale("en"), new Locale("en_US"), new Locale("en_GB"), new Locale("en", ""));
 List<Locale> resultLocaleList = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedLocaleList, resultLocaleList);
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale locale = new Locale("en", "US");
 Locale defaultLocale = new Locale("fr", "FR");
 List<Locale> expectedLocaleList = createTestLocaleList(new Locale("en"), new Locale("en_US"), new Locale("en_GB"), new Locale("en", ""), new Locale("fr", "FR"));
 List<Locale> resultLocaleList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expectedLocaleList, resultLocaleList);
 }

 @Test
 public void testLocaleLookupListWithNullLocale() {
 List<Locale> resultLocaleList = LocaleUtils.localeLookupList(null);
 assertTrue(resultLocaleList.isEmpty());
 }

 @Test
 public void testLocaleLookupListWithNullDefaultLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> resultLocaleList = LocaleUtils.localeLookupList(locale, null);
 List<Locale> expectedLocaleList = createTestLocaleList(new Locale("en"), new Locale("en_US"), new Locale("en_GB"), new Locale("en", ""));
 assertEquals(expectedLocaleList, resultLocaleList);
 }

 @Test
 public void testAvailableLocaleList() {
 Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
 Set<Locale> resultLocaleSet = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
 assertEquals(expectedLocaleSet, resultLocaleSet);
 }

 @Test
 public void testAvailableLocaleListIsEmpty() {
 assertTrue(LocaleUtils.availableLocaleList().isEmpty());
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleNullLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expectedLocaleList = createTestLocaleList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("es", "PR"));
 List<Locale> resultLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLocaleList, resultLocaleList);
 }

 @Test
 public void testLanguagesByCountryWithEmptyCountryCode() {
 List<Locale> resultLocaleList = LocaleUtils.languagesByCountry("");
 assertTrue(resultLocaleList.isEmpty());
 }

 @Test
 public void testLanguagesByCountryWithNullCountryCode() {
 assertNull(LocaleUtils.languagesByCountry(null));
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";List<Locale> expectedLocaleList = createTestLocaleList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"));
 List<Locale> resultLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedLocaleList, resultLocaleList);
 }

 @Test
 public void testCountriesByLanguageWithEmptyLanguageCode() {
 List<Locale> resultLocaleList = LocaleUtils.countriesByLanguage("");
 assertTrue(resultLocaleList.isEmpty());
 }

 @Test
 public void testCountriesByLanguageWithNullLanguageCode() {
 assertNull(LocaleUtils.countriesByLanguage(null));
 }

}