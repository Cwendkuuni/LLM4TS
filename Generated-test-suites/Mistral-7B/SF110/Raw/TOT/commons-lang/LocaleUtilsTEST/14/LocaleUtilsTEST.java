package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.*;

@RunWith(JUnit4.class)
public class LocaleUtilsTest {

 @Test
 public void testToLocaleValidFormats() {
 String[] validFormats = new String[]{"en_US", "fr_FR", "de_DE", "es_ES", "zh_CN"};
 for (String format : validFormats) {
 Locale expectedLocale = new Locale(format.split("_")[0], format.split("_")[1]);
 Locale actualLocale = LocaleUtils.toLocale(format);
 assertEquals(expectedLocale, actualLocale);
 }
 }

 @Test
 public void testToLocaleInvalidFormat() {
 String invalidFormat = "invalidFormat";
 try {
 LocaleUtils.toLocale(invalidFormat);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid locale format: " + invalidFormat, e.getMessage());
 }
 }

 @Test
 public void testLocaleLookupListValidLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> expectedLocales = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"));
 List<Locale> actualLocales = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testLocaleLookupListNullLocale() {
 List<Locale> actualLocales = LocaleUtils.localeLookupList(null);
 assertTrue(actualLocales.isEmpty());
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), defaultLocale);
 List<Locale> actualLocales = LocaleUtils.localeLookupList(defaultLocale);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testAvailableLocaleList() {
 Set<Locale> expectedLocales = LocaleUtils.availableLocaleSet();
 List<Locale> actualLocales = LocaleUtils.availableLocaleList();
 assertEquals(expectedLocales, new HashSet<>(actualLocales));
 }

 @Test
 public void testIsAvailableLocaleTrue() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleFalse() {
 Locale locale = new Locale("xx", "XX"); // Invalid locale
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountryValidCountry() {
 String countryCode = "US";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"));
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testLanguagesByCountryNullCountryCode() {
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(null);
 assertTrue(actualLocales.isEmpty());
 }

 @Test
 public void testCountriesByLanguageValidLanguage() {
 String languageCode = "en";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"), new Locale("en", "AU"));
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testCountriesByLanguageNullLanguageCode() {
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(null);
 assertTrue(actualLocales.isEmpty());
 }
}