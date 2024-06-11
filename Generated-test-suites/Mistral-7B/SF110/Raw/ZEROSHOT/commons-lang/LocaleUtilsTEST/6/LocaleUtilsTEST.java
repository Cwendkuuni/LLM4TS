package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class LocaleUtilsTest {

 private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<String, List<Locale>>();
 private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<String, List<Locale>>();

 @Test
 public void testToLocaleWithValidInput() {
 String input = "en_US";
 Locale expected = new Locale("en", "US");
 Locale actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testToLocaleWithEmptyInput() {
 Locale actual = LocaleUtils.toLocale("");
 assertNull(actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputNoLanguage() {
 String input = "_US";
 LocaleUtils.toLocale(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputNoCountry() {
 String input = "en";
 LocaleUtils.toLocale(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputMissingLanguageCountrySeparator() {
 String input = "enUS";
 LocaleUtils.toLocale(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputMissingCountryLanguageSeparator() {
 String input = "en_";
 LocaleUtils.toLocale(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputLanguageCountryMismatch() {
 String input = "fr_EN";
 LocaleUtils.toLocale(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputLanguageCountryEmpty() {
 String input = "en_";
 LocaleUtils.toLocale(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputCountryEmpty() {
 String input = "en_";
 LocaleUtils.toLocale(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputLanguageUppercase() {
 String input = "EN_US";
 LocaleUtils.toLocale(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidInputCountryUppercase() {
 String input = "en_US";
 LocaleUtils.toLocale(input.toUpperCase());
 }

 @Test
 public void testLocaleLookupListWithNullLocale() {
 List<Locale> actual = LocaleUtils.localeLookupList(null);
 assertTrue(Collections.isEmpty(actual));
 }

 @Test
 public void testLocaleLookupListWithNotNullLocale() {
 Locale locale = new Locale("fr", "CA");
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""));
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListWithNotNullLocaleAndDefaultLocale() {
 Locale locale = new Locale("fr", "CA");
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""), defaultLocale);
 List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListWithNullDefaultLocale() {
 Locale locale = new Locale("fr", "CA");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
 assertTrue(Collections.containsAll(actual, Arrays.asList(locale, new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""))));
 }

 @Test
 public void testLocaleLookupListWithSameLocaleAsDefaultLocale() {
 Locale locale = new Locale("fr", "CA");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, locale);
 assertEquals(Arrays.asList(locale), actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = LocaleUtils.cLanguagesByCountry.get("");
 assertTrue(Collections.containsAll(actual, expected.toArray()));
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = LocaleUtils.cLanguagesByCountry.get("");
 assertTrue(Collections.disjoint(actual, expected));
 }

 @Test
 public void testIsAvailableLocaleWithAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleWithNullLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testIsAvailableLocaleWithInvalidLocale() {
 Locale locale = new Locale("en", "XX");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"), new Locale("en", "AU"));
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryWithNullCountryCode() {
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 assertTrue(Collections.isEmpty(actual));
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"), new Locale("en", "AU"), new Locale("en", "ZA"), new Locale("en", "IN"), new Locale("en", "NZ"), new Locale("en", "JM"));
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageWithNullLanguageCode() {
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 assertTrue(Collections.isEmpty(actual));
 }

}